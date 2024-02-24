package com.example.myapplication.model;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.SubmitHolder;
import com.example.myapplication.Support.User;

import java.util.ArrayList;
import java.util.List;

public class ItemModel extends ViewModel {
    private String name;
    private MutableLiveData<Long> liveDataExample = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> activityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> IssuedactivityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> PendingactivityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> ReviewactivityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<SubmitHolder>> arrayListMutableLiveData=new MutableLiveData<>();

    public void setItemName(String itemName) {
        this.name = itemName;
    }

    public ItemModel() {

    }
    public void updateLiveDataExample(Long value) {
        liveDataExample.setValue(value);
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

    public MutableLiveData<ArrayList<Activity>> getActivityMutableLiveData() {
        return activityMutableLiveData;
    }

    public boolean setActivityMutableLiveData(ArrayList<Activity> activity) {
        this.activityMutableLiveData.postValue(activity);
        return activity!=activityMutableLiveData.getValue();
    }

    public MutableLiveData<ArrayList<Activity>> getIssuedactivityMutableLiveData() {
        return IssuedactivityMutableLiveData;
    }

    public boolean setIssuedactivityMutableLiveData(ArrayList<Activity> issuedactivityMutableLiveData) {
        this.IssuedactivityMutableLiveData.postValue(issuedactivityMutableLiveData);
        return issuedactivityMutableLiveData!=IssuedactivityMutableLiveData.getValue();
    }

    public MutableLiveData<ArrayList<Activity>> getPendingactivityMutableLiveData() {
        return PendingactivityMutableLiveData;
    }

    public boolean setPendingactivityMutableLiveData(ArrayList<Activity> pendingactivityMutableLiveData) {
        PendingactivityMutableLiveData.postValue(pendingactivityMutableLiveData);
        return pendingactivityMutableLiveData!=PendingactivityMutableLiveData.getValue();
    }

    public MutableLiveData<ArrayList<Activity>> getReviewactivityMutableLiveData() {
        return ReviewactivityMutableLiveData;
    }

    public boolean setReviewactivityMutableLiveData(ArrayList<Activity> reviewactivityMutableLiveData) {
        ReviewactivityMutableLiveData.postValue(reviewactivityMutableLiveData);
        return reviewactivityMutableLiveData!=ReviewactivityMutableLiveData.getValue();
    }
}