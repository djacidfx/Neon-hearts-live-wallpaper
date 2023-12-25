package com.demo.lovelivewallpaper.receivers;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import com.demo.lovelivewallpaper.R;

import liveWallpaper.myapplication.ActivitySplashScreen;


public class AlarmReceiverr extends BroadcastReceiver {
    private NotificationCompat.Builder mBuilder;

    @Override 
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().getInt("type") == 0) {
            createNotification(context);
        } else if (intent.getExtras() == null || intent.getExtras().getInt("type") != 1) {
        }
    }

    @SuppressLint("RestrictedApi")
    private void createNotification(Context context) {
        Log.d("Comments", "createNotification");
        context.getPackageName();
        Intent intent = new Intent(context, ActivitySplashScreen.class);
        intent.putExtra("rateUs", true);
        PendingIntent activity = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("my_channel_id_01", "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.setVibrationPattern(new long[]{0, 200});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        this.mBuilder = new NotificationCompat.Builder(context, "my_channel_id_01").setSmallIcon(R.drawable.icon200).setContentTitle(context.getString(R.string.app_name)).setContentText(context.getString(R.string.rate_push)).setContentIntent(activity).setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mBuilder.setSmallIcon(R.drawable.icon200);
        }
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, this.mBuilder.build());
    }

}
