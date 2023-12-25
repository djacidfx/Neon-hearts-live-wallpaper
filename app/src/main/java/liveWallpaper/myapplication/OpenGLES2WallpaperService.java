package liveWallpaper.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.demo.lovelivewallpaper.utils.PrefLoader;


public class OpenGLES2WallpaperService extends WallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener {
    WallpaperEngine engin;
    boolean isPreview = true;
    SharedPreferences sharedPreferences;
    WallpaperEngine wallpaperEngin;

    @Override 
    public Engine onCreateEngine() {
        if (this.engin != null) {
            this.engin = null;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(PrefLoader.DataFileName, 0);
        this.sharedPreferences = sharedPreferences;
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        WallpaperEngine wallpaperEngine = new WallpaperEngine();
        this.engin = wallpaperEngine;
        return wallpaperEngine;
    }

    @Override 
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = this.sharedPreferences;
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            this.sharedPreferences = null;
        }
    }

    @Override 
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        WallpaperEngine wallpaperEngine = this.wallpaperEngin;
        if (wallpaperEngine != null) {
            wallpaperEngine.valuesChanged(str);
        } else {
            this.engin.valuesChanged(str);
        }
    }

    
    private final class WallpaperEngine extends Engine {
        boolean isPortrait;
        private WallpaperGLSurfaceView mGLSurfaceView;
        private MyRenderer mRenderer;
        public boolean shouldReload;

        public WallpaperEngine() {
            super();
            this.shouldReload = true;
            this.isPortrait = true;
            createRenderer();
        }

        MyRenderer getRenderer() {
            return this.mRenderer;
        }

        @Override 
        public void onDestroy() {
            super.onDestroy();
            this.mGLSurfaceView.onDestroy();
            this.mGLSurfaceView = null;
            if (getRenderer() != null) {
                getRenderer().release();
            }
            this.mRenderer = null;
        }

        void createRenderer() {
            this.mRenderer = new MyRenderer(OpenGLES2WallpaperService.this);
        }

        public void init() {
            MyRenderer myRenderer = this.mRenderer;
            if (myRenderer != null) {
                myRenderer.init();
            }
        }

        public void reload() {
            this.shouldReload = false;
            init();
        }

        public void valuesChanged(String str) {
            if (str == "updateWallpaper" || (str.equals("updateWallpaper") && !OpenGLES2WallpaperService.this.isPreview)) {
                reload();
            }
        }

        @Override 
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            OpenGLES2WallpaperService.this.isPreview = isPreview();
            if (!OpenGLES2WallpaperService.this.isPreview) {
                OpenGLES2WallpaperService.this.wallpaperEngin = this;
                OpenGLES2WallpaperService.this.isPreview = true;
            }
            MyRenderer myRenderer = this.mRenderer;
            if (myRenderer != null) {
                myRenderer.isPreview = isPreview();
                this.mRenderer.initGl();
                init();
            }
        }

        @Override 
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            this.mRenderer.width = i2;
            this.mRenderer.height = i3;
            Log.e("tag66", "onSurfaceChanged " + i2 + "/" + i3);
            if (i2 > i3 && this.isPortrait) {
                reload();
            }
            if (i2 < i3 && !this.isPortrait) {
                reload();
            }
            this.isPortrait = i2 < i3;
        }

        @Override 
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            WallpaperGLSurfaceView wallpaperGLSurfaceView = new WallpaperGLSurfaceView(OpenGLES2WallpaperService.this);
            this.mGLSurfaceView = wallpaperGLSurfaceView;
            wallpaperGLSurfaceView.setPreserveEGLContextOnPause(true);
            this.mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            this.mGLSurfaceView.setRenderer(this.mRenderer);
            this.mGLSurfaceView.setRenderMode(1);
        }

        @Override 
        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (z) {
                this.mGLSurfaceView.onResume();
            } else {
                this.mGLSurfaceView.onPause();
            }
        }

        @Override 
        public void onTouchEvent(MotionEvent motionEvent) {
            if (this.mRenderer == null || motionEvent == null) {
                return;
            }
            if (motionEvent.getAction() == 0) {
                this.mRenderer.clickedDown = true;
                if (this.mRenderer.layerManager != null) {
                    this.mRenderer.layerManager.onTouchDown(motionEvent.getX(), motionEvent.getY());
                }
                updateTouchState(motionEvent.getX(), motionEvent.getY());
            } else if (motionEvent.getAction() == 2) {
                updateTouchState(motionEvent.getX(), motionEvent.getY());
            } else if (motionEvent.getAction() == 1) {
                this.mRenderer.clickedDown = false;
                updateTouchState(motionEvent.getX(), motionEvent.getY());
                if (this.mRenderer.layerManager != null) {
                    this.mRenderer.layerManager.onTouchUp(motionEvent.getX(), motionEvent.getY());
                }
            }
            super.onTouchEvent(motionEvent);
        }

        void updateTouchState(float f, float f2) {
            MyRenderer myRenderer = this.mRenderer;
            if (myRenderer == null) {
                return;
            }
            myRenderer.xTouch = f;
            this.mRenderer.yTouch = f2;
        }

        
        private final class WallpaperGLSurfaceView extends GLSurfaceView {
            public WallpaperGLSurfaceView(Context context) {
                super(context);
            }

            @Override 
            public SurfaceHolder getHolder() {
                return WallpaperEngine.this.getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }
        }
    }
}
