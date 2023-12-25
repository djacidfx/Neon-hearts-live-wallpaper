package com.demo.lovelivewallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class PrefLoader {
    public static String DataFileName = "IDFU.data";
    public static SharedPreferences Tpdata = null;
    static String sd = "data";

    public static int LoadPref(String str, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataFileName, 0);
        Tpdata = sharedPreferences;
        try {
            return Integer.parseInt(sharedPreferences.getString(str, "0"));
        } catch (Exception unused) {
            return 0;
        }
    }

    public static String LoadPrefString(String str, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataFileName, 0);
        Tpdata = sharedPreferences;
        try {
            return sharedPreferences.getString(str, "");
        } catch (Exception unused) {
            return "";
        }
    }

    public static void SavePrefString(String str, String str2, Context context) {
        if (Tpdata == null) {
            Tpdata = context.getSharedPreferences(DataFileName, 0);
        }
        SharedPreferences.Editor edit = Tpdata.edit();
        edit.putString(str, str2);
        edit.commit();
        Log.e("prefloader", str2);
    }

    public static void SavePref(String str, String str2, Context context) {
        if (Tpdata == null) {
            Tpdata = context.getSharedPreferences(DataFileName, 0);
        }
        SharedPreferences.Editor edit = Tpdata.edit();
        edit.putString(str, str2);
        edit.commit();
        Log.e("prefloader", str2);
    }
}
