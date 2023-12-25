package UEnginePackage.Components;

import UEnginePackage.Models.Uparticle;
import liveWallpaper.myapplication.MovmentUtils;


public class baseUpdater {
    public long creationTime;
    public int startTime;
    public int time;
    boolean inThePool = false;
    public float speedFactore = 1.0f;
    public long lastUpdateTime = System.currentTimeMillis();

    public void aplyUpdatePercent(float f, Uparticle uparticle) {
    }

    public void lastUpdate(Uparticle uparticle) {
    }

    public void recycle() {
    }

    public void reload() {
        this.creationTime = MovmentUtils.getTimeMs() + this.startTime;
    }

    public baseUpdater(int i, int i2) {
        this.time = i;
        this.startTime = i2;
        this.creationTime = System.currentTimeMillis() + i2;
    }

    public boolean update(Uparticle uparticle) {
        if (isUpdateTime()) {
            aplyUpdatePercent(((float) (System.currentTimeMillis() - this.lastUpdateTime)) / 1000.0f, uparticle);
            this.lastUpdateTime = System.currentTimeMillis();
        }
        if (isFinished()) {
            lastUpdate(uparticle);
            return false;
        }
        return true;
    }

    public boolean isFinished() {
        return this.creationTime + ((long) this.time) < System.currentTimeMillis();
    }

    public boolean isUpdateTime() {
        return this.creationTime <= System.currentTimeMillis() && this.creationTime + ((long) this.time) >= System.currentTimeMillis();
    }
}
