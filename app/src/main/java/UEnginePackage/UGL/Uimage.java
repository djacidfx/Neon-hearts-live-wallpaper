package UEnginePackage.UGL;

import UEnginePackage.Models.UTexture;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import javax.microedition.khronos.opengles.GL10;


public class Uimage extends Urect {
    public boolean antiAlias;
    public boolean enableTint;
    public UTexture image;
    Rect imgRectAspectRatio;
    Rect rectAspectRatio;
    public boolean shinyFilter;
    public UimageSizeType sizeType;
    public int tint;
    public int tintColor;

    public void setImage(UTexture uTexture) {
        this.image = uTexture;
    }

    public UTexture getImage() {
        return this.image;
    }

    public UimageSizeType getSizeType() {
        return this.sizeType;
    }

    public void setSizeType(UimageSizeType uimageSizeType) {
        this.sizeType = uimageSizeType;
    }

    public Uimage(double d, double d2, double d3, double d4, UTexture uTexture) {
        super(d, d2, d3, d4);
        this.enableTint = false;
        this.sizeType = UimageSizeType.FitXY;
        this.antiAlias = false;
        this.paint.setColor(0);
        this.image = uTexture;
    }

    @Override 
    public void Draw(Canvas canvas) {
        int save = canvas.save();
        canvas.rotate((int) getRotate(), (int) getCenterX(), (int) getCenterY());
        this.paint.setAlpha((int) getAlpha());
        boolean z = this.antiAlias;
        if (z) {
            this.paint.setAntiAlias(z);
            this.paint.setFlags(1);
        }
        UTexture uTexture = this.image;
        if (uTexture == null || uTexture.bmp.isRecycled()) {
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle((float) getLeft(), (float) getTop(), (float) getWidth(), this.paint);
        } else {
            int i = AnonymousClass1.$SwitchMap$UEnginePackage$UGL$UimageSizeType[this.sizeType.ordinal()];
            if (i == 1) {
                UTexture uTexture2 = this.image;
                canvas.drawBitmap(uTexture2.bmp, uTexture2.getRect(), GetRect(), this.paint);
            } else if (i == 2) {
                canvas.drawBitmap(this.image.bmp, getImageSizeAcordingToAspectRatio(), GetRect(), this.paint);
            }
        }
        canvas.restoreToCount(save);
    }

    
    static class AnonymousClass1 {
        static final int[] $SwitchMap$UEnginePackage$UGL$UimageSizeType;

        AnonymousClass1() {
        }

