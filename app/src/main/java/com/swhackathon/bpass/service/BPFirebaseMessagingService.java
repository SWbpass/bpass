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
import com.swhackathon.bpass.ui.ListPersonActivity;

public class BPFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        PendingIntent pendingIntent = null;

        if(remoteMessage.getData().size() > 0) {

            String storeName = remoteMessage.getData().get("storeName");
            String storePhoneNumber = remoteMessage.getData().get("storePhoneNumber");
            String address = remoteMessage.getData().get("address");
            double latitude = Double.parseDouble(remoteMessage.getData().get("latitude"));
            double longitude = Double.parseDouble(remoteMessage.getData().get("longitude"));

            Intent notificationIntent = new Intent(this, ListPersonActivity.class);

            notificationIntent.putExtra("storeName", storeName);
            notificationIntent.putExtra("storePhoneNumber", storePhoneNumber);
            notificationIntent.putExtra("address", address);
            notificationIntent.putExtra("latitude", latitude);
            notificationIntent.putExtra("longitude", longitude);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if(remoteMessage != null) {
            Log.e("remoteMessage Title >>", remoteMessage.getNotification().getTitle());
            Log.e("remoteMessage Body >>", remoteMessage.getNotification().getBody());
            Log.e("remoteMessage Size >>", String.valueOf(remoteMessage.getData().size()));

            sendNotification(remoteMessage.getNotification(), pendingIntent); // Notification 발생
        }
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
        builder.setSmallIcon(R.drawable.ic_launcher);
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