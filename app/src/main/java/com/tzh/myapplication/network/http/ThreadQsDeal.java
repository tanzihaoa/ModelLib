package com.tzh.myapplication.network.http;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadQsDeal {
    public ExecutorService widgetExecutor = null;
    public ExecutorService netGoExecutor = null;
    public ExecutorService appPreExecutor = null;

    public ThreadQsDeal() {
    }

    public static ThreadQsDeal getInstance() {
        return ThreadQsDeal.Holder.Instance;
    }

    public void set_widgetDeal_executors(Runnable var1) {
        ExecutorService var2;
        if ((var2 = this.widgetExecutor) == null || var2.isShutdown()) {
            this.widgetExecutor = this.create_qs_own_threadPool(100);
        }

        this.widgetExecutor.submit(var1);
    }

    public void set_netGoDeal_executors(Runnable var1) {
        ExecutorService var2;
        if ((var2 = this.netGoExecutor) == null || var2.isShutdown()) {
            this.netGoExecutor = this.create_qs_own_threadPool(200);
        }

        this.netGoExecutor.submit(var1);
    }

    public void set_appPre_executors(Runnable var1) {
        ExecutorService var2;
        if ((var2 = this.appPreExecutor) == null || var2.isShutdown()) {
            this.appPreExecutor = this.create_qs_own_threadPool(100);
        }

        this.appPreExecutor.submit(var1);
    }

    public ExecutorService create_qs_own_threadPool(int var1) {
        ArrayBlockingQueue var4;
        var4 = new ArrayBlockingQueue(200);
        ThreadFactory var2 = Executors.defaultThreadFactory();
        ThreadPoolExecutor.AbortPolicy var3;
        var3 = new ThreadPoolExecutor.AbortPolicy();
        int var10002 = var1;
        int var10003 = var1 + 1;
        TimeUnit var5 = TimeUnit.SECONDS;
        return new ThreadPoolExecutor(var10002, var10003, 30L, var5, var4, var2, var3);
    }

    private static class Holder {
        public static final ThreadQsDeal Instance = new ThreadQsDeal();

        public Holder() {
        }
    }
}
