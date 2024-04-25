package com.tzh.mylibrary.util.voice;

import android.os.Handler;
import android.os.Looper;

public final class MainHandler extends Handler {

    private static volatile MainHandler sInstance;

    public static MainHandler getInstance() {
        if (null == sInstance) {
            synchronized (MainHandler.class) {
                if (null == sInstance) {
                    sInstance = new MainHandler();
                }
            }
        }
        return sInstance;
    }

    public void postRunnable(Runnable runnable) {
        sInstance.post(runnable);
    }


    private MainHandler() {
        super(Looper.getMainLooper());
    }
}
