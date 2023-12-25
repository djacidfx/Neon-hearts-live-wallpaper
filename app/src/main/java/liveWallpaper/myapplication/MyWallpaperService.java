package liveWallpaper.myapplication;

import UEnginePackage.Models.UTexture;
import UEnginePackage.Models.range;
import UEnginePackage.Utils.AppConfig;
import UEnginePackage.Views.LiveWallpaperViewUgl;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import UEnginePackage.Models.layers.WallpaperLayer;
import UEnginePackage.touchEffectsPackage.TouchEffectsBase;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import androidx.exifinterface.media.ExifInterface;

import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.DialogHelper;
import com.demo.lovelivewallpaper.utils.ImageUtils;
import com.demo.lovelivewallpaper.utils.ImageUtils2;
import com.demo.lovelivewallpaper.utils.PrefLoader;
import com.demo.lovelivewallpaper.utils.Uscreen;


public class MyWallpaperService extends WallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener {
    CountDownTimer c;
    MyWallpaperEngine engin;
    private LiveWallpaperViewUgl glSurfaceView;
    ParticleSystem glitterPS;
    LayerManager layerManager;
    MovmentUtils movment;
    notificationHelper notifHelper;
    private boolean rendererHasBeenSet;
    SharedPreferences sharedPreferences;
    float sizeFactore;
    float speedFactore;
    public boolean clickedDown = false;
    public float xTouch = 0.0f;
    public float yTouch = 0.0f;
    String tag = "liveWallpaperService";
    float lastPitch = -500.0f;
    float lastRoll = -500.0f;
    int maxMovment = AppConfig.phoneMaxMovment;
    int movmentFactor = AppConfig.movmentFactor;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { 
        @Override 
        public void onReceive(Context context, Intent intent) {
            MyWallpaperService.this.handelColors(intent.getAction());
        }
    };

    @Override 
    public void onCreate() {
        super.onCreate();
        registerBroadcast();
    }

    @Override 
    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    void initPhoneMovment() {
        MovmentUtils movmentUtils = new MovmentUtils(this);
        this.movment = movmentUtils;
        movmentUtils.startListening(new MovmentUtils.Listener() { 
            @Override 
            public void onOrientationChanged(float f, float f2) {
                if (MyWallpaperService.this.c != null) {
                    return;
                }
                if (MyWallpaperService.this.lastPitch == -500.0f) {
                    MyWallpaperService.this.lastPitch = f;
                }
                if (MyWallpaperService.this.lastRoll == -500.0f) {
                    MyWallpaperService.this.lastRoll = f2;
                }
                float abs = Math.abs(MyWallpaperService.this.lastPitch - f);
                float abs2 = Math.abs(MyWallpaperService.this.lastRoll - f2);
                if (abs <= abs2) {
                    abs = abs2;
                }
                MovmentUtils.additionalTime = (long) (((float) MovmentUtils.additionalTime) + abs);
                MyWallpaperService.this.lastPitch = f;
                MyWallpaperService.this.lastRoll = f2;
                if (MyWallpaperService.this.glitterPS != null) {
                    long j = (long) (abs * MyWallpaperService.this.movmentFactor);
                    if (j > MyWallpaperService.this.maxMovment) {
                        j = MyWallpaperService.this.maxMovment;
                    }
                    MyWallpaperService.this.glitterPS.forwardTimeBy(j);
                }
            }
        });
    }

    @Override 
    public Engine onCreateEngine() {
        Log.e(this.tag, "createEngine");
        if (this.engin != null) {
            this.engin = null;
        }
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences(PrefLoader.DataFileName, 0);
        this.sharedPreferences = sharedPreferences;
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        MyWallpaperEngine myWallpaperEngine = new MyWallpaperEngine();
        this.engin = myWallpaperEngine;
        return myWallpaperEngine;
    }

