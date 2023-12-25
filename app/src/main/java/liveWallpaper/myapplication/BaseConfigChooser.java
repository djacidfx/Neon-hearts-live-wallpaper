package liveWallpaper.myapplication;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;



abstract class BaseConfigChooser implements EGLConfigChooser {
    protected int[] mConfigSpec;

    abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

    public BaseConfigChooser(int[] iArr) {
        this.mConfigSpec = iArr;
    }

    @Override 
    public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
        int[] iArr = new int[1];
        egl10.eglChooseConfig(eGLDisplay, this.mConfigSpec, null, 0, iArr);
        int i = iArr[0];
        if (i <= 0) {
            throw new IllegalArgumentException("No configs match configSpec");
        }
        EGLConfig[] eGLConfigArr = new EGLConfig[i];
        egl10.eglChooseConfig(eGLDisplay, this.mConfigSpec, eGLConfigArr, i, iArr);
        EGLConfig chooseConfig = chooseConfig(egl10, eGLDisplay, eGLConfigArr);
        if (chooseConfig != null) {
            return chooseConfig;
        }
        throw new IllegalArgumentException("No config chosen");
    }

    
    
    public static class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue;

        @Override 
        public   EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            return super.chooseConfig(egl10, eGLDisplay);
        }

        public ComponentSizeChooser(int i, int i2, int i3, int i4, int i5, int i6) {
            super(new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344});
            this.mValue = new int[1];
            this.mRedSize = i;
            this.mGreenSize = i2;
            this.mBlueSize = i3;
            this.mAlphaSize = i4;
            this.mDepthSize = i5;
            this.mStencilSize = i6;
        }

        @Override 
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            EGLConfig eGLConfig = null;
            int i = 1000;
            for (EGLConfig eGLConfig2 : eGLConfigArr) {
                int findConfigAttrib = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12325, 0);
                int findConfigAttrib2 = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12326, 0);
                if (findConfigAttrib >= this.mDepthSize && findConfigAttrib2 >= this.mStencilSize) {
                    int abs = Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12324, 0) - this.mRedSize) + Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12323, 0) - this.mGreenSize) + Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12322, 0) - this.mBlueSize) + Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12321, 0) - this.mAlphaSize);
                    if (abs < i) {
                        i = abs;
                        eGLConfig = eGLConfig2;
                    }
                }
            }
            return eGLConfig;
        }

        private int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.mValue) ? this.mValue[0] : i2;
        }
    }

    
    
    public static class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public SimpleEGLConfigChooser(boolean z) {
            super(4, 4, 4, 0, z ? 16 : 0, 0);
            this.mRedSize = 5;
            this.mGreenSize = 6;
            this.mBlueSize = 5;
        }
    }
}
