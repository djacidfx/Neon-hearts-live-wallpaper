package UEnginePackage.touchEffectsPackage;

import UEnginePackage.Components.positionUpdater;
import UEnginePackage.Models.Uparticle;
import UEnginePackage.Models.Uspawner;
import UEnginePackage.Models.range;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import android.content.Context;
import android.util.Log;
import com.demo.lovelivewallpaper.utils.AssetsLoader;


public class TFMagicTouchDirectional extends TouchEffectsBase {
    float lastX;
    float lastY;

    public TFMagicTouchDirectional() {
        this.title = "Magic Touch Directional";
    }

    @Override 
    public void init(Context context) {
        super.init(context);
        Uspawner createSimpleSpawner = AssetsLoader.createSimpleSpawner();
        createSimpleSpawner.maxParticles = 0;
        range rangeVar = new range(10.0d, 7.0d);
        rangeVar.max /= 100.0d;
        rangeVar.min /= 100.0d;
        createSimpleSpawner.particleLife = new range(1500.0d, 500.0d);
        createSimpleSpawner.particlesize = rangeVar;
        createSimpleSpawner.maxParticles = 0;
        ParticleSystem createSimpleParticleSystem = AssetsLoader.createSimpleParticleSystem(createSimpleSpawner, "touchEffecsAssets", "stars_8.-s.png");
        this.particleSystem = createSimpleParticleSystem;
        createSimpleParticleSystem.components = AssetsLoader.getComponant("touchEffecsAssets/directional stars.json");
        this.particleSystem.moveInsideBounds = true;
        this.initiated = true;
    }

    @Override 
    public void update(LayerManager layerManager, float f, float f2) {
        Uparticle updateSpawner;
        super.update(layerManager, f, f2);
        ParticleSystem secondaryParticleSystem = layerManager.getSecondaryParticleSystem();
        if (secondaryParticleSystem != null && (updateSpawner = secondaryParticleSystem.updateSpawner(true, f, f2)) != null) {
            float f3 = this.lastX;
            double d = (f - f3) / 1.0f;
            float f4 = this.lastY;
            double d2 = (f2 - f4) / 1.0f;
            double abs = Math.abs(calculateDistanceBetweenPoints(f3, f4, updateSpawner.getLeft(), updateSpawner.getTop()));
            positionUpdater posisionUpdater = updateSpawner.getPosisionUpdater();
            Log.e("tag31", abs + "/" + d);
            if (posisionUpdater != null) {
                Double.isNaN(d);
                posisionUpdater.xSpeed = d * abs * 0.5d;
                Double.isNaN(d2);
                posisionUpdater.ySpeed = d2 * abs * 0.5d;
            }
        }
        this.lastX = f;
        this.lastY = f2;
        Log.e("partSize", this.particleSystem.particles.size() + "");
    }

    @Override 
    public ParticleSystem getParticleSystem() {
        return this.particleSystem;
    }

    @Override 
    public void updateBegin(LayerManager layerManager, float f, float f2) {
        super.updateBegin(layerManager, f, f2);
        this.lastX = f;
        this.lastY = f2;
    }
}
