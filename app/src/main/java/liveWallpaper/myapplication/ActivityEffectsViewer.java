package liveWallpaper.myapplication;

import com.demo.lovelivewallpaper.AdAdmob;
import com.demo.lovelivewallpaper.Adapters.LiveWallpapersEffectsAdapter;
import UEnginePackage.Utils.AppConfig;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lovelivewallpaper.R;

import java.util.ArrayList;
import java.util.List;

import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.DialogHelper;
import com.demo.lovelivewallpaper.utils.PrefLoader;
import com.demo.lovelivewallpaper.utils.Uscreen;
import com.demo.lovelivewallpaper.utils.onReady2;


public class ActivityEffectsViewer extends basActivity {
    public static LayerManager simple;
    boolean activityRunning = true;
    LiveWallpapersEffectsAdapter adapter;
    List<LayerManager> lms;
    RecyclerView recyclerView;

    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.wallpapers_activity);




        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd(this);



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityEffectsViewer.this.onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.title)).setText("Choose Effect");
        Uscreen.Init(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        String string = getIntent().getExtras().getString("imagePath");
        this.lms = new ArrayList();
        findViewById(R.id.gallery).setVisibility(View.INVISIBLE);
        simple = AssetsLoader.getParticlesSimple1(PrefLoader.LoadPref(Statics.lastSelectedParticlePref, this));
        List<String> listBehaviors = AppConfig.listBehaviors();

        for (int i = 0; i < listBehaviors.size(); i++) {
            LayerManager m2clone = AssetsLoader.getParticlesSimple1(i);
            
            DialogHelper.setlayerComponants(listBehaviors.get(i), m2clone);
            this.lms.add(m2clone);

            ParticleSystem particleSystem = m2clone.getParticleSystem();
            particleSystem.setSizeFactore(2.5f);
            particleSystem.getSpawner().maxParticles = 25;

        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { 
            @Override 
            public int getSpanSize(int i2) {
                return i2 == 4 ? 2 : 1;
            }
        });
        this.recyclerView.setLayoutManager(gridLayoutManager);
        LiveWallpapersEffectsAdapter liveWallpapersEffectsAdapter = new LiveWallpapersEffectsAdapter(this.lms, this, string, new onReady2() { 
            @Override 
            public void ready(Object obj, Object obj2) {
                int intValue = ((Integer) obj2).intValue();
                if (intValue > 3) {
                    intValue--;
                }
                PrefLoader.SavePref(Statics.lastSelectedBehaviorPref, intValue + "", ActivityEffectsViewer.this);
                ActivityEffectsViewer.this.onBackPressed();
            }
        });
        this.adapter = liveWallpapersEffectsAdapter;
        this.recyclerView.setAdapter(liveWallpapersEffectsAdapter);
    }

    @Override
    
    public void onResume() {
        super.onResume();
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
        this.activityRunning = false;
    }

    
    @Override
    
    public void onDestroy() {
        super.onDestroy();
        this.recyclerView.setVisibility(View.GONE);
        this.adapter.onItemChoosed = null;
        this.adapter.context = null;
        this.adapter.list = null;
        this.adapter = null;
        simple = null;
        if (this.lms != null) {
            for (int i = 0; i < this.lms.size(); i++) {
                if (this.lms.get(i) != null) {
                    this.lms.get(i).destroyAll();
                }
            }
            this.lms = null;
        }
    }
}
