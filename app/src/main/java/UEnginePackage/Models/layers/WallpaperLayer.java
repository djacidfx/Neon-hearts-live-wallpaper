package UEnginePackage.Models.layers;

import UEnginePackage.Models.UTexture;
import UEnginePackage.UGL.Uimage;
import UEnginePackage.UGL.UimageSizeType;
import UEnginePackage.UGL.Urect;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import javax.microedition.khronos.opengles.GL10;


public class WallpaperLayer extends baseLayer {
    public UTexture fullBitmap;
    public UTexture previewBitmap;
    public Uimage wallpaper;
    public String wallpaperPath;

    @Override 
    public void destroy() {
        super.destroy();
        UTexture uTexture = this.previewBitmap;
        if (uTexture != null) {
            uTexture.destroyTexture();
        }
        this.previewBitmap = null;
        UTexture uTexture2 = this.fullBitmap;
        if (uTexture2 != null) {
            uTexture2.destroyTexture();
        }
        this.fullBitmap = null;
        Uimage uimage = this.wallpaper;
        if (uimage != null) {
            if (uimage.image != null) {
                this.wallpaper.image.destroyTexture();
            }
            this.wallpaper.image = null;
        }
        this.wallpaper = null;
    }

    public WallpaperLayer(UTexture uTexture, Urect urect, String str) {
        this.previewBitmap = uTexture;
        if (uTexture != null) {
            uTexture.preserveBMPPreview = true;
        }
        Log.e("tag1", "preservepreview is set to true in wallpaperLayer");
        Urect urect2 = urect == null ? new Urect(0.0d, 0.0d, 100.0d, 100.0d) : urect;
        this.wallpaperPath = str;
        Uimage uimage = new Uimage(urect2.getLeft(), urect2.getTop(), urect2.getWidth(), urect2.getHeight(), this.previewBitmap);
        this.wallpaper = uimage;
        uimage.setSizeType(UimageSizeType.FitXY);
        this.wallpaper.antiAlias = true;
    }

    @Override 
    public boolean update() {
        return super.update();
    }

    @Override 
    public void setBound(Urect urect) {
        super.setBound(urect);

        this.wallpaper.setWidth(urect.getWidth());
        this.wallpaper.setHeight(urect.getHeight());
    }

    @Override 
    public void draw(GL10 gl10, float f) {
        super.draw(gl10, f);
        Uimage uimage = this.wallpaper;
        if (uimage == null || uimage.image == null) {
            return;
        }
        Uimage uimage2 = this.wallpaper;
        if (uimage2 != null && uimage2.image != null) {
            UTexture uTexture = this.wallpaper.image;
            if (!uTexture.preserveBMPPreview) {
                uTexture.preserveBMPPreview = true;
                Log.e("tag1", "preserveBMPPreview set in draw");
            }
        }
        this.wallpaper.Draw(gl10, f);
    }

    @Override 
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.wallpaper.Draw(canvas);
        UTexture uTexture = this.wallpaper.image;
    }

    
    public WallpaperLayer m2clone() {
        WallpaperLayer wallpaperLayer = new WallpaperLayer(null, this.bound, this.wallpaperPath);
        wallpaperLayer.fullBitmap = this.fullBitmap;
        wallpaperLayer.previewBitmap = this.previewBitmap;
        wallpaperLayer.wallpaper = this.wallpaper.m1clone();
        return wallpaperLayer;
    }

    public int getColorAt(float f, float f2) {
        if (f < 0.0f || f2 < 0.0f || f > 1.0f || f2 > 1.0f) {
            return -1;
        }
        Bitmap bitmap = null;
        UTexture uTexture = this.previewBitmap;
        if (uTexture != null && uTexture.bmp != null) {
            bitmap = this.previewBitmap.bmp;
        } else {
            Uimage uimage = this.wallpaper;
            if (uimage != null && uimage.image != null) {
                UTexture uTexture2 = this.wallpaper.image;
                if (uTexture2.bmp != null) {
                    bitmap = uTexture2.bmp;
                }
            }
        }
        if (bitmap != null) {
            try {
                return bitmap.getPixel((int) (bitmap.getWidth() * f), (int) (bitmap.getHeight() * f2));
            } catch (Exception e) {
            }
        }
        return Color.parseColor("#28FFFFFF");
    }
}
