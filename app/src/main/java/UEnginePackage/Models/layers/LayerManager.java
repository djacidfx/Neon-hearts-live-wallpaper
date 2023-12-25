package UEnginePackage.Models.layers;

import UEnginePackage.Models.UTexture;
import UEnginePackage.UGL.Uimage;
import UEnginePackage.UGL.Urect;
import UEnginePackage.touchEffectsPackage.TouchEffectsBase;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.Log;
import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.Uprocess;
import com.demo.lovelivewallpaper.utils.Utask;
import com.demo.lovelivewallpaper.utils.onReady;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;


public class LayerManager {
    public Urect bound;
    public boolean clearDrawing;
    public String filePath;
    public int position;
    public String title;
    public TouchEffectsBase touchEffect;
    public boolean touchEffectEnabled = true;
    public boolean loaded = false;
    public boolean loading = false;
    public List<baseLayer> layers = new ArrayList();

    public void setTouchEffect(TouchEffectsBase touchEffectsBase, Context context) {
        if (touchEffectsBase == null) {
            return;
        }
        TouchEffectsBase touchEffectsBase2 = this.touchEffect;
        if (touchEffectsBase2 != null) {
            touchEffectsBase2.destroy();
            this.touchEffect = null;
        }
        this.touchEffect = touchEffectsBase;
        ParticleSystem secondaryParticleSystem = getSecondaryParticleSystem();
        if (secondaryParticleSystem != null) {
            this.layers.remove(secondaryParticleSystem);
        }
        addLayer(touchEffectsBase.getParticleSystem());
    }

    public LayerManager(String str, Urect urect) {
        this.filePath = str;
        this.title = str;
        this.bound = urect;
    }

    public void loadLayers(Context context, final onReady onready) {
        if (this.loaded || this.loading) {
            return;
        }
        new Utask(null, new Uprocess() { 
            @Override 
            public void onPreRun(Object obj) {
                LayerManager.this.loading = true;
            }

            @Override 
            public Object onRun(Object obj) {
                LayerManager layerManager = LayerManager.this;
                layerManager.loading = true;
                return AssetsLoader.loadLayers(layerManager);
            }

            @Override 
            public void onFinish(Object obj) {
                onReady onready2;
                LayerManager layerManager = LayerManager.this;
                layerManager.loaded = true;
                layerManager.loading = false;
                if (!(obj instanceof LayerManager) || (onready2 = onready) == null) {
                    return;
                }
                onready2.ready(layerManager);
            }
        });
    }

    public void addLayer(baseLayer baselayer) {
        if (baselayer == null) {
            return;
        }
        baselayer.setBound(this.bound);
        this.layers.add(baselayer);
    }

    public void addLayer(int i, baseLayer baselayer) {
        baselayer.setBound(this.bound);
        this.layers.add(i, baselayer);
    }

