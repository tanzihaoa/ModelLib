package com.tzh.mylibrary.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object AppKtx {
    /**
     * 获取状态栏高度
     *
     * return px
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 24
        val resId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                result.toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }
}