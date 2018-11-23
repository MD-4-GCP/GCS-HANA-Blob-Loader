package de.dietzm.gcs2hana.services;

import de.dietzm.foundation.api.APIResult;
import de.dietzm.foundation.api.APIResultBuilder;
import de.dietzm.foundation.api.APITools;
import de.dietzm.gcs2hana.base.TransferFile;
import de.dietzm.gcs2hana.gcs.GCSFileReader;
import de.dietzm.gcs2hana.hana.HANAFileStorer;
import spark.Request;
import spark.Response;

public class SyncAPI{

    private static final String BUCKET_NAME_ENV = "GCS_STORAGE_BUCKET";
    private static final String DESTINATION_NAME_ENV = "HANA_DESTINATION_NAME";

    public APIResult syncFilesFromGCSToHANA(Request request, Response response) throws Exception {
        
        String bucketName = APITools.readEnvAndQueryParameters("GCS_STORAGE_BUCKET", request, "Bucket");
        String destination = APITools.readEnvAndQueryParameters("HANA_DESTINATION_NAME", request, "Destination");
        int numOfFiles = new Integer(APITools.readEnvAndQueryParametersOrDefault("FILE_CHUNK_SIZE", request, "NumOfFiles", "10")).intValue();

        GCSFileReader gfr = new GCSFileReader(bucketName);
        HANAFileStorer hfs = new HANAFileStorer(destination);

        //Get latest data stored in HANA
        long latestDate = hfs.getLatestFileDate();

        //Read files since date from GCS
        TransferFile[] files = gfr.readNextFiles(numOfFiles, latestDate);
        
        //Store files in HANA
        hfs.storeFiles(files);
    
	    return APIResultBuilder.success(files.length + " files have been transfered. Chunk size is " + numOfFiles + ".");
    }
    
    public static void main(String[] args) throws Exception {
        
        String bucketName = System.getenv("GCS_STORAGE_BUCKET");
        String destination = System.getenv("HANA_DESTINATION_NAME");
        int numOfFiles = 10;

        GCSFileReader gfr = new GCSFileReader(bucketName);
        HANAFileStorer hfs = new HANAFileStorer(destination);

        //Get latest data stored in HANA
        long latestDate = hfs.getLatestFileDate();

        //Read files since date from GCS
        TransferFile[] files = gfr.readNextFiles(numOfFiles, latestDate);
        
        //Store files in HANA
        hfs.storeFiles(files);
    
	    System.out.println(files.length + " files have been transfered. Chunk size is " + numOfFiles + ".");

    }

}