    public void update() {
        synchronized (this.layers) {
            for (int i = 0; i < this.layers.size(); i++) {
                try {
                    baseLayer baselayer = this.layers.get(i);
                    if (baselayer != null) {
                        baselayer.update();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void draw(GL10 gl10, float f) {
        synchronized (this.layers) {
            for (int i = 0; i < this.layers.size(); i++) {
                try {
                    baseLayer baselayer = this.layers.get(i);
                    if (baselayer != null) {
                        baselayer.draw(gl10, f);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        List<baseLayer> list = this.layers;
        if (list != null && list.size() > 0 && this.clearDrawing) {
            canvas.drawColor(-1, PorterDuff.Mode.CLEAR);
        }
        synchronized (this.layers) {
            for (int i = 0; i < this.layers.size(); i++) {
                baseLayer baselayer = this.layers.get(i);
                if (baselayer != null) {
                    baselayer.draw(canvas);
                }
            }
        }
    }

    public void setBound(Urect urect) {
        this.bound = urect;
        Log.e("LayerManager", "setBound" + urect.getWidth() + "/" + urect.getHeight());
        synchronized (this.layers) {
            for (int i = 0; i < this.layers.size(); i++) {
                this.layers.get(i).setBound(urect);
            }
        }
    }

    public LayerManager m2clone() {
        LayerManager layerManager = new LayerManager(this.filePath, this.bound);
        synchronized (this.layers) {
            for (int i = 0; i < this.layers.size(); i++) {
                layerManager.layers.add(this.layers.get(i));
            }
        }
        layerManager.title = this.title;
        layerManager.filePath = this.filePath;
        layerManager.loaded = this.loaded;
        return layerManager;
    }

    public ParticleSystem getParticleSystem() {
        synchronized (this.layers) {
            Log.e("layers t", "" + this.layers.get(0));
            for (int i = 0; i < this.layers.size(); i++) {
                if (this.layers.get(i) instanceof ParticleSystem) {
                    return (ParticleSystem) this.layers.get(i);
                }
            }
            return null;
        }
    }

    public ParticleSystem getSecondaryParticleSystem() {
        TouchEffectsBase touchEffectsBase = this.touchEffect;
        if (touchEffectsBase != null) {
            return touchEffectsBase.getParticleSystem();
        }
        return null;
    }

    public WallpaperLayer getWallpaper() {
        synchronized (this.layers) {
            for (int i = 0; i < this.layers.size(); i++) {
                if (this.layers.get(i) instanceof WallpaperLayer) {
                    return (WallpaperLayer) this.layers.get(i);
                }
            }
            return null;
        }
    }

    public void onTouchDown(float f, float f2) {
        TouchEffectsBase touchEffectsBase = this.touchEffect;
        if (touchEffectsBase == null || !this.touchEffectEnabled) {
            return;
        }
        touchEffectsBase.updateBegin(this, f, f2);
    }

    public void onTouchMove(float f, float f2) {
        TouchEffectsBase touchEffectsBase = this.touchEffect;
        if (touchEffectsBase == null || !this.touchEffectEnabled) {
            return;
        }
        touchEffectsBase.update(this, f, f2);
    }

    public void onTouchUp(float f, float f2) {
        TouchEffectsBase touchEffectsBase = this.touchEffect;
        if (touchEffectsBase == null || !this.touchEffectEnabled) {
            return;
        }
        touchEffectsBase.updateEnd(this, f, f2);
    }

    public void destroyAll() {
        List<baseLayer> list = this.layers;
        if (list != null) {
            synchronized (list) {
                for (int i = 0; i < this.layers.size(); i++) {
                    this.layers.get(i).destroy();
                }
            }
            this.layers.clear();
        }
    }

    public void clearTextures() {
        List<baseLayer> list = this.layers;
        if (list != null) {
            synchronized (list) {
                for (int i = 0; i < this.layers.size(); i++) {
                    if (this.layers.get(i) instanceof ParticleSystem) {
                        ParticleSystem particleSystem = (ParticleSystem) this.layers.get(i);
                        for (int i2 = 0; i2 < particleSystem.images.size(); i2++) {
                            particleSystem.images.get(i2).image.clearTexture();
                        }
                    } else {
                        WallpaperLayer wallpaperLayer = (WallpaperLayer) this.layers.get(i);
                        clearTexture(wallpaperLayer.wallpaper);
                        clearTexture(wallpaperLayer.previewBitmap);
                        clearTexture(wallpaperLayer.fullBitmap);
                    }
                }
            }
        }
    }

    void clearTexture(Uimage uimage) {
        UTexture uTexture;
        if (uimage == null || (uTexture = uimage.image) == null) {
            return;
        }
        uTexture.clearTexture();
    }

    void clearTexture(UTexture uTexture) {
        if (uTexture != null) {
            uTexture.clearTexture();
        }
    }

    public void loadTextures() {
        List<baseLayer> list = this.layers;
        if (list != null) {
            synchronized (list) {
                for (int i = 0; i < this.layers.size(); i++) {
                    if (this.layers.get(i) instanceof ParticleSystem) {
                        ParticleSystem particleSystem = (ParticleSystem) this.layers.get(i);
                        for (int i2 = 0; i2 < particleSystem.images.size(); i2++) {
                            if (particleSystem.images.get(i2).image.bmp != null) {
                                particleSystem.images.get(i2).image.load();
                            }
                        }
                    } else {
                        WallpaperLayer wallpaperLayer = (WallpaperLayer) this.layers.get(i);
                        Uimage uimage = wallpaperLayer.wallpaper;
                        if (uimage != null) {
                            loadTexture(uimage);
                        }
                        loadTexture(wallpaperLayer.previewBitmap);
                        loadTexture(wallpaperLayer.fullBitmap);
                    }
                }
            }
        }
    }

    void loadTexture(Uimage uimage) {
        UTexture uTexture;
        if (uimage == null || (uTexture = uimage.image) == null) {
            return;
        }
        uTexture.load();
    }

    void loadTexture(UTexture uTexture) {
        if (uTexture != null) {
            uTexture.load();
        }
    }
}
