package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InspectorReview extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspector_review, container, false);

        // Hide the FAB in InspectorReview fragment
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

            // Create dummy data
            List<ItemModel> itemList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                itemList.add(new ItemModel("Item " + i));
            }

            // Set up RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewins);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            CustomAdapter customAdapter = new CustomAdapter(itemList);
            recyclerView.setAdapter(customAdapter);

            return view;
        // Your InspectorReview fragment logic goes here

    }
}
