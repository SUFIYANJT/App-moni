package com.example.myapplication.model;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.Machine;
import com.example.myapplication.Support.SubmitHolder;
import com.example.myapplication.Support.Task;
import com.example.myapplication.Support.User;

import java.util.ArrayList;
import java.util.List;

public class ItemModel extends ViewModel {
    private String name;
    public MutableLiveData<ArrayList<User>> userMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<Task> taskData = new MutableLiveData<>();
    public MutableLiveData<Integer> integerMutableLiveData2=new MutableLiveData<>();
    private MutableLiveData<Long> liveDataExample = new MutableLiveData<>();
    private MutableLiveData<Integer> integerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Machine>> MachineMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Machine>> ComponentMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Machine>> ScheduleMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> activityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> IssuedactivityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> PendingactivityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<Activity>> ReviewactivityMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<SubmitHolder>> arrayListMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<String> callBack=new MutableLiveData<>();

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

    public boolean setActivityMutableLiveDatas(ArrayList<Activity> activity) {
        this.activityMutableLiveData.setValue(activity);
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

    public MutableLiveData<ArrayList<Machine>> getMachineMutableLiveData() {
        return MachineMutableLiveData;
    }

    public void setMachineMutableLiveData(ArrayList<Machine> machineMutableLiveData) {
        MachineMutableLiveData.postValue(machineMutableLiveData);
    }

    public MutableLiveData<ArrayList<Machine>> getComponentMutableLiveData() {
        return ComponentMutableLiveData;
    }

    public void setComponentMutableLiveData(ArrayList<Machine> componentMutableLiveData) {
        ComponentMutableLiveData.postValue(componentMutableLiveData);
    }

    public MutableLiveData<ArrayList<Machine>> getScheduleMutableLiveData() {
        return ScheduleMutableLiveData;
    }

    public void setScheduleMutableLiveData(ArrayList<Machine> scheduleMutableLiveData) {
        ScheduleMutableLiveData.postValue(scheduleMutableLiveData);
    }
    public void setUserMutableLiveData(ArrayList<User> userMutableLiveData){
        this.userMutableLiveData.postValue(userMutableLiveData);
    }
    public MutableLiveData<ArrayList<User>> getUserMutableLiveData(){
        return userMutableLiveData;
    }

    public MutableLiveData<String> getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack.postValue(callBack);
    }

    public MutableLiveData<Integer> getIntegerMutableLiveData() {
        return integerMutableLiveData;
    }

    public void setIntegerMutableLiveData(Integer integerMutableLiveData) {
        this.integerMutableLiveData.setValue(integerMutableLiveData);
    }
    public void setIntegerMutableLiveData2(Integer integerMutableLiveData2){
        this.integerMutableLiveData2.setValue(integerMutableLiveData2);
    }
    public MutableLiveData<Integer> getIntegerMutableLiveData2(){
        return integerMutableLiveData2;
    }

    public MutableLiveData<Task> getTaskData() {
        return taskData;
    }

    public void setTaskData(Task taskData) {
        this.taskData.postValue(taskData);
    }
}