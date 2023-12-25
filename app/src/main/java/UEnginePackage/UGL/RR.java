package UEnginePackage.UGL;

import UEnginePackage.Models.UTexture;
import android.graphics.Rect;
import java.util.Random;


public class RR {
    public static Rect emptyRect = new Rect();
    public static Random random = new Random();

    public static Rect GetRectOfImage1(UTexture uTexture) {
        if (uTexture == null) {
            return new Rect(0, 0, 0, 0);
        }
        return new Rect(0, 0, uTexture.getWidth(), uTexture.getHeight());
    }
}
