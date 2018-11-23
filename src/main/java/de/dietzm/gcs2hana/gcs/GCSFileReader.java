package de.dietzm.gcs2hana.gcs;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.Storage.BucketField;
import com.google.cloud.storage.Storage.BucketGetOption;
import com.google.cloud.storage.StorageOptions;

import de.dietzm.gcs2hana.base.IFileReader;
import de.dietzm.gcs2hana.base.TransferFile;
import de.dietzm.gcs2hana.base.TransferFileOnBlob;

public class GCSFileReader implements IFileReader{

	private String bucketName;

	public GCSFileReader(String bucketName) {
		this.bucketName = bucketName;
	}

	public TransferFile[] readNextFiles(int chunkSize, long sinceDate) throws Exception {

		
		ArrayList<TransferFile> fileResultList = new ArrayList<TransferFile>();

		// Get Destinations from Storage
		Storage storage = StorageOptions.getDefaultInstance().getService();
		Bucket bucket = storage.get(bucketName, BucketGetOption.fields(BucketField.ID, BucketField.NAME));
		
		for (Blob blob : bucket.list(BlobListOption.currentDirectory()).iterateAll()) {
			if (!blob.isDirectory() && !blob.getName().endsWith("/")) {
				TransferFileOnBlob file = new TransferFileOnBlob(blob);
			}
		}

		return fileResultList.toArray(new TransferFile[fileResultList.size()]);
	}
	
}
