package com.example.myapplication.service;

import static com.example.myapplication.LoginActivity.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.myapplication.MainActivity;
import com.example.myapplication.NetworkConnector;
import com.example.myapplication.R;
import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.User;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.network.WebSocketClient;
import com.google.gson.Gson;

import java.io.Serializable;

public class MyForegroundService extends Service implements NetworkConnector {
    public static MyForegroundService foregroundService;
    public User user=null;
    private static final int NOTIFICATION_ID = 123;
    public static int no_of_running_service=0;
    WebSocketClient webSocketClient;
    ItemModel itemModel;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: created the service and static var is init ");
        no_of_running_service++;
        foregroundService=this;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        webSocketClient = new WebSocketClient(this,this);
        createNotification();
       // getExistingActivity();
        return START_STICKY;
    }
    public void getExistingActivity(){
        webSocketClient.getExistingActivity();
        Log.d(TAG, "getExistingActivity: fore ground called form the main activity ");
    }
    public void setExistingActivity(Activity activity,boolean isUpdate){
        Log.d(TAG, "setExistingActivity: activity obtained "+activity.activityName);
        Intent intent = new Intent("com.example.ACTION_SEND_DATA");
        intent.putExtra("activity", (Serializable) activity);
        if(!isUpdate) {
            intent.putExtra("change","create");
        }else{
            intent.putExtra("change","update");
        }
        sendBroadcast(intent);
    }
    public void setUser(User user){
        this.user=user;
        Intent intent = new Intent("com.example.ACTION_SEND_DATA");
        intent.putExtra("key",(Serializable) user);
        sendBroadcast(intent);

    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service");
        } else {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                    .setContentTitle("Foreground Service")
                    .setContentText("This is a foreground service")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Use PRIORITY_DEFAULT or higher
                    .setCategory(NotificationCompat.CATEGORY_SERVICE);

            Notification notification = builder.build();
            startForeground(1, notification);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Foreground Service is Destroyed ");
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName) {
        Intent resultIntent = new Intent(this, MainActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_MUTABLE);

        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(resultPendingIntent) //intent
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, notificationBuilder.build());
        Log.d(TAG, "createNotificationChannel: called service from service ");
        startForeground(1, notification);
    }

    @Override
    public int checkAuth(String username, String password) {
        return webSocketClient.Auth(username,password)?1:0;
    }

    @Override
    public int sendMessage(Gson gson) {
        return 0;
    }

    @Override
    public Gson message() {
        return null;
    }
    public class MyBinder extends Binder {
        public MyForegroundService getService() {
            return MyForegroundService.this;
        }
    }
}
