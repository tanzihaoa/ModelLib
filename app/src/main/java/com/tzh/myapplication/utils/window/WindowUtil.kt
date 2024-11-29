package com.tzh.myapplication.utils.window

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.tzh.myapplication.R
import com.tzh.mylibrary.shapeview.ShapeLinearLayout

/**
 * 悬浮窗管理
 */
class WindowUtil(val mContext: Context) {

    init {
        initView()
    }

    val windowManager by lazy {
        (mContext.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).apply {

        }
    }

    val mWindowParams by lazy {
        WindowManager.LayoutParams().apply {
            this.format = PixelFormat.TRANSPARENT
            // 设置悬浮窗不获取焦点的原因就是为了传递事件
            this.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            this.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            this.gravity = Gravity.CENTER_HORIZONTAL
            this.height = WindowManager.LayoutParams.WRAP_CONTENT
            this.width = WindowManager.LayoutParams.WRAP_CONTENT
        }
    }

    var view : View ?= null

    fun initView(){
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_window,null)
        view?.findViewById<ShapeLinearLayout>(R.id.layout)?.setOnClickListener {
            removeFloatWindow()
        }
    }

    fun showAsFloatingWindow() {
        windowManager.addView(view, mWindowParams) // 将悬浮窗添加到WindowManager中
    }

    fun removeFloatWindow(){
        windowManager.removeView(view)
    }
}