package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.FileData;
import com.example.myapplication.Support.UserPreferences;
import com.example.myapplication.model.ReportActivityAdapter;
import com.example.myapplication.model.ReportCollection;
import com.example.myapplication.model.ReportInterface;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity implements ReportInterface {
    MaterialToolbar materialToolbar;
    MyForegroundService myForegroundService;
    ArrayList<String> stringArrayList=new ArrayList<>();
    HashMap<String,ReportCollection> reportCollectionHashMap=new HashMap<>();
    RecyclerView recyclerView;
    ReportActivityAdapter reportActivityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        materialToolbar=findViewById(R.id.materialToolbar);
        materialToolbar.setTitle("History");
        setSupportActionBar(materialToolbar);
        myForegroundService=MainActivity.myForegroundService;
        myForegroundService.getAllReports(this);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportActivityAdapter=new ReportActivityAdapter(this,reportCollectionHashMap,stringArrayList);
        recyclerView.setAdapter(reportActivityAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        Log.d(TAG, "onCreateOptionsMenu: history menu inflating");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        MenuItem menuItem = menu.findItem(R.id.search_View);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(HistoryActivity.this, "changing", Toast.LENGTH_SHORT).show();
                    updateFragmentSearchQuery(query);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    updateFragmentSearchQuery(newText);
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }
    public void updateFragmentSearchQuery(String text){

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle item selection
        if (id == R.id.search_View) {
            // Handle Item 1 selection
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setReports(String text) {
        ReportInterface.super.setReports(text);
        try {
            JSONObject jsonObject=new JSONObject(text);
            int activityId=jsonObject.getInt("activity-id");
            String activityName=jsonObject.getString("activity-name");
            String activityDescription=jsonObject.getString("activity-description");
            String reportFrom=jsonObject.getString("report_from");
            int report_user_id=jsonObject.getInt("report_user_id");
            JSONObject reportsObject = jsonObject.getJSONObject("reports-all");
            JSONArray textArray = reportsObject.getJSONArray("text");
            JSONArray audioArray = reportsObject.getJSONArray("audio");
            JSONArray imageArray = reportsObject.getJSONArray("image");
            ArrayList<FileData> fileDatas=new ArrayList<>();
            for (int i = 0; i < imageArray.length(); i++) {
                String imagePath = imageArray.getString(i);
                FileData fileData=new FileData();
                fileData.setType("image");
                fileData.setFile(imagePath);
                fileDatas.add(fileData);
                Log.d(TAG, "onMessage: imageArray..."+imagePath.replace("\"",""));
            }
            for (int i = 0; i < audioArray.length(); i++) {
                String audioPath = audioArray.getString(i);
                FileData fileData=new FileData();
                fileData.setType("audio");
                fileData.setFile(audioPath);
                fileDatas.add(fileData);
                Log.d(TAG, "onMessage: audioArray..."+audioPath.replace("\"",""));
            }
            for (int i = 0; i < textArray.length(); i++) {
                String textPath = textArray.getString(i);
                FileData fileData=new FileData();
                fileData.setType("text");
                fileData.setFile(textPath);
                fileDatas.add(fileData);
                Log.d(TAG, "onMessage: textArray..."+textPath.replace("\"",""));
            }
            ReportCollection reportCollection = new ReportCollection();
            reportCollection.setActivityName(activityName);
            reportCollection.setActivityDescription(activityDescription);
            reportCollection.setActivityId(activityId);
            reportCollection.setReportFrom(reportFrom);
            reportCollection.setReport_user_id(report_user_id);
            reportCollection.setReports(fileDatas);
            if(reportCollectionHashMap.containsKey(activityName)){
                Log.d(TAG, "setReports: similar found");
                reportCollection=reportCollectionHashMap.get(activityName);
                reportCollection.setActivityName(activityName);
                reportCollection.setActivityDescription(activityDescription);
                reportCollection.setActivityId(activityId);
                reportCollection.setReportFrom(reportFrom);
                reportCollection.setReport_user_id(report_user_id);
                Log.d(TAG, "setReports: reportCollection "+reportCollection.getActivityName());
                reportCollection.getReports().addAll(fileDatas);
                Log.d(TAG, "setReports: size..."+reportCollection.getReports().size());
                reportCollectionHashMap.put(activityName,reportCollection);
            }else {
                stringArrayList.add(activityName);
                reportCollectionHashMap.put(activityName,reportCollection);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HistoryActivity.this.runOnUiThread(() ->{
            reportActivityAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void setReportData(byte[] data) {
        ReportInterface.super.setReportData(data);
    }
}