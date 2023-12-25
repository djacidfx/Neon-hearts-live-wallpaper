package UEnginePackage.Models.layers;

import UEnginePackage.Components.baseUpdater;
import UEnginePackage.Components.positionUpdater;
import UEnginePackage.Models.Uparticle;
import UEnginePackage.Models.Uspawner;
import UEnginePackage.Models.Usprite;
import UEnginePackage.UGL.Uimage;
import UEnginePackage.UGL.Urect;
import android.graphics.Canvas;
import com.demo.lovelivewallpaper.Callbacks.ParticleDieCallback;
import com.demo.lovelivewallpaper.utils.AssetsLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;
import liveWallpaper.myapplication.MovmentUtils;
import org.json.JSONArray;


public class ParticleSystem extends baseLayer {
    public JSONArray components;
    public List<Uimage> images;
    public long lasTimeUpdated;
    ParticleDieCallback particleDieCallback;
    public List<Uparticle> particles;
    private Uspawner spawner;
    public boolean moveInsideBounds = false;
    public boolean checkInsideBoundByPS = false;
    public float speedFactore = 1.0f;
    public float sizeFactore = 1.0f;
    public int particleCount = 0;
    boolean destoryed = false;
    public boolean immortalParticles = false;

    public void init() {
    }

    @Override 
    public void destroy() {
        super.destroy();
        this.destoryed = true;
        this.components = null;
        List<Uimage> list = this.images;
        if (list != null) {
            synchronized (list) {
                for (int i = 0; i < this.images.size(); i++) {
                    this.images.get(i).destroy();
                }
            }
            this.images.clear();
        }
        clearParticles();
    }

    private void clearParticles() {
        synchronized (this.particles) {
            for (int i = 0; i < this.particles.size(); i++) {
                this.particles.get(i).recycle();
            }
        }
        this.particles.clear();
    }

    public Uspawner getSpawner() {
        return this.spawner;
    }

    public void setSpawner(Uspawner uspawner) {
        if (uspawner == null) {
            return;
        }
        this.spawner = uspawner;
        int i = this.particleCount;
        if (i != 0) {
            uspawner.maxParticles = i;
        }
        float f = this.sizeFactore;
        if (f != 0.0f) {
            uspawner.sizeFactore = f;
        }
        uspawner.maxParticles = this.particleCount;
    }

    public void setSpeedFactore(float f) {
        this.speedFactore = f;
    }

    public void setParticleCount(int i) {
        this.particleCount = i;
        Uspawner uspawner = this.spawner;
        if (uspawner != null) {
            uspawner.maxParticles = i;
        }
    }

    public void setSizeFactore(float f) {
        this.sizeFactore = f;
        Uspawner uspawner = this.spawner;
        if (uspawner != null) {
            uspawner.sizeFactore = f;
        }
    }

    @Override 
    public void setBound(Urect urect) {
        if (urect == null) {
            return;
        }
        super.setBound(urect);
        this.bound.setLeft(0.0d);
        this.particles.clear();
        this.bound.clearChilds();
    }

    public ParticleSystem(List<Uimage> list, Urect urect, Uspawner uspawner) {
        this.particles = new ArrayList();
        this.bound = urect;
        this.images = list;
        this.spawner = uspawner;
        this.particles = new ArrayList();
    }

