package UEnginePackage.Components;

import UEnginePackage.Models.Uparticle;
import UEnginePackage.Models.cords;
import UEnginePackage.UGL.Urect;
import androidx.core.util.Pools;


public class positionUpdater extends baseUpdater {
    static int objectCountInThePool;
    private static final Pools.Pool<positionUpdater> sPool = new Pools.SimplePool(300);
    boolean killWhenFinish;
    long lastTimeRandomizedSpeed;
    public boolean moveInsideBounds;
    public int movmentBehavior;
    public cords randomize;
    boolean rotateWithMovment;
    public int rotationOffset;
    public cords velocity;
    public double xSpeed;
    public double ySpeed;

    public static positionUpdater obtain(Uparticle uparticle, double d, double d2, int i, int i2, boolean z, int i3, cords cordsVar, cords cordsVar2, boolean z2, boolean z3) {
        positionUpdater acquire = sPool.acquire();
        if (acquire != null) {
            acquire.inThePool = false;
            objectCountInThePool--;
            acquire.initiate(uparticle, d, d2, i, i2, z, i3, cordsVar, cordsVar2, z2, z3);
            return acquire;
        }
        return new positionUpdater(uparticle, d, d2, i, i2, z, i3, cordsVar, cordsVar2, z2, z3);
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

    private positionUpdater(Uparticle uparticle, double d, double d2, int i, int i2, boolean z, int i3, cords cordsVar, cords cordsVar2, boolean z2, boolean z3) {
        super(i, i2);
        this.moveInsideBounds = false;
        initiate(uparticle, d, d2, i, i2, z, i3, cordsVar, cordsVar2, z2, z3);
    }

    private void initiate(Uparticle uparticle, double d, double d2, int i, int i2, boolean z, int i3, cords cordsVar, cords cordsVar2, boolean z2, boolean z3) {
        this.xSpeed = d;
        this.rotateWithMovment = z3;
        this.velocity = cordsVar;
        this.moveInsideBounds = z2;
        this.ySpeed = d2;
        this.randomize = cordsVar2;
        this.time = i;
        this.movmentBehavior = i3;
        this.killWhenFinish = z;
        this.creationTime = System.currentTimeMillis();
        this.lastTimeRandomizedSpeed = System.currentTimeMillis();
        this.time = i;
        this.startTime = i2;
        this.creationTime = System.currentTimeMillis() + i2;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    @Override 
    public boolean update(Uparticle uparticle) {
        return super.update(uparticle);
    }

    @Override 
    public void lastUpdate(Uparticle uparticle) {
        super.lastUpdate(uparticle);
    }

    @Override 
    public void aplyUpdatePercent(float f, Uparticle uparticle) {
        double d;
        double d2;
        double left = uparticle.getLeft();
        double top = uparticle.getTop();
        int i = this.movmentBehavior;
        if (i == 0) {
            double d3 = this.lastTimeRandomizedSpeed;
            double d4 = this.randomize.z;
            Double.isNaN(d3);
            double d5 = 0.0d;
            if (d3 + d4 < System.currentTimeMillis()) {
                this.lastTimeRandomizedSpeed = System.currentTimeMillis();
                double d6 = Math.random() * 10.0d < 5.0d ? 1.0d : -1.0d;
                double d7 = this.randomize.x;
                Double.isNaN(d6);
                double d8 = d6 * d7;
                double d62 = f;
                Double.isNaN(d62);
                double random = d8 * d62 * Math.random();
                double d10 = Math.random() * 10.0d >= 5.0d ? -1.0d : 1.0d;
                double d11 = this.randomize.y;
                Double.isNaN(d10);
                Double.isNaN(d62);
                d2 = Math.random() * d10 * d11 * d62;
                d5 = random;
            } else {
                d2 = 0.0d;
            }
            double d12 = this.xSpeed;
            double d13 = this.velocity.x;
            d = top;
            double d9 = f;
            Double.isNaN(d9);
            this.xSpeed = d12 + (d13 * d9) + d5;
            double d15 = this.ySpeed;
            double d16 = this.velocity.y;
            Double.isNaN(d9);
            this.ySpeed = d15 + (d16 * d9) + d2;
            double left2 = uparticle.getLeft();
            double d162 = this.xSpeed;
            Double.isNaN(d9);
            double d18 = this.speedFactore;
            Double.isNaN(d18);
            double d17 = left2 + (d162 * d9 * d18);
            uparticle.setLeft(d17);
            double top2 = uparticle.getTop();
            double left22 = this.ySpeed;
            Double.isNaN(d9);
            double d20 = left22 * d9;
            double d21 = this.speedFactore;
            Double.isNaN(d21);
            uparticle.setTop(top2 + (d20 * d21));
        } else {
            double sin = Math.sin(i);
            double abs = Math.abs(this.xSpeed);
            double d22 = f;
            Double.isNaN(d22);
            uparticle.setLeft((sin * abs * d22) + uparticle.getLeft());
            double cos = Math.cos(this.movmentBehavior);
            double abs2 = this.xSpeed;
            double abs22 = Math.abs(abs2);
            Double.isNaN(d22);
            uparticle.setTop((cos * abs22 * d22) + uparticle.getTop());
            d = top;
        }
        if (this.rotateWithMovment) {
            double calculateAngle = calculateAngle(left, d, uparticle.getLeft(), uparticle.getTop());
            double d23 = this.rotationOffset;
            Double.isNaN(d23);
            uparticle.setRotate(calculateAngle + d23);
        }
        super.aplyUpdatePercent(f, uparticle);
    }

    public static double calculateAngle(double d, double d2, double d3, double d4) {
        double degrees = Math.toDegrees(Math.atan2(d3 - d, d4 - d2));
        return (Math.ceil((-degrees) / 360.0d) * 360.0d) + degrees;
    }

    public void checkIfOutOfBound(Uparticle uparticle, Urect urect, boolean z) {
        if (z || this.moveInsideBounds) {
            if (uparticle.getLeft() < urect.getLeft()) {
                this.xSpeed = Math.abs(this.xSpeed) * 0.7d;
            } else if (uparticle.getRight() > urect.getRight()) {
                this.xSpeed = Math.abs(this.xSpeed) * (-0.7d);
            } else if (uparticle.getTop() < urect.getTop()) {
                this.ySpeed = Math.abs(this.ySpeed) * 0.7d;
            } else if (uparticle.getBottom() > urect.getBottom()) {
                this.ySpeed = Math.abs(this.ySpeed) * (-0.7d);
            }
        }
    }
}
