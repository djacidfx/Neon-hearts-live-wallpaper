package liveWallpaper.myapplication;

import UEnginePackage.Models.UTexture;
import UEnginePackage.Models.range;
import UEnginePackage.Utils.AppConfig;
import UEnginePackage.Views.LiveWallpaperViewUgl;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import UEnginePackage.Models.layers.WallpaperLayer;
import UEnginePackage.touchEffectsPackage.TouchEffectsBase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.demo.lovelivewallpaper.R;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;
import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.DialogHelper;
import com.demo.lovelivewallpaper.utils.ImageUtils;
import com.demo.lovelivewallpaper.utils.ImageUtils2;
import com.demo.lovelivewallpaper.utils.PrefLoader;
import com.demo.lovelivewallpaper.utils.Uprocess;
import com.demo.lovelivewallpaper.utils.Uscreen;
import com.demo.lovelivewallpaper.utils.Utask;
import com.demo.lovelivewallpaper.utils.Utoast;
import com.demo.lovelivewallpaper.utils.onReady2;


public class ActivityApply extends basActivity implements NavigationView.OnNavigationItemSelectedListener {
    View behaviorButton;
    Bitmap bgBitmap;
    View buttonsLayout;
    CountDownTimer c;
    View clickEffectButton;
    SeekBar count;
    DrawerLayout drawer;
    int effectId;
    ParticleSystem glitterPS;
    Switch glitterSwitch;
    LayerManager layerManager;
    LiveWallpaperViewUgl liveWallpaperView;
    MovmentUtils movment;
    View optionsButton;
    View optionsLayout;
    View particlesButton;
    ImageView randomize;
    IRun resumeCallback;
    boolean showRateUsOnResume;
    SeekBar size;
    SoundPool soundPool;
    SeekBar speed;
    Switch switch1;
    View wallpapersButton;
    String backgroundPath = "0.jpg";
    boolean activityVisible = false;
    float speedFactore = 1.0f;
    float sizeFactore = 1.0f;
    public int clickCounter = 0;
    float lastPitch = -500.0f;
    float lastRoll = -500.0f;
    int maxMovment = AppConfig.phoneMaxMovment;
    int movmentFactor = AppConfig.movmentFactor;
    String imageName = "-1";
    boolean destroyWhenPause = true;
    boolean firstResume = false;

    void enableGlitter() {
        ParticleSystem particleSystem = this.glitterPS;
        if (particleSystem != null) {
            particleSystem.enabled = true;
        } else {
            initGlitterEffect();
        }
    }

    void disableGlitter() {
        ParticleSystem particleSystem = this.glitterPS;
        if (particleSystem != null) {
            particleSystem.enabled = false;
        }
    }

