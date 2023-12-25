package liveWallpaper.myapplication;

import UEnginePackage.Models.UTexture;
import UEnginePackage.Models.range;
import UEnginePackage.Utils.AppConfig;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import UEnginePackage.Models.layers.WallpaperLayer;
import UEnginePackage.touchEffectsPackage.TouchEffectsBase;

import android.app.Service;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.CountDownTimer;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.DialogHelper;
import com.demo.lovelivewallpaper.utils.ImageUtils;
import com.demo.lovelivewallpaper.utils.ImageUtils2;
import com.demo.lovelivewallpaper.utils.PrefLoader;
import com.demo.lovelivewallpaper.utils.Uscreen;


public class MyRenderer implements GLSurfaceView.Renderer {
    CountDownTimer c;
    float extraX;
    int frames;
    ParticleSystem glitterPS;
    public int height;
    int lastFps;
    long lastTimeChecked;
    LayerManager layerManager;
    MovmentUtils movment;
    String name;
    Service service;
    float sizeFactore;
    float speedFactore;
    FloatBuffer vertexBuffer;
    public int width;
    public boolean isPreview = true;
    public boolean clickedDown = false;
    public float xTouch = 0.0f;
    public float yTouch = 0.0f;
    String tag = "tag1 liveWallpaperService";
    String imageName = "";
    float lastPitch = -500.0f;
    float lastRoll = -500.0f;
    int maxMovment = AppConfig.phoneMaxMovment;
    int movmentFactor = AppConfig.movmentFactor;
    boolean texturesLoaded = false;

    public MyRenderer(Service service) {
        Log.e("tag2", "MyRenderer ctr");
        this.service = service;
        this.name = (Math.random() * 1000.0d) + " renderer";
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        LayerManager layerManager;
        if (gl10 == null) {
            return;
        }
        if (!this.texturesLoaded && (layerManager = this.layerManager) != null) {
            layerManager.loadTextures();
            this.texturesLoaded = true;
        }
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl10.glClear(16640);
        gl10.glClear(16384);
        gl10.glVertexPointer(3, 5126, 0, this.vertexBuffer);
        LayerManager layerManager2 = this.layerManager;
        if (layerManager2 != null) {
            try {
                if (this.clickedDown) {
                    layerManager2.onTouchMove(this.xTouch, this.yTouch);
                }
                this.layerManager.update();
                this.layerManager.draw(gl10, this.extraX);
                return;
            } catch (Exception unused) {
                return;
            }
        }
        Log.e("tag1", "layer manager still null");
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        Log.e("tag2", "MyRenderer onSurfaceChanged");
        if (gl10 == null) {
            return;
        }
        gl10.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        this.width = i;
        this.height = i2;
        gl10.glViewport(0, 0, i, i2);
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        gl10.glOrthof(0.0f, i, i2, 0.0f, -1.0f, 1.0f);
        gl10.glMatrixMode(5888);
        gl10.glLoadIdentity();
        Log.e("tag1", "onSurfaceChanged");
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        Log.e("tag2", "MyRenderer onSurfaceCreated");
        if (gl10 == null) {
            return;
        }
        gl10.glEnable(3008);
        gl10.glEnable(3042);
        gl10.glBlendFunc(1, 771);
        gl10.glDisable(2929);
        gl10.glEnableClientState(32884);
        gl10.glEnableClientState(32888);
    }

