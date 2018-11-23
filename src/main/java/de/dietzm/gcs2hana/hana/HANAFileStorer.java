package de.dietzm.gcs2hana.hana;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.dietzm.gcs2hana.base.DatabaseOrganizer;
import de.dietzm.gcs2hana.base.IFileStorer;
import de.dietzm.gcs2hana.base.TransferFile;

public class HANAFileStorer implements IFileStorer{

	private String destination;

	public HANAFileStorer(String destination) {
		this.destination = destination;
	}

	public void storeFiles(TransferFile[] files) throws Exception {

		
		// Get a DB connection
		Connection con = DatabaseOrganizer.getInstance(destination).getConnection();	
		
		// Loop files and store in db
		for(TransferFile file : files) {
			
			InputStream is = file.getFileContent();

			//Prepare UPSERT statement and fill parameters
			PreparedStatement prep = con.prepareStatement("UPSERT FILE_DATA (ID,NAME,LASTMODIFIED,CONTENT) VALUES (?,?,?,?) WITH PRIMARY KEY");
			
			prep.setString(1,    createHashFromFileNameAndPath(file.getName()));		
			prep.setString(2,   file.getName());	 
			prep.setLong(3, 	file.getLastChangeDate());
			prep.setBlob(4,     is);
			
			//Execute
			prep.executeUpdate();
	
			//Log info
			//log("Updated : " + file.getName() );
			
			//Finalize
			prep.close();
			is.close();		
		}
		
		con.close();

				
		
		
	}

	public String createHashFromFileNameAndPath(String filename) throws NoSuchAlgorithmException {

		String input = filename;
		
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

	public long getLatestFileDate() throws Exception {

		// Get a DB connection
		Connection con = DatabaseOrganizer.getInstance(destination).getConnection();	
		ResultSet rs = con.createStatement().executeQuery("SELECT MAX(LASTMODIFIED) FROM DOCS ");

		if(rs.next()){
			return rs.getLong(1);
		}

		return 0;
	}

}
