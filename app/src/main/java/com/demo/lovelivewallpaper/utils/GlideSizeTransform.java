package com.demo.lovelivewallpaper.utils;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.nio.charset.Charset;
import java.security.MessageDigest;


public class GlideSizeTransform extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.transformations.GlideSizeTransform";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName(Key.STRING_CHARSET_NAME));
    public int height;
    public int width;

    @Override 
    public int hashCode() {
        return -662720152;
    }

    @Override 
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return bitmap;
    }

    public GlideSizeTransform(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    @Override 
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override 
    public boolean equals(Object obj) {
        return obj instanceof GlideSizeTransform;
    }
}
