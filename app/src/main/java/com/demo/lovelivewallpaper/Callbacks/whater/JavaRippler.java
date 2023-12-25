package com.demo.lovelivewallpaper.Callbacks.whater;


public class JavaRippler implements Rippler {
    @Override 
    public void transformRipples(int i, int i2, short[] sArr, short[] sArr2, int[] iArr, int[] iArr2, boolean z) {
        int i3 = i2 / 2;
        int i4 = i / 2;
        int i5 = z ? i2 : (i + 3) * i2;
        int i6 = !z ? i2 : (i + 3) * i2;
        int i7 = 0;
        for (int i8 = i; i8 > 0; i8--) {
            for (int i9 = i2; i9 > 0; i9--) {
                int i10 = i5 + i7;
                int i11 = i6 + i7;
                int i12 = ((((sArr[i10 - i2] + sArr[i10 + i2]) + sArr[i10 - 1]) + sArr[i10 + 1]) >> 1) - sArr[i11];
                int i13 = i12 - (i12 >> 5);
                sArr[i11] = (short) i13;
                int i14 = 1024 - i13;
                short s = sArr2[i7];
                sArr2[i7] = (short) i14;
                if (s != i14) {
                    int i15 = ((((i9 - i3) * i14) / 1024) << 0) + i3;
                    int i16 = ((((i8 - i4) * i14) / 1024) << 0) + i4;
                    if (i15 >= i2) {
                        i15 = i2 - 1;
                    }
                    if (i15 < 0) {
                        i15 = 0;
                    }
                    if (i16 >= i) {
                        i16 = i - 1;
                    }
                    if (i16 < 0) {
                        i16 = 0;
                    }
                    iArr2[i7] = iArr[i15 + (i16 * i2)];
                }
                i7++;
            }
        }
    }

    @Override 
    public void disturb(int i, int i2, int i3, int i4, short s, short[] sArr, boolean z) {
        int i5 = i << 0;
        int i6 = i2 << 0;
        int i7 = !z ? i3 : (i4 + 3) * i3;
        for (int i8 = i6 - s; i8 < i6 + s; i8++) {
            for (int i9 = i5 - s; i9 < i5 + s; i9++) {
                int i10 = (i8 * i3) + i7 + i9;
                sArr[i10] = (short) (sArr[i10] + 128);
            }
        }
    }
}
