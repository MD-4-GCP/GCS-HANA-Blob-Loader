package de.dietzm.gcs2hana.base;

import java.io.InputStream;
import java.nio.channels.Channels;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;

public class TransferFileOnBlob extends TransferFile {

    private Blob blob;

    public TransferFileOnBlob(Blob blob){
        super();
        this.blob = blob;
        this.setName(blob.getName());
        this.setLastChangeDate(blob.getUpdateTime()); 
    }


    public InputStream getFileContent() {
        ReadChannel reader = blob.reader();
		InputStream inputStream = Channels.newInputStream(reader);
        return inputStream;
    }

    public void setFileContent(InputStream fileContent) {
        //Nothing
    }
    

}