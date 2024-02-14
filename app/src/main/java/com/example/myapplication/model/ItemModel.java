package com.example.myapplication.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ItemModel {
    private String name;
    private String description;
    private int id;
    private int status_id;
    private int imageResource;
    private MutableLiveData<Long> liveDataExample = new MutableLiveData<>();

    public void setItemName(String itemName) {
        this.name = itemName;
    }

    public ItemModel() {

    }
    public LiveData<Long> getLiveDataExample() {
        return liveDataExample;
    }
    public void updateLiveDataExample(Long value) {
        liveDataExample.setValue(value);
    }
    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getItemName() {
        return name;
    }

}