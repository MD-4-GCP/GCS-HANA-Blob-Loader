package de.dietzm.gcs2hana.hana;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import de.dietzm.gcs2hana.base.DatabaseOrganizer;

public class FileLoaderAndTransferer {

	public String rootFolder;
	private PrintWriter out;

	public FileLoaderAndTransferer(String rootFolder) {
		this.rootFolder = rootFolder;
		this.out = null;
	}

	private void log(String text){
		if(out != null){
			out.println(text);
			out.flush();
		}else
			System.out.println(text);
	}
	
	public void executeFileLoading() throws Exception {

		// Traverse folders and subfolders and get all files
		ArrayList<File> fileList = new ArrayList<File>();
		traverseFolder(rootFolder, fileList);

		// Get a DB connection
		Connection con = DatabaseOrganizer.getInstance("HANA").getConnection();	
		Iterator<File> fileIterator = fileList.iterator();
		
		// Loop files and store in db
		while (fileIterator.hasNext()) {
			
			File file = (File) fileIterator.next();
			FileInputStream fis = new FileInputStream(file);
			
			//Prepare UPSERT statement and fill parameters
			PreparedStatement prep = con.prepareStatement("UPSERT FILE_DATA VALUES (?,?,?,?,?,?) WITH PRIMARY KEY");
			
			prep.setString(1,    createHashFromFileNameAndPath(file));		
			prep.setString(2,    file.getName());	 
			prep.setString(3,    file.getParent());		
			prep.setLong(4,      file.length());
			prep.setTimestamp(5, new Timestamp(file.lastModified()));
			prep.setBlob(6,      fis);
			
			//Execute
			prep.executeUpdate();
	
			//Log info
			log("Updated : " + file.getParent() + file.getName() );
			
			//Finalize
			prep.close();
			fis.close();		
		}
		
		con.close();

				
		
		
	}

	public void traverseFolder(String path, ArrayList<File> foundFiles) {

		File folder = new File(path);
		File[] files = folder.listFiles();

		if (files == null)
			return;

		for (int i = 0; i < files.length; i++) {
			File file = files[i];

			if (file.getName().equalsIgnoreCase("thumbs.db"))
				continue;

			if (file.isDirectory()) {
				traverseFolder(path + file.getName() + File.separator,
						foundFiles);
			} else {
				foundFiles.add(file);
			}
		}
	}

	public String createHashFromFileNameAndPath( File file) throws NoSuchAlgorithmException {
		String filename = file.getName();
		String filepath = file.getParent() + File.separator;	
		String input = filepath + filename;
		
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.reset();
		byte[] buffer = input.getBytes();
		md.update(buffer);
		byte[] digest = md.digest();

		String hexStr = "";
		for (int i = 0; i < digest.length; i++) {
			hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		return hexStr;
	}

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.out
					.println("usage: FileLoaderAndTransferer <path_to_root_folder>");
			return;
		}

		String rootFolder = args[0];
		FileLoaderAndTransferer flt = new FileLoaderAndTransferer(rootFolder);
		flt.executeFileLoading();

	}

	public void setOutput(PrintWriter writer) {
		this.out = writer;
	}

}
