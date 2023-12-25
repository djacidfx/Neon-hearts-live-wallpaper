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


public class TFMagicTouch extends TouchEffectsBase {
    public TFMagicTouch() {
        this.title = "Magic Touch";
    }

    @Override 
    public void init(Context context) {
        super.init(context);
        Uspawner createSimpleSpawner = AssetsLoader.createSimpleSpawner();
        createSimpleSpawner.maxParticles = 0;
        range rangeVar = new range(15.0d, 9.0d);
        rangeVar.max /= 100.0d;
        rangeVar.min /= 100.0d;
        createSimpleSpawner.particleLife = new range(3000.0d, 1500.0d);
        createSimpleSpawner.particlesize = rangeVar;
        createSimpleSpawner.maxParticles = 0;
        ParticleSystem createSimpleParticleSystem = AssetsLoader.createSimpleParticleSystem(createSimpleSpawner, "touchEffecsAssets", "stars_8.-s.png");
        this.particleSystem = createSimpleParticleSystem;
        createSimpleParticleSystem.components = AssetsLoader.getComponant("touchEffecsAssets/growingStars.json");
    }

    @Override 
    public void update(LayerManager layerManager, float f, float f2) {
        Uparticle updateSpawner;
        super.update(layerManager, f, f2);
        ParticleSystem secondaryParticleSystem = layerManager.getSecondaryParticleSystem();
        if (secondaryParticleSystem != null && (updateSpawner = secondaryParticleSystem.updateSpawner(true, f, f2)) != null && updateSpawner.img != null && layerManager.getWallpaper() != null) {
            updateSpawner.img.setColor(AppUtils.lighten(layerManager.getWallpaper().getColorAt(f / Uscreen.width, f2 / Uscreen.height), 0.3d));
        }
        Log.e("partSize", this.particleSystem.particles.size() + "");
    }

    @Override 
    public ParticleSystem getParticleSystem() {
        return this.particleSystem;
    }

    @Override 
    public void destroy() {
        super.destroy();
        this.particleSystem.destroy();
    }
}
