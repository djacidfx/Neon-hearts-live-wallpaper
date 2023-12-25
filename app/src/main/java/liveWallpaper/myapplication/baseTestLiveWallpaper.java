package liveWallpaper.myapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;


public abstract class baseTestLiveWallpaper extends WallpaperService {

    
    public class GLEngine extends Engine {
        private static final String TAG = "GLEngine";
        private WallpaperGLSurfaceView glSurfaceView;
        private boolean rendererHasBeenSet;

        public GLEngine() {
            super();
        }

        @Override 
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.glSurfaceView = new WallpaperGLSurfaceView(baseTestLiveWallpaper.this);
        }

        @Override 
        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (this.rendererHasBeenSet) {
                if (z) {
                    this.glSurfaceView.onResume();
                } else {
                    this.glSurfaceView.onPause();
                }
            }
        }

        @Override 
        public void onDestroy() {
            super.onDestroy();
            this.glSurfaceView.onDestroy();
        }

        protected void setRenderer(GLSurfaceView.Renderer renderer) {
            this.glSurfaceView.setRenderer(renderer);
            this.rendererHasBeenSet = true;
        }

        protected void setEGLContextClientVersion(int i) {
            this.glSurfaceView.setEGLContextClientVersion(i);
        }

        protected void setPreserveEGLContextOnPause(boolean z) {
            this.glSurfaceView.setPreserveEGLContextOnPause(z);
        }

        
        class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";

            WallpaperGLSurfaceView(Context context) {
                super(context);
            }

            @Override 
            public SurfaceHolder getHolder() {
                return GLEngine.this.getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }
        }
    }
}
