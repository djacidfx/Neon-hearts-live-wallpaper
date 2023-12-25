package liveWallpaper.myapplication;

import com.demo.lovelivewallpaper.Callbacks.ParticleDieCallback;
import UEnginePackage.Models.Uparticle;
import UEnginePackage.Models.Uspawner;
import UEnginePackage.Models.range;
import UEnginePackage.UGL.RR;
import UEnginePackage.UGL.Uimage;
import UEnginePackage.UGL.Urect;
import UEnginePackage.Utils.AppConfig;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.demo.lovelivewallpaper.utils.AssetsLoader;
import com.demo.lovelivewallpaper.utils.DialogHelper;
import com.demo.lovelivewallpaper.utils.Uscreen;


public class GlitterHelper {
    
    public static ParticleSystem initGlitterEffect(LayerManager layerManager, String str, Context context) {
        float f;
        float f2;
        Bitmap bitmap;
        int i;
        List<Uimage> list;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        Log.e("tag3", " initGlitterEffect ");
        if (layerManager == null) {
            return null;
        }
        range rangeVar = new range(5.0d, 15.0d);
        rangeVar.max /= 100.0d;
        rangeVar.min /= 100.0d;
        Uspawner uspawner = new Uspawner(rangeVar, new range(7000.0d, 2000.0d), new Urect(0.0d, 0.0d, 100.0d, 100.0d), 0);
        List<Uimage> imagesInFolder = AssetsLoader.getImagesInFolder(App.c(), "glitterEffect/smallStars");
        List<Uimage> imagesInFolder2 = AssetsLoader.getImagesInFolder(App.c(), "glitterEffect/bigStars");
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(imagesInFolder);
        arrayList.addAll(imagesInFolder2);
        final ParticleSystem particleSystem = new ParticleSystem(arrayList, Uscreen.getBound(App.c()), uspawner);
        particleSystem.enabled = false;
        particleSystem.particleCount = 0;
        DialogHelper.setlayerComponants("", AppConfig.getGlitterBehavior(), particleSystem);
        layerManager.addLayer(particleSystem);
        if (imagesInFolder2 != null) {
            for (int i7 = 0; i7 < imagesInFolder2.size(); i7++) {
                imagesInFolder2.get(i7).shinyFilter = true;
                imagesInFolder2.get(i7).image.setShiny(true);
            }
        }
        if (imagesInFolder != null) {
            for (int i8 = 0; i8 < imagesInFolder.size(); i8++) {
                imagesInFolder.get(i8).shinyFilter = true;
                imagesInFolder.get(i8).image.setShiny(true);
            }
        }
        Log.e("tag3", "mask wallpaper :glitterEffect/wallpapersMasks/" + str);

      
        Bitmap loadBitmap = AssetsLoader.loadBitmap(context, "wallpapersMasks/" + str, false);


        if (loadBitmap == null && (loadBitmap = AssetsLoader.loadBitmap(context, "glitterEffect/wallpapersMasks/default.jpg", false)) == null) {
            Log.e("tag3", "default mask is null");
            return null;
        }
        Log.e("tag3", "mask size :" + loadBitmap.getWidth() + "/" + loadBitmap.getHeight());
        int width = loadBitmap.getWidth();
        int height = loadBitmap.getHeight();
        if (layerManager.getWallpaper() == null || layerManager.getWallpaper().wallpaper == null) {
            f = 0.0f;
            f2 = 0.0f;
        } else {
            Rect imageSizeAcordingToAspectRatio = layerManager.getWallpaper().wallpaper.getImageSizeAcordingToAspectRatio();
            f2 = imageSizeAcordingToAspectRatio.left;
            f = imageSizeAcordingToAspectRatio.top;
        }
        int i9 = 0;
        int i10 = 0;
        while (i9 < width) {
            int i11 = 0;
            while (i11 < height) {
                int pixel = loadBitmap.getPixel(i9, i11);
                if (Color.red(pixel) > 200 && Color.blue(pixel) > 200 && Color.green(pixel) > 200) {
                    double d = i9 / width;
                    double width2 = layerManager.bound.getWidth();
                    Double.isNaN(d);
                    double d2 = d * width2;
                    list = imagesInFolder;
                    double d3 = i11 / height;
                    double height2 = layerManager.bound.getHeight();
                    Double.isNaN(d3);
                    double d4 = d3 * height2;
                    if (f2 > 0.0f) {
                        double width3 = layerManager.bound.getWidth() / 2.0d;
                        if (d2 > width3) {
                            i2 = i11;
                            double d5 = f2;
                            Double.isNaN(d5);
                            d2 += ((width3 - (d2 - width3)) / width3) * d5;
                        } else {
                            i2 = i11;
                            if (d2 < width3) {
                                double d6 = (width3 - d2) / width3;
                                bitmap = loadBitmap;
                                i = width;
                                double d7 = f2;
                                Double.isNaN(d7);
                                d2 -= d6 * d7;
                            }
                        }
                        bitmap = loadBitmap;
                        i = width;
                    } else {
                        bitmap = loadBitmap;
                        i = width;
                        i2 = i11;
                        if (f > 0.0f) {
                            double height3 = layerManager.bound.getHeight() / 2.0d;
                            if (d4 > height3) {
                                double d8 = (height3 - (d4 - height3)) / height3;
                                double d9 = f;
                                Double.isNaN(d9);
                                d4 += d8 * d9;
                            } else if (d2 < height3) {
                                double d10 = (height3 - d4) / height3;
                                double d11 = f;
                                Double.isNaN(d11);
                                d4 -= d10 * d11;
                            }
                        }
                    }
                    Uparticle updateSpawner = particleSystem.updateSpawner(true, (float) d2, (float) d4);
                    if (updateSpawner != null) {
                        updateSpawner.setLeft(d2 - (updateSpawner.getWidth() / 2.0d));
                        updateSpawner.setTop(d4 - (updateSpawner.getHeight() / 2.0d));
                        if (updateSpawner.img != null) {
                            updateSpawner.img.shinyFilter = true;
                        }
                        i10++;
                    }
                } else {
                    bitmap = loadBitmap;
                    i = width;
                    list = imagesInFolder;
                    i2 = i11;
                    if (Color.red(pixel) > 100 && Color.blue(pixel) < 100 && Color.green(pixel) < 100) {
                        int i12 = i10 + 1;
                        double d12 = i9 / i;
                        double width4 = layerManager.bound.getWidth();
                        Double.isNaN(d12);
                        double d13 = d12 * width4;
                        i3 = i2;
                        double d14 = i3 / height;
                        double height4 = layerManager.bound.getHeight();
                        Double.isNaN(d14);
                        double d15 = d14 * height4;
                        if (f2 > 0.0f) {
                            double width5 = layerManager.bound.getWidth() / 2.0d;
                            if (d13 > width5) {
                                i4 = i;
                                double d16 = f2;
                                Double.isNaN(d16);
                                d13 += ((width5 - (d13 - width5)) / width5) * d16;
                            } else {
                                i4 = i;
                                if (d13 < width5) {
                                    double d17 = (width5 - d13) / width5;
                                    i5 = i9;
                                    i6 = i12;
                                    double d18 = f2;
                                    Double.isNaN(d18);
                                    d13 -= d17 * d18;
                                }
                            }
                            i5 = i9;
                            i6 = i12;
                        } else {
                            i4 = i;
                            i5 = i9;
                            i6 = i12;
                            if (f > 0.0f) {
                                double height5 = layerManager.bound.getHeight() / 2.0d;
                                if (d15 > height5) {
                                    double d19 = (height5 - (d15 - height5)) / height5;
                                    double d20 = f;
                                    Double.isNaN(d20);
                                    d15 += d19 * d20;
                                } else if (d13 < height5) {
                                    double d21 = (height5 - d15) / height5;
                                    double d22 = f;
                                    Double.isNaN(d22);
                                    d15 -= d21 * d22;
                                }
                            }
                        }
                        Uparticle updateSpawner2 = particleSystem.updateSpawner(true, (float) d13, (float) d15);
                        if (updateSpawner2 != null) {
                            updateSpawner2.img.shinyFilter = true;
                            updateSpawner2.setLeft(updateSpawner2.getRelativeLeft() - (updateSpawner2.getWidth() / 2.0d));
                            updateSpawner2.setTop(updateSpawner2.getRelativeTop() - (updateSpawner2.getHeight() / 2.0d));
                            updateSpawner2.setImage(imagesInFolder2.get(new Random().nextInt(imagesInFolder2.size())));
                            updateSpawner2.type = 1;
                        }
                        i10 = i6;
                        i11 = i3 + 1;
                        width = i4;
                        imagesInFolder = list;
                        loadBitmap = bitmap;
                        i9 = i5;
                    }
                }
                i3 = i2;
                i4 = i;
                i5 = i9;
                i11 = i3 + 1;
                width = i4;
                imagesInFolder = list;
                loadBitmap = bitmap;
                i9 = i5;
            }
            i9++;
            loadBitmap = loadBitmap;
        }
        final List<Uimage> list2 = imagesInFolder;
        particleSystem.setOnParticleDies(new ParticleDieCallback() { 
            @Override 
            public void particleDied(Uparticle uparticle) {
                uparticle.creationTime = MovmentUtils.getTimeMs();
                uparticle.life = ((int) (Math.random() * 9000.0d)) + 2000;
                uparticle.died = false;
                uparticle.componants.clear();
                particleSystem.addParticle(uparticle);
                if (uparticle.type == 0) {
                    if (list2.size() > 0) {
                        uparticle.setImage((Uimage) list2.get(RR.random.nextInt(list2.size())));
                        return;
                    }
                    return;
                }
                uparticle.life = ((int) (Math.random() * 9000.0d)) + 2000;
            }
        });
        Log.e("tag33", "spawned ps : " + i10);
        new CountDownTimer(1000L, 100L) { 
            @Override 
            public void onTick(long j) {
            }

            @Override 
            public void onFinish() {
                ParticleSystem particleSystem2 = particleSystem;
                if (particleSystem2 != null) {
                    particleSystem2.enabled = true;
                }
            }
        }.start();
        for (int i13 = 0; i13 < particleSystem.particles.size(); i13++) {
            particleSystem.particles.get(i13).img.image.setShiny(true);
        }
        return particleSystem;
    }
}
