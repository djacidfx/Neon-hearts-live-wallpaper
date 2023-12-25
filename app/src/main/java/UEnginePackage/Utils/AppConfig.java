package UEnginePackage.Utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import liveWallpaper.myapplication.App;


public class AppConfig {
    public static int movmentFactor = 53;
    public static int phoneMaxMovment = 600;
    public static int unlockedWallpapersCount = 5;

    public static String getGlitterBehavior() {
        return "glitterEffect/glitter_behavior.json";
    }

    public static List<String> listFiles(String str) {
        try {
            String[] list = App.c().getAssets().list(str);
            ArrayList arrayList = new ArrayList();
            for (String str2 : list) {
                arrayList.add(str2);
            }
            return arrayList;
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> listWallpapers() {
        try {
            String[] list = App.c().getAssets().list("wallpapers");
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.length; i++) {
                arrayList.add("wallpapers/" + list[i]);
            }
            return arrayList;
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> listAssetsParticles() {
        return listFiles("presetParticles");
    }

    public static List<String> listAssetsWallpapers() {
        return listFiles("wallpapers");
    }

    public static List<String> listAssetsLiveWallpapers() {
        return listFiles("liveWallpapers");
    }

    public static List<String> listBehaviors() {
        return listFiles("particleBehaviors");
    }

    public static void OpenAppForRating(Context context) {
        String packageName = context.getPackageName();
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public static void OpenAppForRating(String str, Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + str)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + str)));
        }
    }
}
