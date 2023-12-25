package UEnginePackage.Models;

import UEnginePackage.Components.baseUpdater;
import UEnginePackage.Components.positionUpdater;
import UEnginePackage.UGL.Uimage;
import UEnginePackage.UGL.Urect;
import android.graphics.Canvas;
import android.util.Log;
import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import liveWallpaper.myapplication.MovmentUtils;


public class Uparticle extends Urect {
    static int objectCountInThePool;
    private static final Pools.Pool<Uparticle> sPool = new Pools.SimplePool(1000);
    public List<baseUpdater> componants;
    public long creationTime;
    public boolean died;
    public Uimage img;
    boolean inThePool;
    public int life;
    public boolean reloadComponants;
    public int type;

    public static synchronized Uparticle obtain(double d, double d2, double d3, double d4, Uimage uimage, int i) {
        Uparticle acquire;

            synchronized (Uparticle.class) {
                acquire = sPool.acquire();
                if (acquire != null) {
                    acquire.scale = 1.0d;
                    acquire.alpha = 255.0d;
                    acquire.initiate(d, d2, d3, d4, uimage, i);
                    acquire.died = false;
                    acquire.inThePool = false;
                    objectCountInThePool--;
                } else {
                    acquire = new Uparticle(d, d2, d3, d4, uimage, i);
                }
            }
            return acquire;

    }

    public synchronized void recycle() {
        if (this.inThePool) {
            return;
        }
        this.inThePool = true;
        objectCountInThePool++;
        clearChilds();
        clearComponants();
        sPool.release(this);
    }

    private void clearComponants() {
        synchronized (this.componants) {
            if (this.componants != null) {
                for (int i = 0; i < this.componants.size(); i++) {
                    baseUpdater baseupdater = this.componants.get(i);
                    if (baseupdater != null) {
                        baseupdater.recycle();
                    }
                }
                this.componants.clear();
            }
        }
    }

    boolean isGoingLeft() {
        synchronized (this.componants) {
            int i = 0;
            while (true) {
                if (i >= this.componants.size()) {
                    return false;
                }
                if (!(this.componants.get(i) instanceof positionUpdater)) {
                    i++;
                } else {
                    return ((positionUpdater) this.componants.get(i)).xSpeed < 0.0d;
                }
            }
        }
    }

    public void addComponant(baseUpdater baseupdater) {
        this.componants.add(baseupdater);
    }

    @Override 
    public void Draw(GL10 gl10, float f) {
        if (getChildrens() != null && getChildrens().size() > 1) {
            Log.e("tag1", "particle with children more than 1 " + getChildrens().size());
        }
        if (isGoingLeft()) {
            super.Draw(gl10, f);
        } else {
            super.Draw(gl10, f);
        }
    }

    @Override 
    public void Draw(Canvas canvas) {
        int save = canvas.save();
        double d = this.scale;
        canvas.scale((float) d, (float) d, (float) getCenterX(), (float) getCenterY());
        if (isGoingLeft()) {
            super.Draw(canvas);
        } else {
            super.Draw(canvas);
        }
        canvas.restoreToCount(save);
    }

    private Uparticle(double d, double d2, double d3, double d4, Uimage uimage, int i) {
        super(d, d2, d3, d4);
        this.inThePool = false;
        this.componants = new ArrayList();
        this.type = 0;
        this.reloadComponants = false;
        initiate(d, d2, d3, d4, uimage, i);
    }

    void initiate(double d, double d2, double d3, double d4, Uimage uimage, int i) {
        clearChilds();
        clearComponants();
        this.x = d;
        this.y = d2;
        this.height = d4;
        this.width = d3;
        this.life = i;
        this.creationTime = MovmentUtils.getTimeMs();
        this.img = uimage;
        AddChild(uimage);
    }

    public void update() {
        if (this.died) {
            return;
        }
        if (this.creationTime + this.life < MovmentUtils.getTimeMs()) {
            this.died = true;
        }
        synchronized (this.componants) {
            int i = 0;
            while (i < this.componants.size()) {
                baseUpdater baseupdater = this.componants.get(i);
                if (baseupdater != null && !baseupdater.update(this)) {
                    if (this.reloadComponants) {
                        baseupdater.reload();
                    } else {
                        baseupdater.recycle();
                        this.componants.remove(i);
                        i--;
                    }
                }
                i++;
            }
        }
        Uimage uimage = this.img;
        if (uimage != null) {
            uimage.setWidth(getWidth());
            this.img.setHeight(getHeight());
            this.img.setRotate(this.rotate);
        }
    }

    public positionUpdater getPosisionUpdater() {
        synchronized (this.componants) {
            for (int i = 0; i < this.componants.size(); i++) {
                if (this.componants.get(i) instanceof positionUpdater) {
                    return (positionUpdater) this.componants.get(i);
                }
            }
            return null;
        }
    }

    public void debugComponants() {
        if (this.componants != null) {
            for (int i = 0; i < this.componants.size(); i++) {
                if (this.componants.get(i) != null) {
                    Log.e("tag3", "componant found :" + this.componants.get(i).getClass().getName());
                }
            }
        }
    }

    public void forwardTimeBy(long j) {
        this.creationTime -= j;
        List<baseUpdater> list = this.componants;
        if (list != null) {
            synchronized (list) {
                for (int i = 0; i < this.componants.size(); i++) {
                    try {
                        baseUpdater baseupdater = this.componants.get(i);
                        if (baseupdater != null) {
                            baseupdater.creationTime -= j;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void setImage(Uimage uimage) {
        this.img = uimage.m1clone();
        clearChilds();
        AddChild(this.img);
    }
}
