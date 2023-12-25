package UEnginePackage.Components;

import UEnginePackage.Models.Uparticle;
import UEnginePackage.UGL.Transition;
import UEnginePackage.UGL.Transition_Type;
import androidx.core.util.Pools;
import liveWallpaper.myapplication.MovmentUtils;


public class fadeUpdater extends baseUpdater {
    static int objectCountInThePool;
    private static final Pools.Pool<fadeUpdater> sPool = new Pools.SimplePool(300);
    double from;
    boolean killWhenFinish;
    double to;

    public static fadeUpdater obtain(Uparticle uparticle, double d, double d2, int i, int i2, boolean z) {
        fadeUpdater acquire = sPool.acquire();
        if (acquire != null) {
            objectCountInThePool--;
            acquire.inThePool = false;
            acquire.initiate(uparticle, d, d2, i, i2, z);
            return acquire;
        }
        return new fadeUpdater(uparticle, d, d2, i, i2, z);
    }

    @Override 
    public void recycle() {
        super.recycle();
        if (this.inThePool) {
            return;
        }
        this.inThePool = true;
        sPool.release(this);
        objectCountInThePool++;
    }

    private fadeUpdater(Uparticle uparticle, double d, double d2, int i, int i2, boolean z) {
        super(i, i2);
        initiate(uparticle, d, d2, i, i2, z);
    }

    private void initiate(Uparticle uparticle, double d, double d2, int i, int i2, boolean z) {
        this.to = d2;
        this.from = d;
        this.time = i;
        this.killWhenFinish = z;
        this.startTime = i2;
        this.creationTime = System.currentTimeMillis() + i2;
        this.lastUpdateTime = System.currentTimeMillis();
        update(uparticle);
    }

    @Override 
    public void lastUpdate(Uparticle uparticle) {
        super.lastUpdate(uparticle);
        uparticle.setAlpha(this.to);
    }

    @Override 
    public boolean update(Uparticle uparticle) {
        if (super.update(uparticle)) {
            if (isUpdateTime()) {
                uparticle.setAlpha(Transition.GetValue(Transition_Type.easeInQuad, MovmentUtils.getTimeMs() - this.creationTime, this.from, this.to, this.time));
                return true;
            }
            return true;
        }
        return false;
    }
}
