package com.demo.lovelivewallpaper.Callbacks.whater;

import android.app.Activity;
import android.os.Bundle;


public class SurfaceViewActivity extends Activity {
    BallBounces ball;

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        BallBounces ballBounces = new BallBounces(this);
        this.ball = ballBounces;
        setContentView(ballBounces);
    }
}
