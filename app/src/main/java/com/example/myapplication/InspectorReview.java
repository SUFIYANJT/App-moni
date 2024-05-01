package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InspectorReview extends Fragment  implements SearchableFragment{
    List<ItemModel> itemList;
    CustomAdapter customAdapter;
    ItemModel itemModel;
    ArrayList<Activity> activities=new ArrayList<>();
    MyForegroundService foregroundService;
    public InspectorReview(MyForegroundService foreground) {
        this.foregroundService=foreground;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspector_review, container, false);
        itemModel = new ViewModelProvider(requireActivity()).get(ItemModel.class);
        itemModel.getReviewactivityMutableLiveData().observe(getViewLifecycleOwner(), newData -> {
            activities.clear();
            activities.addAll(newData);
            Log.d(TAG, "onCreateView: Review "+newData.size());
            customAdapter.notifyDataSetChanged();
            Log.d(TAG, "onCreateView: getting the size "+customAdapter.getItemCount()+" "+activities.size());
        });
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
       // fab.setVisibility(View.GONE);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewins);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        customAdapter = new CustomAdapter(view.getContext(),activities, false, true,false);
        recyclerView.setAdapter(customAdapter);
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Activity item, int position) {
                Intent intent = new Intent(requireContext(), NewWindow.class);
                intent.putExtra("activity",item);
                startActivity(intent);
            }

            @Override
            public void onItemClick(Activity item, int position, String message) {

            }
        });

        return view;
    }
    @Override
    public void updateSearchQuery(String query) {
        // Filter the list based on the search query
        List<Activity> filteredList = new ArrayList<>();
        for (Activity item : activities) {
            if (item.activityName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        // Update the RecyclerView with the filtered list
        customAdapter.setFilteredList(filteredList);

    }
}
