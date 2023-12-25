package com.demo.lovelivewallpaper.utils;

import UEnginePackage.Components.baseUpdater;
import UEnginePackage.Components.fadeUpdater;
import UEnginePackage.Components.positionUpdater;
import UEnginePackage.Components.scaleUpdater;
import UEnginePackage.Components.sizeUpdater;
import UEnginePackage.Models.UTexture;
import UEnginePackage.Models.Uparticle;
import UEnginePackage.Models.Uspawner;
import UEnginePackage.Models.Usprite;
import UEnginePackage.Models.cords;
import UEnginePackage.Models.range;
import UEnginePackage.UGL.Uimage;
import UEnginePackage.UGL.Urect;
import UEnginePackage.Utils.AppConfig;
import UEnginePackage.Models.layers.LayerManager;
import UEnginePackage.Models.layers.ParticleSystem;
import UEnginePackage.Models.layers.WallpaperLayer;
import UEnginePackage.Models.layers.baseLayer;
import UEnginePackage.touchEffectsPackage.TFMagicTouch;
import UEnginePackage.touchEffectsPackage.TFMagicTouchDirectional;
import UEnginePackage.touchEffectsPackage.TFPulForce;
import UEnginePackage.touchEffectsPackage.TFSparcles;
import UEnginePackage.touchEffectsPackage.TFWallpaperShreder;
import UEnginePackage.touchEffectsPackage.TouchEffectsBase;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import liveWallpaper.myapplication.App;

import org.json.JSONArray;
import org.json.JSONObject;


public class AssetsLoader {
    public static LayerManager loadLayers(LayerManager layerManager) {
        return loadLayers(layerManager, true);
    }

    public static LayerManager createSimpleLayerManagerWithPrtSystemFromImage(String str) {
        Uspawner createSimpleSpawner = createSimpleSpawner();
        LayerManager layerManager = new LayerManager("", Uscreen.getBound(App.c()));
        layerManager.addLayer(createSimpleParticleSystem(createSimpleSpawner, str));
        layerManager.loaded = true;
        return layerManager;
    }

    public static LayerManager createSimpleLayerManagerWithPrtSystemFromUimage(Uimage uimage) {
        Uspawner createSimpleSpawner = createSimpleSpawner();
        LayerManager layerManager = new LayerManager("", Uscreen.getBound(App.c()));
        layerManager.addLayer(createSimpleParticleSystem(createSimpleSpawner, "presetParticles", uimage));
        layerManager.loaded = true;
        return layerManager;
    }

    public static Uspawner createSimpleSpawner() {
        range rangeVar = new range(15.0d, 20.0d);
        rangeVar.max /= 100.0d;
        rangeVar.min /= 100.0d;
        return new Uspawner(rangeVar, new range(7000.0d, 2000.0d), new Urect(0.0d, 0.0d, 100.0d, 100.0d), 10);
    }

    public static ParticleSystem createSimpleParticleSystem(Uspawner uspawner, String str) {
        return createSimpleParticleSystem(uspawner, "presetParticles", str);
    }

    public static ParticleSystem createSimpleParticleSystem(Uspawner uspawner, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(createUimage(App.c(), str, str2));
        ParticleSystem particleSystem = new ParticleSystem(arrayList, Uscreen.getBound(App.c()), uspawner);
        particleSystem.paralax = 50;
        particleSystem.setBound(new Urect(0.0d, 0.0d, Uscreen.width, Uscreen.height));
        particleSystem.components = getSimplePrtComponants();
        particleSystem.moveInsideBounds = false;
        return particleSystem;
    }

