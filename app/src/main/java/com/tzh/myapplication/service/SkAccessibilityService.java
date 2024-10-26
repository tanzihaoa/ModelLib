package com.tzh.myapplication.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.app.NotificationCompat;
import com.tzh.myapplication.service.auto.AutoDataUtil;
import com.tzh.myapplication.service.auto.Helper.Analyze;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 收款无障碍服务
 */
public class SkAccessibilityService extends AccessibilityService{

    public static List<String> nodeList = new ArrayList<>();
    public static boolean l;
    public static boolean m;
    public final ExecutorService h;
    public volatile boolean useful;
    public volatile int flag;

    public SkAccessibilityService() {
        this.useful = false;
        this.flag = 0;
        this.h = Executors.newSingleThreadExecutor();
    }


    public static List<String> ergodicList(SkAccessibilityService autoService, AccessibilityNodeInfo nodeInfo) {
        Objects.requireNonNull(autoService);
        if (nodeInfo == null) return nodeList;
        if (nodeList.size() > 100) {
            nodeList.remove(0);
        }
        autoService.ergodic(nodeInfo);
        return nodeList;
    }

    public static void clear(SkAccessibilityService autoService) {
        autoService.useful = false;
        autoService.flag = 0;
    }

    public final void ergodic(AccessibilityNodeInfo nodeInfo) {
        int i;
        for (i = 0; i < nodeInfo.getChildCount() && nodeList != null && nodeList.size() <= 120; ++i) {
            AccessibilityNodeInfo nodeChild = nodeInfo.getChild(i);
            if (nodeChild != null && nodeChild.getChildCount() > 0) {
                ergodic(nodeChild);
            } else if (nodeChild != null && !TextUtils.isEmpty(nodeChild.getText()) && nodeList != null) {
                nodeList.add(nodeChild.getText().toString());
            }
        }
    }


    @Override  // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {


        String packageName = accessibilityEvent.getPackageName() == null ? "" : accessibilityEvent.getPackageName().toString();
        //获取监控范围数据
        String[] apps = AutoDataUtil.INSTANCE.getApps().split(",");
        if (!isIn(apps, packageName)){

            return;
        }
        //不再监控范围不管

        AccessibilityNodeInfo nodeInfo = null;
        try {
            nodeInfo = accessibilityEvent.getSource();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String className = "";
        if (accessibilityEvent.getClassName() != null) {
            className = accessibilityEvent.getClassName().toString();
        }


        int type = accessibilityEvent.getEventType();
        if (type == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            //获取Parcelable对象
            Parcelable data = accessibilityEvent.getParcelableData();
            //判断是否是Notification对象
            if (data instanceof Notification notification) {
                // Log.i("通知栏发生变化 > " + accessibilityEvent.getText().toString());
                Bundle extras = notification.extras;
                if (extras != null) {
                    String title = extras.getString(NotificationCompat.EXTRA_TITLE, "");
                    String content = extras.getString(NotificationCompat.EXTRA_TEXT, "");

                    String str = "title=" + title + ",content=" + content;

                }
            }
        } else {
            new Analyze(this, packageName, className, nodeInfo).run();
        }
    }

    @Override  // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override  // android.app.Service
    public void onDestroy() {
        this.stopForeground(true);
        super.onDestroy();
    }

    @Override  // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
    }

    @Override  // android.accessibilityservice.AccessibilityService
    public void onServiceConnected() {
        getApplicationContext().startService(new Intent(getApplicationContext(), AutoBillService.class));
    }

    private boolean isIn(String[] packages, String pack) {
        boolean flag = false;
        for (String package1 : packages) {
            if (pack.equals(package1)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
