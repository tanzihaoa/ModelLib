package com.tzh.mylibrary.shapeview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.tzh.mylibrary.R

open class XBaseShape(val view: View) {

    companion object {
        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        private fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }

    val gradientDrawable = GradientDrawable()

    var btBorderWidth = 0


    fun initShape(typedArray: TypedArray) {
        val transparentColor = ContextCompat.getColor(view.context, android.R.color.transparent)
        //边框厚度
        btBorderWidth =
            typedArray.getDimensionPixelSize(R.styleable.XShapeCommonlyView_shapeBorderWidth, 0)
        //边框颜色
        val btBorderColor = typedArray.getColor(
            R.styleable.XShapeCommonlyView_shapeBorderColor,
            transparentColor
        )
        //背景颜色
        val btBackgroundColor =
            typedArray.getColor(R.styleable.ShapeTextView_shapeBackgroundColor, transparentColor)
        //背景颜色 渐变处理
        val btGradientStartColor =
            typedArray.getColor(
                R.styleable.XShapeCommonlyView_shapeGradientStartColor,
                transparentColor
            )
        val btGradientCenterColor =
            typedArray.getColor(
                R.styleable.XShapeCommonlyView_shapeGradientCenterColor,
                transparentColor
            )
        val btGradientEndColor =
            typedArray.getColor(
                R.styleable.XShapeCommonlyView_shapeGradientEndColor,
                transparentColor
            )
        //渐变方向
        val btGradientAngle =
            typedArray.getInt(R.styleable.XShapeCommonlyView_shapeGradientAngle, 6)

        //按钮弧度
        val btCorners = typedArray.getDimensionPixelSize(
            R.styleable.XShapeCommonlyView_shapeCorners,
            0
        )
        //按钮弧度-左上
        val btCornerTopLeft = typedArray.getDimensionPixelSize(
            R.styleable.XShapeCommonlyView_shapeCornerTopLeft,
            0
        )
        //按钮弧度-右上
        val btCornerTopRight = typedArray.getDimensionPixelSize(
            R.styleable.XShapeCommonlyView_shapeCornerTopRight,
            0
        )
        //按钮弧度-左下
        val btCornerBottomLeft = typedArray.getDimensionPixelSize(
            R.styleable.XShapeCommonlyView_shapeCornerBottomLeft,
            0
        )
        //按钮弧度-右下
        val btCornerBottomRight = typedArray.getDimensionPixelSize(
            R.styleable.XShapeCommonlyView_shapeCornerBottomRight,
            0
        )
        typedArray.recycle()

        gradientDrawable.shape = GradientDrawable.RECTANGLE
        //背景颜色处理
        val gradientColors = mutableListOf<Int>()
        if (btGradientStartColor != transparentColor) {
            gradientColors.add(btGradientStartColor)
        }
        if (btGradientCenterColor != transparentColor) {
            gradientColors.add(btGradientCenterColor)
        }
        if (btGradientEndColor != transparentColor) {
            gradientColors.add(btGradientEndColor)
        }

        if (gradientColors.size == 0) {
            //纯色
            gradientDrawable.setColor(btBackgroundColor)
        } else {
            //设置渐变颜色，最少2个颜色，少于2个颜色，则直接设置为背景色
            if (gradientColors.size == 1) {
                gradientDrawable.setColor(gradientColors[0])
            } else {
                //渐变
                gradientDrawable.colors = gradientColors.toIntArray()
                //渐变方向
                gradientDrawable.orientation = when (btGradientAngle) {
                    0 -> GradientDrawable.Orientation.TOP_BOTTOM
                    1 -> GradientDrawable.Orientation.TR_BL
                    2 -> GradientDrawable.Orientation.RIGHT_LEFT
                    3 -> GradientDrawable.Orientation.BR_TL
                    4 -> GradientDrawable.Orientation.BOTTOM_TOP
                    5 -> GradientDrawable.Orientation.BL_TR
                    6 -> GradientDrawable.Orientation.LEFT_RIGHT
                    7 -> GradientDrawable.Orientation.TL_BR
                    else -> GradientDrawable.Orientation.TOP_BOTTOM
                }
            }
        }

        //边框处理
        gradientDrawable.setStroke(
            btBorderWidth,
            btBorderColor
        )

        //弧度处理
        if (btCorners > 0) {
            gradientDrawable.cornerRadius = btCorners.toFloat()
        } else {
            //top-left, top-right, bottom-right, bottom-left
            gradientDrawable.cornerRadii = floatArrayOf(
                btCornerTopLeft.toFloat(), btCornerTopLeft.toFloat(),
                btCornerTopRight.toFloat(), btCornerTopRight.toFloat(),
                btCornerBottomRight.toFloat(), btCornerBottomRight.toFloat(),
                btCornerBottomLeft.toFloat(), btCornerBottomLeft.toFloat()

            )
        }
        view.background = gradientDrawable
    }

