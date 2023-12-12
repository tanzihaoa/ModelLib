package com.tzh.myapplication.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tzh.myapplication.R;
import com.tzh.myapplication.base.MyApplication;

public class ToastUtil {
    private static Toast toast;
    private static View view;
    private static TextView tv;

    private static final Handler handler = new Handler(Looper.getMainLooper());

    @SuppressLint("InflateParams")
    private synchronized static void initToast(CharSequence msg, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(MyApplication.mContext);
        if (view == null) {
            view = LayoutInflater.from(MyApplication.mContext).inflate(R.layout.toast_custom_tv, null);
            tv = (TextView) view;
        }
        tv.setText(TextUtils.isEmpty(msg) ? "" : msg);
        toast.setDuration(duration);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);

        toast.show();
    }

    /**
     * 短时间显示Toast
     */
    public static void show(CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_SHORT);

    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     */
    public static void showShortToast(CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_SHORT);

    }

    /**
     * 短时间显示Toast
     */
    public static void showToast(CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_SHORT);

    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(int strResId) {
        initToast(strResId + "", Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_LONG);
    }

    public static void showToThread(final String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        handler.post(() -> showShort(str));
    }
}