    void initSound() {
        SoundPool soundPool = new SoundPool(5, 3, 0);
        this.soundPool = soundPool;
        this.effectId = soundPool.load(this, R.raw.fat, 1);
    }

    
    void playEfffectSound() {
        new CountDownTimer(200L, 100L) { 
            @Override 
            public void onTick(long j) {
            }

            @Override 
            public void onFinish() {
                ActivityApply.this.soundPool.play(ActivityApply.this.effectId, 1.0f, 1.0f, 0, 0, 0.0f);
            }
        }.start();
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.apply_menu_no_glitter, menu);
        ImageView imageView = (ImageView) menu.findItem(R.id.op_randomize).getActionView();
        this.randomize = imageView;
        imageView.setImageResource(R.drawable.ic_random);
        this.randomize.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityApply.this.randomize();
                view.setRotation(-360.0f);
                view.animate().rotation(0.0f).setDuration(700L).start();
            }
        });
        return true;
    }

    private boolean hasGlitter() {
        try {
            String[] list = getAssets().list("glitterEffect");
            if (list != null) {
                if (list.length != 0) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.op_randomize) {
            randomize();
            menuItem.getActionView().setRotation(-360.0f);
            menuItem.getActionView().animate().rotation(0.0f).setDuration(700L).start();
        } else if (menuItem.getItemId() == R.id.rateUs) {
            showRateUsDialog(false, true, this);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    void fixIconSize(int i) {
        findViewById(i).getLayoutParams().height = Uscreen.width / 11;
    }

    void initDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0.0f);
        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawer, toolbar, R.string.app_name, R.string.app_name);
        this.drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        toolbar.setTitle(getString(R.string.preview));
    }

    @Override
    
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_privacy) {
            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com"));
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent2.resolveActivity(getPackageManager()) != null) {
                startActivity(intent2);
            } else {
                Toast.makeText(this, "no app found", Toast.LENGTH_LONG).show();
            }
        } else if (itemId == R.id.nav_Rate_us) {
            showRateUsDialog(false, true, this);
        } else if (itemId == R.id.nav_share) {
            Intent intent3 = new Intent();
            intent3.setAction("android.intent.action.SEND");
            intent3.putExtra("android.intent.extra.TEXT", getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + getPackageName());
            intent3.setType("text/plain");
            startActivity(Intent.createChooser(intent3, null));
        }
        return true;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.e("tag1", "apply thread id :" + Thread.currentThread().getId());
        initSound();
        setContentView(R.layout.layout_main);

        initDrawer();
        Uscreen.Init(this);
        fixIconSize(R.id.i1);
        fixIconSize(R.id.i2);
        fixIconSize(R.id.i3);
        fixIconSize(R.id.i4);
        fixIconSize(R.id.i5);
        fixIconSize(R.id.i6);
        if (PrefLoader.LoadPref("tutoShowed", this) == 0) {
            findViewById(R.id.bg).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { 
                @Override 
                public void onGlobalLayout() {
                    ActivityApply.this.findViewById(R.id.bg).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ActivityApply.this.showTutoWallpaper();
                }
            });
        }
        this.glitterSwitch = (Switch) findViewById(R.id.glitterSwitch);
        this.speed = (SeekBar) findViewById(R.id.speed);
        this.count = (SeekBar) findViewById(R.id.count);
        this.size = (SeekBar) findViewById(R.id.size);
        this.switch1 = (Switch) findViewById(R.id.switch1);
        this.optionsLayout = findViewById(R.id.optionsLayout);
        this.buttonsLayout = findViewById(R.id.buttonsLayout);
        this.liveWallpaperView = (LiveWallpaperViewUgl) findViewById(R.id.liveWallpaperView);
        if (PrefLoader.LoadPref("glitterSwitch", this) == 1) {
            this.glitterSwitch.setChecked(true);
        } else {
            this.glitterSwitch.setChecked(false);
        }
        this.glitterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { 
            @Override 
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ActivityApply.this.switchGlitter(z);
                if (z) {
                    PrefLoader.SavePref("glitterSwitch", "1", ActivityApply.this);
                } else {
                    PrefLoader.SavePref("glitterSwitch", "0", ActivityApply.this);
                }
            }
        });
        this.liveWallpaperView.setClickEnabled(false);
        this.optionsLayout.setVisibility(View.GONE);
        this.count.setMax(Integer.parseInt(getString(R.string.particleCountMax)));
        this.speed.setMax(Integer.parseInt(getString(R.string.particleSpeedMax)));
        this.size.setMax(Integer.parseInt(getString(R.string.particleSizeMax)));
        this.count.setProgress(PrefLoader.LoadPref("count", this));
        this.speed.setProgress(PrefLoader.LoadPref("speed", this));
        this.size.setProgress(PrefLoader.LoadPref("size", this));
        this.switch1.setChecked(PrefLoader.LoadPref("border", this) == 1);
        this.sizeFactore = (this.size.getProgress() + 30.0f) / 100.0f;
        this.speedFactore = (this.speed.getProgress() + 30.0f) / 100.0f;
        this.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { 
            @Override 
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PrefLoader.SavePref("border", z ? "1" : "0", ActivityApply.this);
                ActivityApply.this.optionsChanged();
            }
        });
        this.speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            @Override 
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PrefLoader.SavePref("speed", i + "", ActivityApply.this);
                ActivityApply.this.optionsChanged();
            }
        });
        this.count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            @Override 
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PrefLoader.SavePref("count", i + "", ActivityApply.this);
                ActivityApply.this.optionsChanged();
            }
        });
        this.size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            @Override 
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PrefLoader.SavePref("size", i + "", ActivityApply.this);
                ActivityApply.this.optionsChanged();
            }
        });
        View findViewById = findViewById(R.id.wallpapers);
        this.wallpapersButton = findViewById;
        findViewById.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityApply.this.clickCounter++;


                ActivityApply.this.startActivityForResult(new Intent(ActivityApply.this, ActivityWallpaperViewer.class), 1);

            }
        });
        findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {

                ActivityApply.this.apply();

            }
        });
        View findViewById2 = findViewById(R.id.particles);
        this.particlesButton = findViewById2;
        findViewById2.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityApply.this.clickCounter++;

                ActivityApply.this.showParticleChooser();

            }
        });
        View findViewById3 = findViewById(R.id.behavior);
        this.behaviorButton = findViewById3;
        findViewById3.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (PrefLoader.LoadPref(Statics.lastSelectedParticlePref, ActivityApply.this) == 0) {
                    ActivityApply.this.showBehaviorChooser();
                    return;
                }
                ActivityApply.this.clickCounter++;

                ActivityApply.this.showBehaviorChooser();

            }
        });
        View findViewById4 = findViewById(R.id.options);
        this.optionsButton = findViewById4;
        findViewById4.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityApply.this.showOptionsLayout();
            }
        });
        View findViewById5 = findViewById(R.id.clickEffect);
        this.clickEffectButton = findViewById5;
        findViewById5.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityApply.this.showTFChooser();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ActivityApply.this.showButtonsLayout();
            }
        });
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("rateUs")) {
            showRateUsDialog(false, false, this);
        }
        initLayerManager();
        if (hasGlitter()) {
            MovmentUtils movmentUtils = new MovmentUtils(this);
            this.movment = movmentUtils;
            movmentUtils.startListening(new MovmentUtils.Listener() { 
                @Override 
                public void onOrientationChanged(float f, float f2) {
                    if (ActivityApply.this.lastPitch == -500.0f) {
                        ActivityApply.this.lastPitch = f;
                    }
                    if (ActivityApply.this.lastRoll == -500.0f) {
                        ActivityApply.this.lastRoll = f2;
                    }
                    float abs = Math.abs(ActivityApply.this.lastPitch - f);
                    float abs2 = Math.abs(ActivityApply.this.lastRoll - f2);
                    if (abs <= abs2) {
                        abs = abs2;
                    }
                    MovmentUtils.additionalTime = (long) (((float) MovmentUtils.additionalTime) + abs);
                    ActivityApply.this.lastPitch = f;
                    ActivityApply.this.lastRoll = f2;
                    if (ActivityApply.this.glitterPS != null) {
                        long j = (long) (abs * ActivityApply.this.movmentFactor);
                        if (j > ActivityApply.this.maxMovment) {
                            j = ActivityApply.this.maxMovment;
                        }
                        ActivityApply.this.glitterPS.forwardTimeBy(j);
                    }
                }
            });
        }
    }

    
    public void apply() {
        Log.e("apply", " " + MyWallpaperService.class.getName());
        this.showRateUsOnResume = true;
        try {
            Log.e("apply", "applying");
            ComponentName componentName = new ComponentName(this, OpenGLES2WallpaperService.class);
            Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
            startActivityForResult(intent, 101);
        } catch (ActivityNotFoundException e) {
            Log.e("apply", "applying faild");
            Log.e("apply", "could not find activity for livewallpaper on this device " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "this device does not support live wallpaper !", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceType")
    public boolean showRateUsDialog(final boolean z, boolean z2, final basActivity basactivity) {
        if ((PrefLoader.LoadPref("rateUsNever", basactivity) == 1 || PrefLoader.LoadPref("rated", basactivity) == 1) && !z2) {
            return false;
        }
        final Dialog dialog = new Dialog(basactivity);
        dialog.setContentView(R.layout.rate_us_layout);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.show();
        Uscreen.Init(basactivity);
        ViewGroup.LayoutParams layoutParams = dialog.findViewById(R.id.bg).getLayoutParams();
        double d = Uscreen.width;
        Double.isNaN(d);
        layoutParams.width = (int) (d * 0.85d);
        dialog.findViewById(R.id.rateUs).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                AppConfig.OpenAppForRating(ActivityApply.this);
                PrefLoader.SavePref("rated", "1", ActivityApply.this);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.later).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                dialog.dismiss();
                if (z) {
                    basactivity.finish();
                }
            }
        });
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                dialog.dismiss();
                if (z) {
                    basactivity.finish();
                }
            }
        });
        ((RatingBar) dialog.findViewById(R.id.ratingBar)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() { 
            @Override 
            public void onRatingChanged(RatingBar ratingBar, float f, boolean z3) {

                if (f >= 4.0f && z3) {
                    AppConfig.OpenAppForRating(ActivityApply.this);
                } else {
                    basActivity basactivity3 = ActivityApply.this;
                    Utoast.show(basactivity3, basactivity3.getString(R.string.thanksForRating), Utoast.ToastType.sucess);
                }
                PrefLoader.SavePref("rated", "1", ActivityApply.this);
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() { 
            @Override 
            public void onCancel(DialogInterface dialogInterface) {
                if (z) {
                    basactivity.finish();
                }
            }
        });
        return true;
    }

    
    public void switchGlitter(boolean z) {
        if (z) {
            if (this.layerManager != null) {
                disableParticleEffect();
            }
            enableGlitter();
            return;
        }
        disableGlitter();
        if (this.layerManager != null) {
            enableParticleEffect();
        }
    }

    private void disableParticleEffect() {
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            layerManager.touchEffectEnabled = false;
            if (this.layerManager.getParticleSystem() != null) {
                this.layerManager.getParticleSystem().disable();
            }
        }
        disableView(this.behaviorButton);
        disableView(this.optionsButton);
        disableView(this.particlesButton);
        disableView(this.clickEffectButton);
    }

    void disableView(View view) {
        if (view == null) {
            return;
        }
        view.setAlpha(0.4f);
        view.setEnabled(false);
    }

    void enableView(View view) {
        if (view == null) {
            return;
        }
        view.setAlpha(1.0f);
        view.setEnabled(true);
    }

    private void enableParticleEffect() {
        LayerManager layerManager = this.layerManager;
        if (layerManager != null) {
            layerManager.touchEffectEnabled = true;
            if (this.layerManager.getParticleSystem() != null) {
                this.layerManager.getParticleSystem().enable();
            }
        }
        enableView(this.behaviorButton);
        enableView(this.optionsButton);
        enableView(this.particlesButton);
        enableView(this.clickEffectButton);
    }

    
    public void randomize() {
        destroyLayerManager();
        int size = AssetsLoader.getParticlesSimple().size();
        int size2 = AssetsLoader.getListOfTouchEffects().size();
        int size3 = AppConfig.listAssetsWallpapers().size();
        int size4 = AppConfig.listBehaviors().size();
        double random = Math.random();
        double d = size;
        Double.isNaN(d);
        int i = (int) (random * d);
        double random2 = Math.random();
        double d2 = size2;
        Double.isNaN(d2);
        int i2 = (int) (random2 * d2);
        double random3 = Math.random();
        double d3 = size3;
        Double.isNaN(d3);
        int i3 = (int) (random3 * d3);
        double random4 = Math.random();
        double d4 = size4;
        Double.isNaN(d4);
        int i4 = (int) (random4 * d4);
        Log.e("tag2", "max numvbers " + size + "/" + size2 + "/" + size3 + "/" + size4);
        Log.e("tag2", "generated numbers " + i + "/" + i2 + "/" + i3 + "/" + i4);
        String str = Statics.lastSelectedParticlePref;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(i);
        PrefLoader.SavePref(str, sb.toString(), this);
        PrefLoader.SavePref(Statics.lastSelectedTouchEffectPref, "" + i2, this);
        PrefLoader.SavePref(Statics.lastSelectedWallpaperPref, "" + i3, this);
        PrefLoader.SavePref(Statics.lastSelectedBehaviorPref, "" + i4, this);
        playEfffectSound();
        showRandomizeEffect();
        initLayerManager();
    }

    void showRandomizeEffect() {
        View findViewById = findViewById(R.id.randomizeEffect);
        View findViewById2 = findViewById(R.id.randomizeEffect2);
        findViewById.setScaleX(2.0f);
        findViewById.setScaleY(2.0f);
        findViewById.setAlpha(1.0f);
        findViewById2.setScaleX(2.0f);
        findViewById2.setScaleY(2.0f);
        findViewById2.setAlpha(1.0f);
        findViewById.animate().scaleY(0.0f).scaleX(0.0f).setDuration(600L).start();
        findViewById2.animate().scaleY(0.0f).scaleX(0.0f).setStartDelay(0L).setDuration(800L).start();
    }

    void showTutoWallpaper() {
        if (this.buttonsLayout.getVisibility() != View.VISIBLE) {
            showButtonsLayout();
        }
        PrefLoader.SavePref("tutoShowed", "1", this);
        new GuideView.Builder(this).setTitle("Wallpapers").setContentText("Wallpapers gallery").setGravity(Gravity.auto).setDismissType(DismissType.anywhere).setTargetView(findViewById(R.id.wallpapers)).setContentTextSize(12).setTitleTextSize(14).setGuideListener(new GuideListener() { 
            @Override 
            public void onDismiss(View view) {
                ActivityApply.this.showTutoParticles();
            }
        }).build().show();
    }

    void showTutoParticles() {
        new GuideView.Builder(this).setTitle("Particles").setContentText("Decorations gallery").setGravity(Gravity.auto).setDismissType(DismissType.anywhere).setTargetView(findViewById(R.id.particles)).setContentTextSize(12).setTitleTextSize(14).setGuideListener(new GuideListener() { 
            @Override 
            public void onDismiss(View view) {
                ActivityApply.this.showTutoAnimations();
            }
        }).build().show();
    }

    void showTutoAnimations() {
        new GuideView.Builder(this).setTitle("Animations").setContentText("Animations gallery").setGravity(Gravity.auto).setDismissType(DismissType.anywhere).setTargetView(findViewById(R.id.behavior)).setContentTextSize(12).setTitleTextSize(14).setGuideListener(new GuideListener() { 
            @Override 
            public void onDismiss(View view) {
                ActivityApply.this.showTutoTouchEffect();
            }
        }).build().show();
    }

    void showTutoRandom() {
        new GuideView.Builder(this).setTitle("Random").setContentText("Create Random Wallpaper").setGravity(Gravity.auto).setDismissType(DismissType.anywhere).setTargetView(this.randomize).setContentTextSize(12).setTitleTextSize(14).setGuideListener(new GuideListener() { 
            @Override 
            public void onDismiss(View view) {
            }
        }).build().show();
    }

    void showTutoTouchEffect() {
        new GuideView.Builder(this).setTitle("Touch Effects").setContentText("Magic touch effects").setGravity(Gravity.auto).setDismissType(DismissType.anywhere).setTargetView(findViewById(R.id.clickEffect)).setContentTextSize(12).setTitleTextSize(14).setGuideListener(new GuideListener() { 
            @Override 
            public void onDismiss(View view) {
                ActivityApply.this.showTutoApply();
            }
        }).build().show();
    }

    void showTutoApply() {
        new GuideView.Builder(this).setTitle("Apply").setContentText("Activate your live wallpaper").setGravity(Gravity.auto).setDismissType(DismissType.anywhere).setTargetView(findViewById(R.id.apply)).setContentTextSize(12).setTitleTextSize(14).setGuideListener(new GuideListener() { 
            @Override 
            public void onDismiss(View view) {
                ActivityApply.this.showTutoRandom();
            }
        }).build().show();
    }

    void initLayerManager() {
        this.speedFactore = PrefLoader.LoadPref("speed", this);
        float LoadPref = PrefLoader.LoadPref("size", this);
        this.sizeFactore = (LoadPref + 30.0f) / 100.0f;
        this.speedFactore = (this.speedFactore + 30.0f) / 100.0f;
        initPS();
        initTF();
        int LoadPref2 = PrefLoader.LoadPref(Statics.lastSelectedWallpaperPref, this);
        this.imageName = "-1";
        if (LoadPref2 != -1) {
            this.imageName = AppConfig.listAssetsWallpapers().get(LoadPref2);
        }
        String str = this.imageName;
        this.backgroundPath = str;
        loadBackground(str);
        this.layerManager.getParticleSystem().setSpeedFactore(this.speedFactore);
        this.layerManager.getParticleSystem().setSizeFactore(this.sizeFactore);
        this.layerManager.getParticleSystem().setParticleCount(this.count.getProgress());
        this.layerManager.getParticleSystem().checkInsideBoundByPS = true;
        this.layerManager.getParticleSystem().moveInsideBounds = this.switch1.isChecked();
    }

    void initPS() {
        LayerManager particlesSimple = AssetsLoader.getParticlesSimple(PrefLoader.LoadPref(Statics.lastSelectedParticlePref, this));
        this.layerManager = particlesSimple;
        particlesSimple.setBound(Uscreen.getBound(this));
        ParticleSystem particleSystem = this.layerManager.getParticleSystem();
        if (particleSystem != null) {
            if (particleSystem.getSpawner().particlesize != null) {
                this.layerManager.getParticleSystem().getSpawner().particlesize = new range(0.09d, 0.05d);
            }
            DialogHelper.setlayerComponants(AppConfig.listBehaviors().get(PrefLoader.LoadPref(Statics.lastSelectedBehaviorPref, this)), this.layerManager);
        }
        this.liveWallpaperView.setLayerManager(this.layerManager);
    }

    void initTF() {
        TouchEffectsBase touchEffectsBase = AssetsLoader.getListOfTouchEffects().get(PrefLoader.LoadPref(Statics.lastSelectedTouchEffectPref, this));
        touchEffectsBase.init(this);
        this.layerManager.setTouchEffect(touchEffectsBase, this);
        Log.e("liveW", "tfSelected is " + touchEffectsBase.title);
    }

    void optionsChanged() {
        this.sizeFactore = (this.size.getProgress() + 30.0f) / 100.0f;
        this.speedFactore = (this.speed.getProgress() + 30.0f) / 100.0f;
        LayerManager layerManager = this.layerManager;
        if (layerManager == null || layerManager.getParticleSystem() == null) {
            return;
        }
        Log.e("sizeFactore", this.sizeFactore + "");
        this.layerManager.getParticleSystem().setSizeFactore(this.sizeFactore);
        Log.e("particleCount", this.count.getProgress() + "");
        this.layerManager.getParticleSystem().setParticleCount(this.count.getProgress());
        Log.e("speedFactore", this.speedFactore + "");
        this.layerManager.getParticleSystem().setSpeedFactore(this.speedFactore);
        this.layerManager.getParticleSystem().moveInsideBounds = this.switch1.isChecked();
    }

    @Override 
    public void onBackPressed() {
        if (this.optionsLayout.getVisibility() == View.VISIBLE) {
            showButtonsLayout();
        } else {
            super.onBackPressed();
        }
    }

    void showOptionsLayout() {
        this.optionsLayout.setVisibility(View.VISIBLE);
        this.buttonsLayout.setVisibility(View.GONE);
        Log.e("tag1", "applyActivity showOptionsLayout");
    }

    void showButtonsLayout() {
        this.buttonsLayout.setVisibility(View.VISIBLE);
        this.optionsLayout.setVisibility(View.GONE);
        Log.e("tag1", "applyActivity showButtonsLayout");
    }

    void showTFChooser() {
        DialogHelper.showTouchEffectsChooser(this, new onReady2() { 
            @Override 
            public void ready(Object obj, Object obj2) {
                if (obj != null) {
                    TouchEffectsBase touchEffectsBase = (TouchEffectsBase) obj;
                    int intValue = ((Integer) obj2).intValue();
                    touchEffectsBase.init(ActivityApply.this);
                    ActivityApply.this.layerManager.setTouchEffect(touchEffectsBase, ActivityApply.this);
                    String str = Statics.lastSelectedTouchEffectPref;
                    PrefLoader.SavePref(str, intValue + "", ActivityApply.this);
                }
            }
        });
    }

    
    @Override
    
    public void onDestroy() {
        super.onDestroy();

        this.movment.destroy(this);
    }

    void showParticleChooser() {
        Intent intent = new Intent(this, ActivityParticlesViewer.class);
        intent.putExtra("imagePath", this.backgroundPath);
        startActivity(intent);
    }

    
    @Override
    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            if (i2 == -1) {
                intent.getData().toString().equals("-1");
            }
        } else if (i == 101 && i2 == -1) {
            saveWallpaperPrefs();
            Toast.makeText(this, "Wallpaper applied", Toast.LENGTH_LONG).show();
        }
    }

    private void saveWallpaperPrefs() {
        PrefLoader.SavePref("updateWallpaper", (PrefLoader.LoadPref("updateWallpaper", this) + 1) + "", this);
    }

    
    public void showBehaviorChooser() {
        if (PrefLoader.LoadPref(Statics.lastSelectedParticlePref, this) == 0) {
            Utoast.show(this, getString(R.string.select_particles), Utoast.ToastType.warning);
        } else if (this.layerManager == null) {
            showParticleChooser();
        } else {
            Intent intent = new Intent(this, ActivityEffectsViewer.class);
            intent.putExtra("imagePath", this.backgroundPath);
            ActivityEffectsViewer.simple = this.layerManager.m2clone();
            this.destroyWhenPause = false;
            startActivity(intent);
        }
    }

    
    @Override 
    public void onPause() {
        this.activityVisible = false;
        MovmentUtils movmentUtils = this.movment;
        if (movmentUtils != null) {
            movmentUtils.onPause();
        }
        CountDownTimer countDownTimer = this.c;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        destroyLayerManager();
        super.onPause();
    }

    private void destroyLayerManager() {
        LayerManager layerManager = this.liveWallpaperView.getLayerManager();
        Bitmap bitmap = this.bgBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.bgBitmap.recycle();
        }
        this.glitterPS = null;
        this.bgBitmap = null;
        if (layerManager != null) {
            layerManager.destroyAll();
        }
    }

    void loadBackground(final String str) {
        new Utask(Statics.wallpaperFolder + str, new Uprocess() { 
            @Override 
            public void onPreRun(Object obj) {
                WallpaperLayer wallpaper;
                if (ActivityApply.this.bgBitmap != null) {
                    ActivityApply.this.bgBitmap.recycle();
                    ActivityApply.this.bgBitmap = null;
                }
                if (ActivityApply.this.liveWallpaperView == null || ActivityApply.this.liveWallpaperView.getLayerManager() == null || (wallpaper = ActivityApply.this.liveWallpaperView.getLayerManager().getWallpaper()) == null) {
                    return;
                }
                wallpaper.wallpaper.image = null;
            }

            @Override 
            public Object onRun(Object obj) {
                Log.e("tag3", "loading background");
                if (str.equals("-1") || str == "-1") {
                    return ImageUtils2.loadImageFromStorage2("images", "wallpaper.jpg", ActivityApply.this);
                }
                ActivityApply activityApply = ActivityApply.this;
                return ImageUtils.getBitmapFromAsset(activityApply, "wallpapers/" + str);
            }

            @Override 
            public void onFinish(Object obj) {
                Log.e("tag3", "background loaded");
                ActivityApply.this.bgBitmap = (Bitmap) obj;
                if (ActivityApply.this.liveWallpaperView.getLayerManager() != null) {
                    WallpaperLayer wallpaper = ActivityApply.this.liveWallpaperView.getLayerManager().getWallpaper();
                    if (wallpaper == null) {
                        wallpaper = new WallpaperLayer(AssetsLoader.textureFromBitmap(ActivityApply.this.bgBitmap), Uscreen.getBound(ActivityApply.this), ActivityApply.this.backgroundPath);
                        ActivityApply.this.liveWallpaperView.getLayerManager().addLayer(0, wallpaper);
                    }
                    wallpaper.wallpaperPath = str;
                    if (wallpaper != null) {
                        UTexture textureFromBitmap = AssetsLoader.textureFromBitmap(ActivityApply.this.bgBitmap);
                        textureFromBitmap.preserveBMPPreview = true;
                        wallpaper.wallpaper.image = textureFromBitmap;
                        wallpaper.previewBitmap = textureFromBitmap;
                    }
                }
                if (PrefLoader.LoadPref("glitterSwitch", ActivityApply.this) == 1) {
                    ActivityApply.this.switchGlitter(true);
                }
            }
        });
    }

    private void initGlitterEffect() {
        this.glitterPS = GlitterHelper.initGlitterEffect(this.layerManager, this.imageName, this);
    }

    @Override
    
    public void onResume() {
        super.onResume();
        IRun iRun = this.resumeCallback;
        if (iRun != null) {
            iRun.run(null);
        }
        this.resumeCallback = null;
        this.activityVisible = true;

        MovmentUtils movmentUtils = this.movment;
        if (movmentUtils != null) {
            movmentUtils.onResume();
        }
        if (!this.firstResume) {
            this.firstResume = true;
        } else {
            initLayerManager();
        }
    }
}
