package UEnginePackage.touchEffectsPackage;

import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import android.content.Context;


public class TouchEffectsBase {
    boolean initiated;
    ParticleSystem particleSystem;
    public String title = "No Effect";

    public void destroy() {
    }

    public void update(LayerManager layerManager, float f, float f2) {
    }

    public void updateBegin(LayerManager layerManager, float f, float f2) {
    }

    public void updateEnd(LayerManager layerManager, float f, float f2) {
    }

    public void init(Context context) {
        this.initiated = true;
    }

    public double calculateDistanceBetweenPoints(double d, double d2, double d3, double d4) {
        double d5 = d4 - d2;
        double d6 = d3 - d;
        return Math.sqrt((d5 * d5) + (d6 * d6));
    }

    public ParticleSystem getParticleSystem() {
        return this.particleSystem;
    }
}
