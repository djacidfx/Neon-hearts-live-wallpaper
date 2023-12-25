package UEnginePackage.Models;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES10;
import android.opengl.GLUtils;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;


public class UTexture {
    public Bitmap bmp;
    int bmpHeight;
    int bmpWidth;
    public boolean DestroyBMPWhenLoaded = true;
    public boolean preserveBMPPreview = false;
    public FloatBuffer tempTextureBuffer = null;
    public int textureId = -1;
    public int glFlag = 771;
    public int color = -1;
    Rect imgRect = new Rect();

    public void log() {
    }

    public void setShiny(boolean z) {
        if (z) {
            this.glFlag = 772;
        } else {
            this.glFlag = 771;
        }
    }

    public UTexture(Bitmap bitmap) {
        this.bmp = bitmap;
        if (bitmap != null) {
            this.bmpWidth = bitmap.getWidth();
            int height = bitmap.getHeight();
            this.bmpHeight = height;
            Rect rect = this.imgRect;
            rect.right = this.bmpWidth;
            rect.bottom = height;
        }
    }

    public UTexture load() {
        int[] iArr = new int[1];
        GLES10.glGenTextures(1, iArr, 0);
        int i = iArr[0];
        this.textureId = i;
        if (i == 0) {
            Log.e("tag1", "error loading texture : GLES20.GL_FALSE");
            throw new RuntimeException("Error loading texture");
        }
        GLES10.glBindTexture(3553, i);
        GLUtils.texImage2D(3553, 0, this.bmp, 0);
        GLES10.glTexParameterf(3553, 10240, 9729.0f);
        GLES10.glTexParameterf(3553, 10241, 9729.0f);
        buildTextureMapping();
        Log.e("tag1", "load texture " + this.textureId + " at Thread id :" + Thread.currentThread().getId());
        return this;
    }

    public void clearTexture() {
        int i = this.textureId;
        if (i == -1) {
            return;
        }
        GLES10.glDeleteTextures(1, new int[]{i}, 0);
        this.textureId = -1;
    }

    public void destroyTexture() {
        Bitmap bitmap = this.bmp;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.bmp = null;
        int i = this.textureId;
        if (i == -1) {
            return;
        }
        GLES10.glDeleteTextures(1, new int[]{i}, 0);
        this.textureId = -1;
    }

    public final boolean isLoaded() {
        return this.textureId >= 0;
    }

    public int getHeight() {
        return this.bmpHeight;
    }

    public int getWidth() {
        return this.bmpWidth;
    }

    private void buildTextureMapping() {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(32);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        this.tempTextureBuffer = asFloatBuffer;
        asFloatBuffer.put(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});
        this.tempTextureBuffer.position(0);
    }

    public final void prepare(GL10 gl10, int i) {
        if (!isLoaded()) {
            Log.e("tag1", "texture not loaded : ");
        }
        gl10.glEnable(3553);
        gl10.glBlendFunc(1, this.glFlag);
        gl10.glBindTexture(3553, this.textureId);
        float f = i;
        gl10.glTexParameterf(3553, 10242, f);
        gl10.glTexParameterf(3553, 10243, f);
        gl10.glEnableClientState(32888);
        gl10.glTexCoordPointer(2, 5126, 0, this.tempTextureBuffer);
    }

    public void setColor(int i) {
        this.color = i;
    }

    public Rect getRect() {
        return this.imgRect;
    }
}
