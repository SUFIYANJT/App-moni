package com.example.myapplication;

import static com.example.myapplication.BottomSheetDialogFragmentIssued.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.FileData;
import com.example.myapplication.Support.SubmitHolder;
import com.example.myapplication.model.FileAdapter;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;


public class NewWindow extends AppCompatActivity implements ReportTransfer{
    ArrayList<FileData> fileDatas;

    RecyclerView recyclerView;
    FileAdapter fileAdapter;
    ArrayList<SubmitHolder> submitHolders;
    MaterialToolbar materialToolbar;
    MyForegroundService foregroundService;
    ReportTransfer reportTransfer;
    Button approve,reject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_window);
        Intent intent=getIntent();
        materialToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);
        submitHolders=new ArrayList<>();
        fileDatas=new ArrayList<>();
        foregroundService=MainActivity.myForegroundService;
        recyclerView=findViewById(R.id.recyclerViewuser);
        reportTransfer=this;
        approve=findViewById(R.id.approve);
        reject=findViewById(R.id.reject);
        approve.setOnClickListener(v -> {

        });
        fileAdapter = new FileAdapter(NewWindow.this,fileDatas,foregroundService);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fileAdapter);
        Activity activity= (Activity) intent.getSerializableExtra("activity");
        int activityId= 0;
        if (activity != null) {
            activityId = activity.activityId;
            materialToolbar.setSubtitle(activity.activityName);
        }

        foregroundService.getReport(reportTransfer,activityId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_back,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.menu_mark_complete){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setAudio(String[] audio) {
        Log.d(TAG, "setAudio: audio array is..."+audio.length);
        for(String files:audio){
            FileData fileData=new FileData();
            fileData.setFile(files);
            fileData.setType("audio");
            fileDatas.add(fileData);
        }
        this.runOnUiThread(() -> fileAdapter.notifyDataSetChanged());

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setImage(String[] image) {
        for(String files:image){
            FileData fileData=new FileData();
            fileData.setFile(files);
            Log.d(TAG, "setImage: image array is..."+files);
            fileData.setType("image");
            fileDatas.add(fileData);
        }
        this.runOnUiThread(() -> fileAdapter.notifyDataSetChanged());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setText(String[] text) {
        Log.d(TAG, "setText: text array is..."+text.length);
        for(String files:text){
            FileData fileData=new FileData();
            fileData.setFile(files);
            fileData.setType("text");
            fileDatas.add(fileData);
        }
        this.runOnUiThread(() -> fileAdapter.notifyDataSetChanged());
    }

    @Override
    public void setFileData(byte[] data) {

    }

    @Override
    public void fileSize(int size) {

    }
}
