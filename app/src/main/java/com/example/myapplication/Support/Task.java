package com.example.myapplication.Support;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String activityName;
    private String activityDescrption;
    private String activityCreator;
    private int task_id;
    private int activity_id;
    private Date issuedDate;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescrption() {
        return activityDescrption;
    }

    public void setActivityDescrption(String activityDescrption) {
        this.activityDescrption = activityDescrption;
    }

    public String getActivityCreator() {
        return activityCreator;
    }

    public void setActivityCreator(String activityCreator) {
        this.activityCreator = activityCreator;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }
}
