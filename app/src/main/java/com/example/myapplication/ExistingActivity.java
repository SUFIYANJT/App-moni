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

public class ExistingActivity extends Fragment implements SearchableFragment {

    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<ItemModel> itemList;

    public ExistingActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_existing_activity, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Initialize itemList with dummy data
        itemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ItemModel model = new ItemModel();
            model.setItemName("item " + i);
            itemList.add(model);
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        View emptyView =null;
        adapter = new CustomAdapter(itemList, false, false);
        recyclerView.setAdapter(adapter);

        // Set item click listener
        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel item) {
                // Show popup window
                MyBottomSheetFragmentExixting bottomSheetFragment = new MyBottomSheetFragmentExixting();
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
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

        adapter.setFilteredList(filteredList);

    }

        // Update the RecyclerView with the filtered list

    }

