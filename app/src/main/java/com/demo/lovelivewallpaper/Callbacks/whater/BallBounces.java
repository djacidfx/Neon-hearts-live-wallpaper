package com.demo.lovelivewallpaper.Callbacks.whater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.demo.lovelivewallpaper.R;



class BallBounces extends SurfaceView implements SurfaceHolder.Callback {
    float acc;
    int angle;
    Bitmap ball;
    boolean ballFingerMove;
    int ballH;
    int ballW;
    int ballX;
    int ballY;
    Bitmap bgr;
    int bgrH;
    Bitmap bgrReverse;
    int bgrScroll;
    int bgrW;
    int dBgrY;
    float dY;
    Paint fpsPaint;
    int framesCount;
    int framesCountAvg;
    long framesTimer;
    int initialY;
    long now;
    boolean reverseBackroundFirst;
    int screenH;
    int screenW;
    GameThread thread;
    long timeDelta;
    long timeNow;
    long timePrev;
    long timePrevFrame;

    @Override 
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public BallBounces(Context context) {
        super(context);
        this.framesCount = 0;
        this.framesCountAvg = 0;
        this.framesTimer = 0L;
        this.fpsPaint = new Paint();
        this.timePrev = 0L;
        this.timePrevFrame = 0L;
        this.ball = BitmapFactory.decodeResource(getResources(), R.drawable.app0);
        this.bgr = BitmapFactory.decodeResource(getResources(), R.drawable.app1);
        this.ballW = this.ball.getWidth();
        this.ballH = this.ball.getHeight();
        this.reverseBackroundFirst = false;
        this.acc = 0.2f;
        this.dY = 0.0f;
        this.initialY = 100;
        this.angle = 0;
        this.bgrScroll = 0;
        this.dBgrY = 1;
        this.fpsPaint.setTextSize(30.0f);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override 
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.screenW = i;
        this.screenH = i2;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(this.bgr, i, i2, true);
        this.bgr = createScaledBitmap;
        this.bgrW = createScaledBitmap.getWidth();
        this.bgrH = this.bgr.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(-1.0f, 1.0f);
        this.bgrReverse = Bitmap.createBitmap(this.bgr, 0, 0, this.bgrW, this.bgrH, matrix, true);
        this.ballX = (this.screenW / 2) - (this.ballW / 2);
        this.ballY = -50;
    }

    @Override 
    public synchronized boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.ballX = ((int) motionEvent.getX()) - (this.ballW / 2);
            this.ballY = ((int) motionEvent.getY()) - (this.ballH / 2);
            this.ballFingerMove = true;
        } else if (action == 1) {
            this.ballFingerMove = false;
            this.dY = 0.0f;
        } else if (action == 2) {
            this.ballX = ((int) motionEvent.getX()) - (this.ballW / 2);
            this.ballY = ((int) motionEvent.getY()) - (this.ballH / 2);
        }
        return true;
    }

    @Override 
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(0, 0, this.bgrW - this.bgrScroll, this.bgrH);
        Rect rect2 = new Rect(this.bgrScroll, 0, this.bgrW, this.bgrH);
        int i = this.bgrW;
        Rect rect3 = new Rect(i - this.bgrScroll, 0, i, this.bgrH);
        Rect rect4 = new Rect(0, 0, this.bgrScroll, this.bgrH);
        if (!this.reverseBackroundFirst) {
            canvas.drawBitmap(this.bgr, rect, rect2, (Paint) null);
            canvas.drawBitmap(this.bgrReverse, rect3, rect4, (Paint) null);
        } else {
            canvas.drawBitmap(this.bgr, rect3, rect4, (Paint) null);
            canvas.drawBitmap(this.bgrReverse, rect, rect2, (Paint) null);
        }
        int i2 = this.bgrScroll + this.dBgrY;
        this.bgrScroll = i2;
        if (i2 >= this.bgrW) {
            this.bgrScroll = 0;
            this.reverseBackroundFirst = !this.reverseBackroundFirst;
        }
        if (!this.ballFingerMove) {
            int i3 = this.ballY;
            float f = this.dY;
            int i4 = i3 + ((int) f);
            this.ballY = i4;
            if (i4 > this.screenH - this.ballH) {
                this.dY = f * (-1.0f);
            }
            this.dY += this.acc;
        }
        int i5 = this.angle;
        this.angle = i5 + 1;
        if (i5 > 360) {
            this.angle = 0;
        }
        canvas.save();
        canvas.rotate(this.angle, this.ballX + (this.ballW / 2), this.ballY + (this.ballH / 2));
        canvas.drawBitmap(this.ball, this.ballX, this.ballY, (Paint) null);
        canvas.restore();
        this.now = System.currentTimeMillis();
        canvas.drawText(this.framesCountAvg + " fps", 40.0f, 70.0f, this.fpsPaint);
        int i6 = this.framesCount + 1;
        this.framesCount = i6;
        long j = this.now;
        if (j - this.framesTimer > 1000) {
            this.framesTimer = j;
            this.framesCountAvg = i6;
            this.framesCount = 0;
        }
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
        private BallBounces gameView;
        private boolean run = false;
        private SurfaceHolder surfaceHolder;

        public GameThread(SurfaceHolder surfaceHolder, BallBounces ballBounces) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = ballBounces;
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
                BallBounces.this.timeNow = System.currentTimeMillis();
                BallBounces ballBounces = BallBounces.this;
                ballBounces.timeDelta = ballBounces.timeNow - BallBounces.this.timePrevFrame;
                if (BallBounces.this.timeDelta < 16) {
                    try {
                        Thread.sleep(16 - BallBounces.this.timeDelta);
                    } catch (InterruptedException unused) {
                    }
                }
                BallBounces.this.timePrevFrame = System.currentTimeMillis();
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
