package com.example.myapplication.service;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessageService extends FirebaseMessagingService {
    private static final String TAG = "MessageService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        boolean broadcast= Boolean.parseBoolean(message.getData().get("broadcast"));
        String description=message.getData().get("description");
        String change = message.getData().get("change");
        String title = message.getData().get("title");
        String body = message.getData().get("body");
        String name = message.getData().get("name");
        String model = message.getData().get("model");
        String creator = message.getData().get("creator");
        NotificationCompat.Builder builder;
        Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
            expandedView.setTextViewText(R.id.notification_title, name);
            expandedView.setTextViewText(R.id.creator, creator);
            expandedView.setTextViewText(R.id.notification_message, description);

            if (broadcast) {
                builder = new NotificationCompat.Builder(this, "my_service")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(name)  // Concatenate description and name
                        .setContent(expandedView)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);


            } else {

                builder = new NotificationCompat.Builder(this, "my_service")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            }

        Log.d(TAG, "onMessageReceived: from "+message.getFrom());

        // Create an Intent to open your app when the notification is clicked

        // Build the notification


        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public void onMessageSent(@NonNull String msgId) {
        super.onMessageSent(msgId);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