    @Override 
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        String str2 = this.tag;
        Log.e(str2, "shared pref changed " + str);
        if (str == "size" || str.equals("size")) {
            this.sizeFactore = PrefLoader.LoadPref(str, this);
        } else if (str == "speed" || str.equals("speed")) {
            this.speedFactore = PrefLoader.LoadPref(str, this);
        } else if (str == Statics.lastSelectedBehaviorPref || str.equals(Statics.lastSelectedBehaviorPref)) {
            this.engin.shouldReload = true;
        } else if (str == "border" || str.equals("border")) {
            this.engin.shouldReload = true;
        } else if (str == Statics.lastSelectedParticlePref || str.equals(Statics.lastSelectedParticlePref)) {
            this.engin.shouldReload = true;
        } else if (str == Statics.lastSelectedTouchEffectPref || str.equals(Statics.lastSelectedTouchEffectPref)) {
            this.engin.shouldReload = true;
        } else if (str == Statics.lastSelectedWallpaperPref || str.equals(Statics.lastSelectedWallpaperPref)) {
            this.engin.shouldReload = true;
        }
        if (this.engin.shouldReload) {
            this.engin.reload();
        }
    }

    
    private class MyWallpaperEngine extends Engine {
        private final Runnable drawRunner;
        private final Handler handler;
        int height;
        String imageName;
        public boolean shouldReload;
        boolean stoped;
        private boolean visible;
        int width;

        @Override 
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            MyWallpaperService.this.glSurfaceView = new LiveWallpaperViewUgl(MyWallpaperService.this);
        }

        public MyWallpaperEngine() {
            super();
            Handler handler = new Handler();
            this.handler = handler;
            Runnable runnable = new Runnable() { 
                @Override 
                public void run() {
                    if (MyWallpaperEngine.this.shouldReload) {
                        MyWallpaperEngine.this.shouldReload = false;
                        MyWallpaperEngine.this.init();
                    }
                    MyWallpaperEngine.this.draw();
                }
            };
            this.drawRunner = runnable;
            this.visible = true;
            this.imageName = "";
            this.stoped = false;
            init();
            this.shouldReload = false;
            handler.post(runnable);
            initLayerManager();
        }

        void initLayerManager() {
            if (MyWallpaperService.this.layerManager != null) {
                Log.e(MyWallpaperService.this.tag, "reloading");
                MyWallpaperService.this.layerManager.destroyAll();
                MyWallpaperService.this.layerManager = null;
            }
            Uscreen.height = PrefLoader.LoadPref("screenHeight", MyWallpaperService.this);
            Uscreen.width = PrefLoader.LoadPref("screenWidth", MyWallpaperService.this);
            Log.e(MyWallpaperService.this.tag, "init layer manager");
            MyWallpaperService myWallpaperService = MyWallpaperService.this;
            myWallpaperService.speedFactore = PrefLoader.LoadPref("speed", myWallpaperService);
            MyWallpaperService myWallpaperService2 = MyWallpaperService.this;
            myWallpaperService2.sizeFactore = PrefLoader.LoadPref("size", myWallpaperService2);
            int LoadPref = PrefLoader.LoadPref("count", MyWallpaperService.this);
            MyWallpaperService myWallpaperService3 = MyWallpaperService.this;
            myWallpaperService3.sizeFactore = (myWallpaperService3.sizeFactore + 30.0f) / 100.0f;
            MyWallpaperService myWallpaperService4 = MyWallpaperService.this;
            myWallpaperService4.speedFactore = (myWallpaperService4.speedFactore + 30.0f) / 100.0f;
            initPS();
            initTF();
            loadBackground();
            MyWallpaperService.this.layerManager.clearDrawing = true;
            MyWallpaperService.this.layerManager.getParticleSystem().setSpeedFactore(MyWallpaperService.this.speedFactore);
            MyWallpaperService.this.layerManager.getParticleSystem().setSizeFactore(MyWallpaperService.this.sizeFactore);
            MyWallpaperService.this.layerManager.getParticleSystem().setParticleCount(LoadPref);
            MyWallpaperService.this.layerManager.getParticleSystem().moveInsideBounds = PrefLoader.LoadPref("border", MyWallpaperService.this) == 1;
            MyWallpaperService.this.layerManager.getParticleSystem().checkInsideBoundByPS = PrefLoader.LoadPref("border", MyWallpaperService.this) == 1;

            Log.e("aaaaaaaaaaaaaaaaa",""+PrefLoader.LoadPref("glitterSwitch", MyWallpaperService.this));

            if (PrefLoader.LoadPref("glitterSwitch", MyWallpaperService.this) == 1) {
                initGlitterEffect();
                MyWallpaperService.this.initPhoneMovment();
                MyWallpaperService.this.layerManager.getParticleSystem().enabled = false;
                MyWallpaperService.this.layerManager.touchEffectEnabled = false;
            }
        }

        private void initGlitterEffect() {
            MyWallpaperService myWallpaperService = MyWallpaperService.this;

            myWallpaperService.glitterPS = GlitterHelper.initGlitterEffect(myWallpaperService.layerManager, this.imageName, MyWallpaperService.this);
            ParticleSystem particleSystem = MyWallpaperService.this.glitterPS;
        }

        void initPS() {
            MyWallpaperService.this.layerManager = AssetsLoader.getParticlesSimple().get(PrefLoader.LoadPref(Statics.lastSelectedParticlePref, MyWallpaperService.this));
            MyWallpaperService.this.layerManager.setBound(Uscreen.getBound(MyWallpaperService.this));
            ParticleSystem particleSystem = MyWallpaperService.this.layerManager.getParticleSystem();
            if (particleSystem != null) {
                if (particleSystem.getSpawner().particlesize != null) {
                    MyWallpaperService.this.layerManager.getParticleSystem().getSpawner().particlesize = new range(0.09d, 0.05d);
                }
                DialogHelper.setlayerComponants(AppConfig.listBehaviors().get(PrefLoader.LoadPref(Statics.lastSelectedBehaviorPref, MyWallpaperService.this)), MyWallpaperService.this.layerManager);
            }
        }

        void initTF() {
            TouchEffectsBase touchEffectsBase = AssetsLoader.getListOfTouchEffects().get(PrefLoader.LoadPref(Statics.lastSelectedTouchEffectPref, MyWallpaperService.this));
            touchEffectsBase.init(MyWallpaperService.this);
            MyWallpaperService.this.layerManager.setTouchEffect(touchEffectsBase, MyWallpaperService.this);
            String str = MyWallpaperService.this.tag;
            Log.e(str, "tfSelected is " + touchEffectsBase.title);
        }

        void loadBackground() {
            Bitmap bitmapFromAsset;
            int LoadPref = PrefLoader.LoadPref(Statics.lastSelectedWallpaperPref, MyWallpaperService.this);
            this.imageName = "";
            if (LoadPref == -1) {
                bitmapFromAsset = ImageUtils2.loadImageFromStorage2("images", "wallpaper.jpg", MyWallpaperService.this);
            } else {
                this.imageName = AppConfig.listAssetsWallpapers().get(LoadPref);
                MyWallpaperService myWallpaperService = MyWallpaperService.this;
                bitmapFromAsset = ImageUtils.getBitmapFromAsset(myWallpaperService, "wallpapers/" + this.imageName);
            }
            String str = MyWallpaperService.this.tag;
            StringBuilder sb = new StringBuilder();
            sb.append("loading bg ");
            sb.append(bitmapFromAsset != null);
            Log.e(str, sb.toString());
            UTexture textureFromBitmap = AssetsLoader.textureFromBitmap(bitmapFromAsset);
            textureFromBitmap.preserveBMPPreview = true;
            textureFromBitmap.DestroyBMPWhenLoaded = true;
            WallpaperLayer wallpaperLayer = new WallpaperLayer(textureFromBitmap, Uscreen.getBound(MyWallpaperService.this), this.imageName);
            MyWallpaperService.this.layerManager.addLayer(0, wallpaperLayer);
            wallpaperLayer.wallpaperPath = this.imageName;
            wallpaperLayer.wallpaper.image = textureFromBitmap;
            Log.e(MyWallpaperService.this.tag, "bg loaded");
        }

        protected void setRenderer(GLSurfaceView.Renderer renderer) {
            MyWallpaperService.this.glSurfaceView.setRenderer(renderer);
            MyWallpaperService.this.rendererHasBeenSet = true;
        }

        protected void setEGLContextClientVersion(int i) {
            MyWallpaperService.this.glSurfaceView.setEGLContextClientVersion(i);
        }

        protected void setPreserveEGLContextOnPause(boolean z) {
            MyWallpaperService.this.glSurfaceView.setPreserveEGLContextOnPause(z);
        }

        @Override 
        public void onDestroy() {
            super.onDestroy();
            Log.e(MyWallpaperService.this.tag, "wallpaper engin onDestroy");
        }

        public void init() {
            Uscreen.InitFromService(MyWallpaperService.this);
        }

        @Override 
        public void onVisibilityChanged(boolean z) {
            this.visible = z;
            if (MyWallpaperService.this.rendererHasBeenSet) {
                if (z) {
                    MyWallpaperService.this.glSurfaceView.onResume();
                } else {
                    MyWallpaperService.this.glSurfaceView.onPause();
                }
            }
        }

        @Override 
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            super.onSurfaceDestroyed(surfaceHolder);
            this.visible = false;
            this.handler.removeCallbacks(this.drawRunner);
            Log.e(MyWallpaperService.this.tag, "onSurfaceDestroyed");
        }

        @Override 
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            Log.e(MyWallpaperService.this.tag, "onSurfaceCreated");
            if (!MyWallpaperService.this.engin.isPreview()) {
                Log.e(MyWallpaperService.this.tag, "onSurfaceCreated not preview mode");
                MyWallpaperService.this.notifHelper = new notificationHelper();
                MyWallpaperService.this.notifHelper.showNotification(MyWallpaperService.this);
                return;
            }
            Log.e(MyWallpaperService.this.tag, "onSurfaceCreated preview mode");
        }

        @Override 
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            this.width = i2;
            this.height = i3;
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            Log.e(MyWallpaperService.this.tag, "onSurfaceChanged");
        }

        @Override 
        public void onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                MyWallpaperService.this.clickedDown = true;
                MyWallpaperService.this.layerManager.onTouchDown(motionEvent.getX(), motionEvent.getY());
                updateTouchState(motionEvent.getX(), motionEvent.getY());
            } else if (motionEvent.getAction() == 2) {
                updateTouchState(motionEvent.getX(), motionEvent.getY());
            } else if (motionEvent.getAction() == 1) {
                MyWallpaperService.this.clickedDown = false;
                updateTouchState(motionEvent.getX(), motionEvent.getY());
                MyWallpaperService.this.layerManager.onTouchUp(motionEvent.getX(), motionEvent.getY());
            }
            super.onTouchEvent(motionEvent);
        }

        void updateTouchState(float f, float f2) {
            MyWallpaperService.this.xTouch = f;
            MyWallpaperService.this.yTouch = f2;
        }

        
        public void draw() {
            if (this.stoped) {
                Log.e(MyWallpaperService.this.tag, "stoped so cant draw");
                return;
            }
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    MyWallpaperService.this.drawContent(canvas);
                }
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception unused) {
                    }
                }
                this.handler.removeCallbacks(this.drawRunner);
                if (this.visible) {
                    this.handler.postDelayed(this.drawRunner, 1);
                }
            } catch (Throwable th) {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception unused2) {
                    }
                }
                throw th;
            }
        }

        public void reload() {
            Log.e(MyWallpaperService.this.tag, "reload");
            this.shouldReload = false;
            initLayerManager();
        }

        public void stop() {
            this.stoped = true;
        }
    }

    @Override 
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        notificationHelper notificationhelper = this.notifHelper;
        if (notificationhelper != null) {
            notificationhelper.stop(this);
        }
        this.glSurfaceView.onDestroy();
    }

    
    public void drawContent(Canvas canvas) {
        if (this.clickedDown) {
            this.layerManager.onTouchMove(this.xTouch, this.yTouch);
        }
        this.layerManager.update();
    }

    
    public void handelColors(String str) {
        String str2 = Statics.lastSelectedWallpaperPref;
        if (str == "c1") {
            PrefLoader.SavePref(str2, "0", this);
        } else if (str == "c2") {
            PrefLoader.SavePref(str2, "1", this);
        } else if (str == "c3") {
            PrefLoader.SavePref(str2, ExifInterface.GPS_MEASUREMENT_2D, this);
        } else if (str == "c4") {
            PrefLoader.SavePref(str2, ExifInterface.GPS_MEASUREMENT_3D, this);
        } else if (str == "c5") {
            Intent intent = new Intent(this, ActivitySplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("c1");
        intentFilter.addAction("c2");
        intentFilter.addAction("c3");
        intentFilter.addAction("c4");
        intentFilter.addAction("c5");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(this.broadcastReceiver, intentFilter);
    }
}
