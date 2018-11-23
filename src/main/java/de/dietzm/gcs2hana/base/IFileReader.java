package de.dietzm.gcs2hana.base;

public interface IFileReader {

    public TransferFile[] readNextFiles(int chunkSize, long sinceDate) throws Exception;

}