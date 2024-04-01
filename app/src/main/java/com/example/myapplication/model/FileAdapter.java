package com.example.myapplication.model;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.ReportTransfer;
import com.example.myapplication.Support.FileData;
import com.example.myapplication.Support.ImageSearchHelper;
import com.example.myapplication.Support.MediaStoreHelper;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> implements ReportTransfer{
    ArrayList<FileData> fileDatas;
    MyForegroundService foregroundService;
    Queue<Integer> queue=new LinkedList<>();
    ViewHolder viewHolder;
    int fileSize=0;
    int currentFileSize=0;
    android.app.Activity context;
    public FileAdapter(Activity context, ArrayList<FileData> fileDatas, MyForegroundService foregroundService){
        this.context=context;
        this.fileDatas=fileDatas;
        this.foregroundService=foregroundService;
    }
    @NonNull
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileAdapter.ViewHolder holder, int position) {
        holder.imageView.setVisibility(View.VISIBLE);
        holder.seekBar.setVisibility(View.INVISIBLE);
        Glide.with(context).load(R.drawable.baseline_arrow_downward_24).into(holder.imageView);
        holder.textView.setVisibility(View.GONE);
        holder.button.setVisibility(View.INVISIBLE);
        String[] spilt=fileDatas.get(position).getFile().split("/");
        holder.materialTextView.setText(spilt[spilt.length-1]);
        ContentResolver contentResolver = context.getContentResolver();
        Uri imageUri = ImageSearchHelper.searchForImage(contentResolver, spilt[spilt.length-1]);
        if(imageUri!=null){
            holder.imageView.setVisibility(View.GONE);
            holder.button.setVisibility(View.VISIBLE);
            Log.d(TAG, "onBindViewHolder: "+imageUri);
        }
        Log.d(TAG, "onBindViewHolder: item name "+holder.materialTextView.getText()+" "+ fileDatas.get(position).getFile());
        Log.d(TAG, "onBindViewHolder: clicked in item"+fileDatas.get(position).getFileSize()+" "+fileDatas.get(position).getDownloaded());
        if(fileDatas.get(position).getType().equals("audio")){
            holder.seekBar.setVisibility(View.VISIBLE);
        }
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.imageView.setOnClickListener(v -> {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setMax(fileDatas.get(position).getFileSize());
            holder.progressBar.setProgress(fileDatas.get(position).getDownloaded(),true);
            if(queue.isEmpty()) {
                queue.offer(position);
                Log.d(TAG, "onBindViewHolder: called if"+queue.isEmpty());
                getFile(position);
            }else{
                queue.offer(position);
                Log.d(TAG, "onBindViewHolder: called if else");
            }
        });
        if(fileDatas.get(position).getDownloaded()==fileDatas.get(position).getFileSize()){
            holder.imageView.setVisibility(View.GONE);
            Log.d(TAG, "onBindViewHolder: "+fileDatas.get(position).getDownloadFile().length()+" "+fileDatas.get(position).getFileSize());
            holder.button.setVisibility(View.VISIBLE);

        }
        holder.button.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: clicked to view the image ....."+imageUri);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(imageUri, "image/*");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        });
    }
    public void getFile(int position){
        Log.d(TAG, "getFile: is called...");
        foregroundService.getFile(fileDatas.get(position).getFile(),this,position);
    }

    @Override
    public int getItemCount() {
        return fileDatas.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setFileData(byte[] data) {
        Log.d(TAG, "setFileData: is called for creating file..."+queue.isEmpty());
        if (!queue.isEmpty()) {
            int index = queue.element(); // Get the index of the element at the front of the queue
            if (index >= 0 && index < fileDatas.size()) { // Check if the index is valid
                FileData fileData = fileDatas.get(index); // Get the FileData object from the list
                String[] dismantle=fileData.getFile().split("/");
                File file=new File(context.getFilesDir(),dismantle[dismantle.length-1]);
                try{
                    if(!file.exists()){
                        if(file.createNewFile()){
                            Log.d(TAG, "setFileData: file is created.."+file.getAbsolutePath());
                            FileOutputStream fileOutputStream=new FileOutputStream(file);
                            fileDatas.get(queue.element()).setFileOutputStream(fileOutputStream);
                        }else{
                            Log.d(TAG, "setFileData: a error occurred...");
                        }
                    }else{
                        if(fileDatas.get(queue.element()).getFileOutputStream()==null) {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileDatas.get(queue.element()).setFileOutputStream(fileOutputStream);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d(TAG, "setFileData: "+fileSize+" "+currentFileSize+" "+file.length());
                    fileSize=fileDatas.get(queue.element()).getFileSize();
                    currentFileSize=fileDatas.get(queue.element()).getDownloaded();
                    if(fileSize-currentFileSize>=1024) {
                        fileDatas.get(queue.element()).getFileOutputStream().write(data);
                        fileDatas.get(queue.element()).getFileOutputStream().flush();
                        fileDatas.get(queue.element()).setDownloaded(currentFileSize+1024);
                    }else{
                        Log.d(TAG, "setFileData: else is working and going to poll the queue..."+file.getAbsolutePath());
                        fileDatas.get(queue.element()).getFileOutputStream().write(data,0,fileSize-currentFileSize);
                        fileDatas.get(queue.element()).getFileOutputStream().flush();
                        fileDatas.get(queue.element()).setDownloaded(currentFileSize+(fileSize-currentFileSize));
                        fileDatas.get(queue.element()).setDownloadFile(file);
                        File imageFile = new File(context.getFilesDir(), dismantle[dismantle.length-1]);
                        Uri imageUri = MediaStoreHelper.saveImageToMediaStore(context, imageFile);
                        queue.poll();
                        if(!queue.isEmpty()) {
                            Log.d(TAG, "setFileData: queue is not empty ");
                            getFile(queue.element());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Log.d(TAG, "setFileData: queue is empty...");
        }
        context.runOnUiThread(this::notifyDataSetChanged);

    }

    @Override
    public void fileSize(int size) {
        if(!queue.isEmpty()) {
            fileDatas.get(queue.element()).setFileSize(size);
        }
        Log.d(TAG, "fileSize: checking queue "+queue.isEmpty());
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView materialTextView;
        SeekBar seekBar;
        TextView textView;
        ImageButton imageView;
        ProgressBar progressBar;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialTextView=itemView.findViewById(R.id.textView);
            seekBar=itemView.findViewById(R.id.seekBar);
            textView=itemView.findViewById(R.id.imageView);
            imageView=itemView.findViewById(R.id.playAndPause);
            progressBar=itemView.findViewById(R.id.progressBar);
            button=itemView.findViewById(R.id.view);
        }
    }
}
