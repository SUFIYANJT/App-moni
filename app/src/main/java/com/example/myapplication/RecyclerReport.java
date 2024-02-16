package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Support.SubmitHolder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecyclerReport extends RecyclerView.Adapter<RecyclerReport.ItemHolder> {
    Activity activity;
    ArrayList<SubmitHolder> submitHolders;
    UsercardWindow usercardWindow;
    public RecyclerReport(Activity activity, ArrayList<SubmitHolder> submitHolders,UsercardWindow usercardWindow){
        this.activity=activity;
        this.submitHolders=submitHolders;
        this.usercardWindow=usercardWindow;
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
            holder.isPlaying=true;
            usercardWindow.playAudio(submitHolders.get(position).getFile(),0,holder.seekBar);
        });
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    usercardWindow.playAudio(submitHolders.get(position).getFile(), progress, holder.seekBar);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return submitHolders.size();
    }
    class ItemHolder extends RecyclerView.ViewHolder {
        MaterialTextView materialTextView;
        SeekBar seekBar;
        TextView textView;
        ImageView imageView;
        boolean isPlaying=false;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            materialTextView=itemView.findViewById(R.id.textView);
            seekBar=itemView.findViewById(R.id.seekBar);
            textView=itemView.findViewById(R.id.imageView);
            imageView=itemView.findViewById(R.id.playAndPause);
        }
    }
}
