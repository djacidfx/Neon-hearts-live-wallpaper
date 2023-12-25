package liveWallpaper.myapplication;

import UEnginePackage.Utils.AppConfig;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import com.demo.lovelivewallpaper.R;

import java.util.List;


public class notificationHelper {
    public static String NOTIFICATION_CHANNEL_ID = "4565";
    public static final String PRIMARY_NOTIF_CHANNEL = "default";
    Notification notification;
    RemoteViews notificationLayout;

    @SuppressLint("WrongConstant")
    public void showNotification(Context context) {
        NOTIFICATION_CHANNEL_ID = (((int) Math.random()) * 1000000) + "";
        createNotificationChannel(context);
        Log.e("tag1", "service show notification");
        String string = context.getString(R.string.app_name);
        if (Build.VERSION.SDK_INT < 26) {
            this.notificationLayout = new RemoteViews(context.getPackageName(), (int) R.layout.notification_layou3);
            Intent intent = new Intent(context.getApplicationContext(), ActivitySplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setAction("Action");
            intent.setFlags(268468224);
            Notification build = new NotificationCompat.Builder(context).setContentTitle(context.getResources().getString(R.string.app_name)).setTicker(string).setContentText("Background recording working").setCustomContentView(this.notificationLayout).setSmallIcon(R.drawable.ic_done).setContentIntent(PendingIntent.getActivity(context, 0, intent, 0)).setOngoing(false).build();
            this.notification = build;
            build.flags = build.flags;
            this.notificationLayout.setOnClickPendingIntent(R.id.c1, PendingIntent.getBroadcast(context, 0, new Intent("c1"), 0));
            this.notificationLayout.setOnClickPendingIntent(R.id.c2, PendingIntent.getBroadcast(context, 0, new Intent("c2"), 0));
            this.notificationLayout.setOnClickPendingIntent(R.id.c3, PendingIntent.getBroadcast(context, 0, new Intent("c3"), 0));
            this.notificationLayout.setOnClickPendingIntent(R.id.c4, PendingIntent.getBroadcast(context, 0, new Intent("c4"), 0));
            this.notificationLayout.setOnClickPendingIntent(R.id.c5, PendingIntent.getBroadcast(context, 0, new Intent("c5"), 0));
            setImages(this.notificationLayout, context);
            ((NotificationManager) context.getSystemService("notification")).notify(55, this.notification);
            return;
        }
        PendingIntent activity = PendingIntent.getActivity(context, 0, new Intent(context, ActivitySplashScreen.class), 0);
        this.notificationLayout = new RemoteViews(context.getPackageName(), (int) R.layout.notification_layou3);
        this.notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setContentTitle(string).setContentText(string).setCustomContentView(this.notificationLayout).setSmallIcon(R.drawable.ic_done).setContentIntent(activity).build();
        this.notificationLayout.setOnClickPendingIntent(R.id.c1, PendingIntent.getBroadcast(context, 0, new Intent("c1"), 0));
        this.notificationLayout.setOnClickPendingIntent(R.id.c2, PendingIntent.getBroadcast(context, 0, new Intent("c2"), 0));
        this.notificationLayout.setOnClickPendingIntent(R.id.c3, PendingIntent.getBroadcast(context, 0, new Intent("c3"), 0));
        this.notificationLayout.setOnClickPendingIntent(R.id.c4, PendingIntent.getBroadcast(context, 0, new Intent("c4"), 0));
        this.notificationLayout.setOnClickPendingIntent(R.id.c5, PendingIntent.getBroadcast(context, 0, new Intent("c5"), 0));
        setImages(this.notificationLayout, context);
        ((NotificationManager) context.getSystemService("notification")).notify(55, this.notification);
    }

    public void stop(Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(55);
    }

    void setImages(RemoteViews remoteViews, Context context) {
        List<String> listAssetsWallpapers = AppConfig.listAssetsWallpapers();
        String str = "wallpapers/" + listAssetsWallpapers.get(0);
        String str2 = "wallpapers/" + listAssetsWallpapers.get(1);
        String str3 = "wallpapers/" + listAssetsWallpapers.get(2);
        Bitmap bitmapFromAsset = Resources.getBitmapFromAsset(str, context);
        Bitmap bitmapFromAsset2 = Resources.getBitmapFromAsset(str2, context);
        Bitmap bitmapFromAsset3 = Resources.getBitmapFromAsset(str3, context);
        Bitmap bitmapFromAsset4 = Resources.getBitmapFromAsset("wallpapers/" + listAssetsWallpapers.get(3), context);
        Bitmap properImage = getProperImage(bitmapFromAsset);
        Bitmap properImage2 = getProperImage(bitmapFromAsset2);
        Bitmap properImage3 = getProperImage(bitmapFromAsset3);
        Bitmap properImage4 = getProperImage(bitmapFromAsset4);
        remoteViews.setImageViewBitmap(R.id.c1, properImage);
        remoteViews.setImageViewBitmap(R.id.c2, properImage2);
        remoteViews.setImageViewBitmap(R.id.c3, properImage3);
        remoteViews.setImageViewBitmap(R.id.c4, properImage4);
    }

    public Bitmap getProperImage(Bitmap bitmap) {
        return Resources.cropToRectangle(Resources.resizeBitmapByScale(bitmap, 0.4f, true), true);
    }

    @SuppressLint("RestrictedApi")
    void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, PRIMARY_NOTIF_CHANNEL, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        }
    }
}
