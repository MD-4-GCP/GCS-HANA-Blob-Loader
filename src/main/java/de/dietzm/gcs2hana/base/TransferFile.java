package de.dietzm.gcs2hana.base;

import java.io.InputStream;

public class TransferFile {

    private String name;
    private long lastChangeDate;
    private InputStream fileContent;
    private String status;


    public TransferFile(){
        this.status = "UNSPECIFIED";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastChangeDate() {
        return this.lastChangeDate;
    }

    public void setLastChangeDate(long lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public InputStream getFileContent() {
        return this.fileContent;
    }

    public void setFileContent(InputStream fileContent) {
        this.fileContent = fileContent;
    }
    

}