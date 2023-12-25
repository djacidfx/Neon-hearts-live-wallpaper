package com.demo.lovelivewallpaper.utils;

import android.graphics.Color;
import android.util.Log;
import liveWallpaper.myapplication.App;


public class AppUtils {
    public static boolean wallpaperUnlocked(String str, int i) {
        Log.e("tag3", "wallpaper " + ((i + 1) % 5) + "/" + i);
        return (i > 4 && i % 5 == 0 && PrefLoader.LoadPref(str, App.getInstance()) == 0) ? false : true;
    }

    public static int lighten(int i, double d) {
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        return Color.argb(Color.alpha(i), lightenColor(red, d), lightenColor(green, d), lightenColor(blue, d));
    }

    private static int lightenColor(int i, double d) {
        double d2 = i;
        Double.isNaN(d2);
        Double.isNaN(d2);
        return (int) Math.min(d2 + (d * d2), 255.0d);
    }
}
