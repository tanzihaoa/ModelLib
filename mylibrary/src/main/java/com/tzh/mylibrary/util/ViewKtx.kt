package com.tzh.mylibrary.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

fun View?.setOnClickNoDouble(time: Int = 800, block: (view: View) -> Unit) {
    this ?: return
    var oldTime = System.currentTimeMillis()
    this.setOnClickListener {
        if (System.currentTimeMillis() > oldTime + time) {
            oldTime = System.currentTimeMillis()
            block(this)
        }
    }
}

/**
 * 设置图片 染色
 */
fun AppCompatImageView?.setImageTintColorRes(@ColorRes colorRes: Int) {
    this ?: return
    if(colorRes == -1){
        return
    }
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}


/**
 * 设置 text  资源Color
 */
fun TextView?.setTextColorRes(@ColorRes colorRes: Int) {
    this ?: return
    setTextColor(ContextCompat.getColor(context, colorRes))
}

/**
 * 设置 text  资源Color
 */
fun TextView?.setTextStyle(isBold: Boolean) {
    this ?: return
    paint.isFakeBoldText = isBold
    if (isBold) {
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    } else {
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }
}

inline fun <T : ViewGroup.LayoutParams> View?.updateMarginKtx(
    start: Float = -1f,
    top: Float = -1f,
    end: Float = -1f,
    bottom: Float = -1f,
    defaultParams: T? = null
) {
    this ?: return
    var params = layoutParams

    if (params == null && defaultParams != null) {
        params = defaultParams
    }
    (params as? ViewGroup.MarginLayoutParams)?.let { param ->
        val startF = if (start == -1f) paddingStart else DpToUtil.dip2px(context,start)
        val topF = if (top == -1f) paddingTop else DpToUtil.dip2px(context,top)
        val endF = if (end == -1f) paddingEnd else DpToUtil.dip2px(context,end)
        val bottomF = if (bottom == -1f) paddingBottom else DpToUtil.dip2px(context,bottom)

        param.setMargins(startF, topF, endF, bottomF)
        param.marginStart = startF
        param.marginEnd = endF
        layoutParams = param
    }
}

