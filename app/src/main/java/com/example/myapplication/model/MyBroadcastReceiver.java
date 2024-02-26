package com.example.myapplication.model;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Support.Activity;

import java.util.ArrayList;

public class MyBroadcastReceiver extends BroadcastReceiver {
    ArrayList<Activity> ExistingActivities=new ArrayList<>();
    ArrayList<Activity> IssuedActivities=new ArrayList<>();
    ArrayList<Activity> PendingActivities=new ArrayList<>();
    ArrayList<Activity> ReviewActivities=new ArrayList<>();
    ItemModel itemModel;
    MainActivity mainActivity;
    public MyBroadcastReceiver(){
        super();
    }
    public MyBroadcastReceiver(ArrayList<Activity> ExistingActivities,ArrayList<Activity> IssuedActivities,ArrayList<Activity> PendingActivities,ArrayList<Activity> ReviewActivities,ItemModel model){
        this.ExistingActivities=ExistingActivities;
        this.IssuedActivities=IssuedActivities;
        this.PendingActivities=PendingActivities;
        this.ReviewActivities=ReviewActivities;
        this.itemModel=model;
    }

    private static final String TAG = "MyBroadcastReceiver";

    public MyBroadcastReceiver(ArrayList<Activity> existingActivities, ArrayList<Activity> issuedActivities, ArrayList<Activity> pendingActivities, ArrayList<Activity> reviewActivities, ItemModel itemModel, MainActivity mainActivity) {
        this.ExistingActivities=existingActivities;
        this.IssuedActivities=issuedActivities;
        this.PendingActivities=pendingActivities;
        this.ReviewActivities=reviewActivities;
        this.itemModel=itemModel;
        this.mainActivity=mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Activity activity = (Activity) intent.getSerializableExtra("activity");
        if (activity == null) {
            Log.e(TAG, "onReceive: Activity is null");
            return;
        }

        String change = intent.getStringExtra("change");
        Log.d(TAG, "onReceive: Change state: " + change);
        if (activity != null) {
            Log.d(TAG, "onReceive: Received activity: " + activity.activityName);
        }
        if (activity.activity_satuts_id == 1) {
            if (change != null && change.equals("create")) {
                ExistingActivities.add(activity);
                Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
            } else if (change != null && change.equals("update")) {
                int i = 0;
                for (Activity act : ExistingActivities) {
                    Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                    if (activity != null && act.activityId == activity.activityId) {
                        ExistingActivities.set(i, activity);
                        Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                        break;
                    }
                    if(context instanceof MainActivity){
                        MainActivity activity1=(MainActivity) context;
                        // Snackbar.make(activity1.findViewById(R.id.parent),"New Activity created",Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(context,"New activity created",Toast.LENGTH_SHORT).show();
                    }
                    i++;
                }
                Log.d(TAG, "onReceive: ExistingActivities size: " + ExistingActivities.size() + ", i: " + i);
            }
            // Update LiveData
            //itemModel.setActivityMutableLiveData(ExistingActivities);
        } else if (activity.activity_satuts_id == 2) {
            if (change != null && change.equals("create")) {
                IssuedActivities.add(activity);
                Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
            } else if (change != null && change.equals("update")) {
                int i = 0;
                for (Activity act : IssuedActivities) {
                    Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                    if (activity != null && act.activityId == activity.activityId) {
                        IssuedActivities.set(i, activity);
                        Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                        break;
                    }
                    i++;
                }
                Log.d(TAG, "onReceive: ExistingActivities size: " + IssuedActivities.size() + ", i: " + i);
            }
           // itemModel.setIssuedactivityMutableLiveData(IssuedActivities);
        } else if (activity.activity_satuts_id == 3) {
            if (change != null && change.equals("create")) {
                PendingActivities.add(activity);
                Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
            } else if (change != null && change.equals("update")) {
                int i = 0;
                for (Activity act : PendingActivities) {
                    Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                    if (activity != null && act.activityId == activity.activityId) {
                        PendingActivities.set(i, activity);
                        Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                        break;
                    }
                    i++;
                }
                Log.d(TAG, "onReceive: ExistingActivities size: " + PendingActivities.size() + ", i: " + i);
              //  itemModel.setPendingactivityMutableLiveData(PendingActivities);
            }
        } else if (activity.activity_satuts_id == 4) {
            if (change != null && change.equals("create")) {
                ReviewActivities.add(activity);
                Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
            } else if (change != null && change.equals("update")) {
                int i = 0;
                for (Activity act : ReviewActivities) {
                    Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                    if (activity != null && act.activityId == activity.activityId) {
                        ReviewActivities.set(i, activity);
                        Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                        break;
                    }
                    i++;
                }
                Log.d(TAG, "onReceive: ExistingActivities size: " + ReviewActivities.size() + ", i: " + i);
            }
         //   boolean ischanged=itemModel.setReviewactivityMutableLiveData(ReviewActivities);
           // Log.d(TAG, "onReceive: changing the review page "+ischanged+" "+ReviewActivities.size());
        } else {
            Log.d(TAG, "onReceive: Some unexpected activity data received from the server ");

        }
    }
}
