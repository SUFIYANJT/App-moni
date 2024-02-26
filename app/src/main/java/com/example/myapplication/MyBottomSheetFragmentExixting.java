package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.Machine;
import com.example.myapplication.Support.User;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.model.MachineAdapter;
import com.example.myapplication.model.UserAdapter;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MyBottomSheetFragmentExixting extends BottomSheetDialogFragment  {
    public MyBottomSheetFragmentExixting(){

    }
    Activity activity;
    TextInputLayout textInputLayout1;
    AutoCompleteTextView autoCompleteTextView3;
    TextInputLayout textInputLayout3;
    MachineAdapter machine;
    MachineAdapter component;
    MachineAdapter schedule;
    private ArrayList<Machine> machines = new ArrayList<>();
    private  ArrayList<Machine> components = new ArrayList<>();
    private ArrayList<Machine> schedules = new ArrayList<>();
    ItemModel itemModel;
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView1;
    AutoCompleteTextView autoCompleteTextView2;
    UserAdapter userAdapter;
    int user_id=0;
    ArrayList<User> userArrayList=new ArrayList<>();
    MyBottomSheetFragmentExixting bottomSheetFragmentExixting;
    private String lastkey="null";
    public MyBottomSheetFragmentExixting(Activity activity) {
        // Required empty public constructor
        this.activity=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_layout_exixting, container, false);
        bottomSheetFragmentExixting=this;
        // Initialize views and buttons
        Button button1 = view.findViewById(R.id.confirmButton1);
        if(activity!=null){
            textInputLayout1=view.findViewById(R.id.activityNameTextInput);
            autoCompleteTextView3=view.findViewById(R.id.EmployeeNameEditText1);
            textInputLayout3=view.findViewById(R.id.descriptionTextInput);
            autoCompleteTextView = view.findViewById(R.id.machineDropdownAutoComplete);
            autoCompleteTextView1 = view.findViewById(R.id.compoundDropdownAutoComplete);
            autoCompleteTextView2=view.findViewById(R.id.scheduleDropdownAutoComplete);
            Objects.requireNonNull(textInputLayout1.getEditText()).setText(activity.activityName);
            Objects.requireNonNull(textInputLayout3.getEditText()).setText(activity.activityDescription);
            autoCompleteTextView.setText(String.valueOf(activity.machineId));
            autoCompleteTextView1.setText(String.valueOf(activity.componentId));
            autoCompleteTextView2.setText(String.valueOf(activity.scheduleId));
        }
        itemModel = new ViewModelProvider(requireActivity()).get(ItemModel.class);
        components=itemModel.getComponentMutableLiveData().getValue();
        machines=itemModel.getMachineMutableLiveData().getValue();
        schedules=itemModel.getScheduleMutableLiveData().getValue();
        machine = new MachineAdapter(requireContext(),machines);
        component = new MachineAdapter(requireContext(),components);
        schedule = new MachineAdapter(requireContext(),schedules);
        autoCompleteTextView.setAdapter(machine);
        autoCompleteTextView1.setAdapter(component);
        autoCompleteTextView2.setAdapter(schedule);
        userAdapter = new UserAdapter(requireContext(), userArrayList);
        autoCompleteTextView3.setAdapter(userAdapter);
        autoCompleteTextView3.setThreshold(1);
        itemModel = new ViewModelProvider(requireActivity()).get(ItemModel.class);
        itemModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                Log.d(TAG, "onChanged: IN USERS AVAILABLE " + users.size());
                userAdapter.clear();
                userAdapter.addAll(users);
                userAdapter.notifyDataSetChanged();
                Log.d(TAG, "onCreateView: userAdapter value " + userAdapter.getCount());
            } else {
                Log.d(TAG, "onChanged: Users list is empty or null");
            }
        });
        autoCompleteTextView3.setOnItemClickListener((parent, view1, position, id) -> {
            user_id=userAdapter.getItem(position).getUser_id();
        });
        autoCompleteTextView3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MyForegroundService.foregroundService.getUsers(s, requireActivity());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button1.setOnClickListener(v -> {
            Activity activity=new Activity();
            activity.activityName=textInputLayout1.getEditText().getText().toString();
            activity.activityDescription=textInputLayout3.getEditText().getText().toString();
            activity.activity_satuts_id=1;
            int lengthm=autoCompleteTextView.getText().toString().length();
            int lengthc=autoCompleteTextView1.getText().toString().length();
            int lengths=autoCompleteTextView2.getText().toString().length();
            int lengthu=autoCompleteTextView3.getText().toString().length();
            activity.machineId= Integer.parseInt(String.valueOf(autoCompleteTextView.getText().toString().charAt(lengthm-1)));
            activity.componentId= Integer.parseInt(String.valueOf(autoCompleteTextView1.getText().toString().charAt(lengthc-1)));
            activity.scheduleId= Integer.parseInt(String.valueOf(autoCompleteTextView2.getText().toString().charAt(lengths-1)));
            Log.d(TAG, "confirmInput: ");
            activity.assigned_to=user_id;
            activity.activityId=Integer.parseInt(String.valueOf(autoCompleteTextView3.getText().toString().charAt(lengthu-1)));
            Date date=new Date();
            activity.issued_date= String.valueOf(date.getTime());
            MyForegroundService.foregroundService.setChangeActivity(activity);
        });

        return view;
    }
    public void setUserArrayList(ArrayList<User> userArrayList){
        this.userArrayList.clear();
        this.userArrayList.addAll(userArrayList);
        userAdapter.notifyDataSetChanged();
        Log.d(TAG, "setUserArrayList: ");
    }
}
