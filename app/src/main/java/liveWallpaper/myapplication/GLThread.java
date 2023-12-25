package liveWallpaper.myapplication;

import android.view.SurfaceHolder;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import liveWallpaper.myapplication.baseGLWallpaperService;




public class GLThread extends Thread {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    private static final boolean LOG_THREADS = false;
    private EGLConfigChooser mEGLConfigChooser;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private EglHelper mEglHelper;
    private GLThread mEglOwner;
    private boolean mEventsWaiting;
    private GLWrapper mGLWrapper;
    private boolean mHasSurface;
    private boolean mHaveEgl;
    public SurfaceHolder mHolder;
    private boolean mPaused;
    public baseGLWallpaperService.Renderer mRenderer;
    private boolean mWaitingForSurface;
    private final GLThreadManager sGLThreadManager = new GLThreadManager();
    private boolean mSizeChanged = true;
    private ArrayList<Runnable> mEventQueue = new ArrayList<>();
    public boolean mDone = false;
    private int mWidth = 0;
    private int mHeight = 0;
    private boolean mRequestRender = true;
    private int mRenderMode = 1;

    
    public GLThread(baseGLWallpaperService.Renderer renderer, EGLConfigChooser eGLConfigChooser, EGLContextFactory eGLContextFactory, EGLWindowSurfaceFactory eGLWindowSurfaceFactory, GLWrapper gLWrapper) {
        this.mRenderer = renderer;
        this.mEGLConfigChooser = eGLConfigChooser;
        this.mEGLContextFactory = eGLContextFactory;
        this.mEGLWindowSurfaceFactory = eGLWindowSurfaceFactory;
        this.mGLWrapper = gLWrapper;
    }

    @Override 
    public void run() {
        setName("GLThread " + getId());
        try {
            guardedRun();
        } catch (InterruptedException unused) {
        } catch (Throwable th) {
            this.sGLThreadManager.threadExiting(this);
            throw th;
        }
        this.sGLThreadManager.threadExiting(this);
    }

    private void stopEglLocked() {
        if (this.mHaveEgl) {
            this.mHaveEgl = false;
            this.mEglHelper.destroySurface();
            this.sGLThreadManager.releaseEglSurface(this);
        }
    }

    
    
    
    
    
    
    
    private void guardedRun() throws InterruptedException {
        boolean z;
        int i;
        int i2;
        boolean z2;
        this.mEglHelper = new EglHelper(this.mEGLConfigChooser, this.mEGLContextFactory, this.mEGLWindowSurfaceFactory, this.mGLWrapper);
        GL10 gl10 = null;
        boolean z3 = true;
        boolean z4 = true;
        while (!isDone()) {
            try {
                synchronized (this.sGLThreadManager) {
                    boolean z5 = false;
                    while (true) {
                        if (this.mPaused) {
                            stopEglLocked();
                        }
                        if (this.mHasSurface) {
                            if (!this.mHaveEgl && this.sGLThreadManager.tryAcquireEglSurface(this)) {
                                this.mHaveEgl = true;
                                this.mEglHelper.start();
                                this.mRequestRender = true;
                                z5 = true;
                            }
                        } else if (!this.mWaitingForSurface) {
                            stopEglLocked();
                            this.mWaitingForSurface = true;
                            this.sGLThreadManager.notifyAll();
                        }
                        if (this.mDone) {
                            synchronized (this.sGLThreadManager) {
                                stopEglLocked();
                                this.mEglHelper.finish();
                            }
                            return;
                        } else if (this.mEventsWaiting) {
                            this.mEventsWaiting = false;
                            z = true;
                            i = 0;
                            i2 = 0;
                            z2 = false;
                            break;
                        } else if (this.mPaused || !(this.mHasSurface) || !this.mHaveEgl || (i = this.mWidth) <= 0 || (i2 = this.mHeight) <= 0 || (!this.mRequestRender && this.mRenderMode != 1)) {
                            this.sGLThreadManager.wait();
                        }
                    }
                    if (z) {
                        while (true) {
                            Runnable event = getEvent();
                            if (event != null) {
                                event.run();
                                if (isDone()) {
                                    synchronized (this.sGLThreadManager) {
                                        stopEglLocked();
                                        this.mEglHelper.finish();
                                    }
                                    return;
                                }
                            }
                        }
                    } else {
                        if (z5) {
                            z3 = true;
                            z2 = true;
                        }
                        if (z2) {
                            gl10 = (GL10) this.mEglHelper.createSurface(this.mHolder);
                            z4 = true;
                        }
                        if (z3) {
                            this.mRenderer.onSurfaceCreated(gl10, this.mEglHelper.mEglConfig);
                            z3 = false;
                        }
                        if (z4) {
                            this.mRenderer.onSurfaceChanged(gl10, i, i2);
                            z4 = false;
                        }
                        if (i > 0 && i2 > 0) {
                            this.mRenderer.onDrawFrame(gl10);
                            this.mEglHelper.swap();
                        }
                    }
                }
            } catch (Throwable th) {
                synchronized (this.sGLThreadManager) {
                    stopEglLocked();
                    this.mEglHelper.finish();
                    throw th;
                }
            }
        }
        synchronized (this.sGLThreadManager) {
            stopEglLocked();
            this.mEglHelper.finish();
        }
    }

