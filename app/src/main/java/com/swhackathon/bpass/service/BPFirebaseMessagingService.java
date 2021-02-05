package com.swhackathon.bpass.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swhackathon.bpass.R;
import com.swhackathon.bpass.ui.MapActivity;

public class BPFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent notificationIntent = new Intent(this, MapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);;

        if(remoteMessage.getData().size() > 0) {
            Log.e("[remoteMessage lati]", remoteMessage.getData().get("latitude"));
            Log.e("[remoteMessage longi]", remoteMessage.getData().get("longitude"));
            Log.e("[remoteMessage Data]", remoteMessage.getData().toString());

            String storeName = remoteMessage.getData().get("storeName");
            double latitude = Double.parseDouble(remoteMessage.getData().get("latitude"));
            double longitude = Double.parseDouble(remoteMessage.getData().get("longitude"));

            notificationIntent.putExtra("storeName", storeName); //전달할 값
            notificationIntent.putExtra("latitude", latitude); //전달할 값
            notificationIntent.putExtra("longitude", longitude); //전달할 값
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if(remoteMessage != null) {
            Log.e("[remoteMessage Title]", remoteMessage.getNotification().getTitle());
            Log.e("[remoteMessage Body]", remoteMessage.getNotification().getBody());

            Log.e("[remoteMessage TTT]", String.valueOf(remoteMessage.getData().size()));
            Log.e("[remoteMessage TTT]", remoteMessage.getData().get("latitude"));
            sendNotification(remoteMessage.getNotification(), pendingIntent); // Notification 발생
        }
        sendNotification(remoteMessage.getNotification(), pendingIntent); // Notification 발생
    }

    private void sendNotification(RemoteMessage.Notification data, PendingIntent pendingIntent) {
        NotificationManager mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "ChannerName";
            final String CHANNEL_DESCRIPTION = "ChannerDescription";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            // API Level 26
            NotificationChannel mChannel = new NotificationChannel("ChannelId", CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ChannelId");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(data.getTitle());
        builder.setContentText(data.getBody());

        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent); // 사용자가 Notification을 클릭시 MainActivity로 값을 넘김

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            builder.setVibrate(new long[]{500, 500});

        mManager.notify(0, builder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s); // 토근이 새로 갱신된 경우

        /*
        TODO: 토큰값 갱신 API 호출 필요
         */
    }
}