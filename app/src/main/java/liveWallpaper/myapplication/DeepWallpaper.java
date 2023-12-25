package liveWallpaper.myapplication;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.demo.lovelivewallpaper.utils.PrefLoader;


public class DeepWallpaper extends baseGLWallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener {
    MyEngine engin;
    SharedPreferences sharedPreferences;
    MyEngine wallpaperEngin;
    String wlpName;

    public DeepWallpaper() {
        this.wlpName = "";
        Log.e("tag1", this.wlpName + " wallpaper ctr");
        this.wlpName = (Math.random() * 1000.0d) + "wallpaperService";
    }

    @Override 
    public Engine onCreateEngine() {
        if (this.engin != null) {
            this.engin = null;
        }
        Log.e("tag1", this.wlpName + " onCreateEngine");
        SharedPreferences sharedPreferences = getSharedPreferences(PrefLoader.DataFileName, 0);
        this.sharedPreferences = sharedPreferences;
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        MyEngine myEngine = new MyEngine();
        this.engin = myEngine;
        return myEngine;
    }

    @Override 
    public void onDestroy() {
        super.onDestroy();
        Log.e("tag1", this.wlpName + " onDestroy DeepWallpaper ");
        SharedPreferences sharedPreferences = this.sharedPreferences;
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            this.sharedPreferences = null;
        }
    }

    @Override 
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        Log.e("tag1", this.wlpName + " shared pref changed " + str);
        MyEngine myEngine = this.wallpaperEngin;
        if (myEngine != null) {
            myEngine.valuesChanged(str);
        } else {
            this.engin.valuesChanged(str);
        }
    }

    
    class MyEngine extends GLEngine {
        public MyRenderer myRenderer;
        String name;
        public boolean shouldReload;

        public MyEngine() {
            super();
            this.name = (Math.random() * 1000.0d) + " MyEngine";
            Log.e("tag1", this.name + "   " + DeepWallpaper.this.wlpName + " myEngine ctr");
            setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            createRenderer();
            init();
        }

        MyRenderer getRenderer() {
            return this.myRenderer;
        }

        @Override 
        public void onDestroy() {
            super.onDestroy();
            Log.e("tag1", this.name + "    " + DeepWallpaper.this.wlpName + "myEngine onDestroy");
            if (getRenderer() != null) {
                getRenderer().release();
            }
            this.myRenderer = null;
        }

        void createRenderer() {
            this.myRenderer = new MyRenderer(DeepWallpaper.this);
            Log.e("tag1", this.myRenderer.name + "   " + this.name + "    " + DeepWallpaper.this.wlpName + " myEngine createRenderer");
            setRenderMode(1);
            this.myRenderer.initGl();
        }

        public void init() {
            Log.e("tag1", this.name + "    " + DeepWallpaper.this.wlpName + " myEngine init renderer");
            getRenderer().init();
        }

        public void reload() {
            Log.e("tag1", this.name + "    " + DeepWallpaper.this.wlpName + " reloading");
            this.shouldReload = false;
            init();
        }

        public void valuesChanged(String str) {
            if (getRenderer() == null) {
                Log.e("tag1", this.name + "    " + DeepWallpaper.this.wlpName + "valuesChanged renderer null cancel");
                return;
            }
            Log.e("tag1", this.name + "    " + getRenderer().name + "    " + DeepWallpaper.this.wlpName + " valuesChanged: " + str + " : " + PrefLoader.LoadPref(str, DeepWallpaper.this));
            if (str == "size" || str.equals("size")) {
                getRenderer().setSizeFactore(PrefLoader.LoadPref(str, DeepWallpaper.this));
            } else if (str == "speed" || str.equals("speed")) {
                getRenderer().setSpeedFactore(PrefLoader.LoadPref(str, DeepWallpaper.this));
            } else if (str == "count" || str.equals("count")) {
                getRenderer().setPrtCount(PrefLoader.LoadPref(str, DeepWallpaper.this));
            } else if (str == Statics.lastSelectedBehaviorPref || str.equals(Statics.lastSelectedBehaviorPref)) {
                this.shouldReload = true;
            } else if (str == "glitterSwitch") {
                this.shouldReload = true;
            } else if (str == "border" || str.equals("border")) {
                this.shouldReload = true;
            } else if (str == Statics.lastSelectedParticlePref || str.equals(Statics.lastSelectedParticlePref)) {
                this.shouldReload = true;
            } else if (str == Statics.lastSelectedTouchEffectPref || str.equals(Statics.lastSelectedTouchEffectPref)) {
                this.shouldReload = true;
            } else if (str == Statics.lastSelectedWallpaperPref || str.equals(Statics.lastSelectedWallpaperPref)) {
                this.shouldReload = true;
            }
            if (this.shouldReload) {
                reload();
            }
        }

        @Override 
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            if (isPreview()) {
                return;
            }
            DeepWallpaper.this.wallpaperEngin = this;
        }

        @Override 
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            this.myRenderer.width = i2;
            this.myRenderer.height = i3;
            Log.e("tag1", "onSurfaceChanged");
        }

        @Override 
        public void onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.myRenderer.clickedDown = true;
                this.myRenderer.layerManager.onTouchDown(motionEvent.getX(), motionEvent.getY());
                updateTouchState(motionEvent.getX(), motionEvent.getY());
            } else if (motionEvent.getAction() == 2) {
                updateTouchState(motionEvent.getX(), motionEvent.getY());
            } else if (motionEvent.getAction() == 1) {
                this.myRenderer.clickedDown = false;
                updateTouchState(motionEvent.getX(), motionEvent.getY());
                this.myRenderer.layerManager.onTouchUp(motionEvent.getX(), motionEvent.getY());
            }
            super.onTouchEvent(motionEvent);
        }

        void updateTouchState(float f, float f2) {
            this.myRenderer.xTouch = f;
            this.myRenderer.yTouch = f2;
        }
    }
}
