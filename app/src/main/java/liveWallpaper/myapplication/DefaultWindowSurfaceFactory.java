package liveWallpaper.myapplication;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;



class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
    
    @Override 

    public EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj) throws InterruptedException {
        EGLSurface eGLSurface = null;
        while (eGLSurface == null) {
            try {
                eGLSurface = egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, null);
            } catch (Throwable unused) {
                if (eGLSurface != null) {
                }
            }
            if (eGLSurface != null) {
            }
            Thread.sleep(10L);
        }
        return eGLSurface;
    }

    @Override 
    public void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
        egl10.eglDestroySurface(eGLDisplay, eGLSurface);
    }
}
