package com.tzh.mylibrary.shapeview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.tzh.mylibrary.R

open class ShapeRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(
    context,
    attrs,
    defStyleAttr
) {


    val xBaseShape by lazy {  XBaseShape(this) }

    init {
        attrs?.run {
            xBaseShape.initShape(context.obtainStyledAttributes(attrs, R.styleable.ShapeRelativeLayout))
        }
    }

    fun setShapeBackgroundColorRes(@ColorRes resId: Int) {
        xBaseShape.setShapeBackgroundColorRes(resId)
    }

    /**
     * 设置渐变色，及方向
     */
    fun setShapeGradient(orientation: GradientDrawable.Orientation, @ColorRes startColorRes: Int, @ColorRes endColorRes: Int, @ColorRes centerColorRes: Int = android.R.color.transparent) {
        xBaseShape.setShapeGradient(orientation,startColorRes,endColorRes,centerColorRes)
    }

    fun setShapeBackgroundColorInt(@ColorInt color: Int) {
        xBaseShape.setShapeBackgroundColor(color)
    }

    @Deprecated("过时不用", ReplaceWith("xBaseShape.setShapeBorderColorRes(resId, borderWidth)"))
    @JvmOverloads
    fun setShapeBorderColor(@ColorRes resId: Int, borderWidth: Float = -1f) {
        xBaseShape.setShapeBorderColorRes(resId, borderWidth)
    }

    @JvmOverloads
    fun setShapeBorderColorRes(@ColorRes resId: Int, borderWidth: Float = -1f) {
        xBaseShape.setShapeBorderColorRes(resId, borderWidth)
    }


    fun setShapeCorners(cornet: Float) {
        xBaseShape.setShapeCorners(cornet)
    }

    fun setShapeCorners(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        xBaseShape.setShapeCorners(topLeft, topRight, bottomRight, bottomLeft)
    }

}