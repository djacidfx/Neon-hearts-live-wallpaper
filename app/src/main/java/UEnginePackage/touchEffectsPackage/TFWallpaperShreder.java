package UEnginePackage.touchEffectsPackage;

import UEnginePackage.Models.Uparticle;
import UEnginePackage.Models.Uspawner;
import UEnginePackage.Models.range;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import android.content.Context;
import android.util.Log;
import com.demo.lovelivewallpaper.utils.AppUtils;
import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.Uscreen;


public class TFWallpaperShreder extends TouchEffectsBase {
    public TFWallpaperShreder() {
        this.title = "Wallpaper Shreder";
    }

    @Override 
    public void init(Context context) {
        super.init(context);
        Uspawner createSimpleSpawner = AssetsLoader.createSimpleSpawner();
        createSimpleSpawner.maxParticles = 0;
        range rangeVar = new range(7.0d, 2.0d);
        rangeVar.max /= 100.0d;
        rangeVar.min /= 100.0d;
        createSimpleSpawner.particleLife = new range(2500.0d, 1500.0d);
        createSimpleSpawner.particlesize = rangeVar;
        createSimpleSpawner.maxParticles = 0;
        ParticleSystem createSimpleParticleSystem = AssetsLoader.createSimpleParticleSystem(createSimpleSpawner, "", "circle.png");
        this.particleSystem = createSimpleParticleSystem;
        synchronized (createSimpleParticleSystem.images) {
            for (int i = 0; i < this.particleSystem.images.size(); i++) {
                this.particleSystem.images.get(i).shinyFilter = false;
            }
        }
        this.particleSystem.components = AssetsLoader.getComponant("touchEffecsAssets/slowBehavior.json");
    }

    @Override 
    public void update(LayerManager layerManager, float f, float f2) {
        Uparticle updateSpawner;
        super.update(layerManager, f, f2);
        ParticleSystem particleSystem = this.particleSystem;
        if (particleSystem != null && (updateSpawner = particleSystem.updateSpawner(true, f, f2)) != null && updateSpawner.img != null && layerManager != null && layerManager.getWallpaper() != null) {
            int lighten = AppUtils.lighten(layerManager.getWallpaper().getColorAt(f / Uscreen.width, f2 / Uscreen.height), 0.3d);
            Log.e("tag1", "wlp shreder color:" + lighten);
            updateSpawner.img.setColor(lighten);
            updateSpawner.img.shinyFilter = false;
        }
        Log.e("partSize", this.particleSystem.particles.size() + "");
    }

    @Override 
    public ParticleSystem getParticleSystem() {
        return this.particleSystem;
    }
}
