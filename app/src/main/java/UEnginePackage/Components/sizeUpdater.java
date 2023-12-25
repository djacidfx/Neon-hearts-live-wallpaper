package UEnginePackage.Components;

import UEnginePackage.Models.Uparticle;
import UEnginePackage.UGL.Transition;
import UEnginePackage.UGL.Transition_Type;
import androidx.core.util.Pools;


public class sizeUpdater extends baseUpdater {
    static int objectCountInThePool;
    private static final Pools.Pool<sizeUpdater> sPool = new Pools.SimplePool(300);
    double fromScal;
    double initHight;
    double initWidth;
    boolean killWhenFinish;
    double toScale;

    public static sizeUpdater obtain(Uparticle uparticle, double d, double d2, int i, int i2, boolean z) {
        sizeUpdater acquire = sPool.acquire();
        if (acquire != null) {
            objectCountInThePool--;
            acquire.inThePool = false;
            acquire.initiate(uparticle, d, d2, i, i2, z);
            return acquire;
        }
        return new sizeUpdater(uparticle, d, d2, i, i2, z);
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

    private sizeUpdater(Uparticle uparticle, double d, double d2, int i, int i2, boolean z) {
        super(i, i2);
        initiate(uparticle, d, d2, i, i2, z);
    }

    void initiate(Uparticle uparticle, double d, double d2, int i, int i2, boolean z) {
        this.fromScal = d;
        this.toScale = d2;
        this.time = i;
        this.killWhenFinish = z;
        this.initWidth = uparticle.getWidth();
        this.initHight = uparticle.getHeight();
        this.creationTime = System.currentTimeMillis() + i2;
    }

    @Override 
    public void lastUpdate(Uparticle uparticle) {
        super.lastUpdate(uparticle);
        uparticle.setWidth(this.initWidth * this.toScale);
        uparticle.setHeight(this.initHight * this.toScale);
    }

    @Override 
    public boolean update(Uparticle uparticle) {
        if (super.update(uparticle)) {
            if (isUpdateTime()) {
                double d = this.initWidth;
                Transition_Type transition_Type = Transition_Type.easeInQuad;
                double GetValue = Transition.GetValue(transition_Type, System.currentTimeMillis() - this.creationTime, this.fromScal * d, this.toScale * d, this.time);
                double d2 = this.initHight;
                double GetValue2 = Transition.GetValue(transition_Type, System.currentTimeMillis() - this.creationTime, this.fromScal * d2, this.toScale * d2, this.time);
                uparticle.setLeft(uparticle.getLeft() + ((uparticle.getWidth() - GetValue) / 2.0d));
                uparticle.setTop(uparticle.getTop() + ((uparticle.getHeight() - GetValue2) / 2.0d));
                uparticle.setWidth(GetValue);
                uparticle.setHeight(GetValue2);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override 
    public void aplyUpdatePercent(float f, Uparticle uparticle) {
        super.aplyUpdatePercent(f, uparticle);
    }
}
