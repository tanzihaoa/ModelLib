package com.tzh.myapplication.service.auto;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Jason on 2017/9/10.
 */

public class TaskThread {

    static ExecutorService executorService;
    public interface TaskResult {
        void onEnd(Object obj);
    }

    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

    public static void onMain(long msec, final Runnable runnable) {
        Runnable run = () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                Log.i("task", e.toString());
            }
        };
        sMainHandler.postDelayed(run, msec);
    }

    public static void onMain(final Runnable runnable) {
        Runnable run = () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                Log.d("task", e.toString());
            }
        };
        sMainHandler.post(run);
    }

    public static void initThread(int count) {
        executorService = Executors.newFixedThreadPool(count);
    }

    public static void onThread(final Runnable runnable) {
        executorService.execute(runnable);

    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