        static {
            int[] iArr = new int[UimageSizeType.values().length];
            $SwitchMap$UEnginePackage$UGL$UimageSizeType = iArr;
            try {
                iArr[UimageSizeType.FitXY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$UEnginePackage$UGL$UimageSizeType[UimageSizeType.CenterCrop.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$UEnginePackage$UGL$UimageSizeType[UimageSizeType.FitX.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    @Override 
    public void Draw(GL10 gl10, float f) {
        this.paint.setAlpha((int) getAlpha());
        boolean z = this.antiAlias;
        if (z) {
            this.paint.setAntiAlias(z);
            this.paint.setFlags(1);
        }
        UTexture uTexture = this.image;
        if (uTexture == null || !uTexture.isLoaded()) {
            UTexture uTexture2 = this.image;
            if (uTexture2 != null && !uTexture2.isLoaded()) {
                this.image.load();
            }
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            return;
        }
        Rect GetRect = GetRect();
        int i = AnonymousClass1.$SwitchMap$UEnginePackage$UGL$UimageSizeType[this.sizeType.ordinal()];
        if (i == 1) {
            draw(gl10, (GetRect.width() / 2) + GetRect.left + f, GetRect.top + (GetRect.height() / 2), GetRect.width(), GetRect.height(), (float) this.rotate);
        } else if (i == 2) {
            Rect rectAccordingToAspectRation = getRectAccordingToAspectRation();
            draw(gl10, (rectAccordingToAspectRation.width() / 2) + rectAccordingToAspectRation.left + f, rectAccordingToAspectRation.top + (rectAccordingToAspectRation.height() / 2), rectAccordingToAspectRation.width(), rectAccordingToAspectRation.height(), (float) this.rotate);
        } else if (i == 3) {
            draw(gl10, (GetRect.width() / 2) + GetRect.left + f, GetRect.top + (GetRect.height() / 2), GetRect.width(), GetRect.height(), (float) this.rotate);
        }
    }

    void draw(GL10 gl10, float f, float f2, float f3, float f4, float f5) {
        UTexture uTexture = this.image;
        if (uTexture == null) {
            return;
        }
        uTexture.prepare(gl10, 33071);
        gl10.glPushMatrix();
        gl10.glTranslatef(f, f2, 0.0f);
        gl10.glRotatef(f5, 0.0f, 0.0f, 1.0f);
        gl10.glScalef(f3, f4, 0.0f);
        float alpha = ((float) getAlpha()) / 255.0f;
        int i = this.color;
        if (i == 0) {
            gl10.glColor4f(alpha, alpha, alpha, alpha);
        } else {
            gl10.glColor4f(Color.red(i) / 255.0f, Color.green(this.color) / 255.0f, Color.blue(this.color) / 255.0f, (((float) getAlpha()) / 510.0f) + 0.5f);
        }
        gl10.glDrawArrays(5, 0, 4);
        gl10.glPopMatrix();
    }

    public double getRelativeRotation() {
        return this.rotate;
    }

    public Rect getImageSizeAcordingToAspectRatio() {
        UTexture uTexture = this.image;
        if (uTexture == null) {
            return RR.emptyRect;
        }
        Rect rect = uTexture.getRect();
        Rect GetRect = GetRect();
        if (this.imgRectAspectRatio == null) {
            this.imgRectAspectRatio = new Rect(0, 0, 0, 0);
        }
        if (rect.width() == 0 || rect.height() == 0 || GetRect.width() == 0 || GetRect.height() == 0) {
            return rect;
        }
        float height = rect.height() / rect.width();
        float height2 = GetRect.height() / GetRect.width();
        if (height > height2) {
            int width = rect.width();
            int width2 = (int) (rect.width() * height2);
            int height3 = (rect.height() - width2) / 2;
            Rect rect2 = this.imgRectAspectRatio;
            rect2.left = 0;
            rect2.top = height3;
            rect2.right = width;
            rect2.bottom = width2 + height3;
            return rect2;
        }
        int width3 = (height > height2 ? 1 : (height == height2 ? 0 : -1));
        if (width3 < 0) {
            int height4 = (int) (rect.height() / height2);
            int height5 = rect.height();
            int width32 = (rect.width() - height4) / 2;
            Rect rect3 = this.imgRectAspectRatio;
            rect3.left = width32;
            rect3.top = 0;
            rect3.right = height4 + width32;
            rect3.bottom = height5;
            return rect3;
        }
        return rect;
    }

    public Rect getRectAccordingToAspectRation() {
        UTexture uTexture = this.image;
        if (uTexture == null) {
            return RR.emptyRect;
        }
        Rect rect = uTexture.getRect();
        Rect GetRect = GetRect();
        if (this.rectAspectRatio == null) {
            this.rectAspectRatio = new Rect(0, 0, 0, 0);
        }
        if (rect.width() == 0 || rect.height() == 0 || GetRect.width() == 0 || GetRect.height() == 0) {
            return GetRect;
        }
        float height = rect.height() / rect.width();
        float height2 = GetRect.height() / GetRect.width();
        if (height > height2) {
            int width = GetRect.width();
            int width2 = (int) (GetRect.width() * height);
            int height3 = (GetRect.height() - width2) / 2;
            Rect rect2 = this.rectAspectRatio;
            rect2.left = 0;
            rect2.top = height3;
            rect2.right = width;
            rect2.bottom = width2 + height3;
            return rect2;
        }
        int width3 = (height > height2 ? 1 : (height == height2 ? 0 : -1));
        if (width3 < 0) {
            int height4 = (int) (GetRect.height() / height);
            int height5 = GetRect.height();
            int width32 = (GetRect.width() - height4) / 2;
            Rect rect3 = this.rectAspectRatio;
            rect3.left = width32;
            rect3.top = 0;
            rect3.right = height4 + width32;
            rect3.bottom = height5;
            return rect3;
        }
        return rect;
    }

    public Uimage m1clone() {
        return new Uimage(this.x, this.y, this.width, this.height, this.image);
    }

    public void destroy() {
        UTexture uTexture = this.image;
        if (uTexture != null) {
            uTexture.destroyTexture();
        }
        this.image = null;
    }
}
