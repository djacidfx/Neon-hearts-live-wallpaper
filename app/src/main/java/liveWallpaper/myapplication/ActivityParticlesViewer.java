package liveWallpaper.myapplication;

import com.demo.lovelivewallpaper.AdAdmob;
import com.demo.lovelivewallpaper.Adapters.LiveWallpapersEffectsAdapter;
import UEnginePackage.Models.layers.LayerManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lovelivewallpaper.R;

import java.util.List;
import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.PrefLoader;
import com.demo.lovelivewallpaper.utils.Uscreen;
import com.demo.lovelivewallpaper.utils.onReady2;


public class ActivityParticlesViewer extends basActivity {
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
                ActivityParticlesViewer.this.onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.title)).setText("Choose Particle");
        Uscreen.Init(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        String string = getIntent().getExtras().getString("imagePath");
        this.lms = AssetsLoader.getParticlesSimple();
        for (int i = 0; i < this.lms.size(); i++) {
            this.lms.get(i).getParticleSystem().setSizeFactore(1.5f);
        }
        findViewById(R.id.gallery).setVisibility(View.INVISIBLE);
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
                PrefLoader.SavePref(Statics.lastSelectedParticlePref, intValue + "", ActivityParticlesViewer.this);
                ActivityParticlesViewer.this.onBackPressed();
            }
        });
        this.adapter = liveWallpapersEffectsAdapter;
        liveWallpapersEffectsAdapter.firstItemEmpty = true;
        this.recyclerView.setAdapter(this.adapter);
    }

    @Override 
    public void onBackPressed() {
        this.activityRunning = false;
        super.onBackPressed();
    }

    
    @Override 
    public void onDestroy() {
        super.onDestroy();
        this.recyclerView.setVisibility(View.GONE);
        this.adapter.onItemChoosed = null;
        this.adapter.context = null;
        this.adapter.list = null;
        this.adapter = null;
        if (this.lms != null) {
            for (int i = 0; i < this.lms.size(); i++) {
                if (this.lms.get(i) != null) {
                    this.lms.get(i).destroyAll();
                }
            }
            this.lms = null;
        }
    }

    @Override 
    public void onResume() {
        super.onResume();
    }
}
