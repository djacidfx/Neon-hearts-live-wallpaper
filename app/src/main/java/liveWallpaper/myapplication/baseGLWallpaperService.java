package liveWallpaper.myapplication;

import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import liveWallpaper.myapplication.BaseConfigChooser;


public class baseGLWallpaperService extends WallpaperService {
    private static final String TAG = "GLWallpaperService";
    Renderer mr;

    
    public interface Renderer {
        void onDrawFrame(GL10 gl10);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);
    }

    @Override 
    public Engine onCreateEngine() {
        return new GLEngine();
    }

    
    public class GLEngine extends Engine {
        public static final int RENDERMODE_CONTINUOUSLY = 1;
        public static final int RENDERMODE_WHEN_DIRTY = 0;
        private int mDebugFlags;
        private EGLConfigChooser mEGLConfigChooser;
        private EGLContextFactory mEGLContextFactory;
        private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
        public GLThread mGLThread;
        private GLWrapper mGLWrapper;

        public Renderer getBaseRenderer() {
            if (baseGLWallpaperService.this.mr != null) {
                return baseGLWallpaperService.this.mr;
            }
            GLThread gLThread = this.mGLThread;
            if (gLThread == null || gLThread.mRenderer == null) {
                return null;
            }
            return this.mGLThread.mRenderer;
        }

        public GLEngine() {
            super();
        }

        @Override 
        public void onVisibilityChanged(boolean z) {
            if (z) {
                onResume();
            } else {
                onPause();
            }
            super.onVisibilityChanged(z);
        }

        @Override 
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        @Override 
        public void onDestroy() {
            super.onDestroy();
            this.mGLThread.requestExitAndWait();
        }

        @Override 
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            this.mGLThread.onWindowResize(i2, i3);
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
        }

        @Override 
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d(baseGLWallpaperService.TAG, "onSurfaceCreated()");
            this.mGLThread.surfaceCreated(surfaceHolder);
            super.onSurfaceCreated(surfaceHolder);
        }

        @Override 
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.d(baseGLWallpaperService.TAG, "onSurfaceDestroyed()");
            this.mGLThread.surfaceDestroyed();
            super.onSurfaceDestroyed(surfaceHolder);
        }

        public void setGLWrapper(GLWrapper gLWrapper) {
            this.mGLWrapper = gLWrapper;
        }

        public void setDebugFlags(int i) {
            this.mDebugFlags = i;
        }

        public int getDebugFlags() {
            return this.mDebugFlags;
        }

        public void setRenderer(Renderer renderer) {
            baseGLWallpaperService.this.mr = renderer;
            checkRenderThreadState();
            if (this.mEGLConfigChooser == null) {
                this.mEGLConfigChooser = new BaseConfigChooser.SimpleEGLConfigChooser(true);
            }
            if (this.mEGLContextFactory == null) {
                this.mEGLContextFactory = new DefaultContextFactory();
            }
            if (this.mEGLWindowSurfaceFactory == null) {
                this.mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
            }
            GLThread gLThread = new GLThread(renderer, this.mEGLConfigChooser, this.mEGLContextFactory, this.mEGLWindowSurfaceFactory, this.mGLWrapper);
            this.mGLThread = gLThread;
            gLThread.start();
        }

        public void setEGLContextFactory(EGLContextFactory eGLContextFactory) {
            checkRenderThreadState();
            this.mEGLContextFactory = eGLContextFactory;
        }

        public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory eGLWindowSurfaceFactory) {
            checkRenderThreadState();
            this.mEGLWindowSurfaceFactory = eGLWindowSurfaceFactory;
        }

        public void setEGLConfigChooser(EGLConfigChooser eGLConfigChooser) {
            checkRenderThreadState();
            this.mEGLConfigChooser = eGLConfigChooser;
        }

        public void setEGLConfigChooser(boolean z) {
            setEGLConfigChooser(new BaseConfigChooser.SimpleEGLConfigChooser(z));
        }

        public void setEGLConfigChooser(int i, int i2, int i3, int i4, int i5, int i6) {
            setEGLConfigChooser(new BaseConfigChooser.ComponentSizeChooser(i, i2, i3, i4, i5, i6));
        }

        public void setRenderMode(int i) {
            this.mGLThread.setRenderMode(i);
        }

        public int getRenderMode() {
            return this.mGLThread.getRenderMode();
        }

        public void requestRender() {
            this.mGLThread.requestRender();
        }

        public void onPause() {
            this.mGLThread.onPause();
        }

        public void onResume() {
            this.mGLThread.onResume();
        }

        public void queueEvent(Runnable runnable) {
            this.mGLThread.queueEvent(runnable);
        }

        private void checkRenderThreadState() {
            if (this.mGLThread != null) {
                throw new IllegalStateException("setRenderer has already been called for this instance.");
            }
        }
    }
}
