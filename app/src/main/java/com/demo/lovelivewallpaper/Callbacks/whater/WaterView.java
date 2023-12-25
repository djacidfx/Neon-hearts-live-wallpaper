package com.demo.lovelivewallpaper.Callbacks.whater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class WaterView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int line_width = 20;
    private static final int step = 40;
    int[] _rd;
    int[] _td;
    boolean flip;
    Paint fpsPaint;
    int framesCount;
    int framesCountAvg;
    long framesTimer;
    private int height;
    private short[] last_map;
    long now;
    Bitmap ripple;
    private short[] ripplemap;
    private Rippler rippler;
    private short riprad;
    GameThread thread;
    long timeDelta;
    long timeNow;
    long timePrev;
    long timePrevFrame;
    private int width;

    @Override 
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public WaterView(Context context) {
        super(context);
        this.framesCount = 0;
        this.framesCountAvg = 0;
        this.framesTimer = 0L;
        this.fpsPaint = new Paint();
        this.timePrev = 0L;
        this.timePrevFrame = 0L;
        this.width = 400;
        this.height = 400;
        this.riprad = (short) 3;
        initialize();
    }

    void initialize() {
        this.rippler = new NativeRippler();
        reinitgGlobals();
        this.fpsPaint.setTextSize(30.0f);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    void reinitgGlobals() {
        int i = this.width;
        int i2 = this.height;
        int i3 = (i2 + 2) * i * 2;
        this.ripplemap = new short[i3];
        this.last_map = new short[i3];
        Bitmap createBackground = createBackground(i, i2);
        this.ripple = createBackground;
        int i4 = this.width;
        int i5 = this.height;
        int[] iArr = new int[i4 * i5];
        this._td = iArr;
        createBackground.getPixels(iArr, 0, i4, 0, 0, i4, i5);
        this._rd = new int[this.width * this.height];
    }

    void randomizer() {
        final Random random = new Random();
        final Handler handler = new Handler();
        handler.post(new Runnable() { 
            @Override 
            public void run() {
                WaterView waterView = WaterView.this;
                waterView.disturb(random.nextInt(waterView.width), random.nextInt(WaterView.this.height));
                handler.postDelayed(this, 7000L);
            }
        });
    }

    private static Bitmap createBackground(int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(Color.parseColor("#a2ddf8"));
        canvas.save();
        canvas.rotate(-45.0f);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#0077bb"));
        for (int i3 = 0; i3 < i2 / 20; i3++) {
            int i4 = i3 * 40;
            canvas.drawRect(-i, i4, i * 3, i4 + 20, paint);
        }
        canvas.restore();
        return createBitmap;
    }

    @Override 
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.width = i;
        this.height = i2;
        reinitgGlobals();
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        newframe();
        canvas.drawBitmap(this.ripple, 0.0f, 0.0f, (Paint) null);
        this.now = System.currentTimeMillis();
        canvas.drawText(this.framesCountAvg + " fps", 40.0f, 70.0f, this.fpsPaint);
        int i = this.framesCount + 1;
        this.framesCount = i;
        long j = this.now;
        if (j - this.framesTimer > 1000) {
            this.framesTimer = j;
            this.framesCountAvg = i;
            this.framesCount = 0;
        }
    }

    
    public void disturb(int i, int i2) {
        this.rippler.disturb(i, i2, this.width, this.height, this.riprad, this.ripplemap, this.flip);
    }

    private void newframe() {
        System.arraycopy(this._td, 0, this._rd, 0, this.width * this.height);
        boolean z = !this.flip;
        this.flip = z;
        this.rippler.transformRipples(this.height, this.width, this.ripplemap, this.last_map, this._td, this._rd, z);
        Bitmap bitmap = this.ripple;
        int[] iArr = this._rd;
        int i = this.width;
        bitmap.setPixels(iArr, 0, i, 0, 0, i, this.height);
    }

    @Override 
    public synchronized boolean onTouchEvent(MotionEvent motionEvent) {
        disturb((int) motionEvent.getX(), (int) motionEvent.getY());
        return true;
    }

    @Override 
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        GameThread gameThread = new GameThread(getHolder(), this);
        this.thread = gameThread;
        gameThread.setRunning(true);
        this.thread.start();
    }

    @Override 
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.thread.setRunning(false);
        boolean z = true;
        while (z) {
            try {
                this.thread.join();
                z = false;
            } catch (InterruptedException unused) {
            }
        }
    }

    
    class GameThread extends Thread {
        private WaterView gameView;
        private boolean run = false;
        private SurfaceHolder surfaceHolder;

        public GameThread(SurfaceHolder surfaceHolder, WaterView waterView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = waterView;
        }

        public void setRunning(boolean z) {
            this.run = z;
        }

        public SurfaceHolder getSurfaceHolder() {
            return this.surfaceHolder;
        }

        @Override 
        public void run() {
            while (this.run) {
                WaterView.this.timeNow = System.currentTimeMillis();
                WaterView waterView = WaterView.this;
                waterView.timeDelta = waterView.timeNow - WaterView.this.timePrevFrame;
                if (WaterView.this.timeDelta < 16) {
                    try {
                        Thread.sleep(16 - WaterView.this.timeDelta);
                    } catch (InterruptedException unused) {
                    }
                }
                WaterView.this.timePrevFrame = System.currentTimeMillis();
                Canvas canvas = null;
                try {
                    canvas = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {
                        this.gameView.onDraw(canvas);
                    }
                    if (canvas != null) {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                } catch (Throwable th) {
                    if (canvas != null) {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    throw th;
                }
            }
        }
    }
}
