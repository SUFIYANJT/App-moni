package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExistingActivity extends Fragment implements SearchableFragment {

    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<ItemModel> itemList;
    private ItemModel itemModel;
    ArrayList<Activity> activities=new ArrayList<>();

    public ExistingActivity() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_existing_activity, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        itemModel = new ViewModelProvider(requireActivity()).get(ItemModel.class);
        itemModel.getActivityMutableLiveData().observe(getViewLifecycleOwner(), newData -> {
            activities.clear();
            activities.addAll(newData);
            Log.d(TAG, "onCreateView: "+newData.size());
            adapter.notifyDataSetChanged();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        View emptyView =null;
        adapter = new CustomAdapter(activities, false, false);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        adapter.setOnItemClickListener(item -> {
            MyBottomSheetFragmentExixting bottomSheetFragment = new MyBottomSheetFragmentExixting();
            bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
        });

        return view;
    }

    @Override
    public void updateSearchQuery(String query) {
        List<Activity> filteredList = new ArrayList<>();
        for (Activity item:activities) {
            if (item.activityName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.setFilteredList(filteredList);

    }

}

