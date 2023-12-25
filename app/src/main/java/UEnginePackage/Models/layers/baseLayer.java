package UEnginePackage.Models.layers;

import UEnginePackage.UGL.Urect;
import android.graphics.Canvas;
import javax.microedition.khronos.opengles.GL10;


public class baseLayer {
    public Urect bound;
    public int paralax = 0;
    public boolean enabled = true;

    public baseLayer m3clone() {
        return null;
    }

    public void destroy() {
    }

    public void draw(Canvas canvas) {
    }

    public void draw(GL10 gl10, float f) {
    }

    public boolean update() {
        return false;
    }

    public void setBound(Urect urect) {
        this.bound = urect;
    }
}
