package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.model.MyBroadcastReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IssuedActivity extends Fragment  implements SearchableFragment {
    List<ItemModel> itemList;
    CustomAdapter customAdapter;
    ItemModel itemModel;
    ArrayList<Activity> activities=new ArrayList<>();
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issued_activity, container, false);
        itemModel = new ViewModelProvider(requireActivity()).get(ItemModel.class);
        itemModel.getIssuedactivityMutableLiveData().observe(getViewLifecycleOwner(), newData -> {
            activities.clear();
            activities.addAll(newData);
            Log.d(TAG, "onCreateView: issued activity "+newData.size());
            customAdapter.notifyDataSetChanged();
        });
        itemModel.getIntegerMutableLiveData2().observe(getViewLifecycleOwner(),integer -> {
            if(activities.size()>0) {
                Activity activity = activities.get(integer);
                activity.change = "create";
                activity.uiChange = "update";
                activities.remove(activities.get(integer));
                customAdapter.notifyDataSetChanged();
            }
        });
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewIss);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Pass true for isInspecterReview and false for isPendingActivity
        customAdapter = new CustomAdapter(activities);
        recyclerView.setAdapter(customAdapter);
        // Set item click listener
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Activity item, int position) {}
            @Override
            public void onItemClick(Activity item, int position, String message) {
                BottomSheetDialogFragmentIssued bottomSheetFragment = new BottomSheetDialogFragmentIssued(item,position,message);
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
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
