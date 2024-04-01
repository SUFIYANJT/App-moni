package com.example.myapplication;

import static com.example.myapplication.LoginActivity.TAG;
import static com.example.myapplication.service.MyForegroundService.no_of_running_service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.Task;
import com.example.myapplication.Support.User;
import com.example.myapplication.Support.UserPreferences;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.CustomAdapter;
import com.example.myapplication.model.TaskAdapter;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class newmainuser extends AppCompatActivity {
    boolean isBound;
    MyForegroundService foregroundService;
    public static MyForegroundService myForegroundService;
    int BLUETOOTH_PERMISSION=104;
    Intent serviceIntent;
    ArrayList<Task> itemList;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        private ComponentName name;
        private IBinder service;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: connect callback working ");
            this.name = name;
            this.service = service;
            MyForegroundService.MyBinder binder = (MyForegroundService.MyBinder) service;
            foregroundService=binder.getService();
            myForegroundService=foregroundService;
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound=false;
        }
    };
    MaterialToolbar materialToolbar;
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: task is reached in broadcast and to be updated ...");
            Task task=(Task) intent.getSerializableExtra("task");
            itemList.add(task);
            taskAdapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmainuser);
        Log.d(TAG, "onCreate: is this newmainuser activity ");
        materialToolbar=findViewById(R.id.toolbarUSER);
        User user= UserPreferences.getUser(getApplicationContext());
        if (user != null) {
            materialToolbar.setTitle(user.getUsername());
        }
        setSupportActionBar(materialToolbar);
        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewuser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this,itemList,foregroundService);
        recyclerView.setAdapter(taskAdapter);
        permissionCall();
    }
    public boolean isServiceRunning(Class<?> serviceClass) {
        Log.d(TAG, "isServiceRunning: Called to check if a service is running "+no_of_running_service);
        return !isBound;
    }
    public void permissionCall(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: Trying to grant permission ");
            // Request the permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                String[] permissions = {android.Manifest.permission.POST_NOTIFICATIONS, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_CONNECT};
                ActivityCompat.requestPermissions(this, permissions, BLUETOOTH_PERMISSION);
            }
        } else {
            startService();
        }
    }
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public void startService(){
        serviceIntent = new Intent(this.getApplicationContext(), MyForegroundService.class);
        if (isServiceRunning(MyForegroundService.class)) {
            ContextCompat.startForegroundService(newmainuser.this, serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "onCreate: starting the service because no service found ");
        } else {
            foregroundService.getTask();
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        Log.d(TAG, "onCreate: checking service bound ");

        IntentFilter filter = new IntentFilter("com.example.ACTION_SEND_TASK_DATA");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(broadcastReceiver,filter);
            } else {
                Log.d(TAG, "startService: registering broadcast...");
                LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(broadcastReceiver, filter);
            }
        }else{
            Log.d(TAG, "startService: registering broadcast...");
            registerReceiver(broadcastReceiver, filter);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: calling for permission result ");
        if (requestCode == BLUETOOTH_PERMISSION) {
            // Check if the permission has been granted

            for (int permission:
                    grantResults) {
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    permissionCall();
                }
            }
            Log.d(TAG, "onRequestPermissionsResult: all permission granted starting service ");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d(LoginActivity.TAG, "createNotificationChannel: permission not granted ");
                permissionCall();
            }else {
                startService();
            }
        }
    }


    private void showMenu(View view) {
        try {
            PopupMenu popupMenu = new PopupMenu(newmainuser.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_settings) {
                        Toast.makeText(newmainuser.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (item.getItemId() == R.id.menu_logout) {
                        Intent intent = new Intent(newmainuser.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(newmainuser.this, "Logout", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(newmainuser.this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pending, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle item selection
        if (id == R.id.search_View) {
            // Handle Item 1 selection
            return true;
        } else if (id == R.id.menu_settings) {
            // Handle Item 2 selection
            return true;
        } else if (id == R.id.menu_mark_complete) {
            UserPreferences.saveUser(this.getApplicationContext(),null);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            // Handle Item 3 selection
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            unregisterReceiver(broadcastReceiver);
        }else{
            LocalBroadcastManager.getInstance(this.getApplicationContext()).unregisterReceiver(broadcastReceiver);
        }

    }
}
