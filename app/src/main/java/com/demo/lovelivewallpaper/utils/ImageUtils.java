package com.demo.lovelivewallpaper.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import liveWallpaper.myapplication.App;


public class ImageUtils {
    public static Bitmap getBitmapFromAsset(Context context, String str) {
        BitmapFactory.Options options = null;
        Bitmap decodeStream = null;
        AssetManager assets = context.getAssets();
        Uscreen.Init(App.c());
        int i = Uscreen.width;
        int i2 = Uscreen.height;
        Bitmap bitmap = null;
        try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            decodeStream = BitmapFactory.decodeStream(assets.open(str), null, options);
        } catch (Exception e) {
            e = e;
        }
        try {
            options.inSampleSize = calculateInSampleSize(options, i, i2);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(assets.open(str), null, options);
        } catch (Exception e2) {
            bitmap = decodeStream;
            Log.e("tag-", "error while decoding bitmap from assets " + e2.getMessage());
            return bitmap;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2;
            }
        }
        return i5;
    }
}
