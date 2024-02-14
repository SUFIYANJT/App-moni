package com.example.myapplication;

import android.content.Intent;
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

public class InspectorReview extends Fragment  implements SearchableFragment{
    List<ItemModel> itemList;
    CustomAdapter customAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspector_review, container, false);

        // Hide the FAB in InspectorReview fragment
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        // Create dummy data

        itemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.setItemName("item " + i);
            itemList.add(itemModel);
        }

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewins);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Pass false for isPendingActivity as it's InspectorReview

        customAdapter = new CustomAdapter(itemList, false, false);

        recyclerView.setAdapter(customAdapter);

        // Set item click listener
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel item) {
                // Start NewWindow activity when CardView is clicked
                Intent intent = new Intent(requireContext(), NewWindow.class);
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void updateSearchQuery(String query) {
        // Filter the list based on the search query
        List<ItemModel> filteredList = new ArrayList<>();
        for (ItemModel item : itemList) {

            if (item.getItemName().toLowerCase().contains(query.toLowerCase())) {

                filteredList.add(item);

            }

        }
        // Update the RecyclerView with the filtered list
        customAdapter.setFilteredList(filteredList);

    }
}
