package com.example.myapplication.Support;

import java.io.File;
import java.io.FileOutputStream;

public class FileData {
    private String file;
    private String text;
    private String type;
    private int FileSize=100;
    private int downloaded;
    private File downloadFile;
    private FileOutputStream fileOutputStream;
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getFileSize() {
        return FileSize;
    }


    public void setFileSize(int fileSize) {
        FileSize = fileSize;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public File getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(File downloadFile) {
        this.downloadFile = downloadFile;
    }

    public FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public void setFileOutputStream(FileOutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
    }
}
