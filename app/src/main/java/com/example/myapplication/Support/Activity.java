package com.example.myapplication.Support;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Activity implements Serializable, Comparable<Activity> {

    public int activityId;
    public String activityDescription;
    public int activity_satuts_id;
    public int assigned_to;
    public int componentId;
    public int machineId;
    public int scheduleId;
    public String activityName;
    public String issued_date;
    public String assigned_to_user;
    public String activityCreator;
    public String change;
    public String uiChange;
    public String activity_last_reported;
    public int schedule_value;
    public int remaining;
    public int dateInInt(){
        String dateString = activity_last_reported;
        Log.d(TAG, "dateInInt: "+activity_last_reported);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try {
            date = dateFormat.parse(dateString);
            Date date1=new Date();
            long differenceMillis = date1.getTime() - date.getTime();

            // Convert milliseconds to days
            long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillis);
            return  schedule_value-(int)differenceDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int compareTo(Activity o) {
        return Integer.compare(this.remaining/this.schedule_value,o.remaining/o.schedule_value);
    }
}
