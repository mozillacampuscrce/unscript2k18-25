package com.example.ht.unscript;

/**
 * Created by CGT on 09-11-2017.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FireMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        /*
        Log.d("Msg", "Message received ["+remoteMessage+"]");

        // Create Notification
        Intent intent = new Intent(this, DashBoard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());
        */
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String data = String.valueOf(remoteMessage.getData());

        if (remoteMessage.getData().size() > 0){
            //data = "hi";
        }
        else {
            data = "no";
        }

        //click_action = "android.intent.action.ALL_APPS

        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent intent = new Intent(this, DashBoard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle b = new Bundle();
        b.putString("data", data);
        intent.putExtras(b);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(FireMsgService.this,0, intent, PendingIntent.FLAG_ONE_SHOT | Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(FireMsgService.this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        //notificationBuilder.setVibrate(new long[] { 1000, 1000});
        notificationBuilder.setSound(alertSound);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        v.vibrate(80);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
