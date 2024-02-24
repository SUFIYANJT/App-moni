package com.example.myapplication.Support;

import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

public class SubmitHolder {
    private String textView;
    private File file;
    private Uri imageFile;
    private int mode;
    private boolean isPlaying=false;
    public MediaPlayer mediaPlayer=new MediaPlayer();

    public String getTextView() {
        return textView;
    }

    public void setTextView(String textView) {
        this.textView = textView;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Uri getImageFile() {
        return imageFile;
    }

    public void setImageFile(Uri imageFile) {
        this.imageFile = imageFile;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
