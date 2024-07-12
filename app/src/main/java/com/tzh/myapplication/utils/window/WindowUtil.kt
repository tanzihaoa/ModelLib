package com.tzh.myapplication.utils.window

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.utils.PermissionXUtil
import com.tzh.myapplication.utils.OnPermissionCallBackListener


object WindowUtil {
    fun showAsFloatingWindow(activity: AppCompatActivity) {
        PermissionXUtil.requestAnyPermission(activity, Manifest.permission.SYSTEM_ALERT_WINDOW,object : OnPermissionCallBackListener{
            override fun onAgree() {
                val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
                params.gravity = Gravity.TOP or Gravity.START
                params.x = 100 // 初始位置的x坐标
                params.y = 100 // 初始位置的y坐标
                val floatingView = FrameLayout(activity)
                floatingView.setBackgroundColor(Color.BLUE) // 背景颜色只是为了更明显地看到悬浮窗
                val rootView = activity.window.decorView as ViewGroup
                val childView = rootView.getChildAt(0) // 获取到要缩小的视图
                rootView.removeView(childView) // 移除原来的视图
                floatingView.addView(childView) // 将原来的视图添加到悬浮窗中
                windowManager.addView(floatingView, params) // 将悬浮窗添加到WindowManager中

                // 处理悬浮窗的移除
                floatingView.setOnTouchListener(object : OnTouchListener {
                    private var startX = 0f
                    private var startY = 0f
                    private var dx = 0f
                    private var dy = 0f
                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                startX = event.rawX
                                startY = event.rawY
                            }

                            MotionEvent.ACTION_MOVE -> {
                                dx = event.rawX - startX
                                dy = event.rawY - startY
                                params.x += dx.toInt()
                                params.y += dy.toInt()
                                windowManager.updateViewLayout(floatingView, params)
                                startX = event.rawX
                                startY = event.rawY
                            }

                            MotionEvent.ACTION_UP -> {}
                        }
                        return true
                    }
                })
            }

            override fun onDisAgree() {

            }
        })
    }
}