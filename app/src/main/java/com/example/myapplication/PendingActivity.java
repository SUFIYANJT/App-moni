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
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PendingActivity extends Fragment  implements SearchableFragment{
    List<ItemModel> itemList;
    CustomAdapter customAdapter;
    ItemModel itemModel;
    ArrayList<Activity> activities=new ArrayList<>();
    MyForegroundService foregroundService;
    public PendingActivity(MyForegroundService foreground) {
        this.foregroundService=foreground;
    }

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
            Log.d(TAG, "onCreateView: pending activity"+newData.size());
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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPen);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        customAdapter = new CustomAdapter(view.getContext(),activities, true, false,false);
        recyclerView.setAdapter(customAdapter);
        setHasOptionsMenu(true);
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Activity item, int position) {
                Log.d(TAG, "onItemClick: called in the pending ");
                MyBottomSheetFragmentExixting bottomSheetFragment = new MyBottomSheetFragmentExixting(item,activities,position,foregroundService);
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            }
            @Override
            public void onItemClick(Activity item, int position, String message) {
                Log.d(TAG, "onItemClick: called in pending");
                BottomSheetDialogFragmentIssued bottomSheetDialogFragmentIssued=new BottomSheetDialogFragmentIssued(item,position,message,foregroundService);
                bottomSheetDialogFragmentIssued.show(getParentFragmentManager(),bottomSheetDialogFragmentIssued.getTag());
            }
        });

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
