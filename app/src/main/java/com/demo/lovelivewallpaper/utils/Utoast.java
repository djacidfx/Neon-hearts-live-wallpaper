package com.demo.lovelivewallpaper.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;

import com.demo.lovelivewallpaper.R;


public class Utoast {
    public static Toast lastToast;

    
    public enum ToastType {
        sucess,
        error,
        info,
        warning,
        normal
    }

    public static void show(Context context, String str) {
        show(context, str, ToastType.normal);
    }

    public static void show(Context context, String str, ToastType toastType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.toas_layout, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.text);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.icon);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.bg);
        if (toastType == ToastType.info) {
            ViewCompat.setBackgroundTintList(linearLayout, ColorStateList.valueOf(Color.parseColor("#2196f3")));
            imageView.setImageResource(R.drawable.ic_info);
        }
        if (toastType == ToastType.normal) {
            ViewCompat.setBackgroundTintList(linearLayout, ColorStateList.valueOf(Color.parseColor("#2196f3")));
            imageView.setVisibility(View.GONE);
        } else if (toastType == ToastType.warning) {
            ViewCompat.setBackgroundTintList(linearLayout, ColorStateList.valueOf(Color.parseColor("#ffb818")));
            imageView.setImageResource(R.drawable.ic_warning);
        } else if (toastType == ToastType.error) {
            ViewCompat.setBackgroundTintList(linearLayout, ColorStateList.valueOf(Color.parseColor("#e53935")));
            imageView.setImageResource(R.drawable.ic_close);
        } else if (toastType == ToastType.sucess) {
            ViewCompat.setBackgroundTintList(linearLayout, ColorStateList.valueOf(Color.parseColor("#4caf50")));
            imageView.setImageResource(R.drawable.ic_done_black_24dp);
        }
        textView.setText(str);
        Toast toast = lastToast;
        if (toast != null) {
            toast.cancel();
            lastToast = null;
        }
        Toast toast2 = new Toast(context);
        lastToast = toast2;
        toast2.setDuration(Toast.LENGTH_SHORT);
        lastToast.setView(inflate);
        lastToast.show();
    }
}
