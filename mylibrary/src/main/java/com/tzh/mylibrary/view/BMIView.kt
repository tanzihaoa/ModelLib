package com.tzh.mylibrary.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tzh.mylibrary.R
import com.tzh.mylibrary.databinding.LayoutBmiViewBinding
import com.tzh.mylibrary.util.*

class BMIView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 指针图片资源
     */
    private var mImgSrc: Drawable? = null

    /**
     * BMI值
     */
    var mBmi : Float = 0f

    /**
     * 宽度
     */
    private var mView1Width : Int = 0
    private var mView2Width : Int = 0
    private var mView3Width : Int = 0
    private var mView4Width : Int = 0

    /**
     * 间隔
     */
    private val interval = DpToUtil.dip2px(context, 4f)

    /**
     * 需要移动的距离
     */
    private var mMargin : Float = 0f

    var binding : LayoutBmiViewBinding ?= null

    init {
        attrs?.run {
            init(this)
        }
    }

    private fun init(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BMIView)

        mBmi = typedArray.getFloat(R.styleable.BMIView_bvBMI,0f)

        //图片路径
        mImgSrc = typedArray.getDrawable(R.styleable.BMIView_bvImgSrc)

        typedArray.recycle()

        binding = bindingInflateLayout<LayoutBmiViewBinding>(R.layout.layout_bmi_view).also {
            /**
             * 获取空间宽度
             */
            it.view1.post {
                mView1Width = it.view1.width
                mView2Width = it.view2.width
                mView3Width = it.view3.width
                mView4Width = it.view4.width

                /**
                 * 设置指针图片
                 */
                mImgSrc?.let { d->
                    it.ivZUp.setImageDrawable(d)
                    it.ivZDown.setImageDrawable(d)
                }

                upData()
            }
        }
    }

    /**
     * 设置BMI值 设置最大为60
     */
    fun setBmi(bmi : Float){
        mBmi = bmi
        if(mBmi<0){
            mBmi = 0f
        }else if(mBmi > 60){
            mBmi = 60f
        }
        upData()
    }

    private fun upData(){
        mMargin = 0f
        /**
         * 计算移动距离
         */
        binding?.let {
            //BMI偏低
            if(mBmi < 18.5f){
                mMargin = mView1Width*(mBmi/18.5f) + interval / 3
                it.ivZUp.setImageTint(R.color.color_9ac6e1)
                it.ivZDown.setImageTint(R.color.color_9ac6e1)
                setTextColor(1)
            }else{
                mMargin += mView1Width+interval
            }

            //BMI标准
            if(mBmi >= 18.5f){
                if(mBmi < 24f){
                    mMargin += mView2Width*((mBmi-18.5f)/(24f - 18.5f)) + interval / 3
                    it.ivZUp.setImageTint(R.color.color_9ebf6a)
                    it.ivZDown.setImageTint(R.color.color_9ebf6a)
                    setTextColor(2)
                }else{
                    mMargin += mView2Width+interval
                }
            }

            //BMI超重
            if(mBmi >= 24f){
                if(mBmi < 28f){
                    mMargin += mView3Width*((mBmi-24f)/(28f - 24f)) + interval / 3
                    it.ivZUp.setImageTint(R.color.color_ffbc5c)
                    it.ivZDown.setImageTint(R.color.color_ffbc5c)
                    setTextColor(3)
                }else{
                    mMargin += mView3Width+interval
                }
            }

            //BMI肥胖
            if(mBmi >= 28f){
                mMargin += interval + mView4Width*((mBmi-28f)/(60f - 28f))
                it.ivZUp.setImageTint(R.color.color_f15c4d)
                it.ivZDown.setImageTint(R.color.color_f15c4d)
                setTextColor(4)
            }

            post {
                ViewMarginsUtil.setMarginsToPx(it.ivZUp,mMargin.toInt(),0,0,0)
                ViewMarginsUtil.setMarginsToPx(it.ivZDown,mMargin.toInt(),0,0,0)
            }
        }
    }

    /**
     * 设置选中的TextView的颜色
     */
    private fun setTextColor(type : Int){
        binding?.let {
            it.tvText1.setTextColorRes(if(type==1) R.color.color_333 else R.color.color_50333 )
            it.tvText2.setTextColorRes(if(type==2) R.color.color_333 else R.color.color_50333 )
            it.tvText3.setTextColorRes(if(type==3) R.color.color_333 else R.color.color_50333 )
            it.tvText4.setTextColorRes(if(type==4) R.color.color_333 else R.color.color_50333 )
        }
    }
}