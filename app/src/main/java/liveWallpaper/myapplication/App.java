package liveWallpaper.myapplication;

import android.app.Application;
import android.content.Context;


public class App extends Application {
    private static App myApp;

    @Override 
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override 
    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
    }

    @Override 
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }


    public static App getInstance() {
        return myApp;
    }

    public static App c() {
        return myApp;
    }

    @Override 
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}
