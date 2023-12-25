package liveWallpaper.myapplication;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;

import com.demo.lovelivewallpaper.R;


import com.demo.lovelivewallpaper.utils.PrefLoader;
import com.demo.lovelivewallpaper.utils.Uscreen;

public class ActivitySplashScreen extends basActivity {
    ProgressBar progressBar;
    boolean activityVisible = false;
    boolean supportLiveWallpaper = false;
    boolean splashInter = true;

    @Override 
    public void onBackPressed() {

    }

    void nextStatus() {
        Intent intent = new Intent(this, ActivityApply.class);
        ActivitySplashScreen.this.startActivity(intent);
        ActivitySplashScreen.this.finish();
    }

    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);
        continueOnCreate();



        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextStatus();
            }
        }, 3000);
    }

    private void continueOnCreate() {
        Log.e("tagS", "continueOnCreate ");
        this.supportLiveWallpaper = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER").resolveActivityInfo(getPackageManager(), 0) != null;
        Log.e("tagS", "supportLiveWallpaper " + this.supportLiveWallpaper);
        Uscreen.Init(this);
        setupPrefs();
        initScreenSize();
        try {
            if (getIntent().getExtras().getBoolean("rateUs")) {
                this.splashInter = false;
            }
        } catch (Exception unused) {
        }
        if (PrefLoader.LoadPref("glitterValueInitiated", this) == 0) {
            PrefLoader.SavePref("glitter", getString(R.string.glitter_enabled_by_default), this);
            PrefLoader.SavePref("glitterValueInitiated", "1", this);
        }
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.imageView2).setScaleX(2.0f);
        findViewById(R.id.imageView2).setScaleY(2.0f);
        findViewById(R.id.imageView2).setAlpha(0.0f);
        findViewById(R.id.imageView2).animate().scaleY(1.0f).scaleX(1.0f).alpha(1.0f).setStartDelay(300L).setDuration(600L).start();
    }

    @Override
    
    public void onDestroy() {
        super.onDestroy();
    }

    void initScreenSize() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        Log.e("screenSize3", point.x + "/" + point.y);
        StringBuilder sb = new StringBuilder();
        sb.append(point.y);
        sb.append("");
        PrefLoader.SavePref("screenHeight", sb.toString(), this);
        PrefLoader.SavePref("screenWidth", point.x + "", this);
        Uscreen.width = point.x;
        Uscreen.height = point.y;
    }

    private void setupPrefs() {
        if (PrefLoader.LoadPref("firstOpen", this) == 0) {
            PrefLoader.SavePref("firstOpen", "1", this);
            PrefLoader.SavePref(Statics.lastSelectedTouchEffectPref, "4", this);
            PrefLoader.SavePref(Statics.lastSelectedParticlePref, "1", this);
            PrefLoader.SavePref("speed", getString(R.string.defaultParticleSpeed), this);
            PrefLoader.SavePref("size", getString(R.string.defaultParticleSize), this);
            PrefLoader.SavePref("count", getString(R.string.defaultParticleCount), this);
            PrefLoader.SavePref(Statics.lastSelectedBehaviorPref, getString(R.string.defaultAnimation), this);
            PrefLoader.SavePref(Statics.lastSelectedTouchEffectPref, getString(R.string.defaultTouchEffect), this);
            PrefLoader.SavePref(Statics.lastSelectedWallpaperPref, getString(R.string.defaultWallpaper), this);
        }
        if (PrefLoader.LoadPref("notification3", this) == 0 && this.supportLiveWallpaper) {
            PrefLoader.SavePref("notification3", "1", this);
        }
    }

    @Override 
    public void onPause() {
        super.onPause();
        this.activityVisible = false;
    }
}
