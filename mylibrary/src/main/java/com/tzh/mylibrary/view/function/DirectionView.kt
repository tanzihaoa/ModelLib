package com.tzh.mylibrary.view.function

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.tzh.mylibrary.R
import com.tzh.mylibrary.databinding.LayoutDirectionViewBinding
import com.tzh.mylibrary.util.ViewMarginsUtil
import com.tzh.mylibrary.util.bindingInflateLayout
import com.tzh.mylibrary.util.getColorInt

/**
 *
 */
class DirectionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mProgress = 60f // 进度值

    private val max = 100f// 最大进度值

    private var mFinishColor = 0
    private var mUnFinishColor = 0
    private var mTextColor = 0
    private var mHasLine = false
    private var mHasText = false
    private var direction = 0
    private var mDvCorners = 0
    private var mDvCornerTopLeft = 0
    private var mDvCornerTopRight = 0
    private var mDvCornerBottomLeft = 0
    private var mDvCornerBottomRight = 0

    private var mWidth : Int = 0
    private var mHeight : Int = 0

    init {
        attrs?.run {
            initView(this)
        }
    }

    var binding: LayoutDirectionViewBinding? = null

    private fun initView(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DirectionView)
        mProgress = attributes.getFloat(R.styleable.DirectionView_dv_progress, 60f)
        //设置完成进度条颜色
        mFinishColor = attributes.getColor(R.styleable.DirectionView_dv_finished_color, context.getColorInt(R.color.color_f15c4d))
        //设置未完成进度条颜色
        mUnFinishColor = attributes.getColor(R.styleable.DirectionView_dv_unfinished_color, context.getColorInt(R.color.color_f4f4f4))
        //设置字体颜色
        mTextColor = attributes.getColor(R.styleable.DirectionView_dv_text_color, context.getColorInt(R.color.color_333))
        //设置是否有边框
        mHasLine = attributes.getBoolean(R.styleable.DirectionView_dv_hasline, false)
        //设置是否有字体
        mHasText = attributes.getBoolean(R.styleable.DirectionView_dv_hasText, false)
        //设置方向
        direction = attributes.getInt(R.styleable.DirectionView_dv_direction, 3)
        //圆角
        mDvCorners = attributes.getDimensionPixelSize(R.styleable.DirectionView_dv_corners, 0)
        //左上圆角
        mDvCornerTopLeft = attributes.getDimensionPixelSize(R.styleable.DirectionView_dv_cornerTopLeft, 0)
        //左下圆角
        mDvCornerBottomLeft = attributes.getDimensionPixelSize(R.styleable.DirectionView_dv_cornerBottomLeft, 0)
        //右上圆角
        mDvCornerTopRight = attributes.getDimensionPixelSize(R.styleable.DirectionView_dv_cornerTopRight, 0)
        //右下圆角
        mDvCornerBottomRight = attributes.getDimensionPixelSize(R.styleable.DirectionView_dv_cornerBottomRight, 0)


        attributes.recycle()
        binding = bindingInflateLayout<LayoutDirectionViewBinding>(R.layout.layout_direction_view).also {
            post {
                binding?.let {
                    it.unFinish.setShapeBackgroundColor(mUnFinishColor)
                    it.finish.setShapeBackgroundColor(mFinishColor)
                    if (mDvCorners == 0) {
                        it.unFinish.setShapeCorners(mDvCornerTopLeft.toFloat(), mDvCornerTopRight.toFloat(), mDvCornerBottomRight.toFloat(), mDvCornerBottomLeft.toFloat())
                        it.finish.setShapeCorners(mDvCornerTopLeft.toFloat(), mDvCornerTopRight.toFloat(), mDvCornerBottomRight.toFloat(), mDvCornerBottomLeft.toFloat())
                    } else {
                        it.unFinish.setShapeCorners(mDvCorners.toFloat())
                        it.finish.setShapeCorners(mDvCorners.toFloat())
                    }

                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
        changeUI()
    }

    fun setColor(@ColorRes color: Int) {
        mFinishColor = ContextCompat.getColor(context, color)
        binding?.finish?.setShapeBackgroundColor(mFinishColor)
    }

    fun setColorRes(@ColorRes color: Int) {
        mFinishColor = color
        binding?.finish?.setShapeBackgroundColor(mFinishColor)
    }

    fun getProgress(): Float {
        return mProgress
    }

    fun setProgress(progress: Float) {
        mProgress = progress
        changeUI()
    }

    private fun changeUI(){
        post {
            binding?.let {
                when (direction) {
                    0 -> {//
                        it.finish.let { mIt ->
                            val height = mHeight
                            val width = mWidth - (mWidth * (mProgress/max)).toInt()
                            ViewMarginsUtil.setMarginsToPx(mIt,0,0,width,0)
                        }
                    }
                    1 -> {
                        it.finish.let { mIt ->
                            val height = mHeight
                            val width = mWidth - (mWidth * (mProgress/max)).toInt()
                            ViewMarginsUtil.setMarginsToPx(mIt,width,0,0,0)
                        }
                    }
                    2 -> {
                        it.finish.let { mIt ->
                            val height = mHeight- (mHeight * (mProgress/max)).toInt()
                            val width = mWidth
                            ViewMarginsUtil.setMarginsToPx(mIt,0,0,0,height)
                        }
                    }
                    3 -> {
                        it.finish.let { mIt ->
                            val height = mHeight- (mHeight * (mProgress/max)).toInt()
                            val width = mWidth
                            ViewMarginsUtil.setMarginsToPx(mIt,0,height,0,0)
                        }
                    }
                    else->{

                    }
                }
            }
        }
    }

}