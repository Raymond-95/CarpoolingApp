package com.example.raymond.share.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.raymond.share.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Raymond on 23/1/2017.
 */
public class FirebaseMessageService extends FirebaseMessagingService{

    private static final String TAG = "FirebaseMessageService";
    private static Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FROM:" + remoteMessage.getFrom());

        //Check if the message contains data
        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        //Check if the message contains notification

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Mesage title:" + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());
            categorizeNotification(remoteMessage.getNotification().getTitle(),
                             remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Dispay the notification
     * @param body
     */
    private void categorizeNotification(String title, String body) {

        intent = null;

        intent = new Intent(this, NotificationList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        sendNotification(title, body);

    }

    private void sendNotification(String title, String body){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
    }
}
