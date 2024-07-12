package com.tzh.myapplication.utils.window;

import android.app.Activity;

import java.util.List;

public interface ActivityState {
    /**
     * 得到当前Activity
     *
     * @return
     */
    Activity current();

    /**
     * 得到Activity集合
     *
     * @return
     */
    List<Activity> getActivityList();

    /**
     * 任务栈中Activity的总数
     *
     * @return
     */
    int count();

    /**
     * 判断应用是否处于前台，即是否可见
     *
     * @return
     */
    boolean isFront();
}
