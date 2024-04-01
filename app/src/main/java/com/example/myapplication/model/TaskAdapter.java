package com.example.myapplication.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Support.Task;
import com.example.myapplication.UsercardWindow;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    ArrayList<Task> tasks;
    MyForegroundService foregroundService;
    Context context;
    public TaskAdapter(Context context, ArrayList<Task> tasks, MyForegroundService foregroundService){
        this.tasks=tasks;
        this.context=context;
        this.foregroundService=foregroundService;
    }
    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        holder.imageView.setVisibility(View.GONE);
        holder.imageView2.setVisibility(View.GONE);
        holder.imageButton.setVisibility(View.GONE);
        holder.textView.setText(tasks.get(position).getActivityName());
        holder.linearProgressIndicator.setVisibility(View.GONE);
        holder.materialTextView.setText(tasks.get(position).getActivityDescrption());
        holder.textViewAssignedBy.setText(tasks.get(position).getActivityCreator());
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, UsercardWindow.class);
            intent.putExtra("task",(Serializable) tasks.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;
        LinearProgressIndicator linearProgressIndicator;
        ImageView imageView;
        MaterialTextView materialTextView,textViewAssignedTo,textViewAssignedBy;
        ImageView imageButton,imageView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textView = itemView.findViewById(R.id.textView);
            linearProgressIndicator = itemView.findViewById(R.id.linear_progress_indicator);
            imageView = itemView.findViewById(R.id.imageView);
            imageView2 = itemView.findViewById(R.id.imageButtonReset);
            imageButton=itemView.findViewById(R.id.imageButtonDel);
            materialTextView=itemView.findViewById(R.id.materialDescription);
            textViewAssignedTo=itemView.findViewById(R.id.text_assign_to);
            textViewAssignedBy=itemView.findViewById(R.id.text_assign_by);
        }
    }
}