    public static ParticleSystem createSimpleParticleSystem(Uspawner uspawner, String str, Uimage uimage) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(uimage);
        ParticleSystem particleSystem = new ParticleSystem(arrayList, Uscreen.getBound(App.c()), uspawner);
        particleSystem.paralax = 50;
        particleSystem.setBound(new Urect(0.0d, 0.0d, Uscreen.width, Uscreen.height));
        particleSystem.components = getSimplePrtComponants();
        particleSystem.moveInsideBounds = false;
        return particleSystem;
    }

    private static JSONArray getSimplePrtComponants() {
        try {
            return new JSONObject(LoadData("particleBehaviors/behavior1.json")).getJSONArray("componants");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<TouchEffectsBase> getListOfTouchEffects() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new TouchEffectsBase());
        arrayList.add(new TFWallpaperShreder());
        arrayList.add(new TFMagicTouch());
        arrayList.add(new TFSparcles());
        arrayList.add(new TFMagicTouchDirectional());
        arrayList.add(new TFPulForce());
        return arrayList;
    }

    public static JSONArray getComponant(String str) {
        try {
            return new JSONObject(LoadData(str)).getJSONArray("componants");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LayerManager loadLayers(LayerManager layerManager, boolean z) {
        parseLayerManager(LoadData(layerManager.filePath), layerManager);
        return layerManager;
    }

    public static LayerManager parseLayerManager(String str, LayerManager layerManager) {
        try {
            Uscreen.Init(App.c());
            JSONObject jSONObject = new JSONObject(str);
            layerManager.title = jSONObject.getString("title");
            JSONArray jSONArray = jSONObject.getJSONArray("layers");
            for (int i = 0; i < jSONArray.length(); i++) {
                layerManager.addLayer(parseLayer(jSONArray.getJSONObject(i)));
            }
            return layerManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static baseLayer parseLayer(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString("type");
            if (string.equals("wallpaper")) {
                return parseWallpaperLayer(jSONObject);
            }
            if (string.equals("particleSystem")) {
                return parseParticleLayer(jSONObject);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static WallpaperLayer parseWallpaperLayer(JSONObject jSONObject) {
        String str;
        Bitmap loadBitmap;
        try {
            int i = jSONObject.getInt("paralax");
            String string = jSONObject.getString("imagePath");
            if (string.indexOf("../") == 0) {
                str = string.replace("../", "");
                loadBitmap = loadBitmap(App.c(), str, true);
            } else {
                str = "wallpapers/" + string;
                loadBitmap = loadBitmap(App.c(), str, true);
            }
            WallpaperLayer wallpaperLayer = new WallpaperLayer(textureFromBitmap(loadBitmap), new Urect(0.0d, 0.0d, Uscreen.width, Uscreen.height), str);
            wallpaperLayer.paralax = i;
            return wallpaperLayer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Uimage> parsePrtcImages(JSONArray jSONArray, Context context) {
        try {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.addAll(createUimage(context, jSONArray.getJSONObject(i).getString("Path")));
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Uimage> createUimage(Context context, String str) {
        return createUimage(context, "presetParticles", str);
    }

    
    
    
    

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            
        }

        return bitmap;
    }


    public static List<Uimage> createUimage(Context context, String str, String str2) {

        ArrayList arrayList = new ArrayList();

        if(str.equals("presetParticles")){

            Bitmap bb = getBitmapFromAsset(context, "presetParticles/butterfly_1_200_10.png");
            int ww = 0;
            int hh = 0;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128,  128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));
            hh = hh + 128;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 128, 128))));

        } else if(str2.equals("stars_8.-s.png")){

            Bitmap bb = getBitmapFromAsset(context, str+"/"+str2);
            int ww = 0;
            int hh = 0;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));
            ww = ww + 31;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));
            ww = ww + 31;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));
            ww = ww + 31;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));
            ww = ww + 31;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));
            ww = ww + 31;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));
            ww = ww + 31;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));
            ww = ww + 31;
            arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bb, ww, hh, 31, 30))));

        }else if(str2.equals("circle.png")){

            Bitmap bb = getBitmapFromAsset(context, str2);
            Uimage uimage= new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(bb));
            uimage.image.setShiny(true);
            arrayList.add(uimage);

        }else{

            Bitmap bb = getBitmapFromAsset(context, str+"/"+str2);
            Uimage uimage= new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(bb));
           
            arrayList.add(uimage);

        }


        return arrayList;

    }

    private static List<Uimage> cropParticlesFromBitmap(Bitmap bitmap, int i, int i2) {
        int i3 = i;
        int i4 = i2;
        ArrayList arrayList = new ArrayList();
        int width = bitmap.getWidth() / i3;
        int height = bitmap.getHeight() / i4;
        if (i4 == 1 && i3 > 1) {
            for (int i5 = 0; i5 < i3; i5++) {
                arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bitmap, i5 * width, 0, width, height))));
            }
        } else if (i4 <= 1 || i3 != 1) {
            int i6 = 0;
            while (i6 < i4) {
                ArrayList arrayList2 = new ArrayList();
                for (int i7 = 0; i7 < i3; i7++) {
                    arrayList2.add(textureFromBitmap(Bitmap.createBitmap(bitmap, i7 * width, i6 * height, width, height)));
                }
                ArrayList arrayList3 = arrayList;
                arrayList3.add(new Usprite(0.0d, 0.0d, 0.0d, 0.0d, arrayList2, i, 0, 1, true, 0, 0));
                i6++;
                i3 = i;
                i4 = i2;
                arrayList = arrayList3;
                height = height;
                width = width;
            }
        } else {
            for (int i8 = 0; i8 < i4; i8++) {
                arrayList.add(new Uimage(0.0d, 0.0d, 0.0d, 0.0d, textureFromBitmap(Bitmap.createBitmap(bitmap, 0, i8 * height, width, height))));
            }
        }
        return arrayList;
    }

    public static UTexture textureFromBitmap(Bitmap bitmap) {
        return new UTexture(bitmap);
    }

    public static Bitmap loadBitmap(Context context, String str, boolean z) {
        Bitmap bitmapFromAsset = ImageUtils.getBitmapFromAsset(context, str);


        Log.e("strstrstr",""+str);
        if (z) {
            double width = bitmapFromAsset.getWidth();
            Double.isNaN(width);
            double height = bitmapFromAsset.getHeight();
            Double.isNaN(height);
            return Bitmap.createScaledBitmap(bitmapFromAsset, (int) (width * 0.3d), (int) (height * 0.3d), true);
        }
        return bitmapFromAsset;
    }

    public static Uspawner parseSpawner(JSONObject jSONObject) {
        try {
            range parse = range.parse(jSONObject.getString("p_size"));
            parse.max /= 100.0d;
            parse.min /= 100.0d;
            range parse2 = range.parse(jSONObject.getString("p_life"));
            int i = jSONObject.getInt("max_live_particles");
            String[] split = jSONObject.getString("spawnBound").split(",");
            return new Uspawner(parse, parse2, new Urect(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])), i);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static ParticleSystem parseParticleLayer(JSONObject jSONObject) {
        try {
            int i = jSONObject.getInt("paralax");
            boolean z = jSONObject.getBoolean("moveInsideBounds");
            ParticleSystem particleSystem = new ParticleSystem(parsePrtcImages(jSONObject.getJSONArray("images"), App.c()), Uscreen.getBound(App.c()), parseSpawner(jSONObject.getJSONObject("spawner")));
            particleSystem.paralax = i;
            particleSystem.setBound(new Urect(0.0d, 0.0d, Uscreen.width, Uscreen.height));
            particleSystem.components = jSONObject.getJSONArray("componants");
            particleSystem.moveInsideBounds = z;
            return particleSystem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static baseUpdater parseComponant(JSONObject jSONObject, Uparticle uparticle, Urect urect) {
        try {
            String string = jSONObject.getString("type");
            if (string.equals("fade")) {
                return parseFadeUpdate(jSONObject, uparticle);
            }
            if (string.equals("move")) {
                return parsePositionUpdate(jSONObject, uparticle, urect);
            }
            if (string.equals("scal")) {
                return parseSizerUpdate(jSONObject, uparticle);
            }
            if (string.equals("scaler")) {
                return parsescalerUpdate(jSONObject, uparticle);
            }
            return null;
        } catch (Exception e) {
            Log.e("AssetsLoader", "parseComponant exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static fadeUpdater parseFadeUpdate(JSONObject jSONObject, Uparticle uparticle) {
        try {
            float f = jSONObject.getInt("from");
            float f2 = jSONObject.getInt("to");
            if (f2 != 0.0f) {
                f2 = (int) ((f2 / 100.0f) * 255.0f);
            }
            if (f != 0.0f) {
                f = (int) ((f / 100.0f) * 255.0f);
            }
            int i = jSONObject.getInt("duration");
            if (i == -3) {
                i = uparticle.life / 2;
            }
            int i2 = i;
            int i3 = jSONObject.getInt("startDelay");
            if (i3 == -1) {
                i3 = uparticle.life - i2;
            }
            return fadeUpdater.obtain(uparticle, f, f2, i2, i3, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static positionUpdater parsePositionUpdate(JSONObject jSONObject, Uparticle uparticle, Urect urect) {
        cords cordsVar;
        cords cordsVar2;
        try {
            range parse = range.parse(jSONObject.getString("xSpeed"));
            range parse2 = range.parse(jSONObject.getString("ySpeed"));
            String string = jSONObject.getString("movmentBehavior");
            parse.max = urect.getWidth() * (parse.max / 100.0d);
            parse.min = urect.getWidth() * (parse.min / 100.0d);
            parse2.max = urect.getHeight() * (parse2.max / 100.0d);
            parse2.min = urect.getHeight() * (parse2.min / 100.0d);
            cords cordsVar3 = new cords(0.0d, 0.0d, 0.0d);
            cords cordsVar4 = new cords(0.0d, 0.0d, 0.0d);
            boolean z = jSONObject.has("moveInsideBounds") ? jSONObject.getBoolean("moveInsideBounds") : false;
            boolean z2 = jSONObject.has("rotateWithMovment") ? jSONObject.getBoolean("rotateWithMovment") : false;
            if (jSONObject.has("velocity")) {
                cordsVar3 = cords.parse(jSONObject.getString("velocity"));
                cordsVar = cordsVar4;
                cordsVar3.x = urect.getWidth() * (cordsVar3.x / 100.0d);
                cordsVar3.y = urect.getWidth() * (cordsVar3.y / 100.0d);
            } else {
                cordsVar = cordsVar4;
            }
            cords cordsVar5 = cordsVar3;
            int i = jSONObject.has("rotationOffset") ? jSONObject.getInt("rotationOffset") : 0;
            if (jSONObject.has("randomize")) {
                cords parse3 = cords.parse(jSONObject.getString("randomize"));
                parse3.x = urect.getWidth() * (parse3.x / 100.0d);
                parse3.y = urect.getWidth() * (parse3.y / 100.0d);
                cordsVar2 = parse3;
            } else {
                cordsVar2 = cordsVar;
            }
            int i2 = jSONObject.getInt("duration");
            int i3 = jSONObject.getInt("startDelay");
            if (i2 == -2) {
                i2 = uparticle.life;
            }
            positionUpdater obtain = positionUpdater.obtain(uparticle, parse.getRandomValue(), parse2.getRandomValue(), i2, i3, false, parseMovmentBehavior(string), cordsVar5, cordsVar2, z, z2);
            obtain.rotationOffset = i;
            return obtain;
        } catch (Exception e) {
            Log.e("AssetsLoader", "parsePositionUpdate exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static int parseMovmentBehavior(String str) {
        if (str.indexOf("random") > -1) {
            return 0;
        }
        if (str.indexOf("randomDirectional") > -1) {
            return 1;
        }
        if (str.indexOf("directional") > -1) {
            return Integer.parseInt(str.split("-")[1]);
        }
        return 0;
    }

    public static sizeUpdater parseSizerUpdate(JSONObject jSONObject, Uparticle uparticle) {
        int i;
        try {
            double d = jSONObject.getDouble("fromScal") / 100.0d;
            double d2 = jSONObject.getDouble("toScal") / 100.0d;
            int i2 = jSONObject.getInt("duration");
            int i3 = jSONObject.getInt("startDelay");
            if (i2 == -2) {
                i2 = uparticle.life;
            } else if (i2 == -3) {
                i2 = uparticle.life / 2;
            }
            int i4 = i2;
            if (i3 == -2) {
                i = uparticle.life - i3;
            } else {
                if (i3 == -3) {
                    i3 = uparticle.life / 2;
                }
                i = i3;
            }
            return sizeUpdater.obtain(uparticle, d, d2, i4, i, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static scaleUpdater parsescalerUpdate(JSONObject jSONObject, Uparticle uparticle) {
        int i;
        try {
            double d = jSONObject.getDouble("from") / 100.0d;
            double d2 = jSONObject.getDouble("to") / 100.0d;
            int i2 = jSONObject.getInt("duration");
            int i3 = jSONObject.getInt("startDelay");
            if (i2 == -2) {
                i2 = uparticle.life;
            } else if (i2 == -3) {
                i2 = uparticle.life / 2;
            }
            int i4 = i2;
            if (i3 == -2) {
                i = uparticle.life - i3;
            } else {
                if (i3 == -3) {
                    i3 = uparticle.life / 2;
                }
                i = i3;
            }
            return scaleUpdater.obtain(uparticle, d, d2, i4, i, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String LoadData(String str) {



        try {
            InputStream open = App.c().getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr);
        } catch (IOException unused) {
            return "";
        }
    }

    public static List<LayerManager> getParticlesSimple() {
        ArrayList arrayList = new ArrayList();
        List<String> listAssetsParticles = AppConfig.listAssetsParticles();
        int i = 0;
        if (listAssetsParticles.size() == 2) {

            Log.e("getParticlesSimple", "if");
            arrayList.add(createSimpleLayerManagerWithPrtSystemFromImage(listAssetsParticles.get(0)));
            ((LayerManager) arrayList.get(0)).position = 0;
            arrayList.add(createSimpleLayerManagerWithPrtSystemFromImage(listAssetsParticles.get(1)));
            ((LayerManager) arrayList.get(1)).position = 1;
            List<Uimage> createUimage = createUimage(App.c(), "presetParticles", listAssetsParticles.get(1));
            while (i < createUimage.size()) {
                arrayList.add(createSimpleLayerManagerWithPrtSystemFromUimage(createUimage.get(i)));
                int i2 = i + 2;
                ((LayerManager) arrayList.get(i2)).position = i2;
                i++;
            }
        } else {

            Log.e("getParticlesSimple", "else");

            while (i < listAssetsParticles.size()) {
                arrayList.add(createSimpleLayerManagerWithPrtSystemFromImage(listAssetsParticles.get(i)));
                ((LayerManager) arrayList.get(i)).position = i;
                i++;
            }
        }


        return arrayList;
    }

    public static LayerManager getParticlesSimple(int i) {
        List<LayerManager> particlesSimple = getParticlesSimple();

        if (particlesSimple.size() == 0) {
            return null;
        }
        if (i >= particlesSimple.size()) {
            return particlesSimple.get(particlesSimple.size() - 1);
        }
        return particlesSimple.get(i);
    }


    public static LayerManager getParticlesSimple1(int i) {
        List<LayerManager> particlesSimple = getParticlesSimple1();

        if (particlesSimple.size() == 0) {
            return null;
        }
        if (i >= particlesSimple.size()) {
            return particlesSimple.get(particlesSimple.size() - 1);
        }
        return particlesSimple.get(i);
    }


    public static List<LayerManager> getParticlesSimple1() {
        ArrayList arrayList = new ArrayList();
        List<String> listAssetsParticles = AppConfig.listAssetsParticles();
        int i = 0;
        while (i < listAssetsParticles.size()) {
            arrayList.add(createSimpleLayerManagerWithPrtSystemFromImage(listAssetsParticles.get(i)));
            ((LayerManager) arrayList.get(i)).position = i;
            i++;
        }


        return arrayList;
    }


    static void expandIfOnlyOneParticleFound(List<LayerManager> list) {
        if (list.size() == 2) {
            if (list.get(1).getParticleSystem().hasOneImageMultipleParticles()) {
                expandParticle(1, list);
            } else if (list.get(1).getParticleSystem().hasOneImageMultipleSprites()) {
                expandParticle(1, list);
            }
        }
    }

    static void expandParticle(int i, List<LayerManager> list) {
        LayerManager layerManager = list.get(i);
        if (layerManager.getParticleSystem().hasOneImageMultipleParticles()) {
            int i2 = ((Usprite) layerManager.getParticleSystem().images.get(0)).frames;
            for (int i3 = 0; i3 < i2; i3++) {
                LayerManager m2clone = layerManager.m2clone();
                Usprite usprite = (Usprite) m2clone.getParticleSystem().images.get(0);
                usprite.currentFrame = i3;
                usprite.selectedFrame = i3;
                list.add(m2clone);
            }
        } else if (layerManager.getParticleSystem().hasOneImageMultipleSprites()) {
            int i4 = ((Usprite) layerManager.getParticleSystem().images.get(0)).rows;
            int i5 = 0;
            while (i5 < i4) {
                LayerManager m2clone2 = layerManager.m2clone();
                Usprite usprite2 = (Usprite) m2clone2.getParticleSystem().images.get(0);
                i5++;
                usprite2.currentRow = i5;
                usprite2.selectedRow = i5;
                list.add(m2clone2);
            }
        }
    }

    public static List<Uimage> getImagesInFolder(Context context, String str) {
        List<String> listFiles = AppConfig.listFiles(str);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < listFiles.size(); i++) {
            arrayList.addAll(createUimage(App.c(), str, listFiles.get(i)));
        }


       

        return arrayList;
    }
}