    public void release() {
        Log.e("tag2", "MyRenderer release");
        Log.e("tag1", this.name + " MyRenderer release");
        MovmentUtils movmentUtils = this.movment;
        if (movmentUtils != null) {
            movmentUtils.destroy(this.service);
        }
        this.service = null;
        this.movment = null;
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            layerManager.destroyAll();
        }
        this.layerManager = null;
    }


    public void init() {
        Log.e("tag1", this.name + " MyRenderer init " + this.isPreview);
        initLayerManager();
    }

    public void initGl() {
        Log.e("tag2", "MyRenderer initGl");
        Log.e("tag1", this.name + " MyRenderer initGl");
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(48);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        this.vertexBuffer = asFloatBuffer;
        asFloatBuffer.put(new float[]{-0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, -0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.0f});
        this.vertexBuffer.position(0);
    }

    void initLayerManager() {
        Log.e("tag2", "MyRenderer initLayerManager " + this.isPreview);
        Log.e("tag1", this.name + " MyRenderer initLayerManager");
        if (this.layerManager != null) {
            Log.e(this.tag, "reloading");
            this.layerManager.destroyAll();
            this.layerManager = null;
        }
        Uscreen.height = PrefLoader.LoadPref("screenHeight", this.service);
        Uscreen.width = PrefLoader.LoadPref("screenWidth", this.service);
        if (Uscreen.height == 0 || Uscreen.width == 0) {
            Uscreen.Init(this.service);
        }
        String str = this.tag;
        Log.e(str, "init layer manager Thread id:" + Thread.currentThread().getId());
        this.speedFactore = (float) PrefLoader.LoadPref("speed", this.service);
        this.sizeFactore = (float) PrefLoader.LoadPref("size", this.service);
        int LoadPref = PrefLoader.LoadPref("count", this.service);
        initPS();
        initTF();
        loadBackground();
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            layerManager.clearDrawing = true;
        }
        setSpeedFactore(this.speedFactore);
        setSizeFactore(this.sizeFactore);
        setPrtCount(LoadPref);
        int LoadPref2 = PrefLoader.LoadPref("border", this.service);
        LayerManager layerManager2 = this.layerManager;
        if (layerManager2 != null) {
            layerManager2.getParticleSystem().moveInsideBounds = LoadPref2 == 1;
            this.layerManager.getParticleSystem().checkInsideBoundByPS = LoadPref2 == 1;
        }


        Log.e("glitter fff", "=" + PrefLoader.LoadPref("glitterSwitch", this.service));


        if (PrefLoader.LoadPref("glitterSwitch", this.service) == 1) {
            initGlitterEffect();
            initPhoneMovment();
            LayerManager layerManager3 = this.layerManager;
            if (layerManager3 != null) {
                layerManager3.getParticleSystem().enabled = false;
                this.layerManager.touchEffectEnabled = false;
            }
        }
    }


    public void setSizeFactore(float f) {
        this.sizeFactore = (f + 30.0f) / 100.0f;
        LayerManager layerManager = this.layerManager;
        if (layerManager == null || layerManager.getParticleSystem() == null) {
            return;
        }
        this.layerManager.getParticleSystem().setSizeFactore(this.sizeFactore);
    }


    public void setPrtCount(int i) {
        LayerManager layerManager = this.layerManager;
        if (layerManager == null || layerManager.getParticleSystem() == null) {
            return;
        }
        this.layerManager.getParticleSystem().setParticleCount(i);
    }


    public void setSpeedFactore(float f) {
        this.speedFactore = (f + 30.0f) / 100.0f;
        LayerManager layerManager = this.layerManager;
        if (layerManager == null || layerManager.getParticleSystem() == null) {
            return;
        }
        this.layerManager.getParticleSystem().setSpeedFactore(this.speedFactore);
    }

    void initPS() {
        this.layerManager = AssetsLoader.getParticlesSimple().get(PrefLoader.LoadPref(Statics.lastSelectedParticlePref, this.service));
        Uscreen.Init(this.service);
        int i = this.width;
        int i2 = this.height;
        if (i > i2) {
            Uscreen.height = i2;
            Uscreen.width = (Uscreen.width / Uscreen.height) * this.height;
            this.extraX = (this.width / 2) - (Uscreen.width / 2);
        } else {
            this.extraX = 0.0f;
        }
        this.layerManager.setBound(Uscreen.getBound(this.service));
        Log.e("tag66", "initPS " + Uscreen.width + "/" + Uscreen.height);
        ParticleSystem particleSystem = this.layerManager.getParticleSystem();
        if (particleSystem != null) {
            if (particleSystem.getSpawner().particlesize != null) {
                this.layerManager.getParticleSystem().getSpawner().particlesize = new range(0.09d, 0.05d);
            }
            DialogHelper.setlayerComponants(AppConfig.listBehaviors().get(PrefLoader.LoadPref(Statics.lastSelectedBehaviorPref, this.service)), this.layerManager);
        }
    }

    void initTF() {
        TouchEffectsBase touchEffectsBase = AssetsLoader.getListOfTouchEffects().get(PrefLoader.LoadPref(Statics.lastSelectedTouchEffectPref, this.service));
        touchEffectsBase.init(this.service);
        this.layerManager.setTouchEffect(touchEffectsBase, this.service);
        String str = this.tag;
        Log.e(str, "tfSelected is " + touchEffectsBase.title);
    }

    void loadBackground() {
        Bitmap bitmapFromAsset;
        if (this.layerManager == null) {
            return;
        }
        int LoadPref = PrefLoader.LoadPref(Statics.lastSelectedWallpaperPref, this.service);
        this.imageName = "";
        if (LoadPref == -1) {
            bitmapFromAsset = ImageUtils2.loadImageFromStorage2("images", "wallpaper.jpg", this.service);
        } else {
            this.imageName = AppConfig.listAssetsWallpapers().get(LoadPref);
            Service service = this.service;
            bitmapFromAsset = ImageUtils.getBitmapFromAsset(service, "wallpapers/" + this.imageName);
        }
        String str = this.tag;
        StringBuilder sb = new StringBuilder();
        sb.append("loading bg ");
        sb.append(bitmapFromAsset != null);
        Log.e(str, sb.toString());
        UTexture textureFromBitmap = AssetsLoader.textureFromBitmap(bitmapFromAsset);
        WallpaperLayer wallpaperLayer = new WallpaperLayer(textureFromBitmap, Uscreen.getBound(this.service), this.imageName);
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            layerManager.addLayer(0, wallpaperLayer);
            wallpaperLayer.wallpaperPath = this.imageName;
            wallpaperLayer.wallpaper.image = textureFromBitmap;
        }
    }

    private void initGlitterEffect() {

        Log.e("layerManager ", "= " + layerManager);
        Log.e("imageName ", "= " + imageName);
        Log.e("service ", "= " + service);

        this.glitterPS = GlitterHelper.initGlitterEffect(this.layerManager, this.imageName, this.service);
        Log.e("glitterPS ", "= " + glitterPS);
    }

    private void showFps() {
        this.frames++;
        if (this.lastTimeChecked / 1000 < System.currentTimeMillis() / 1000) {
            this.lastTimeChecked = System.currentTimeMillis();
            Log.e("tag1", this.name + " fps : " + this.frames + " Thread id : " + Thread.currentThread().getId());
            this.lastFps = this.frames;
            this.frames = 0;
        }
    }

    void initPhoneMovment() {
        MovmentUtils movmentUtils = new MovmentUtils(this.service);
        this.movment = movmentUtils;
        movmentUtils.startListening(new MovmentUtils.Listener() {
            @Override
            public void onOrientationChanged(float f, float f2) {
                if (MyRenderer.this.c != null) {
                    return;
                }
                if (MyRenderer.this.lastPitch == -500.0f) {
                    MyRenderer.this.lastPitch = f;
                }
                if (MyRenderer.this.lastRoll == -500.0f) {
                    MyRenderer.this.lastRoll = f2;
                }
                float abs = Math.abs(MyRenderer.this.lastPitch - f);
                float abs2 = Math.abs(MyRenderer.this.lastRoll - f2);
                if (abs <= abs2) {
                    abs = abs2;
                }
                MovmentUtils.additionalTime = (long) (((float) MovmentUtils.additionalTime) + abs);
                MyRenderer.this.lastPitch = f;
                MyRenderer.this.lastRoll = f2;
                if (MyRenderer.this.glitterPS != null) {
                    long j = (long) (abs * MyRenderer.this.movmentFactor);
                    if (j > MyRenderer.this.maxMovment) {
                        j = MyRenderer.this.maxMovment;
                    }
                    MyRenderer.this.glitterPS.forwardTimeBy(j);
                }
            }
        });
    }

    public void reloadTextures() {
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            layerManager.clearTextures();
        }
        this.texturesLoaded = false;
    }
}
