package UEnginePackage.Models;

import UEnginePackage.UGL.Uimage;
import android.graphics.Canvas;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;


public class Usprite extends Uimage {
    public int currentFrame;
    public int currentRow;
    public int frames;
    public int framesSpeed;
    List<UTexture> imgs;
    public boolean isMultipleImages;
    long lastTimeUpdated;
    public int rows;
    public int selectedFrame;
    public int selectedRow;

    public Usprite(double d, double d2, double d3, double d4, List<UTexture> list, int i, int i2, int i3, boolean z, int i4, int i5) {
        super(d, d2, d3, d4, list.get(0));
        init(d, d2, d3, d4, list, i, i2, i3, z, i4, i5);
    }

    public Usprite(double d, double d2, double d3, double d4, List<UTexture> list, int i, int i2, int i3, boolean z) {
        super(d, d2, d3, d4, list.get(0));
        init(d, d2, d3, d4, list, i, i2, i3, z, -1, -1);
    }

    public void init(double d, double d2, double d3, double d4, List<UTexture> list, int i, int i2, int i3, boolean z, int i4, int i5) {
        int i6;
        this.imgs = list;
        this.frames = i;
        this.rows = i3;
        this.isMultipleImages = z;
        this.framesSpeed = i2;
        this.lastTimeUpdated = System.currentTimeMillis();
        int i7 = 1;
        if (i4 != -1) {
            i7 = i4;
        } else if (i3 != 1) {
            double random = Math.random();
            double d5 = i3;
            Double.isNaN(d5);
            i7 = (int) (random * d5);
        }
        this.currentRow = i7;
        if (i5 == -1) {
            double random2 = Math.random();
            double d6 = i;
            Double.isNaN(d6);
            i6 = (int) (random2 * d6);
        } else {
            i6 = i5;
        }
        this.currentFrame = i6;
        this.selectedRow = i4;
        this.selectedFrame = i5;
    }

    @Override 
    public void Draw(GL10 gl10, float f) {
        if (this.lastTimeUpdated + this.framesSpeed < System.currentTimeMillis()) {
            nextFrame();
        }
        super.Draw(gl10, f);
    }

    @Override 
    public void Draw(Canvas canvas) {
        if (this.lastTimeUpdated + this.framesSpeed < System.currentTimeMillis()) {
            nextFrame();
        }
        super.Draw(canvas);
    }

    private void nextFrame() {
        this.lastTimeUpdated = System.currentTimeMillis();
        int i = this.currentFrame + 1;
        this.currentFrame = i;
        if (i >= this.frames) {
            this.currentFrame = 0;
        }
        this.image = this.imgs.get(this.currentFrame);
    }

    
    public Usprite m0clone() {
        return new Usprite(this.x, this.y, 0.0d, 0.0d, this.imgs, this.frames, this.framesSpeed, this.rows, this.isMultipleImages, this.selectedRow, this.selectedFrame);
    }
}
