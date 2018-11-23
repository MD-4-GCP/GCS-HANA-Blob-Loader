package de.dietzm.gcs2hana.base;

public interface IFileStorer {

    public void storeFiles(TransferFile[] files) throws Exception;

}