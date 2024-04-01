package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Support.FileNameExtractor;
import com.example.myapplication.Support.SubmitHolder;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecyclerReport extends RecyclerView.Adapter<RecyclerReport.ItemHolder> {
    Activity activity;
    public ArrayList<SubmitHolder> submitHolders;
    Handler handler = new Handler();
    UsercardWindow usercardWindow;

    public RecyclerReport(Activity activity, ArrayList<SubmitHolder> submitHolders,UsercardWindow usercardWindow){
        this.activity=activity;
        this.submitHolders =submitHolders;
        this.usercardWindow=usercardWindow;
    }

    public RecyclerReport(Activity activity, ArrayList<SubmitHolder> submitHolders) {
        this.activity=activity;
        this.submitHolders=submitHolders;
    }

    @NonNull
    @Override
    public RecyclerReport.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerReport.ItemHolder holder, @SuppressLint("RecyclerView") int position) {
        if(submitHolders.get(position).getMode()==1){
            holder.textView.setVisibility(View.GONE);
            holder.seekBar.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
        }else if(submitHolders.get(position).getMode()==2){
            holder.materialTextView.setVisibility(View.GONE);
            holder.textView.setVisibility(View.GONE);
        }else if(submitHolders.get(position).getMode()==3){
            holder.textView.setVisibility(View.GONE);
            holder.seekBar.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: isPlaying "+submitHolders.get(position).isPlaying());
            submitHolders.get(position).setPlaying(true);
            if(submitHolders.get(position).mediaPlayer.isPlaying()){
                Glide.with(activity)
                        .load(R.drawable.baseline_pause_24)
                        .into(holder.imageView);
            }else{
                Glide.with(activity)
                        .load(R.drawable.baseline_play_arrow_24)
                        .into(holder.imageView);
            }
            AudioPlay(submitHolders.get(position).mediaPlayer,submitHolders.get(position).getFile(),holder.seekBar,0,position);
        });
        if(submitHolders.get(position).getImageFile()!=null) {
            Log.d(TAG, "onBindViewHolder: uri is null "+FileNameExtractor.getFileName(activity, submitHolders.get(position).getImageFile()));
            holder.materialTextView.setText(FileNameExtractor.getFileName(activity, submitHolders.get(position).getImageFile()));
            Log.d(TAG, "onBindViewHolder: text is "+holder.materialTextView.getText());
            holder.materialTextView.setOnClickListener(v -> {
                Log.d(TAG, "onClick: on click worked ");
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(submitHolders.get(position).getImageFile(), "image/*");
                viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (viewIntent.resolveActivity(activity.getPackageManager()) != null) {
                    // Start the activity if there is an app available to handle the intent
                    activity.startActivity(viewIntent);
                } else {
                    // Handle the case where no app is available to view the image
                    // You may display a toast or a dialog to inform the user
                }
            });
            holder.textView.setText(submitHolders.get(position).getTextView());
        }

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    AudioPlay(submitHolders.get(position).mediaPlayer,submitHolders.get(position).getFile(),holder.seekBar,progress,position);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    @Override
    public int getItemCount() {
        return submitHolders.size();
    }
    public void AudioPlay(MediaPlayer mediaPlayer, File audioFile ,SeekBar seekBar,int seek,int position){
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
                        if(mediaPlayer != null){
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            seekBar.setProgress(currentPosition);
                            if(currentPosition==mediaPlayer.getDuration()){
                                seekBar.setProgress(0);
                                if(!mediaPlayer.isPlaying()){
                                    submitHolders.get(position).setPlaying(false);
                                }
                            }
                        }
                        handler.postDelayed(this, 100);
                    }
                };
                handler.postDelayed(runnable,10);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Failed to play recording", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "No recording available to play", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ItemHolder holder) {
        super.onViewDetachedFromWindow(holder);
        handler.removeCallbacksAndMessages(null);
        Log.d(TAG, "onViewDetachedFromWindow: removing all handlers ");
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        MaterialTextView materialTextView;
        SeekBar seekBar;
        TextView textView;
        ImageView imageView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            materialTextView=itemView.findViewById(R.id.textView);
            seekBar=itemView.findViewById(R.id.seekBar);
            textView=itemView.findViewById(R.id.imageView);
            imageView=itemView.findViewById(R.id.playAndPause);
        }
    }
}
