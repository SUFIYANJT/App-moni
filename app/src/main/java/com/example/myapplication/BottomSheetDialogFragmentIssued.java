package com.example.myapplication;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.service.MyForegroundService;

public class BottomSheetDialogFragmentIssued extends DialogFragment {
    public static String TAG="Maintenance";
    private Activity activity;
    private Integer position;
    ItemModel itemModel;
    private String message;
    public BottomSheetDialogFragmentIssued(Activity activity,Integer position,String message){
        Log.d(TAG, "BottomSheetDialogFragmentIssued: called on click "+activity.activityId);
        this.activity=activity;
        this.position=position;
        this.message=message;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet_layout_issued, container, false);
        itemModel= new ViewModelProvider(requireActivity()).get(ItemModel.class);
        TextView textView=view.findViewById(R.id.promptTextView);
        textView.setText(message);
        Button button=view.findViewById(R.id.confirmButtonIss);
        button.setOnClickListener(v -> {
            activity.activity_satuts_id=1;
            activity.assigned_to=0;
            activity.assigned_to_user="None";
            Log.d(TAG, "onCreateView: activity id is "+activity.activityId);
            itemModel.setIntegerMutableLiveData2(position);
            if(message.contains("delete")){
               activity.change="delete";
            }else {
                activity.change="update";
            }
            MyForegroundService.foregroundService.setChangeActivity(activity);
            dismiss();
        });
        return view;
    }
}
