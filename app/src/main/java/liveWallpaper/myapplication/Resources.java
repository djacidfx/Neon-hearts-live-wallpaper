package liveWallpaper.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


public class Resources {
    public static void Inicial() {
    }

    public static Rect GetRectOfImage(Bitmap bitmap) {
        return new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap CreateBitmap(int i, Context context) {
        if (context == null) {
            Log.i("ctx null cls Resources", "context is null on class resource createRectofimage");
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inScaled = false;
            return BitmapFactory.decodeResource(context.getResources(), i, options);
        } catch (Exception unused) {
            return null;
        }
    }

    public static Bitmap CreateBitmapFromPath(String str) {
        try {
            return BitmapFactory.decodeFile(str, new BitmapFactory.Options());
        } catch (Exception unused) {
            return null;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return Bitmap.createBitmap(createScaledBitmap, 0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getBitmapFromAsset(String str, Context context) {
        return getBitmapFromAsset(str, context, 1.0f);
    }

    public static Bitmap getBitmapFromAsset(String str, Context context, float f) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(str));
            return f != 1.0f ? scalBitmap(bitmap, f) : bitmap;
        } catch (Exception e) {
            Log.e("tag5", "image load exception " + e.getMessage());
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap scalBitmap(Bitmap bitmap, float f) {
        int width = (int) (bitmap.getWidth() * f);
        int height = (int) (bitmap.getHeight() * f);
        Log.e("tag5", "scaling bitmap to " + width + "/" + height);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return createScaledBitmap;
    }

    public static Bitmap resizeBitmapByScale(Bitmap bitmap, float f, boolean z) {
        int round = Math.round(bitmap.getWidth() * f);
        int round2 = Math.round(bitmap.getHeight() * f);
        if (round == bitmap.getWidth() && round2 == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(round, round2, getConfig(bitmap));
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(f, f);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint(6));
        if (z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static Bitmap cropToRectangle(Bitmap bitmap, boolean z) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getWidth());
        if (z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    private static Bitmap.Config getConfig(Bitmap bitmap) {
        Bitmap.Config config = bitmap.getConfig();
        return config == null ? Bitmap.Config.ARGB_8888 : config;
    }
}
