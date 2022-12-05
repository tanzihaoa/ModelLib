package com.tzh.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityOptionsCompat;

import com.tzh.mylibrary.R;

import java.util.Stack;

public class XAppActivityManager {
    private static Stack<Activity> mActivityStack;
    private static XAppActivityManager mInstance;

    private XAppActivityManager() {
        mActivityStack = new Stack<>();
    }

    public static XAppActivityManager getInstance() {
        if (mInstance == null) {
            mInstance = new XAppActivityManager();
        }
        return mInstance;
    }

    /**
     * 将当前Activity推入栈中
     *
     * @param activity Acitivity
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获得当前栈顶Activity
     *
     * @return 当前栈顶Activity
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (null != mActivityStack) {
            if (!mActivityStack.empty()) {
                activity = mActivityStack.lastElement();
            }
        }
        return activity;
    }


    /**
     * 销毁单个activity
     *
     * @param activity 需要销毁的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 销毁单个activity
     *
     * @param cls 需要销毁的Activity Name
     */
    public void finishActivity(Class<?> cls) {
        if (mActivityStack == null) {
            return;
        }
        Activity closeActivity = null;
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (mActivityStack.get(i).getClass().equals(cls)) {
                closeActivity = mActivityStack.get(i);
                break;
            }

        }
        finishActivity(closeActivity);
    }

    /**
     * 从栈中移除某个Activity
     */
    public void removeActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        if (mActivityStack == null) {
            return;
        }
        mActivityStack.remove(activity);
    }

    /**
     * 退出栈中所有Activity,到指定的activity截止
     *
     * @param cls Activity
     */
    public void finishAllActivityExceptOne(Class cls) {
        //先循环找出来
        Activity activityExceptOne = null;
        for (int i = 0; i < mActivityStack.size(); i++) {
            Activity activity = mActivityStack.get(i);
            if (activity.getClass().getName().endsWith(cls.getName())) {
                activityExceptOne = activity;
            } else {
                activity.finish();
            }
        }

        if (activityExceptOne != null) {
            mActivityStack.clear();
            mActivityStack.add(activityExceptOne);
        }
    }

    /**
     * 退出栈中所有Activity,到指定的activity截止
     *
     * @param activity Activity
     */
    public void finishAllActivityExceptOne(Activity activity) {
        //先循环找出来
        Activity activityExceptOne = null;
        for (int i = 0; i < mActivityStack.size(); i++) {
            Activity activityStack = mActivityStack.get(i);
            if (activityStack == activity) {
                activityExceptOne = activity;
            } else {
                activityStack.finish();
            }
        }

        if (activityExceptOne != null) {
            mActivityStack.clear();
            mActivityStack.add(activityExceptOne);
        }
    }


    /**
     * 退出栈中所有的activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < mActivityStack.size(); i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 获取指定的activity
     *
     * @param cls Activity Name
     * @return
     */
    public Activity getActivityByClass(Class cls) {
        if (mActivityStack == null) {
            return null;
        }
        for (int i = 0; i < mActivityStack.size(); i++) {
            if (null != mActivityStack.get(i)) {
                Activity activity = mActivityStack.get(i);
                String name1 = activity.getClass().getName();
                String name2 = cls.getName();
                if (name1.equals(name2)) {
                    return activity;
                }
            }
        }
        return null;
    }


    /**
     * 判断当前的activity是当前的运行
     *
     * @param cls Activity名字
     * @return boolean
     */
    public boolean isCurrentActivity(Class cls) {
        String name1 = currentActivity().getClass().getName();
        String name2 = cls.getName();
        return name1.endsWith(name2);
    }

    /**
     * 动画打开Activity
     *
     * @param context
     * @param intent
     */
    public static void startActivityRtl(Context context, Intent intent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
        context.startActivity(intent, options.toBundle());
    }
}
