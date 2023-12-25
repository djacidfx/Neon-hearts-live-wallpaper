package com.demo.lovelivewallpaper.utils;

import com.demo.lovelivewallpaper.Adapters.TouchEffectsAdapter;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lovelivewallpaper.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import liveWallpaper.myapplication.Statics;
import org.json.JSONObject;


public class DialogHelper {
    static onReady or1;

    public static void showTouchEffectsChooser(Context context, final onReady2 onready2) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.requestWindowFeature(1);
        bottomSheetDialog.setContentView(R.layout.wallpaper_chooser_layout);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(17170445);
        bottomSheetDialog.show();
        ((TextView) bottomSheetDialog.findViewById(R.id.title)).setText("Choose Touch Effect");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        RecyclerView recyclerView = (RecyclerView) bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new TouchEffectsAdapter(AssetsLoader.getListOfTouchEffects(), PrefLoader.LoadPref(Statics.lastSelectedTouchEffectPref, context), context, new onReady2() {
            @Override 
            public void ready(Object obj, Object obj2) {
                bottomSheetDialog.dismiss();
                onready2.ready(obj, obj2);
            }
        }));
        bottomSheetDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    public static void setlayerComponants(String str, LayerManager layerManager) {
        setlayerComponants("particleBehaviors", str, layerManager.getParticleSystem());
    }

    public static void setlayerComponants(String str, String str2, ParticleSystem particleSystem) {
        if (particleSystem == null) {
            return;
        }
        try {
            if (str.length() > 0) {
                str = str + "/";
                Log.e("tag3", "layerComponants folder is " + str + str2);
            } else {
                Log.e("tag3", "layerComponants folder is empty " + str2);
            }

            Log.e("ddddddd", "dddddd " + str + str2);
            JSONObject jSONObject = new JSONObject(AssetsLoader.LoadData(str + str2));
            JSONObject jSONObject2 = jSONObject.getJSONObject("spawner");
            particleSystem.components = jSONObject.getJSONArray("componants");
            particleSystem.setSpawner(AssetsLoader.parseSpawner(jSONObject2));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void showUnlockWallpaperDialog(Context context, String str, onReady onready) {
        or1 = onready;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.unlock_wallpaper_dialog);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.show();
        Uscreen.Init(context);
        ViewGroup.LayoutParams layoutParams = dialog.findViewById(R.id.bg).getLayoutParams();
        double d = Uscreen.width;
        Double.isNaN(d);
        layoutParams.width = (int) (d * 0.8d);
        dialog.findViewById(R.id.watchIt).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                dialog.dismiss();
                if (DialogHelper.or1 != null) {
                    DialogHelper.or1.ready(1);
                }
                DialogHelper.or1 = null;
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() { 
            @Override 
            public void onCancel(DialogInterface dialogInterface) {
                if (DialogHelper.or1 != null) {
                    DialogHelper.or1.ready(0);
                }
                DialogHelper.or1 = null;
            }
        });
    }
}
