package com.example.myapplication.model;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Support.SubmitHolder;

import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    private String name;
    private String description;
    private int id;
    private int status_id;
    private int imageResource;
    private MutableLiveData<Long> liveDataExample = new MutableLiveData<>();
    private MutableLiveData<ArrayList<SubmitHolder>> arrayListMutableLiveData=new MutableLiveData<>();

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

    public ArrayList<SubmitHolder> getArrayListMutableLiveData() {
        if(arrayListMutableLiveData!=null)
            return arrayListMutableLiveData.getValue();
        else
            return null;
    }


    public void setArrayListMutableLiveData(ArrayList<SubmitHolder> arrayListMutableLiveData) {
        Log.d(TAG, "setArrayListMutableLiveData: value added ");
        this.arrayListMutableLiveData.setValue(arrayListMutableLiveData);
    }
}