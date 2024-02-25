package com.example.myapplication;

import static androidx.core.content.ContextCompat.registerReceiver;

import static com.example.myapplication.LoginActivity.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.Support.Machine;
import com.example.myapplication.Support.MachinePreference;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.model.MachineAdapter;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextInputLayout activityNameInputLayout;
    private TextInputLayout activityDescriptionInputLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView1;
    private AutoCompleteTextView autoCompleteTextView2;
    private Button confirmButton;
    private ArrayList<Machine> machines;
    private  ArrayList<Machine> components;
    private ArrayList<Machine> schedules;
    MachineAdapter machine;
    MachineAdapter component;
    MachineAdapter schedule;
    ItemModel itemModel;
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MachinePreference machinePreference = new MachinePreference(context);
            String type=intent.getStringExtra("type");
            Machine data=(Machine) intent.getSerializableExtra("data");
            Log.d(TAG, "onReceive: Receiving data from background of type "+type);
            if(Objects.equals(type, "machine")){
                machines.add(data);
                machinePreference.saveMachines("machine",machines);
                machine.notifyDataSetChanged();
                Log.d(TAG, "onReceive: machine data are "+machines.size());
            } else if (Objects.requireNonNull(type).equals("component")) {
                components.add(data);
                machinePreference.saveMachines("component",machines);
                component.notifyDataSetChanged();
                Log.d(TAG, "onReceive: component data are "+components.size());
            } else if (type.equals("schedule")) {
                schedules.add(data);
                machinePreference.saveMachines("schedule",machines);
                schedule.notifyDataSetChanged();
                Log.d(TAG, "onReceive: schedule data are "+schedules.size());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        activityNameInputLayout = view.findViewById(R.id.Acivity_name);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView1 = view.findViewById(R.id.autoCompleteTextView1);
        autoCompleteTextView2 = view.findViewById(R.id.autoCompleteTextView2);
        confirmButton = view.findViewById(R.id.confirmButton);
        IntentFilter filter = new IntentFilter("com.example.ACTION_SEND_DATA_BOTTOM_SHEET");
        LocalBroadcastManager.getInstance(requireContext().getApplicationContext()).registerReceiver(broadcastReceiver, filter);
        machines = new ArrayList<>();
        components = new ArrayList<>();
        schedules = new ArrayList<>();
        machine = new MachineAdapter(requireContext(),machines);
        component = new MachineAdapter(requireContext(),components);
        schedule = new MachineAdapter(requireContext(),schedules);
        autoCompleteTextView.setAdapter(machine);
        autoCompleteTextView1.setAdapter(component);
        autoCompleteTextView2.setAdapter(schedule);

        MyForegroundService.foregroundService.getComponent();
        MyForegroundService.foregroundService.getMachine();
        MyForegroundService.foregroundService.getSchedule();
        confirmButton.setOnClickListener(view1 -> confirmInput());
        activityDescriptionInputLayout=view.findViewById(R.id.Description);
        return view;
    }
    //docker run --name my-redis-container -p 6379:6379 -d redis
    private void confirmInput() {
        // Validate input, for example:
        if (Objects.requireNonNull(activityNameInputLayout.getEditText()).getText().toString().isEmpty()) {
            activityNameInputLayout.setError("Activity name cannot be empty");
            return;
        } else if (Objects.requireNonNull(activityDescriptionInputLayout.getEditText()).getText().toString().isEmpty()) {
            activityDescriptionInputLayout.setError("Insert the Description/Procedure");
        } else if (autoCompleteTextView.getText().toString().isEmpty()) {
            autoCompleteTextView.setError("select a machine");
        }else if(autoCompleteTextView1.getText().toString().isEmpty()){
            autoCompleteTextView1.setError("select a component");
        } else if (autoCompleteTextView2.getText().toString().isEmpty()) {
            autoCompleteTextView2.setError("select a schedule ");
        }
        Toast.makeText(requireContext(), "Activity created" ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext().getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }
}
