package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.model.ItemModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Activity> itemList;
    private OnItemClickListener listener;
    private boolean isPendingActivity;
    private boolean isInspecterReview;
    private boolean isIssuedActivity;
    private FragmentManager fragmentManager;

    public void setFilteredList(List<Activity> filteredList) {

        this.itemList = filteredList;

        notifyDataSetChanged();

    }
    public CustomAdapter(List<Activity> itemList, boolean isPendingActivity, boolean isInspecterReview,boolean isIssuedActivity) {
        this.itemList = itemList;
        this.isPendingActivity = isPendingActivity;
        this.isInspecterReview = isInspecterReview;
        this.isIssuedActivity=isIssuedActivity;
    }
    public CustomAdapter(ArrayList<Activity> activities){
        this.itemList=activities;
        isIssuedActivity=true;
        isInspecterReview=false;
        isPendingActivity=false;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Activity item,int position);
        void onItemClick(Activity item,int position,String message);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity item = itemList.get(position);
        holder.textView.setText(item.activityName);
        if(!isIssuedActivity) {
            holder.itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(item, position);
                }
            });
        }else{
            holder.imageView2.setOnClickListener(v -> {
                if(listener!=null){
                    listener.onItemClick(item,position,"reset the activity status to inert");
                }
            });
            holder.imageButton.setOnClickListener(v -> {
                if(listener!=null){
                    listener.onItemClick(item,position,"do you want to delete this activity");
                }
            });
        }
        holder.linearProgressIndicator.setVisibility(View.GONE);
        holder.materialTextView.setText(item.activityDescription);
        holder.textViewAssignedTo.setText("Assigned to "+itemList.get(position).assigned_to_user);
        holder.textViewAssignedBy.setText("Assigned by "+itemList.get(position).activityCreator);
        if(isIssuedActivity){
            Log.d(TAG, "onBindViewHolder: imageButton visibliltiy true ");
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView2.setVisibility(View.VISIBLE);
        }else{
            holder.imageButton.setVisibility(View.GONE);
            holder.imageView2.setVisibility(View.GONE);
        }
        if (isInspecterReview) {
            // For inspector review, show only the image view
            holder.imageView.setVisibility(View.VISIBLE);
            Log.d(TAG, "onBindViewHolder: inspectorReview rendering");
            holder.linearProgressIndicator.setVisibility(View.GONE);
        } else {
            // For other cases, show both image view and linear progress indicator
            holder.imageView.setVisibility(View.GONE);
            holder.linearProgressIndicator.setVisibility(View.VISIBLE);

            // Set progress visibility based on the current fragment
            if (isPendingActivity) {
                holder.linearProgressIndicator.setVisibility(View.VISIBLE);
                // Set progress (value between 0 and 1)
                holder.linearProgressIndicator.setProgress(50); // Example: 50%
            } else {
                holder.linearProgressIndicator.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