    private boolean isDone() {
        boolean z;
        synchronized (this.sGLThreadManager) {
            z = this.mDone;
        }
        return z;
    }

    public void setRenderMode(int i) {
        if (i < 0 || i > 1) {
            throw new IllegalArgumentException("renderMode");
        }
        synchronized (this.sGLThreadManager) {
            this.mRenderMode = i;
            if (i == 1) {
                this.sGLThreadManager.notifyAll();
            }
        }
    }

    public int getRenderMode() {
        int i;
        synchronized (this.sGLThreadManager) {
            i = this.mRenderMode;
        }
        return i;
    }

    public void requestRender() {
        synchronized (this.sGLThreadManager) {
            this.mRequestRender = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHolder = surfaceHolder;
        synchronized (this.sGLThreadManager) {
            this.mHasSurface = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void surfaceDestroyed() {
        synchronized (this.sGLThreadManager) {
            this.mHasSurface = false;
            this.sGLThreadManager.notifyAll();
            while (!this.mWaitingForSurface && isAlive() && !this.mDone) {
                try {
                    this.sGLThreadManager.wait();
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void onPause() {
        synchronized (this.sGLThreadManager) {
            this.mPaused = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void onResume() {
        synchronized (this.sGLThreadManager) {
            this.mPaused = false;
            this.mRequestRender = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void onWindowResize(int i, int i2) {
        synchronized (this.sGLThreadManager) {
            this.mWidth = i;
            this.mHeight = i2;
            this.mSizeChanged = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void requestExitAndWait() {
        synchronized (this.sGLThreadManager) {
            this.mDone = true;
            this.sGLThreadManager.notifyAll();
        }
        try {
            join();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
    }

    public void queueEvent(Runnable runnable) {
        synchronized (this) {
            this.mEventQueue.add(runnable);
            synchronized (this.sGLThreadManager) {
                this.mEventsWaiting = true;
                this.sGLThreadManager.notifyAll();
            }
        }
    }

    private Runnable getEvent() {
        synchronized (this) {
            if (this.mEventQueue.size() > 0) {
                return this.mEventQueue.remove(0);
            }
            return null;
        }
    }

    
    
    
    public class GLThreadManager {
        private GLThreadManager() {
        }

        public synchronized void threadExiting(GLThread gLThread) {
            gLThread.mDone = true;
            if (GLThread.this.mEglOwner == gLThread) {
                GLThread.this.mEglOwner = null;
            }
            notifyAll();
        }

        public synchronized boolean tryAcquireEglSurface(GLThread gLThread) {
            if (GLThread.this.mEglOwner == gLThread || GLThread.this.mEglOwner == null) {
                GLThread.this.mEglOwner = gLThread;
                notifyAll();
                return true;
            }
            return false;
        }

        public synchronized void releaseEglSurface(GLThread gLThread) {
            if (GLThread.this.mEglOwner == gLThread) {
                GLThread.this.mEglOwner = null;
            }
            notifyAll();
        }
    }
}
