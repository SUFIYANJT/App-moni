package com.example.myapplication.service;

import static com.example.myapplication.LoginActivity.TAG;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ReportTransfer;
import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.Machine;
import com.example.myapplication.Support.SubmitHolder;
import com.example.myapplication.Support.Task;
import com.example.myapplication.Support.User;
import com.example.myapplication.Support.UserPreferences;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.model.MyBroadcastReceiver;
import com.example.myapplication.model.ReportInterface;
import com.example.myapplication.network.WebSocketClient;

import java.io.Serializable;
import java.util.ArrayList;

public class MyForegroundService extends Service implements Serializable {
    public static MyForegroundService foregroundService;
    private FragmentActivity activity;
    public ReportInterface reportInterface;
    public User user = null;
    private ArrayList<User> arrayList;
    private static final int NOTIFICATION_ID = 123;
    public static int no_of_running_service = 0;
    WebSocketClient webSocketClient;
    ItemModel itemModel;
    Context context;
    ReportTransfer reportTransfer;
    MyBinder binder=new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: created the service and static var is init ");
        no_of_running_service++;
        foregroundService = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: starting s");
        webSocketClient = new WebSocketClient(this, this);
        createNotification();
        // getExistingActivity();
        User user1=UserPreferences.getUser(getApplicationContext());
        if(user1!=null&&user1.getUsermode().equals("B"))
            getTask();
        return START_STICKY;
    }

    public void getExistingActivity() {
        webSocketClient.getExistingActivity();
        Log.d(TAG, "getExistingActivity: fore ground called form the main activity ");
    }

    public void setExistingActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "setExistingActivity: activity obtained " + activity.activityName);
            Intent intent = new Intent(this, MyBroadcastReceiver.class); // Replace MyBroadcastReceiver with your actual receiver class
            intent.setAction("com.example.ACTION_SEND_DATA");
            intent.putExtra("activity", (Serializable) activity);
            Log.d(TAG, "setExistingActivity: activity change check "+activity.change+" "+activity.uiChange);
            LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(intent);
        } else {
            Log.d(TAG, "setExistingActivity: activity obtained " + activity.activityName);
            Intent intent = new Intent("com.example.ACTION_SEND_DATA");
            intent.putExtra("calledby","setExistingActivity");
            intent.putExtra("activity", (Serializable) activity);
            Log.d(TAG, "setExistingActivity: activity change check "+activity.change+" "+activity.uiChange);
            sendBroadcast(intent);
        }
    }

    public void setComponent(Machine machine, boolean isUpdate) {
        Intent intent = new Intent("com.example.ACTION_SEND_DATA_BOTTOM_SHEET");
        intent.putExtra("data", (Serializable) machine);
        intent.putExtra("type", "machine");
        if (!isUpdate) {
            intent.putExtra("change", "create");
        } else {
            intent.putExtra("change", "update");
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void setMachine(Machine machine, boolean isUpdate) {
        Log.d(TAG, "setMachine: is called for updating ui ");
        Intent intent = new Intent("com.example.ACTION_SEND_DATA_BOTTOM_SHEET");
        intent.putExtra("data", (Serializable) machine);
        intent.putExtra("type", "component");
        if (!isUpdate) {
            intent.putExtra("change", "create");
        } else {
            intent.putExtra("change", "update");
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void setSchedule(Machine machine, boolean isUpdate) {
        Log.d(TAG, "setSchedule: is called for updating ui ");
        Intent intent = new Intent("com.example.ACTION_SEND_DATA_BOTTOM_SHEET");
        intent.putExtra("data", (Serializable) machine);
        intent.putExtra("type", "schedule");
        if (!isUpdate) {
            intent.putExtra("change", "create");
        } else {
            intent.putExtra("change", "update");
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void setUser(User user) {
        this.user = user;
        Intent intent = new Intent("com.example.ACTION_SEND_DATA");
        intent.putExtra("key", (Serializable) user);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void createNotification() {
        Log.d(TAG, "createNotification: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service");
        } else {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_service")
                    .setContentTitle("Foreground Service")
                    .setContentText("This is a foreground service")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Use PRIORITY_DEFAULT or higher
                    .setCategory(NotificationCompat.CATEGORY_SERVICE);

            Notification notification = builder.build();
            Log.d(TAG, "createNotification: called this method due to some version things ");
            startForeground(1, notification);
        }
    }

    public void getSchedule() {
        webSocketClient.getSchedules();
    }

    public void getMachine() {
        Log.d(TAG, "getMachine: called for machine data ");
        webSocketClient.getMachines();
    }

    public void getComponent() {
        webSocketClient.getComponents();
    }

    public void CreateActivity(Activity activity) {
        webSocketClient.CreateActivity(activity);
    }
    public void setUi(FragmentActivity activity){
        this.activity=activity;
    }

    public void updateUi(String callback,String model) {
        Intent intent = new Intent("com.example.ACTION_SEND_DATA_BOTTOM_SHEET");
        intent.putExtra("data", (Serializable) null);
        intent.putExtra("type", "ui");
        intent.putExtra("change", "update");
        intent.putExtra("modal",model);
        Log.d(TAG, "updateUi: reached in back ground to update ui "+activity);
        itemModel=new ViewModelProvider(activity).get(ItemModel.class);
        itemModel.setCallBack("update");
    }

    public void getUsers(CharSequence sequence, FragmentActivity activity) {
        webSocketClient.getUsers(sequence);
        this.activity = activity;
    }

    public void setUsers(User user, String key) {
        Log.d(TAG, "setUsers: data reeached users setting in background ");
        itemModel = new ViewModelProvider(activity).get(ItemModel.class);
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        for (User u :
                users) {
            Log.d(TAG, "setUsers: user " + u.getUsername());
        }
        itemModel.setUserMutableLiveData(users);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Foreground Service is Destroyed ");
    }

    public void setChangeActivity(Activity activity) {
        Log.d(TAG, "setChangeActivity: called ");
        if(activity.change!=null&&activity.change.equals("delete")){
            webSocketClient.deleteActivity(activity);
        }else {
            webSocketClient.updateActivity(activity);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName) {
        Log.d(TAG, "createNotificationChannel: called for service ");
        Intent resultIntent = new Intent(this, MainActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_MUTABLE);

        NotificationChannel chan = null;
        Log.d(TAG, "createNotificationChannel: if worked ");
        chan =  new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "createNotificationChannel: permission not granted ");
            return;
        }
        notificationManager.notify(1, notificationBuilder.build());
        Log.d(TAG, "createNotificationChannel: called service from service ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification,ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE);
        } else {
            startForeground(1, notification);
        }
    }

    public void getTask() {
        webSocketClient.getTask();
    }

    public void setTask(Task task) {
        Intent intent = new Intent("com.example.ACTION_SEND_TASK_DATA");
        intent.putExtra("task",(Serializable) task);
        Log.d(TAG, "setTask: task is has been reached and to update ");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void sendReport(ArrayList<SubmitHolder> submitHolders, int taskId) {
        webSocketClient.sendReport(submitHolders,getApplicationContext(),taskId);
    }

    public void getReport(ReportTransfer newWindow, int activityId) {
        webSocketClient.getReport(newWindow,activityId);
    }

    public void getFile(String file, ReportTransfer reportTransfer, int position) {
        this.reportTransfer=reportTransfer;
        webSocketClient.getFile(file,position);
    }
    public void SendFileData(byte[] data){
        reportTransfer.setFileData(data);
    }
    public void fileSize(int size){
        reportTransfer.fileSize(size);
    }
    public void getAllReports(ReportInterface reportInterface) {
        this.reportInterface=reportInterface;
        webSocketClient.getAllReports();
    }

    public void setAllReport(String text) {
        reportInterface.setReports(text);
    }

    public class MyBinder extends Binder {
        public MyForegroundService getService() {
            return MyForegroundService.this;
        }
    }
}
