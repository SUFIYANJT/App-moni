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

public class IssuedActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issued_activity, container, false);


        // Create dummy data
        List<ItemModel> itemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.setItemName("item " + i);
            itemList.add(itemModel);
        }

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewIss);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Pass true for isInspecterReview and false for isPendingActivity
        CustomAdapter customAdapter = new CustomAdapter(itemList, false, false);

        recyclerView.setAdapter(customAdapter);

        // Set item click listener
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel item) {
                // Show popup window
                BottomSheetDialogFragmentIssued bottomSheetFragment = new BottomSheetDialogFragmentIssued();
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        return view;
    }
}
