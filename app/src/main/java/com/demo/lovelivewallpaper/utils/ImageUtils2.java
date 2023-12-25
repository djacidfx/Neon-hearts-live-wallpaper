package com.demo.lovelivewallpaper.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtils2 {
    public static Bitmap loadImageFromStorage2(String str, String str2, Context context) {
        Log.e("tag1", "themes/" + str + "/" + str2);
        try {
            File file = new File(new File(new ContextWrapper(context.getApplicationContext()).getDir("themes", 0), str), str2);
            new BitmapFactory.Options();
            Bitmap decodeStream = BitmapFactory.decodeStream(new FileInputStream(file));
            Log.e("tag1", "image loaded " + str + "/" + str2);
            return decodeStream;
        } catch (FileNotFoundException e) {
            Log.e("tag1", "image not loaded storage " + str + "/" + str2);
            e.printStackTrace();
            return null;
        }
    }

    static long getFolderSize(File file) {
        if (file.isDirectory()) {
            long j = 0;
            for (File file2 : file.listFiles()) {
                j += getFolderSize(file2);
            }
            return j;
        }
        return file.length();
    }

    public static String getFolderPath(String str, Context context) {
        File dir = new ContextWrapper(context.getApplicationContext()).getDir("themes", 0);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, str);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static String saveToInternalStorage(Bitmap bitmap, String str, String str2, Context context) {
        StringBuilder sb;
        FileOutputStream fileOutputStream = null;
        Log.e("tag1", "saving image :" + str + "/" + str2);
        File dir = new ContextWrapper(context.getApplicationContext()).getDir("themes", 0);

        Log.e("dir",""+dir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, str);
        if (!file.exists()) {
            file.mkdir();
        }
        Log.e("tag4", "saving file at path " + file.getAbsolutePath() + "/" + str2);
        File file2 = new File(file, str2);
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file2);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e2) {
                sb = new StringBuilder();
                sb.append("error 2 saving file ");
                sb.append(e2.getMessage());
                Log.e("tag12", sb.toString());
                e2.printStackTrace();
                return file2.getAbsolutePath();
            }
        } catch (Exception e3) {

            fileOutputStream2 = fileOutputStream;
            Log.e("tag13", "error 1 saving file " + e3.getMessage());
            e3.printStackTrace();
            if (file2.exists()) {
                file2.delete();
            }
            try {
                fileOutputStream2.close();
            } catch (IOException e4) {

                sb = new StringBuilder();
                sb.append("error 2 saving file ");
                sb.append(e4.getMessage());
                Log.e("tag12", sb.toString());
                e4.printStackTrace();
                return file2.getAbsolutePath();
            }
            return file2.getAbsolutePath();
        } catch (Throwable th2) {

            fileOutputStream2 = fileOutputStream;
            try {
                fileOutputStream2.close();
            } catch (IOException e5) {
                Log.e("tag12", "error 2 saving file " + e5.getMessage());
                e5.printStackTrace();
            }
        }
        return file2.getAbsolutePath();
    }

}
