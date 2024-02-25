package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.service.MyForegroundService.no_of_running_service;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.User;
import com.example.myapplication.Support.UserPreferences;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private BottomSheetFragment bottomSheetFragment;
    MyForegroundService foreground;
    boolean isBound=false;
    Intent serviceIntent;
    ArrayList<Activity> ExistingActivities=new ArrayList<>();
    ArrayList<Activity> IssuedActivities=new ArrayList<>();
    ArrayList<Activity> PendingActivities=new ArrayList<>();
    ArrayList<Activity> ReviewActivities=new ArrayList<>();
    private static final String TAG = "MainActivity"; // Or any appropriate tag name
    ItemModel itemModel;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        private ComponentName name;
        private IBinder service;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            this.name = name;
            this.service = service;
            MyForegroundService.MyBinder binder = (MyForegroundService.MyBinder) service;
            foreground = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Activity activity = (Activity) intent.getSerializableExtra("activity");
            String change = intent.getStringExtra("change");
            Log.d(TAG, "onReceive: Change state: " + change);
            if (activity != null) {
                Log.d(TAG, "onReceive: Received activity: " + activity.activityName);
            }
            if (activity.activity_satuts_id == 1) {
                if (change != null && change.equals("create")) {
                    ExistingActivities.add(activity);
                    Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
                } else if (change != null && change.equals("update")) {
                    int i = 0;
                    for (Activity act : ExistingActivities) {
                        Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                        if (activity != null && act.activityId == activity.activityId) {
                            ExistingActivities.set(i, activity);
                            Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                            break;
                        }
                        i++;
                    }
                    Log.d(TAG, "onReceive: ExistingActivities size: " + ExistingActivities.size() + ", i: " + i);
                }
                // Update LiveData
                itemModel.setActivityMutableLiveData(ExistingActivities);
            } else if (activity.activity_satuts_id == 2) {
                if (change != null && change.equals("create")) {
                    IssuedActivities.add(activity);
                    Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
                } else if (change != null && change.equals("update")) {
                    int i = 0;
                    for (Activity act : IssuedActivities) {
                        Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                        if (activity != null && act.activityId == activity.activityId) {
                            IssuedActivities.set(i, activity);
                            Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                            break;
                        }
                        i++;
                    }
                    Log.d(TAG, "onReceive: ExistingActivities size: " + IssuedActivities.size() + ", i: " + i);
                }
                itemModel.setIssuedactivityMutableLiveData(IssuedActivities);
            } else if (activity.activity_satuts_id == 3) {
                if (change != null && change.equals("create")) {
                    PendingActivities.add(activity);
                    Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
                } else if (change != null && change.equals("update")) {
                    int i = 0;
                    for (Activity act : PendingActivities) {
                        Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                        if (activity != null && act.activityId == activity.activityId) {
                            PendingActivities.set(i, activity);
                            Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                            break;
                        }
                        i++;
                    }
                    Log.d(TAG, "onReceive: ExistingActivities size: " + PendingActivities.size() + ", i: " + i);
                    itemModel.setPendingactivityMutableLiveData(PendingActivities);
                }
            } else if (activity.activity_satuts_id == 4) {
                if (change != null && change.equals("create")) {
                    ReviewActivities.add(activity);
                    Log.d(TAG, "onReceive: Added new activity: " + activity.activityName);
                } else if (change != null && change.equals("update")) {
                    int i = 0;
                    for (Activity act : ReviewActivities) {
                        Log.d(TAG, "onReceive: activity id checking " + act.activityId + " " + activity.activityId);
                        if (activity != null && act.activityId == activity.activityId) {
                            ReviewActivities.set(i, activity);
                            Log.d(TAG, "onReceive: Updated activity: " + activity.activityName);
                            break;
                        }
                        i++;
                    }
                    Log.d(TAG, "onReceive: ExistingActivities size: " + ReviewActivities.size() + ", i: " + i);
                }
                boolean ischanged=itemModel.setReviewactivityMutableLiveData(ReviewActivities);
                Log.d(TAG, "onReceive: changing the review page "+ischanged+" "+ReviewActivities.size());
            } else {
                Log.d(TAG, "onReceive: Some unexpected activity data received from the server ");

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemModel=new ViewModelProvider(this).get(ItemModel.class);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        Log.d(TAG, "onCreate: checking service bound ");
        serviceIntent = new Intent(this, MyForegroundService.class);
        IntentFilter filter = new IntentFilter("com.example.ACTION_SEND_DATA");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(broadcastReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(broadcastReceiver, filter);
            }
        }else{
            registerReceiver(broadcastReceiver, filter);
        }
        if (isServiceRunning(MyForegroundService.class)) {
            ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "onCreate: starting the service because no service found ");
        } else {
            Log.d(TAG, "onCreate: service is running call for data from server");
            MyForegroundService.foregroundService.getExistingActivity();
        }
        fab = findViewById(R.id.fab);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentAdapter.addFragment(new ExistingActivity(), "Existing Activity");
        fragmentAdapter.addFragment(new IssuedActivity(), "Issued Activity");
        fragmentAdapter.addFragment(new PendingActivity(), "Pending Activity");
        fragmentAdapter.addFragment(new InspectorReview(), "Inspector Review");
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        MaterialToolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Tranvancore Cements");
        setSupportActionBar(toolbar);
        bottomSheetFragment = new BottomSheetFragment();
        fab.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: clicked the to open/close the bottom sheet fragment ");
            if (bottomSheetFragment.isVisible()) {
                bottomSheetFragment.dismiss();
            } else {
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        Log.d(TAG, "isServiceRunning: Called to check if a service is running "+no_of_running_service);
        return no_of_running_service == 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        Log.d(TAG, "onCreateOptionsMenu: called this function "+isBound);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search_View);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, "changing", Toast.LENGTH_SHORT).show();
                    updateFragmentSearchQuery(query);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    updateFragmentSearchQuery(newText);
                    return true;
                }
            });
        }
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
        } else if (id == R.id.menu_logout) {
            // Handle Item 3 selection
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void updateFragmentSearchQuery(String query) {

        // Update each fragment with the new search query

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

            if (fragment instanceof SearchableFragment) {

                ((SearchableFragment) fragment).updateSearchQuery(query);

            }

        }

    }

}

