package com.tzh.mylibrary.shapeview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import com.tzh.mylibrary.R

open class ShapeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatEditText(
    context,
    attrs,
    defStyleAttr
) {


    val xBaseShape by lazy {  XBaseTextShape(this) }

    init {
        attrs?.run {
            xBaseShape.initShape(context.obtainStyledAttributes(attrs, R.styleable.ShapeEditText))
            isFocusable=true
            isFocusableInTouchMode=true
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

    @JvmOverloads
    fun setShapeBorderColor(@ColorRes resId: Int, borderWidth: Float = -1f) {
        xBaseShape.setShapeBorderColor(resId, borderWidth)
    }

    fun setShapeCorners(cornet: Float) {
        xBaseShape.setShapeCorners(cornet)
    }

    fun setShapeCorners(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        xBaseShape.setShapeCorners(topLeft, topRight, bottomRight, bottomLeft)
    }

}