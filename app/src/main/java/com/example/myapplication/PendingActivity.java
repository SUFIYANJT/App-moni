package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.model.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PendingActivity extends Fragment  implements SearchableFragment{
    List<ItemModel> itemList;
    CustomAdapter customAdapter;
    ItemModel itemModel;
    ArrayList<Activity> activities=new ArrayList<>();
    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_activity, container, false);
        itemModel = new ViewModelProvider(requireActivity()).get(ItemModel.class);
        itemModel.getPendingactivityMutableLiveData().observe(getViewLifecycleOwner(), newData -> {
            activities.clear();
            activities.addAll(newData);
            Log.d(TAG, "onCreateView: "+newData.size());
            customAdapter.notifyDataSetChanged();
        });
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPen);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        customAdapter = new CustomAdapter(activities, true, false);
        recyclerView.setAdapter(customAdapter);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pending, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_mark_complete) {
            // Handle the "Mark as Complete" menu item click
            Toast.makeText(requireContext(), "Mark as Complete clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void updateSearchQuery(String query) {
        // Filter the list based on the search query
        List<Activity> filteredList = new ArrayList<>();
        for (Activity item:activities) {
            if (item.activityName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        customAdapter.setFilteredList(filteredList);

    }
}
