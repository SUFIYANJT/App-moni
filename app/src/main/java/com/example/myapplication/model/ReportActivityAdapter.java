package com.example.myapplication.model;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportActivityAdapter extends RecyclerView.Adapter<ReportActivityAdapter.ViewHolder> {
    Activity activity;
    HashMap<String,ReportCollection> reportCollections;
    ArrayList<String> stringArrayList;
    public ReportActivityAdapter(Activity activity, HashMap<String,ReportCollection> reportCollections, ArrayList<String> stringArrayList){
        this.activity=activity;
        this.reportCollections=reportCollections;
        this.stringArrayList=stringArrayList;
    }
    @NonNull
    @Override
    public ReportActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.activity_report_item, parent, false);
        return new ReportActivityAdapter.ViewHolder(itemView);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ReportActivityAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: reportActivity "+reportCollections.size());
        if(holder.materialTextView.getText().equals("")) {
            holder.materialTextView.setText(reportCollections.get(stringArrayList.get(position)).getActivityName());
            holder.recyclerView.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "onBindViewHolder: working checker..."+position);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        FileAdapter fileAdapter=new FileAdapter(activity,reportCollections.get(stringArrayList.get(position)).getReports(), MainActivity.myForegroundService);
        holder.recyclerView.setAdapter(fileAdapter);
        fileAdapter.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView materialTextView;
        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialTextView=itemView.findViewById(R.id.materialTextView);
            recyclerView=itemView.findViewById(R.id.recyclerView);
        }
    }
}
