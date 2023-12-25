package UEnginePackage.Views;

import UEnginePackage.UGL.Urect;
import UEnginePackage.Models.layers.LayerManager;
import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.demo.lovelivewallpaper.utils.UCallback;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class LiveWallpaperViewUgl extends GLSurfaceView {
    UCallback TouchCallback;
    boolean clickEnabled;
    boolean clickedDown;
    boolean destroyed;
    private LayerManager layerManager;
    FloatBuffer vertexBuffer;
    public float xTouch;
    public float yTouch;

    public LiveWallpaperViewUgl(Context context) {
        super(context);
        this.destroyed = false;
        this.clickEnabled = true;
        this.clickedDown = false;
        initGl();
    }

    public LiveWallpaperViewUgl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.destroyed = false;
        this.clickEnabled = true;
        this.clickedDown = false;
        initGl();
    }

    public void onDestroy() {
        super.onDetachedFromWindow();
        this.destroyed = true;
    }

    public void setClickEnabled(boolean z) {
        this.clickEnabled = z;
        if (z) {
            removeTouches();
        } else {
            initTouches();
        }
    }

    void initGl() {
        setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        setRenderer(new MyRenderer());
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(48);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        this.vertexBuffer = asFloatBuffer;
        asFloatBuffer.put(new float[]{-0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, -0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.0f});
        this.vertexBuffer.position(0);
    }

    @Override 
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    @Override 
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            layerManager.setBound(new Urect(0.0d, 0.0d, i, i2));
        }
    }

    void removeTouches() {
        setOnTouchListener(null);
        removeTouches();
    }

    void initTouches() {
        setSoundEffectsEnabled(false);
        setOnTouchListener(new OnTouchListener() { 
            @Override 
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    UCallback uCallback = LiveWallpaperViewUgl.this.TouchCallback;
                    if (uCallback != null) {
                        uCallback.onDataReady(null);
                    }
                    LiveWallpaperViewUgl.this.layerManager.onTouchDown(motionEvent.getX(), motionEvent.getY());
                    LiveWallpaperViewUgl.this.updateTouchState(motionEvent.getX(), motionEvent.getY());
                    LiveWallpaperViewUgl.this.clickedDown = true;
                } else if (motionEvent.getAction() == 2) {
                    LiveWallpaperViewUgl.this.updateTouchState(motionEvent.getX(), motionEvent.getY());
                    return LiveWallpaperViewUgl.this.clickedDown;
                } else if (motionEvent.getAction() == 1) {
                    LiveWallpaperViewUgl liveWallpaperViewUgl = LiveWallpaperViewUgl.this;
                    liveWallpaperViewUgl.clickedDown = false;
                    liveWallpaperViewUgl.updateTouchState(motionEvent.getX(), motionEvent.getY());
                    LiveWallpaperViewUgl.this.layerManager.onTouchUp(motionEvent.getX(), motionEvent.getY());
                    return LiveWallpaperViewUgl.this.clickedDown;
                }
                return true;
            }
        });
    }

    void updateTouchState(float f, float f2) {
        this.xTouch = f;
        this.yTouch = f2;
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            if (this.clickedDown) {
                layerManager.onTouchMove(this.xTouch, this.yTouch);
            }
            this.layerManager.update();
        }
        invalidate();
    }

    public LayerManager getLayerManager() {
        return this.layerManager;
    }

    public void setLayerManager(LayerManager layerManager) {
        LayerManager layerManager2 = this.layerManager;
        if (layerManager2 != null && layerManager2 != layerManager) {
            layerManager2.destroyAll();
        }
        this.layerManager = layerManager;
        if (layerManager != null) {
            layerManager.setBound(new Urect(0.0d, 0.0d, getWidth(), getHeight()));
        }
    }

    
    public final class MyRenderer implements Renderer {
        int frames;
        double lastFps;
        double lastTimeChecked;
        private FloatBuffer tempTextureBuffer;

        void init() {
        }

        private MyRenderer() {
            this.tempTextureBuffer = null;
            this.frames = 0;
            this.lastTimeChecked = 0.0d;
        }

        @Override 
        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            gl10.glEnable(3008);
            gl10.glEnable(3042);
            gl10.glBlendFunc(1, 771);
            gl10.glDisable(2929);
            gl10.glEnableClientState(32884);
            gl10.glEnableClientState(32888);
            Log.e("tag1", "onSurfaceCreated");
        }

        @Override 
        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
            gl10.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            gl10.glViewport(0, 0, i, i2);
            gl10.glMatrixMode(5889);
            gl10.glLoadIdentity();
            gl10.glOrthof(0.0f, i, i2, 0.0f, -1.0f, 1.0f);
            gl10.glMatrixMode(5888);
            gl10.glLoadIdentity();
            Log.e("tag1", "onSurfaceChanged");
            init();
        }

        @Override 
        public void onDrawFrame(GL10 gl10) {
            gl10.glClear(16384);
            gl10.glVertexPointer(3, 5126, 0, LiveWallpaperViewUgl.this.vertexBuffer);
            LiveWallpaperViewUgl liveWallpaperViewUgl = LiveWallpaperViewUgl.this;
            if (!liveWallpaperViewUgl.destroyed) {
                if (liveWallpaperViewUgl.layerManager != null) {
                    LiveWallpaperViewUgl liveWallpaperViewUgl2 = LiveWallpaperViewUgl.this;
                    if (liveWallpaperViewUgl2.clickedDown) {
                        LayerManager layerManager = liveWallpaperViewUgl2.layerManager;
                        LiveWallpaperViewUgl liveWallpaperViewUgl3 = LiveWallpaperViewUgl.this;
                        layerManager.onTouchMove(liveWallpaperViewUgl3.xTouch, liveWallpaperViewUgl3.yTouch);
                    }
                    LiveWallpaperViewUgl.this.layerManager.update();
                    LiveWallpaperViewUgl.this.layerManager.draw(gl10, 0.0f);
                }
                showFps();
            }
        }

        private void showFps() {
            this.frames++;
            if (this.lastTimeChecked / 1000.0d < System.currentTimeMillis() / 1000) {
                this.lastTimeChecked = System.currentTimeMillis();
                Log.e("tag1", "fps : " + this.frames);
                this.lastFps = (double) this.frames;
                this.frames = 0;
            }
        }
    }
}
