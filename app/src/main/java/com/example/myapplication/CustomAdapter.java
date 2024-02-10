package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.ItemModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<ItemModel> itemList;
    private OnItemClickListener listener;
    private boolean isPendingActivity;
    private boolean isInspecterReview;

    public CustomAdapter(List<ItemModel> itemList, boolean isPendingActivity, boolean isInspecterReview) {
        this.itemList = itemList;
        this.isPendingActivity = isPendingActivity;
        this.isInspecterReview = isInspecterReview;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemModel item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.textView.setText(item.getItemName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            }
        });

        if (isInspecterReview) {
            // For inspector review, show only the image view
            holder.imageView.setVisibility(View.VISIBLE);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textView = itemView.findViewById(R.id.textView);
            linearProgressIndicator = itemView.findViewById(R.id.linear_progress_indicator);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
