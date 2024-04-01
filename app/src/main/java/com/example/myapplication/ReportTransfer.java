package com.example.myapplication;
public interface ReportTransfer{
    default void setAudio(String[] audio){

    }
    default void setImage(String[] image){

    }
    default void setText(String[] text){

    }
    default void setFileData(byte[] data){

    }
    default void fileSize(int size){

    }
}