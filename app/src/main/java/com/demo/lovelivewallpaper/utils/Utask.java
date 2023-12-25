package com.demo.lovelivewallpaper.utils;

import android.os.AsyncTask;


public class Utask extends AsyncTask<Object, Object, Object> {
    public Uprocess callback;
    public boolean canceled = false;
    public Object obj;

    public Utask(Object obj, Uprocess uprocess) {
        this.callback = null;
        this.callback = uprocess;
        this.obj = obj;
        execute(new Object[0]);
    }

    @Override 
    protected void onPreExecute() {
        super.onPreExecute();
        Uprocess uprocess = this.callback;
        uprocess.onPreRun(uprocess);
    }

    @Override 
    protected Object doInBackground(Object... objArr) {
        return this.callback.onRun(this.obj);
    }

    @Override 
    protected void onPostExecute(Object obj) {
        if (this.canceled) {
            return;
        }
        this.callback.onFinish(obj);
    }
}
