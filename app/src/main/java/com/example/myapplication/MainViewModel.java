package com.example.myapplication;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private boolean isPageOpen = false;

    public boolean isPageOpen() {
        return isPageOpen;
    }

    public void setPageOpen(boolean pageOpen) {
        isPageOpen = pageOpen;
    }
}