    /**
     * 设置渐变色，及方向
     */
    fun setShapeGradient(orientation: GradientDrawable.Orientation, @ColorRes startColorRes: Int, @ColorRes endColorRes: Int, @ColorRes centerColorRes: Int = android.R.color.transparent) {
        val transparentColor = ContextCompat.getColor(view.context, android.R.color.transparent)
        val startColor = ContextCompat.getColor(view.context, startColorRes)
        val centerColor = ContextCompat.getColor(view.context, android.R.color.transparent)
        val endColor = ContextCompat.getColor(view.context, endColorRes)

        //背景颜色处理
        gradientDrawable.colors = mutableListOf<Int>().apply {
            add(startColor)
            if (centerColor != transparentColor) {
                add(centerColor)
            }
            add(endColor)
        }.toIntArray()
        //渐变方向
        gradientDrawable.orientation = orientation

        view.background = gradientDrawable
    }


    fun setShapeBackgroundColorRes(@ColorRes resId: Int) {
        gradientDrawable.setColor(ContextCompat.getColor(view.context, resId))
        view.background = gradientDrawable
    }


    fun setShapeBackgroundColor(@ColorInt color: Int) {
        gradientDrawable.setColor(color)
        view.background = gradientDrawable
    }


    /**
     * 设置弧度
     */
    fun setShapeCorners(cornet: Float) {
        gradientDrawable.cornerRadius = dip2px(view.context, cornet).toFloat()
        view.background = gradientDrawable
    }

    /**
     * 设置弧度
     */
    fun setShapeCorners(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        val tLeft = dip2px(view.context, topLeft).toFloat()
        val bLeft = dip2px(view.context, bottomLeft).toFloat()
        val tRight = dip2px(view.context, topRight).toFloat()
        val bRight = dip2px(view.context, bottomRight).toFloat()
        //top-left, top-right, bottom-right, bottom-left
        gradientDrawable.cornerRadii = floatArrayOf(
            tLeft, tLeft,
            tRight, tRight,
            bRight, bRight,
            bLeft, bLeft
        )
        view.background = gradientDrawable
    }

    /**
     * 设置边框颜色
     */
    fun setShapeBorderColorRes(@ColorRes resId: Int, borderWidth: Float = -1f) {
        val bWidth = if (borderWidth == -1f) {
            btBorderWidth
        } else {
            dip2px(view.context, borderWidth)
        }
        gradientDrawable.setStroke(bWidth, ContextCompat.getColor(view.context, resId))
        view.background = gradientDrawable
    }


    /**
     * 设置边框颜色
     */
    fun setShapeBorderColor(@ColorInt color: Int, borderWidth: Float = -1f) {
        val bWidth = if (borderWidth == -1f) {
            btBorderWidth
        } else {
            dip2px(view.context, borderWidth)
        }
        gradientDrawable.setStroke(bWidth, color)
        view.background = gradientDrawable
    }

}