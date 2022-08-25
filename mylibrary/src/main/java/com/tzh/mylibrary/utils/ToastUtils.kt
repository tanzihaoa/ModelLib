package com.tzh.mylibrary.utils

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.tzh.mylibrary.R
import com.tzh.mylibrary.base.BaseApplication

class ToastUtils {
    private var toast: Toast? = null
    private var view: View? = null
    private var tv: TextView? = null

    private val handler = Handler(Looper.getMainLooper())

    @Synchronized
    private fun initToast(msg: CharSequence?, duration: Int) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast(BaseApplication.sContext)
        if (view == null) {
            view = LayoutInflater.from(BaseApplication.sContext)
                .inflate(R.layout.toast_custom_tv, null)
            tv = view as TextView?
        }
        tv?.text = if (TextUtils.isEmpty(msg)) "" else msg
        toast?.duration = duration
        toast?.view = view
        toast?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER, 0, 0)
        toast?.show()
    }

    /**
     * 短时间显示Toast
     */
    fun showShort(message: CharSequence?) {
        if (TextUtils.isEmpty(message)) return
        initToast(message, Toast.LENGTH_SHORT)
    }

    /**
     * 短时间显示Toast
     */
    fun showShortToast(message: CharSequence?) {
        if (TextUtils.isEmpty(message)) return
        initToast(message, Toast.LENGTH_SHORT)
    }

    /**
     * 短时间显示Toast
     */
    fun showToast(message: CharSequence?) {
        if (TextUtils.isEmpty(message)) return
        initToast(message, Toast.LENGTH_SHORT)
    }

    /**
     * 短时间显示Toast
     */
    fun showShort(strResId: Int) {
        initToast(strResId.toString() + "", Toast.LENGTH_SHORT)
    }

    /**
     * 长时间显示Toast
     */
    fun showLong(message: CharSequence?) {
        if (TextUtils.isEmpty(message)) return
        initToast(message, Toast.LENGTH_LONG)
    }

    fun showToThread(str: String?) {
        if (TextUtils.isEmpty(str)) {
            return
        }
        handler.post { showShort(str) }
    }
}