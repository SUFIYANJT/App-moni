package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.snackbar.Snackbar;
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
    Activity activity1=new Activity();
    private ArrayList<Machine> machines = new ArrayList<>();
    private  ArrayList<Machine> components = new ArrayList<>();
    private ArrayList<Machine> schedules = new ArrayList<>();
    ItemModel itemModel,itemModel2;
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView1;
    AutoCompleteTextView autoCompleteTextView2;
    UserAdapter userAdapter;
    int user_id=0;
    ArrayList<User> userArrayList=new ArrayList<>();
    MyBottomSheetFragmentExixting bottomSheetFragmentExixting;
    private String lastkey="null";
    ArrayList<Activity> activities;
    int i=0;
    int position;
    public MyBottomSheetFragmentExixting(Activity activity, ArrayList<Activity> activities, int position) {
        // Required empty public constructor
        this.activity=activity;
        this.position=position;
        this.activities=activities;
        Log.d(TAG, "MyBottomSheetFragmentExixting: activity id "+activity.activityId);
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
        autoCompleteTextView3.setText(activity.assigned_to_user);
        itemModel=null;
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
        MyForegroundService.foregroundService.setUi(this.requireActivity());
        itemModel.getCallBack().observe(getViewLifecycleOwner(), data -> {
            Log.d(TAG, "onCreateView: updating the string from background ");
            if (data.equals("update")&&i!=0) {
                Snackbar snackbar=Snackbar.make(requireView(), "Activity changed its properties", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(getResources().getColor(R.color.darkGreen));
                snackbar.setBackgroundTint(getResources().getColor(R.color.black));
                snackbar.addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        dismiss();
                    }
                });
                snackbar.show();
            }i++;

        });

        autoCompleteTextView3.setOnItemClickListener((parent, view1, position, id) -> {
            user_id= Objects.requireNonNull(userAdapter.getItem(position)).getUser_id();
            activity1.assigned_to= Objects.requireNonNull(userAdapter.getItem(position)).getUser_id();
            activity1.assigned_to_user= Objects.requireNonNull(userAdapter.getItem(position)).getUsername();
            Log.d(TAG, "onCreateView: user_id updated "+user_id);
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
            i++;
            Log.d(TAG, "onCreateView: final checking size "+activities.size()+" "+itemModel.getActivityMutableLiveData().getValue().size());
            activity1.activityName=textInputLayout1.getEditText().getText().toString();
            activity1.activityDescription=textInputLayout3.getEditText().getText().toString();
            activity1.activity_satuts_id=1;
            int lengthm=autoCompleteTextView.getText().toString().length();
            int lengthc=autoCompleteTextView1.getText().toString().length();
            int lengths=autoCompleteTextView2.getText().toString().length();
            int lengthu=autoCompleteTextView3.getText().toString().length();
            activity1.machineId= Integer.parseInt(String.valueOf(autoCompleteTextView.getText().toString().charAt(lengthm-1)));
            activity1.componentId= Integer.parseInt(String.valueOf(autoCompleteTextView1.getText().toString().charAt(lengthc-1)));
            activity1.scheduleId= Integer.parseInt(String.valueOf(autoCompleteTextView2.getText().toString().charAt(lengths-1)));
            Log.d(TAG, "confirmInput: "+position);
            activity1.activityId=this.activity.activityId;
            Date date=new Date();
            activity1.issued_date= String.valueOf(date.getTime());
            if(activity.assigned_to_user.equals("None")&&activity.assigned_to==0) {
                itemModel.setIntegerMutableLiveData(position);
                Log.d(TAG, "onCreateView: activity assigned to " + activity1.activityName + " " + activity1.activityId);
                activity.change="update";
                MyForegroundService.foregroundService.setChangeActivity(activity1);
            }else{
                Log.d(TAG, "onCreateView: activity name " + activity.activityName+" "+activity.assigned_to_user);
            }
        });
        return view;
    }
    public void setUserArrayList(ArrayList<User> userArrayList){
        this.userArrayList.clear();
        this.userArrayList.addAll(userArrayList);
        userAdapter.notifyDataSetChanged();
        Log.d(TAG, "setUserArrayList: ");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: destroying bottomFragment");
    }
}
