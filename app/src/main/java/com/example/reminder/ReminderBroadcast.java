package com.example.reminder;


import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



        String reminder_title = intent.getStringExtra("id");


        Log.i("Receiver", "Broadcast received: " + reminder_title);


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "notifyMe")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("Reminder")
                        .setContentText(reminder_title)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200, builder.build());


    }
}
