package com.tzh.mylibrary.shapeview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.tzh.mylibrary.R

open class XBaseTextShape<T : TextView>(val view: T) {


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

    /**
     * 圆角
     */
    private var mTopLeft = 0f
    private var mBottomLeft = 0f
    private var mTopRight = 0f
    private var mBottomRight = 0f


    fun initShape(typedArray: TypedArray) {
        val transparentColor = ContextCompat.getColor(view.context, android.R.color.transparent)
        //边框厚度
        btBorderWidth =
            typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_shapeBorderWidth, 0)
        //边框颜色
        val btBorderColor =
            typedArray.getColor(R.styleable.ShapeTextView_shapeBorderColor, transparentColor)
        //背景颜色
        val btBackgroundColor =
            typedArray.getColor(R.styleable.ShapeTextView_shapeBackgroundColor, transparentColor)
        //背景颜色 渐变处理
        val btGradientStartColor =
            typedArray.getColor(R.styleable.ShapeTextView_shapeGradientStartColor, transparentColor)
        val btGradientCenterColor =
            typedArray.getColor(
                R.styleable.ShapeTextView_shapeGradientCenterColor,
                transparentColor
            )
        val btGradientEndColor =
            typedArray.getColor(R.styleable.ShapeTextView_shapeGradientEndColor, transparentColor)
        //渐变方向
        val btGradientAngle = typedArray.getInt(R.styleable.ShapeTextView_shapeGradientAngle, 6)
        //按钮弧度
        val btCorners = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_shapeCorners, 0)
        //按钮弧度-左上
        mTopLeft = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_shapeCornerTopLeft, 0).toFloat()
        mTopRight = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_shapeCornerTopRight, 0).toFloat()
        //按钮弧度-左下
        mBottomLeft = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_shapeCornerBottomLeft, 0).toFloat()
        //按钮弧度-右下
        mBottomRight = typedArray.getDimensionPixelSize(R.styleable.ShapeTextView_shapeCornerBottomRight, 0).toFloat()

        //文字状态颜色处理
        //按下
        val txtStatePressedColor =
            typedArray.getColor(R.styleable.ShapeTextView_textStatePressedColor, -1)
        //选择
        val txtStateSelectColor =
            typedArray.getColor(R.styleable.ShapeTextView_textStateSelectedColor, -1)
        //选中
        val txtStateCheckedColor =
            typedArray.getColor(R.styleable.ShapeTextView_textStateCheckedColor, -1)
        //启用
        val txtStateEnableColor =
            typedArray.getColor(R.styleable.ShapeTextView_textStateEnableColor, -1)
        //默认颜色
        val txtStateDefaultColor =
            typedArray.getColor(R.styleable.ShapeTextView_textStateDefaultColor, -1)
        typedArray.recycle()

        //背景颜色变化处理
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
        gradientDrawable.setStroke(btBorderWidth, btBorderColor)
        //弧度处理
        if (btCorners > 0) {
            gradientDrawable.cornerRadius = btCorners.toFloat()
        } else {
            gradientDrawable.cornerRadii = floatArrayOf(
                mTopLeft, mTopLeft,
                mTopRight, mTopRight,
                mBottomRight, mBottomRight,
                mBottomLeft, mBottomLeft
            )
        }

        view.background = gradientDrawable

        //文字颜色变化处理
        val txtStates = mutableListOf<IntArray>()
        val txtStateColors = mutableListOf<Int>()

        if (txtStatePressedColor != -1) {
            txtStates.add(intArrayOf(android.R.attr.state_pressed))
            txtStateColors.add(txtStatePressedColor)
        }
        if (txtStateSelectColor != -1) {
            txtStates.add(intArrayOf(android.R.attr.state_selected))
            txtStateColors.add(txtStateSelectColor)
        }
        if (txtStateCheckedColor != -1) {
            txtStates.add(intArrayOf(android.R.attr.state_checked))
            txtStateColors.add(txtStateCheckedColor)
        }
        if (txtStateEnableColor != -1) {
            txtStates.add(intArrayOf(android.R.attr.state_enabled))
            txtStateColors.add(txtStateEnableColor)
        }
        if (txtStateDefaultColor != -1) {
            txtStates.add(intArrayOf())
            txtStateColors.add(txtStateDefaultColor)
        }
        if (txtStates.size > 0 && txtStateColors.size > 0) {
            view.setTextColor(ColorStateList(txtStates.toTypedArray(), txtStateColors.toIntArray()))
        }
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
    fun setShapeCorners(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float, layoutDirection: Int = View.LAYOUT_DIRECTION_LTR) {
        mTopLeft = dip2px(view.context, topLeft).toFloat()
        mBottomLeft = dip2px(view.context, bottomLeft).toFloat()
        mTopRight = dip2px(view.context, topRight).toFloat()
        mBottomRight = dip2px(view.context, bottomRight).toFloat()

        //top-left, top-right, bottom-right, bottom-left
        gradientDrawable.cornerRadii = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
            floatArrayOf(
                mTopLeft, mTopLeft,
                mTopRight, mTopRight,
                mBottomRight, mBottomRight,
                mBottomLeft, mBottomLeft
            )
        } else {
            floatArrayOf(
                mTopRight, mTopRight,
                mTopLeft, mTopLeft,
                mBottomLeft, mBottomLeft,
                mBottomRight, mBottomRight
            )
        }
        view.background = gradientDrawable
    }

    /**
     * shape 圆角方向
     * 先简化处理一下
     */
    fun setShapeCornersDirection(layoutDirection: Int = View.LAYOUT_DIRECTION_LTR) {
        gradientDrawable.cornerRadii = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
            floatArrayOf(
                mTopLeft, mTopLeft,
                mTopRight, mTopRight,
                mBottomRight, mBottomRight,
                mBottomLeft, mBottomLeft
            )
        } else {
            floatArrayOf(
                mTopRight, mTopRight,
                mTopLeft, mTopLeft,
                mBottomLeft, mBottomLeft,
                mBottomRight, mBottomRight
            )
        }
    }


    /**
     * 设置边框颜色
     */
    fun setShapeBorderColor(@ColorRes resId: Int, borderWidth: Float = -1f) {
        val bWidth = if (borderWidth == -1f) {
            btBorderWidth
        } else {
            dip2px(view.context, borderWidth)
        }
        gradientDrawable.setStroke(bWidth, ContextCompat.getColor(view.context, resId))
        view.background = gradientDrawable
    }


}