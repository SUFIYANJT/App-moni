package com.example.myapplication.helper.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.helper.SuppotClass.DataTable;

import java.util.ArrayList;

public class CustomRecycler extends RecyclerView.Adapter<CustomRecycler.ItemHolder> {
    ArrayList<DataTable> dataTables;
    Context context;
    public CustomRecycler(Context context, ArrayList<DataTable> dataTables){
        this.dataTables=dataTables;
        this.context=context;
    }
    @NonNull
    @Override
    public CustomRecycler.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        ItemHolder itemHolder=new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecycler.ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataTables.size();
    }
    class ItemHolder extends RecyclerView.ViewHolder{

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
