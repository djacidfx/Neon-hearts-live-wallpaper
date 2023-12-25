package liveWallpaper.myapplication;

import android.view.SurfaceHolder;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;



class EglHelper {
    private EGLConfigChooser mEGLConfigChooser;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private EGL10 mEgl;
    EGLConfig mEglConfig;
    private EGLContext mEglContext;
    private EGLDisplay mEglDisplay;
    private EGLSurface mEglSurface;
    private GLWrapper mGLWrapper;

    public EglHelper(EGLConfigChooser eGLConfigChooser, EGLContextFactory eGLContextFactory, EGLWindowSurfaceFactory eGLWindowSurfaceFactory, GLWrapper gLWrapper) {
        this.mEGLConfigChooser = eGLConfigChooser;
        this.mEGLContextFactory = eGLContextFactory;
        this.mEGLWindowSurfaceFactory = eGLWindowSurfaceFactory;
        this.mGLWrapper = gLWrapper;
    }

    public void start() {
        if (this.mEgl == null) {
            this.mEgl = (EGL10) EGLContext.getEGL();
        }
        if (this.mEglDisplay == null) {
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        }
        if (this.mEglConfig == null) {
            this.mEgl.eglInitialize(this.mEglDisplay, new int[2]);
            this.mEglConfig = this.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);
        }
        if (this.mEglContext == null) {
            EGLContext createContext = this.mEGLContextFactory.createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
            this.mEglContext = createContext;
            if (createContext == null || createContext == EGL10.EGL_NO_CONTEXT) {
                throw new RuntimeException("createContext failed");
            }
        }
        this.mEglSurface = null;
    }

    public GL createSurface(SurfaceHolder surfaceHolder) throws InterruptedException {
        EGLSurface eGLSurface = this.mEglSurface;
        if (eGLSurface != null && eGLSurface != EGL10.EGL_NO_SURFACE) {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
        }
        EGLSurface createWindowSurface = this.mEGLWindowSurfaceFactory.createWindowSurface(this.mEgl, this.mEglDisplay, this.mEglConfig, surfaceHolder);
        this.mEglSurface = createWindowSurface;
        if (createWindowSurface == null || createWindowSurface == EGL10.EGL_NO_SURFACE) {
            return null;
        }
        EGL10 egl10 = this.mEgl;
        EGLDisplay eGLDisplay = this.mEglDisplay;
        EGLSurface eGLSurface2 = this.mEglSurface;
        if (!egl10.eglMakeCurrent(eGLDisplay, eGLSurface2, eGLSurface2, this.mEglContext)) {
            throw new RuntimeException("eglMakeCurrent failed.");
        }
        GL gl = this.mEglContext.getGL();
        GLWrapper gLWrapper = this.mGLWrapper;
        return gLWrapper != null ? gLWrapper.wrap(gl) : gl;
    }

    public boolean swap() {
        this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
        return this.mEgl.eglGetError() != 12302;
    }

    public void destroySurface() {
        EGLSurface eGLSurface = this.mEglSurface;
        if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
            return;
        }
        this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        this.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
        this.mEglSurface = null;
    }

    public void finish() {
        EGLContext eGLContext = this.mEglContext;
        if (eGLContext != null) {
            this.mEGLContextFactory.destroyContext(this.mEgl, this.mEglDisplay, eGLContext);
            this.mEglContext = null;
        }
        EGLDisplay eGLDisplay = this.mEglDisplay;
        if (eGLDisplay != null) {
            this.mEgl.eglTerminate(eGLDisplay);
            this.mEglDisplay = null;
        }
    }
}
