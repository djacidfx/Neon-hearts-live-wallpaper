package com.demo.lovelivewallpaper.utils;

import UEnginePackage.UGL.Urect;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;


public class Uscreen2 {
    public static int height;
    public static int width;

    public static void Init(Context context) {
        if (height == 0 || width == 0) {
            Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            width = point.x;
            height = point.y;
        }
    }

    public static void InitFromService(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        width = point.x;
        height = point.y;
    }

    public static Urect getBound(Context context) {
        Init(context);
        return new Urect(0.0d, 0.0d, width, height);
    }
}
