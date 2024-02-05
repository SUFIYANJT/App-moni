package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.myapplication.helper.SuppotClass.DataTable;
import com.example.myapplication.helper.ui.CustomRecycler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ExistingActivity extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<DataTable> dataTables;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_existing_activity, container, false);
        recyclerView=view.findViewById(R.id.recycler_existing);
        dataTables=new ArrayList<>();
        for(int i=0;i<4;i++){
            DataTable dataTable=new DataTable();
            dataTable.setId(i);
            dataTable.setName("name"+i);
            dataTable.setDescription("Description"+i);
            dataTables.add(dataTable);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new CustomRecycler(view.getContext(),dataTables));
        return view;
    }

    }


