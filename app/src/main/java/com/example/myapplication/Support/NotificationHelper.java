package com.example.myapplication.Support;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "my_channel_id";
    private static final String CHANNEL_NAME = "My Channel";

    public static void showNotification(Context context, String title, String message) {
        // Create a NotificationManager instance
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a NotificationCompat.Builder to build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_broken_image_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Check if device is running Android Oreo or higher, as notification channels are required for these versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Create the notification channel
            notificationManager.createNotificationChannel(channel);
        }

        // Inflate the custom layout for the notification content
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.notification_title, title);
        remoteViews.setTextViewText(R.id.notification_message, message);

        // Set the custom layout as the notification content
        builder.setCustomContentView(remoteViews);

        // Build the notification
        Notification notification = builder.build();

        // Display the notification
        notificationManager.notify(1, notification);
    }
}
