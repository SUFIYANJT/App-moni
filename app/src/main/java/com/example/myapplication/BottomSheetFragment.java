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
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.Machine;
import com.example.myapplication.Support.MachinePreference;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.model.MachineAdapter;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextInputLayout activityNameInputLayout;
    private TextInputLayout activityDescriptionInputLayout;
    private TextInputLayout activityMachine;
    private TextInputLayout activityComponent;
    private TextInputLayout activitySchedule;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView1;
    private AutoCompleteTextView autoCompleteTextView2;
    private Button confirmButton;
    View view;
    private ArrayList<Machine> machines = new ArrayList<>();
    private  ArrayList<Machine> components = new ArrayList<>();
    private ArrayList<Machine> schedules = new ArrayList<>();
    ItemModel itemModel;
    MachineAdapter machine;
    MachineAdapter component;
    MachineAdapter schedule;
    BottomSheetFragment bottomSheetFragment;
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            itemModel = new ViewModelProvider(requireActivity()).get(ItemModel.class);
            String type=intent.getStringExtra("type");
            String modal=intent.getStringExtra("modal");
            String change=intent.getStringExtra("change");
            if(type.equals("ui")){
                bottomSheetFragment.dismiss();
                if(change.equals("update"))
                    Snackbar.make(view,"Activity Changed its properties",Snackbar.LENGTH_SHORT).show();
                else if (change.equals("create"))
                    Snackbar.make(view,"Activity created",Snackbar.LENGTH_SHORT).show();
            }else {

                Machine data = (Machine) intent.getSerializableExtra("data");
                Log.d(TAG, "onReceive: Receiving data from background of type " + type);
                if (Objects.equals(type, "machine")) {
                    machines.add(data);
                    itemModel.setMachineMutableLiveData(machines);
                    machine.notifyDataSetChanged();
                    Log.d(TAG, "onReceive: machine data are " + machines.size());
                } else if (Objects.requireNonNull(type).equals("component")) {
                    components.add(data);
                    itemModel.setComponentMutableLiveData(components);
                    component.notifyDataSetChanged();
                    Log.d(TAG, "onReceive: component data are " + components.size());
                } else if (type.equals("schedule")) {
                    schedules.add(data);
                    itemModel.setScheduleMutableLiveData(schedules);
                    schedule.notifyDataSetChanged();
                    Log.d(TAG, "onReceive: schedule data are " + schedules.size());
                }
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        activityNameInputLayout = view.findViewById(R.id.Acivity_name);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView1 = view.findViewById(R.id.autoCompleteTextView1);
        autoCompleteTextView2 = view.findViewById(R.id.autoCompleteTextView2);
        confirmButton = view.findViewById(R.id.confirmButton);
        machine = new MachineAdapter(requireContext(),machines);
        activityMachine=view.findViewById(R.id.textInputDropdown);
        activityComponent=view.findViewById(R.id.textInputDropdown1);
        activitySchedule=view.findViewById(R.id.textInputDropdown2);
        component = new MachineAdapter(requireContext(),components);
        schedule = new MachineAdapter(requireContext(),schedules);
        bottomSheetFragment=this;
        IntentFilter filter = new IntentFilter("com.example.ACTION_SEND_DATA_BOTTOM_SHEET");
        LocalBroadcastManager.getInstance(requireContext().getApplicationContext()).registerReceiver(broadcastReceiver, filter);
        autoCompleteTextView.setAdapter(machine);
        autoCompleteTextView1.setAdapter(component);
        autoCompleteTextView2.setAdapter(schedule);
        if(machines.size()==0&&components.size()==0&&schedules.size()==0) {
            Log.d(TAG, "onCreateView: all are null ");
            MyForegroundService.foregroundService.getComponent();
            MyForegroundService.foregroundService.getMachine();
            MyForegroundService.foregroundService.getSchedule();
        }
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
            activityMachine.setError("select a machine");
        }else if(autoCompleteTextView1.getText().toString().isEmpty()){
            activityComponent.setError("select a component");
        } else if (autoCompleteTextView2.getText().toString().isEmpty()) {
            activitySchedule.setError("select a schedule ");
        }else {
            Activity activity = new Activity();
            activity.activityName = activityNameInputLayout.getEditText().getText().toString();
            activity.activityDescription = activityDescriptionInputLayout.getEditText().getText().toString();
            activity.activity_satuts_id = 1;
            int lengthm = autoCompleteTextView.getText().toString().length();
            int lengthc = autoCompleteTextView1.getText().toString().length();
            int lengths = autoCompleteTextView2.getText().toString().length();
            activity.machineId = Integer.parseInt(String.valueOf(autoCompleteTextView.getText().toString().charAt(lengthm - 1)));
            activity.componentId = Integer.parseInt(String.valueOf(autoCompleteTextView1.getText().toString().charAt(lengthc - 1)));
            activity.scheduleId = Integer.parseInt(String.valueOf(autoCompleteTextView2.getText().toString().charAt(lengths - 1)));
            Log.d(TAG, "confirmInput: ");
            Date date = new Date();
            activity.issued_date = String.valueOf(date.getTime());
            activity.assigned_to_user="None";
            activity.assigned_to=0;
            MyForegroundService.foregroundService.CreateActivity(activity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext().getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }
}
