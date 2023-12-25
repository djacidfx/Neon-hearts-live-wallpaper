package liveWallpaper.myapplication;

import com.demo.lovelivewallpaper.AdAdmob;
import com.demo.lovelivewallpaper.Adapters.AdapterStaticWallpapers;
import UEnginePackage.Utils.AppConfig;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lovelivewallpaper.R;

import com.demo.lovelivewallpaper.utils.Uscreen;
import com.demo.lovelivewallpaper.utils.onReady2;


public class ActivityStaticWallpapers extends basActivity {
    AdapterStaticWallpapers adapter;
    RecyclerView recyclerView;

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wallpapers_normal);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd(this);



        Uscreen.Init(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { 
            @Override 
            public int getSpanSize(int i) {
                return i == 4 ? 2 : 1;
            }
        });
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.adapter = new AdapterStaticWallpapers(AppConfig.listWallpapers(), this, new onReady2() { 
            @Override 
            public void ready(Object obj, Object obj2) {
                ActivityOptionsCompat makeSceneTransitionAnimation = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityStaticWallpapers.this, (View) obj2, "image");
                Intent intent = new Intent(ActivityStaticWallpapers.this, ActivityImageViewer.class);
                intent.putExtra("image", (String) obj);
                ActivityStaticWallpapers.this.startActivity(intent, makeSceneTransitionAnimation.toBundle());
            }
        });
        this.recyclerView.scrollToPosition(0);
        new Handler().postDelayed(new Runnable() { 
            @Override 
            public void run() {
                ActivityStaticWallpapers.this.recyclerView.scrollToPosition(0);
            }
        }, 1000L);
        this.recyclerView.setAdapter(this.adapter);
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override 
    public void onResume() {
        super.onResume();
    }
}
