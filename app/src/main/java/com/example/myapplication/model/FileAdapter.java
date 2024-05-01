package com.example.myapplication.model;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.ReportTransfer;
import com.example.myapplication.Support.FileData;
import com.example.myapplication.Support.ImageSearchHelper;
import com.example.myapplication.Support.MediaStoreHelper;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.Hold;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> implements ReportTransfer{
    ArrayList<FileData> fileDatas;
    MyForegroundService foregroundService;
    Queue<Integer> queue=new LinkedList<>();
    ViewHolder viewHolder;
    Handler handler = new Handler();
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
        holder.imageView.setVisibility(View.GONE);
        holder.image.setVisibility(View.GONE);
        holder.seekBar.setVisibility(View.INVISIBLE);
        Glide.with(context).load(R.drawable.baseline_arrow_downward_24).into(holder.imageView);
        holder.textView.setVisibility(View.GONE);
        holder.button.setVisibility(View.GONE);
        if(fileDatas.get(position).getType().equals("text")){
            holder.layout.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
        } else if (fileDatas.get(position).getType().equals("audio")) {
            Log.d(TAG, "onBindViewHolder: audio part is active ");
            holder.layout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
        } else if (fileDatas.get(position).getType().equals("image")) {
            holder.layout.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
        }
        String[] spilt=fileDatas.get(position).getFile().split("/");
        holder.materialTextView.setText(spilt[spilt.length-1]);
        ContentResolver contentResolver = context.getContentResolver();
        Uri imageUri = ImageSearchHelper.searchForImage(contentResolver, spilt[spilt.length-1]);
        Log.d(TAG, "onBindViewHolder: file name is "+spilt[spilt.length-1]+imageUri);
        File file=new File(context.getFilesDir(),spilt[spilt.length-1]);
        if(file.exists()){
            holder.imageView.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.layout.setVisibility(View.GONE);
          //  holder.button.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions()
                    .fitCenter();
            Glide.with(context)
                    .load(file)
                    .apply(requestOptions)
                    .into(holder.image);
            Log.d(TAG, "onBindViewHolder: IMAGE URL"+imageUri);
        }else{
            Log.d(TAG, "onBindViewHolder: image uri is null");
        }
        Log.d(TAG, "onBindViewHolder: item name "+holder.materialTextView.getText()+" "+ fileDatas.get(position).getFile());
        Log.d(TAG, "onBindViewHolder: clicked in item"+fileDatas.get(position).getFileSize()+" "+fileDatas.get(position).getDownloaded());
        if(fileDatas.get(position).getType().equals("audio")){
            holder.seekBar.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.layout.setVisibility(View.VISIBLE);
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
            if(fileDatas.get(position).getType().equals("image")) {
                holder.imageView.setVisibility(View.GONE);
                Log.d(TAG, "onBindViewHolder: " + fileDatas.get(position).getDownloadFile().length() + " " + fileDatas.get(position).getFileSize());
                holder.image.setVisibility(View.VISIBLE);
                holder.layout.setVisibility(View.GONE);
                Glide.with(context)
                        .load(file)
                        .centerCrop().into(holder.image);
               // File file1=new File(context.getFilesDir(),)
                Log.d(TAG, "onBindViewHolder: IMAGE URL" );
            } else if (fileDatas.get(position).getType().equals("audio")) {
                Glide.with(context).load(R.drawable.baseline_play_arrow_24).into(holder.imageView);
                holder.imageView.setOnClickListener(v -> {
                    fileDatas.get(position).setPlaying(true);
                    AudioPlay(fileDatas.get(position).mediaPlayer,new File(context.getFilesDir()+"/"+spilt[spilt.length-1]),holder.seekBar,0,position);
                });
            }
        }
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    AudioPlay(fileDatas.get(position).mediaPlayer,new File(context.getFilesDir()+"/"+spilt[spilt.length-1]),holder.seekBar,0,position);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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
                        Log.d(TAG, "setFileData: imageuri is made "+imageUri);
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
    public void AudioPlay(MediaPlayer mediaPlayer, File audioFile , SeekBar seekBar, int seek, int position){
        if (audioFile != null && audioFile.exists()) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                mediaPlayer.prepare();
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.seekTo(seek);
                mediaPlayer.setVolume(1.0f,1.0f);
                mediaPlayer.start();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: playing audio ");
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        if(currentPosition==mediaPlayer.getDuration()){
                            seekBar.setProgress(0);
                            if(!mediaPlayer.isPlaying()){
                                fileDatas.get(position).setPlaying(false);
                            }
                        }else {
                            handler.postDelayed(this, 100);
                        }
                    }
                };
                handler.postDelayed(runnable,10);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to play recording", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No recording available to play", Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView materialTextView;
        SeekBar seekBar;
        TextView textView;
        ImageButton imageView;
        ProgressBar progressBar;
        ImageView image;
        LinearLayout layout;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialTextView=itemView.findViewById(R.id.textView);
            seekBar=itemView.findViewById(R.id.seekBar);
            textView=itemView.findViewById(R.id.imageView);
            imageView=itemView.findViewById(R.id.playAndPause);
            progressBar=itemView.findViewById(R.id.progressBar);
            button=itemView.findViewById(R.id.view);
            image=itemView.findViewById(R.id.image);
            layout=itemView.findViewById(R.id.linear);
        }
    }
}
