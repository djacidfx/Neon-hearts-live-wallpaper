package UEnginePackage.touchEffectsPackage;

import UEnginePackage.Components.positionUpdater;
import UEnginePackage.Models.Uparticle;
import UEnginePackage.Models.cords;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import java.util.List;


public class TFPulForce extends TouchEffectsBase {
    public TFPulForce() {
        this.title = "Pull Force";
    }

    @Override 
    public void update(LayerManager layerManager, float f, float f2) {
        List<Uparticle> list;
        super.update(layerManager, f, f2);
        ParticleSystem particleSystem = layerManager.getParticleSystem();
        if (particleSystem == null || (list = particleSystem.particles) == null) {
            return;
        }
        synchronized (list) {
            for (int i = 0; i < particleSystem.particles.size(); i++) {
                applyEffectToParticle(particleSystem.particles.get(i), f, f2);
            }
        }
    }

    private void applyEffectToParticle(Uparticle uparticle, float f, float f2) {
        positionUpdater posisionUpdater = uparticle.getPosisionUpdater();
        if (posisionUpdater == null) {
            return;
        }
        double d = f;
        double left = uparticle.getLeft();
        Double.isNaN(d);
        double d2 = f2;
        double top = uparticle.getTop();
        Double.isNaN(d2);
        cords cordsVar = posisionUpdater.velocity;
        cordsVar.x = ((d - left) / 1.0d) * 1.5d;
        cordsVar.y = ((d2 - top) / 1.0d) * 1.5d;
    }
}
