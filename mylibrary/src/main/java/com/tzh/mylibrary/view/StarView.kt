package com.tzh.mylibrary.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tzh.mylibrary.R
import com.tzh.mylibrary.databinding.LayoutStartViewBinding
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.bindingInflateLayout
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.util.updatePaddingKtx

/**
 * 星星控件
 */
class StarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var mStvImagePadding = 0
    private var mStvImageWidth = 0
    private var mType = 0
    var mListener : StvClickListener? = null
    init {
        attrs?.run {
            initView(this)
        }
    }

    var binding: LayoutStartViewBinding? = null

    private fun initView(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StarView)

        //间距
        mStvImagePadding = attributes.getDimensionPixelSize(R.styleable.StarView_stv_padding, 0)
        //图片宽高
        mStvImageWidth = attributes.getDimensionPixelSize(R.styleable.StarView_stv_image_width, 14)

        //星星的类型 0只显示 1可以点击 2显示背景图片但不能点击
        mType = attributes.getInt(R.styleable.StarView_stv_type,0)
        LogUtils.e("StarView", "$mType====0")

        attributes.recycle()
        binding = bindingInflateLayout<LayoutStartViewBinding>(R.layout.layout_start_view).also {

            it.layout2.updatePaddingKtx(mStvImagePadding.toFloat())
            it.layout3.updatePaddingKtx(mStvImagePadding.toFloat())
            it.layout4.updatePaddingKtx(mStvImagePadding.toFloat())
            it.layout5.updatePaddingKtx(mStvImagePadding.toFloat())

            if(mType == 0){
                it.startBg1.visibility = GONE
                it.startBg2.visibility = GONE
                it.startBg3.visibility = GONE
                it.startBg4.visibility = GONE
                it.startBg5.visibility = GONE
            }else{
                it.startBg1.visibility = VISIBLE
                it.startBg2.visibility = VISIBLE
                it.startBg3.visibility = VISIBLE
                it.startBg4.visibility = VISIBLE
                it.startBg5.visibility = VISIBLE
            }
            setImageWidth(it)
            it.layout1.setOnClickNoDouble {
                if(mType == 1){
                    setStartNum(1f)
                    mListener?.click(1)
                }
            }
            it.layout2.setOnClickNoDouble {
                if(mType == 1){
                    setStartNum(2f)
                    mListener?.click(2)
                }
            }
            it.layout3.setOnClickNoDouble {
                if(mType == 1){
                    setStartNum(3f)
                    mListener?.click(3)
                }
            }
            it.layout4.setOnClickNoDouble {
                if(mType == 1){
                    setStartNum(4f)
                    mListener?.click(4)
                }
            }
            it.layout5.setOnClickNoDouble {
                if(mType == 1){
                    setStartNum(5f)
                    mListener?.click(5)
                }
            }
        }

    }

    fun setStartNum(num: Float) {
        binding?.let {
            if(num>=1){
                it.start1.visibility = VISIBLE
            }else{
                it.start1.visibility = GONE
            }
            if(num>=2){
                it.start2.visibility = VISIBLE
            }else{
                it.start2.visibility = GONE
            }
            if(num>=3){
                it.start3.visibility = VISIBLE
            }else{
                it.start3.visibility = GONE
            }
            if(num>=4){
                it.start4.visibility = VISIBLE
            }else{
                it.start4.visibility = GONE
            }
            if(num>=5){
                it.start5.visibility = VISIBLE
            }else{
                it.start5.visibility = GONE
            }
        }
    }

    private fun setImageWidth(b : LayoutStartViewBinding){
        b.start1.layoutParams = b.start1.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.start2.layoutParams = b.start2.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.start3.layoutParams = b.start3.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.start4.layoutParams = b.start4.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.start5.layoutParams = b.start5.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.startBg1.layoutParams = b.startBg1.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.startBg2.layoutParams = b.startBg2.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.startBg3.layoutParams = b.startBg3.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.startBg4.layoutParams = b.startBg4.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }
        b.startBg5.layoutParams = b.startBg5.layoutParams.apply {
            this.width = mStvImageWidth
            this.height = mStvImageWidth
        }

    }

    /**
     *
     */
    interface  StvClickListener{
        fun click(num : Int)

    }
}