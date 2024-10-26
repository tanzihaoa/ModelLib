package com.tzh.myapplication.service;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.tzh.myapplication.R;
import com.tzh.myapplication.ui.activity.main.MainActivity;

public class AutoBillService extends Service {

    private static final int serviceId = 6699;
    public static boolean isStart = false;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

    }

    public int onStartCommand(Intent intent, int i, int i2) {
        isStart = true;
        stopForeground(true);
        startServer();
        return super.onStartCommand(intent, i, i2);
    }

    public void onDestroy() {
        super.onDestroy();
        isStart = false;
        stopForeground(true);

    }


    private void startServer() {
        PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), FLAG_IMMUTABLE);


        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());

        if (intent == null) {
            intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        }

        PendingIntent activity_qianji = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE);


        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notify);
        remoteViews.setImageViewResource(R.id.btn_qianji, R.mipmap.ic_launcher);
        remoteViews.setOnClickPendingIntent(R.id.icon, activity);
        remoteViews.setOnClickPendingIntent(R.id.btn_qianji, activity_qianji);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteViews);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("AutoBillService", "自动记账后台服务通知", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(false);
            notificationChannel.setShowBadge(false);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
            builder.setChannelId("AutoBillService");
        }
        Notification notification = builder.build();
        notification.defaults = 1;
        notification.flags = 2;
        startForeground(serviceId, notification);
    }


}
