package UEnginePackage.Components;

import UEnginePackage.Models.Uparticle;
import UEnginePackage.UGL.Transition;
import UEnginePackage.UGL.Transition_Type;


public class glitterUpdater extends baseUpdater {
    double from;
    boolean killWhenFinish;
    double to;

    public static glitterUpdater random(Uparticle uparticle) {
        return new glitterUpdater(uparticle, 0.0d, 100.0d, ((int) (Math.random() * 4000.0d)) + 2000, (int) (Math.random() * 3000.0d), false);
    }

    public glitterUpdater(Uparticle uparticle, double d, double d2, int i, int i2, boolean z) {
        super(i, i2);
        this.to = d2;
        this.from = d;
        this.time = i;
        this.killWhenFinish = z;
        update(uparticle);
    }

    @Override 
    public void reload() {
        super.reload();
        this.time = ((int) (Math.random() * 4000.0d)) + 2000;
        this.startTime = (int) (Math.random() * 3000.0d);
        this.creationTime = System.currentTimeMillis() + this.startTime;
    }

    @Override 
    public boolean update(Uparticle uparticle) {
        if (super.update(uparticle)) {
            if (isUpdateTime()) {
                uparticle.setAlpha(Transition.GetValue(Transition_Type.easeInQuad, System.currentTimeMillis() - this.creationTime, this.from, this.to, this.time));
                return true;
            }
            return true;
        }
        return false;
    }
}
