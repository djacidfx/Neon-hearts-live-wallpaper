package liveWallpaper.myapplication;

import com.demo.lovelivewallpaper.AdAdmob;
import com.demo.lovelivewallpaper.Adapters.WallpapersAdapter;
import UEnginePackage.Utils.AppConfig;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lovelivewallpaper.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import com.demo.lovelivewallpaper.utils.AppUtils;
import com.demo.lovelivewallpaper.utils.DialogHelper;
import com.demo.lovelivewallpaper.utils.ImageUtils2;
import com.demo.lovelivewallpaper.utils.PrefLoader;
import com.demo.lovelivewallpaper.utils.Uprocess;
import com.demo.lovelivewallpaper.utils.Uscreen;
import com.demo.lovelivewallpaper.utils.Utask;
import com.demo.lovelivewallpaper.utils.Utoast;
import com.demo.lovelivewallpaper.utils.onReady;
import com.demo.lovelivewallpaper.utils.onReady2;


public class ActivityWallpaperViewer extends basActivity {
    WallpapersAdapter adapter;
    RecyclerView recyclerView;

    boolean activityRunning = true;
    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        List<String> listAssetsWallpapers = AppConfig.listAssetsWallpapers();
        AppConfig.unlockedWallpapersCount = listAssetsWallpapers.size() - 1;
        setContentView(R.layout.wallpapers_activity);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd(this);



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityWallpaperViewer.this.onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.title)).setText("Choose Wallpaper");
        Uscreen.Init(this);
        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityWallpaperViewer.this.showWallpaperChooser();
            }
        });
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { 
            @Override 
            public int getSpanSize(int i) {
                return i == 4 ? 2 : 1;
            }
        });
        this.recyclerView.setLayoutManager(gridLayoutManager);
        WallpapersAdapter wallpapersAdapter = new WallpapersAdapter(listAssetsWallpapers, new onReady2() { 
            @Override 
            public void ready(Object obj, Object obj2) {
                int intValue = ((Integer) obj2).intValue();
                if (intValue > 3) {
                    intValue--;
                }
                String str = (String) obj;
                if (!AppUtils.wallpaperUnlocked(str, intValue + 1)) {
                    ActivityWallpaperViewer.this.startUnlockProcess(str);
                    return;
                }
                PrefLoader.SavePref(Statics.lastSelectedWallpaperPref, intValue + "", ActivityWallpaperViewer.this);
                ActivityWallpaperViewer.this.returnResult(str);
            }
        }, this);
        this.adapter = wallpapersAdapter;
        this.recyclerView.setAdapter(wallpapersAdapter);
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
        this.activityRunning = false;
    }

    void showWallpaperChooser() {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 2);
        }
    }

    
    @Override
    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2 && i2 == -1) {
            saveGalleryImage(intent.getData(), intent);
        } else if (i2 == -1 && i == 69) {
            UCrop.getOutput(intent);
            if (PrefLoader.LoadPref(Statics.lastSelectedWallpaperPref, this) == -1) {
                PrefLoader.SavePref("forceReload", (PrefLoader.LoadPref("forceReload", this) + 1) + "", this);
            } else {
                PrefLoader.SavePref(Statics.lastSelectedWallpaperPref, "-1", this);
            }
            returnResult("-1");
        }  else {
            returnResult("-1");
        }
    }

    private void saveGalleryImage(final Uri uri, Intent intent) {
        new Utask(null, new Uprocess() { 
            @Override 
            public void onPreRun(Object obj) {
            }

            @Override 
            public Object onRun(Object obj) {
                try {
                    ContentResolver contentResolver = ActivityWallpaperViewer.this.getContentResolver();
                    int i = Build.VERSION.SDK_INT;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                        return bitmap == null ? "" : ImageUtils2.saveToInternalStorage(bitmap, "images", "wallpaper.jpg", ActivityWallpaperViewer.this);
                    } catch (Exception unused) {
                        return "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override 
            public void onFinish(Object obj) {
                StringBuilder sb = new StringBuilder();
                String str = (String) obj;
                sb.append(str);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                UCrop.of(Uri.fromFile(new File(str)), Uri.fromFile(new File(str))).withAspectRatio(width, height).start(ActivityWallpaperViewer.this);
            }
        });
    }

    
    @Override
    
    public void onDestroy() {
        this.adapter.onItemSelected = null;
        this.adapter.list = null;
        this.adapter = null;
        super.onDestroy();
    }

    void returnResult(String str) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(str));
        setResult(-1, intent);
        onBackPressed();
    }

    
    
    
    public class AnonymousClass6 implements onReady {
        final  String val$text;

        AnonymousClass6(String str) {
            this.val$text = str;
        }

        @Override 
        public void ready(Object obj) {
            if (((Integer) obj).intValue() == 1) {
                ActivityWallpaperViewer.this.unlockWallpaper(AnonymousClass6.this.val$text);
            }
            Utoast.show(App.c(), ActivityWallpaperViewer.this.getString(R.string.wallpaper_not_unlocked), Utoast.ToastType.warning);
        }
    }

    
    public void startUnlockProcess(String str) {
        DialogHelper.showUnlockWallpaperDialog(this, str, new AnonymousClass6(str));
    }




    
    public void unlockWallpaper(String str) {
        WallpapersAdapter wallpapersAdapter = this.adapter;
        if (wallpapersAdapter != null) {
            wallpapersAdapter.notifyDataSetChanged();
        }
        PrefLoader.SavePref(str, "1", this);
        Utoast.show(this, getString(R.string.wallpaper_unlocked), Utoast.ToastType.sucess);
    }

    
    public void showRewardedVideo(final String str) {

        ActivityWallpaperViewer.this.unlockWallpaper(str);

    }


    @Override
    
    public void onResume() {
        super.onResume();
    }
}
