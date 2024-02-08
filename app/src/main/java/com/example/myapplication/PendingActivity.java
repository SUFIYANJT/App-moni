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
import java.util.List;

public class PendingActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_activity, container, false);

        // Create dummy data
        List<ItemModel> itemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ItemModel model = new ItemModel();
            model.setItemName("item " + i);
            itemList.add(model);
        }

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPen);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Pass true for isPendingActivity to show progress indicators
        CustomAdapter customAdapter = new CustomAdapter(itemList, true, false);

        recyclerView.setAdapter(customAdapter);

        return view;
    }
}
