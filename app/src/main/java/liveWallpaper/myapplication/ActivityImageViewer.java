package liveWallpaper.myapplication;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.demo.lovelivewallpaper.AdAdmob;
import com.demo.lovelivewallpaper.R;

import java.io.IOException;


public class ActivityImageViewer extends AppCompatActivity {
    ImageView close;
    String image;
    Bitmap img;
    ImageView myZoomageView;

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.image = getIntent().getStringExtra("image");
        setContentView(R.layout.activity_image_viewer);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd(this);


        this.myZoomageView = (ImageView) findViewById(R.id.myZoomageView);
        ImageView imageView = (ImageView) findViewById(R.id.close);
        this.close = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityImageViewer.this.onBackPressed();
            }
        });
        RequestBuilder<Bitmap> asBitmap = Glide.with((FragmentActivity) this).asBitmap();
        asBitmap.load("file:///android_asset/" + this.image).into(this.myZoomageView);
        findViewById(R.id.setAsWallpaper).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityImageViewer.this.setAsWallpaper();
            }
        });
    }

    void setAsWallpaper() {
        new Thread() { 
            @Override 
            public void run() {
                try {
                    WallpaperManager.getInstance(ActivityImageViewer.this.getApplicationContext()).setStream(ActivityImageViewer.this.getAssets().open(ActivityImageViewer.this.image));
                    ActivityImageViewer.this.runOnUiThread(new Runnable() { 
                        @Override 
                        public void run() {
                            Toast.makeText(ActivityImageViewer.this, "wallpaper set successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    Log.e("tag1", "error while seting wallpaper");
                    e.printStackTrace();
                    ActivityImageViewer.this.runOnUiThread(new Runnable() { 
                        @Override 
                        public void run() {
                            Toast.makeText(ActivityImageViewer.this, "cant set wallpaper , check logs", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }

    @Override 
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (iArr.length <= 0 || iArr[0] != 0) {
            return;
        }
        Log.v("tag1", "Permission: " + strArr[0] + "was " + iArr[0]);
        setAsWallpaper();
    }

    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 1);
    }

    public static boolean isPermissionAccepted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
                Log.e("tag1", "Permission is granted");
                return true;
            }
            return false;
        }
        return true;
    }
}
