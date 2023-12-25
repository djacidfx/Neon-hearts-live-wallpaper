package com.demo.lovelivewallpaper.Callbacks.whater;


public class NativeRippler implements Rippler {
    @Override 
    public native void disturb(int i, int i2, int i3, int i4, short s, short[] sArr, boolean z);

    @Override 
    public native void transformRipples(int i, int i2, short[] sArr, short[] sArr2, int[] iArr, int[] iArr2, boolean z);

    static {
        System.loadLibrary("rippler");
    }
}
