package UEnginePackage.Views;

import UEnginePackage.UGL.Urect;
import UEnginePackage.Models.layers.LayerManager;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.demo.lovelivewallpaper.utils.UCallback;


public class LiveWallpaperViewCanvas extends View {
    UCallback TouchCallback;
    boolean clickEnabled;
    boolean clickedDown;
    private LayerManager layerManager;
    public float xTouch;
    public float yTouch;

    public void setTouchCallback(UCallback uCallback) {
        this.TouchCallback = uCallback;
    }

    public void setClickEnabled(boolean z) {
        this.clickEnabled = z;
        if (z) {
            removeTouches();
        } else {
            initTouches();
        }
    }

    public LiveWallpaperViewCanvas(Context context) {
        super(context);
        this.clickEnabled = true;
        this.clickedDown = false;
    }

    public LiveWallpaperViewCanvas(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.clickEnabled = true;
        this.clickedDown = false;
    }

    public LiveWallpaperViewCanvas(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.clickEnabled = true;
        this.clickedDown = false;
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
                    UCallback uCallback = LiveWallpaperViewCanvas.this.TouchCallback;
                    if (uCallback != null) {
                        uCallback.onDataReady(null);
                    }
                    LiveWallpaperViewCanvas.this.layerManager.onTouchDown(motionEvent.getX(), motionEvent.getY());
                    LiveWallpaperViewCanvas.this.updateTouchState(motionEvent.getX(), motionEvent.getY());
                    LiveWallpaperViewCanvas.this.clickedDown = true;
                } else if (motionEvent.getAction() == 2) {
                    LiveWallpaperViewCanvas.this.updateTouchState(motionEvent.getX(), motionEvent.getY());
                    return LiveWallpaperViewCanvas.this.clickedDown;
                } else if (motionEvent.getAction() == 1) {
                    LiveWallpaperViewCanvas liveWallpaperViewCanvas = LiveWallpaperViewCanvas.this;
                    liveWallpaperViewCanvas.clickedDown = false;
                    liveWallpaperViewCanvas.updateTouchState(motionEvent.getX(), motionEvent.getY());
                    LiveWallpaperViewCanvas.this.layerManager.onTouchUp(motionEvent.getX(), motionEvent.getY());
                    return LiveWallpaperViewCanvas.this.clickedDown;
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
            this.layerManager.draw(canvas);
        }
        invalidate();
    }

    public LayerManager getLayerManager() {
        return this.layerManager;
    }

    public void setLayerManager(LayerManager layerManager) {
        this.layerManager = layerManager;
        if (layerManager != null) {
            layerManager.setBound(new Urect(0.0d, 0.0d, getWidth(), getHeight()));
        }
    }
}
