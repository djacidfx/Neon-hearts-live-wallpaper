package liveWallpaper.myapplication;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.WindowManager;


public class MovmentUtils implements SensorEventListener {
    private static final int SENSOR_DELAY_MICROS = 50000;
    public static long additionalTime;
    private int mLastAccuracy;
    private Listener mListener;
    private final Sensor mRotationSensor;
    private final SensorManager mSensorManager;
    private final WindowManager mWindowManager;
    boolean isLestening = false;
    public boolean paused = false;
    long lastTimeUpdated = 0;
    int timeBetweenUpdates = 50;

    
    public interface Listener {
        void onOrientationChanged(float f, float f2);
    }

    public static long getTimeMs() {
        return System.currentTimeMillis();
    }

    public void onPause() {
        this.paused = true;
    }

    public void onResume() {
        this.paused = false;
    }

    public void destroy(Context context) {
        if (this.isLestening) {
            this.mSensorManager.unregisterListener(this);
        }
    }

    public MovmentUtils(Activity activity) {
        this.mWindowManager = activity.getWindow().getWindowManager();
        SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        this.mSensorManager = sensorManager;
        this.mRotationSensor = sensorManager.getDefaultSensor(11);
    }

    public MovmentUtils(Service service) {
        this.mWindowManager = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
        SensorManager sensorManager = (SensorManager) service.getSystemService(Context.SENSOR_SERVICE);
        this.mSensorManager = sensorManager;
        this.mRotationSensor = sensorManager.getDefaultSensor(11);
    }

    public void startListening(Listener listener) {
        if (this.mListener == listener) {
            return;
        }
        this.mListener = listener;
        Sensor sensor = this.mRotationSensor;
        if (sensor == null) {
            Log.e("tag4", "Rotation vector sensor not available; will not provide orientation data.");
            return;
        }
        this.mSensorManager.registerListener(this, sensor, SENSOR_DELAY_MICROS);
        this.isLestening = true;
    }

    public void stopListening() {
        this.mSensorManager.unregisterListener(this);
        this.mListener = null;
    }

    @Override 
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (this.mLastAccuracy != i) {
            this.mLastAccuracy = i;
        }
    }

    @Override 
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.mListener == null || this.mLastAccuracy == 0 || sensorEvent.sensor != this.mRotationSensor) {
            return;
        }
        updateOrientation(sensorEvent.values);
    }

    private void updateOrientation(float[] fArr) {
        if (this.lastTimeUpdated + this.timeBetweenUpdates < System.currentTimeMillis()) {
            this.lastTimeUpdated = System.currentTimeMillis();
            if (this.paused) {
                return;
            }
            float[] fArr2 = new float[9];
            SensorManager.getRotationMatrixFromVector(fArr2, fArr);
            int rotation = this.mWindowManager.getDefaultDisplay().getRotation();
            int i = 131;
            int i2 = 129;
            if (rotation == 1) {
                i = 3;
            } else if (rotation == 2) {
                i = 129;
                i2 = 131;
            } else if (rotation != 3) {
                i = 1;
                i2 = 3;
            } else {
                i2 = 1;
            }
            float[] fArr3 = new float[9];
            SensorManager.remapCoordinateSystem(fArr2, i, i2, fArr3);
            float[] fArr4 = new float[3];
            SensorManager.getOrientation(fArr3, fArr4);
            this.mListener.onOrientationChanged(fArr4[1] * (-57.0f), fArr4[2] * (-57.0f));
        }
    }
}
