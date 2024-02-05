package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.ItemModel;

import java.util.ArrayList;
import java.util.List; // Add this import statement

public class IssuedActivity extends Fragment {

    @Override
    
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_issued_activity, container, false);

            // Create dummy data
            List<ItemModel> itemList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                itemList.add(new ItemModel("Item " + i));
            }

            // Set up RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewIss);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            CustomAdapter customAdapter = new CustomAdapter(itemList);
            recyclerView.setAdapter(customAdapter);

            return view;
        }
    }