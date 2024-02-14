package com.example.myapplication;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class PendingActivity extends Fragment  implements SearchableFragment{
    List<ItemModel> itemList;
    CustomAdapter customAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_activity, container, false);

        // Create dummy data

        itemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ItemModel model = new ItemModel();
            model.setItemName("item " + i);
            itemList.add(model);
        }

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPen);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Pass true for isPendingActivity to show progress indicators

        customAdapter = new CustomAdapter(itemList, true, false);

        recyclerView.setAdapter(customAdapter);

        // Inflate the menu
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
