package com.tzh.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs

class XImageView : AppCompatImageView {
    private var scaleX = 0.0
    private var scaleY = 0.0
    private var lenX = 0f
    private var lenY = 0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        scaleType = ScaleType.CENTER
    }

    override fun setScaleType(scaleType: ScaleType) {
        super.setScaleType(ScaleType.CENTER)
    }

    fun setGyroscopeManager(gyroscopeManager: GyroscopeManager?) {
        gyroscopeManager?.addView(this)
    }

    fun update(scaleX: Double, scaleY: Double) {
        this.scaleX = scaleX
        this.scaleY = scaleY
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width: Int =
            MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val height: Int =
            MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        if (drawable != null) {
            val drawableWidth: Int = drawable.intrinsicWidth
            val drawableHeight: Int = drawable.intrinsicHeight
            lenX = abs((drawableWidth - width) * 0.5f)
            lenY = abs((drawableHeight - height) * 0.5f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null || isInEditMode) {
            super.onDraw(canvas)
            return
        }
        val currentOffsetX = (lenX * scaleX).toFloat()
        val currentOffsetY = (lenY * scaleY).toFloat()
        canvas.save()
        canvas.translate(currentOffsetX, 0f)
        canvas.translate(0f, currentOffsetY)
        super.onDraw(canvas)
        canvas.restore()
    }
}