    @Override 
    public boolean update() {
        if (this.enabled) {
            synchronized (this.particles) {
                int i = 0;
                while (i < this.particles.size()) {
                    try {
                        Uparticle uparticle = this.particles.get(i);
                        if (uparticle != null) {
                            uparticle.update();
                            checkIfOutOfBound(uparticle);
                            if (uparticle.died) {
                                if (!this.immortalParticles) {
                                    List<Uparticle> list = this.particles;
                                    if (list != null) {
                                        list.remove(uparticle);
                                        uparticle.recycle();
                                    }
                                    i--;
                                    ParticleDieCallback particleDieCallback = this.particleDieCallback;
                                    if (particleDieCallback != null) {
                                        particleDieCallback.particleDied(uparticle);
                                    }
                                } else {
                                    uparticle.died = false;
                                    uparticle.creationTime = MovmentUtils.getTimeMs();
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    i++;
                }
            }
            return updateSpawner(false, 0.0f, 0.0f) != null;
        }
        return false;
    }

    public Uparticle updateSpawner(boolean z, float f, float f2) {
        Uspawner uspawner = this.spawner;
        if (uspawner != null) {
            List<Uparticle> list = this.particles;
            Uparticle update = uspawner.update(list != null ? list.size() : 0, this, z);
            if (update == null) {
                return null;
            }
            if (f != f2) {
                update.setLeft(f);
                update.setTop(f2);
            }
            addParticle(update);
            return update;
        }
        return null;
    }

    public void addParticle(Uparticle uparticle) {
        if (uparticle == null) {
            return;
        }
        try {
            JSONArray jSONArray = this.components;
            if (jSONArray != null && !this.destoryed) {
                synchronized (jSONArray) {
                    for (int i = 0; i < this.components.length(); i++) {
                        baseUpdater parseComponant = AssetsLoader.parseComponant(this.components.getJSONObject(i), uparticle, this.bound);
                        if (this.immortalParticles) {
                            uparticle.reloadComponants = true;
                        }
                        uparticle.addComponant(parseComponant);
                        if (parseComponant instanceof positionUpdater) {
                            parseComponant.speedFactore = this.speedFactore;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        List<Uparticle> list = this.particles;
        if (list == null) {
            return;
        }
        list.add(uparticle);
    }

    private void checkIfOutOfBound(Uparticle uparticle) {
        positionUpdater posisionUpdater = uparticle.getPosisionUpdater();
        if (posisionUpdater != null) {
            boolean z = this.checkInsideBoundByPS;
            if (z && this.moveInsideBounds) {
                posisionUpdater.checkIfOutOfBound(uparticle, this.bound, true);
            } else if (!z) {
                posisionUpdater.checkIfOutOfBound(uparticle, this.bound, false);
            }
        }
    }

    public Uimage getRandomImage() {
        List<Uimage> list = this.images;
        if (list == null || list.size() == 0) {
            return null;
        }
        return this.images.get(new Random().nextInt(this.images.size())).m1clone();
    }

    @Override 
    public void draw(GL10 gl10, float f) {
        super.draw(gl10, f);
        if (this.enabled) {
            this.lasTimeUpdated = System.currentTimeMillis();
            synchronized (this.particles) {
                for (int i = 0; i < this.particles.size(); i++) {
                    try {
                        Uparticle uparticle = this.particles.get(i);
                        if (uparticle != null) {
                            uparticle.Draw(gl10, f);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Override 
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.enabled) {
            this.lasTimeUpdated = System.currentTimeMillis();
            synchronized (this.particles) {
                for (int i = 0; i < this.particles.size(); i++) {
                    this.particles.get(i).Draw(canvas);
                }
            }
        }
    }

    public List<Uimage> cloneImages() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.images) {
            for (int i = 0; i < this.images.size(); i++) {
                arrayList.add(this.images.get(i).m1clone());
            }
        }
        return arrayList;
    }

    
    public ParticleSystem m1clone() {
        ParticleSystem particleSystem = new ParticleSystem(cloneImages(), this.bound, this.spawner.m0clone());
        particleSystem.components = this.components;
        return particleSystem;
    }

    public boolean hasOneImageMultipleParticles() {
        return this.images.size() != 0 && (this.images.get(0) instanceof Usprite) && ((Usprite) this.images.get(0)).isMultipleImages;
    }

    public boolean hasOneImageMultipleSprites() {
        return this.images.size() != 0 && (this.images.get(0) instanceof Usprite) && ((Usprite) this.images.get(0)).rows > 1;
    }

    public void setOnParticleDies(ParticleDieCallback particleDieCallback) {
        this.particleDieCallback = particleDieCallback;
    }

    public void forwardTimeBy(long j) {
        List<Uparticle> list = this.particles;
        if (list != null) {
            synchronized (list) {
                for (int i = 0; i < this.particles.size(); i++) {
                    try {
                        Uparticle uparticle = this.particles.get(i);
                        if (uparticle != null) {
                            uparticle.forwardTimeBy(j);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void disable() {
        this.enabled = false;
        clearParticles();
    }

    public void enable() {
        this.enabled = true;
    }
}
