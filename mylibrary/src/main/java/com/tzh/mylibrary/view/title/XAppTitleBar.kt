package com.tzh.mylibrary.view.title

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tzh.mylibrary.R
import com.tzh.mylibrary.util.*
import com.tzh.mylibrary.util.AppKtx.getStatusBarHeight

class XAppTitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {

    companion object {

        const val ViewNone = 0
        const val ViewText = 1
        const val ViewImage = 2
    }

    private var mBackIv: AppCompatImageView? = null
    private var mTitleTv: TextView? = null


    private var mRightViewText: TextView? = null
    private var mRightViewImage: AppCompatImageView? = null

    /**
     * 右边文字
     */
    private var mRightTxt = ""

    /**
     * 右边文字颜色
     */
    private var mRightTextColor = R.color.color_333


    /**
     * 右边文字大小  px
     */
    private var mRightTextSize = 0f

    /**
     * 右边文字是否加粗
     */
    private var mRightTextIsBold = false

    /**
     * 右边图片资源
     */
    private var mRightImgSrc: Drawable? = null

    /**
     * 左边图片资源
     */
    private var mLeftImgSrc: Drawable? = null


    /**
     * 右边文图片颜色
     */
    private var mRightImgColor = 0

    /**
     * 左边图片距离左边距离
     */
    private var mLeftImgPaddingLeft = 0

    /**
     * 左边图片距离右边距离
     */
    private var mLeftImgPaddingRight = 0

    /**
     * 右边图片宽 px
     */
    private var mRightImgWidth = 0

    /**
     * 右边图片高  px
     */
    private var mRightImgHeight = 0

    /**
     * 右边 view 点击处理
     * 提成全局 变量 是为了 当remove后再次添加，点击事件依然可以使用
     */
    private var mRightClickListener: OnClickListener? = null

    init {
        attrs?.run {
            init(this)
        }
    }

    private fun init(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.XAppTitleBar)

        val backIconColor = typedArray.getResourceId(R.styleable.XAppTitleBar_xtbBackIconColor,-1)

        val backIconIsShow = typedArray.getBoolean(R.styleable.XAppTitleBar_xtbBackIconIsShow, true)

        val titleStr = typedArray.getString(R.styleable.XAppTitleBar_xtbTitleText)
        val titleTxtColor =
            typedArray.getResourceId(
                R.styleable.XAppTitleBar_xtbTitleTxtColor,
                R.color.color_333
            )


        //右边是否显示图片
        val rightShowView = typedArray.getInt(R.styleable.XAppTitleBar_xtbRightShowView, 0)

        mRightTxt = typedArray.getString(R.styleable.XAppTitleBar_xtbRightText).toString()

        mRightTextColor =
            typedArray.getResourceId(
                R.styleable.XAppTitleBar_xtbRightTxtColor,
                R.color.color_333
            )

        mRightTextSize =
            typedArray.getDimension(
                R.styleable.XAppTitleBar_xtbRightTxtSize,
                DpToUtil.sp2px(context, 14f).toFloat()
            )

        mRightTextIsBold = typedArray.getBoolean(R.styleable.XAppTitleBar_xtbRightTxtIsBold, false)

        //图片路径
        mRightImgSrc = typedArray.getDrawable(R.styleable.XAppTitleBar_xtbRightImgSrc)

        //图片路径
        mLeftImgSrc = typedArray.getDrawable(R.styleable.XAppTitleBar_xtbLeftImgSrc)

        mRightImgColor = typedArray.getResourceId(R.styleable.XAppTitleBar_xtbRightImgColor, 0)

        mRightImgWidth = typedArray.getDimensionPixelOffset(
            R.styleable.XAppTitleBar_xtbRightImgWidth,
            DpToUtil.dip2px(context, 24f)
        )
        mRightImgHeight = typedArray.getDimensionPixelOffset(
            R.styleable.XAppTitleBar_xtbRightImgHeight,
            DpToUtil.dip2px(context, 24f)
        )

        mLeftImgPaddingLeft = typedArray.getDimensionPixelOffset(
            R.styleable.XAppTitleBar_xtbLeftImgPaddingLeft,
            16
        )

        mLeftImgPaddingRight = typedArray.getDimensionPixelOffset(
            R.styleable.XAppTitleBar_xtbLeftImgPaddingRight,
            4
        )

        /**
         * 顶部状态栏是否透明
         */
        val isWindowTranslucentStatus =
            typedArray.getBoolean(R.styleable.XAppTitleBar_xtbIsWindowTranslucentStatus, true)

        typedArray.recycle()

        LayoutInflater.from(context).inflate(R.layout.layout_app_title_bar, this, true)

        mBackIv = findViewById<AppCompatImageView>(R.id.atb_back_iv).apply {
            mLeftImgSrc?.let {
                setImageDrawable(mLeftImgSrc)
            }
            if (backIconIsShow) {
                setWillNotDraw(false)
                setImageTintColorRes(backIconColor)
            } else {
                setWillNotDraw(true)
            }

            updatePaddingKtx(mLeftImgPaddingLeft.toFloat(),0f,mLeftImgPaddingRight.toFloat(),0f)
        }
        mTitleTv = findViewById<TextView>(R.id.atb_title_tv).apply {
            text = titleStr
            isSelected = true
            setTextColor(ContextCompat.getColor(context, titleTxtColor))
        }

        showRightView(rightShowView)

        if (isWindowTranslucentStatus) {
            //重置高度
            setPadding(0,getStatusBarHeight(context), 0, 0)
        } else {
            setPadding(0, 0, 0, 0)
        }

        if (background == null) {
            setBackgroundColor(Color.TRANSPARENT)
        }
        //设置默认的返回按钮事件
        mBackIv?.setOnClickListener {
            (context as? Activity)?.onBackPressed()
        }
    }

    private fun showRightView(rightShowView: Int) {
        mRightViewText?.let {
            removeView(it)
        }
        mRightViewText = null
        mRightViewImage?.let {
            removeView(it)
        }

        mRightViewImage = null
        when (rightShowView) {
            ViewNone -> {
                //当没有右边的 控件时，可以把标题view的宽度扩大一点
                mTitleTv.updateMarginKtx(50f, 0f, 50f, 0f, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            }
            ViewText -> {
                addView(TextView(context).apply {
                    mRightViewText = this
                    text = mRightTxt
                    if (mRightTextSize != 0f) {
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize)
                    } else {
                        textSize = 14f
                    }
                    setTextStyle(mRightTextIsBold)
                    setTextColorRes(mRightTextColor)
                    setPadding(DpToUtil.dip2px(context, 10f), 0, DpToUtil.dip2px(context, 15f), 0)
                    setOnClickNoDouble { mRightClickListener?.onClick(it) }
                }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.END or Gravity.CENTER_VERTICAL
                })
            }
            ViewImage -> {
                addView(AppCompatImageView(context).apply {
                    mRightViewImage = this
                    mRightImgSrc?.let {
                        setImageDrawable(it)
                    }
                    if (mRightImgColor != 0) {
                        setImageTintColorRes(mRightImgColor)
                    }
                    setPadding(DpToUtil.dip2px(context, 10f), 0, DpToUtil.dip2px(context, 15f), 0)
                    setOnClickNoDouble { mRightClickListener?.onClick(it) }
                }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    //需要加上左右边距
                    width = mRightImgWidth + DpToUtil.dip2px(context, 25f)
                    height = mRightImgHeight
                })
            }
        }
    }

    fun onRemoveRightView() {
        showRightView(ViewNone)
    }

    fun rightShowImageView(
        @DrawableRes res: Int,
        @ColorRes color: Int = R.color.color_333,
        width: Float = 24f,
        height: Float = 24f
    ) {
        post {
            mRightImgSrc = ContextCompat.getDrawable(context, res)
            mRightImgColor = color
            mRightImgWidth = DpToUtil.dip2px(context, width)
            mRightImgHeight = DpToUtil.dip2px(context, height)
            showRightView(ViewImage)
        }
    }

    fun setBackListener(listener: OnClickListener) {
        mBackIv?.setOnClickListener(listener)
    }

    fun setBackListener(activity: Activity) {
        mBackIv?.setOnClickListener {
            activity.onBackPressed()
        }
    }

    fun setBackColor(color : Int) {
        mBackIv?.drawable?.setTint(ContextCompat.getColor(context,color))
    }

    fun setTitleTxt(title: String?) {
        mTitleTv?.text = title ?: ""
    }

    fun setTitleTxtColor(color : Int) {
        mTitleTv?.setTextColorRes(color)
    }


    fun setTitleGravity(gravity: Int) {
        mTitleTv?.gravity = gravity
    }

    fun getTitleTxt(): String {
        return mTitleTv?.text.toString()
    }

    fun setRightTitleTxt(txt: String?) {
        mRightViewText?.text = txt
    }

    fun getRightTitleTxt(): String {
        return mRightViewText?.text.toString()
    }

    /**
     * 右边view的点击处理
     *
     * @param listener 点击监听
     */
    fun setRightViewClickListener(listener: OnClickListener) {
        mRightClickListener = listener
        mRightViewText?.setOnClickNoDouble { listener.onClick(it) }
        mRightViewImage?.setOnClickNoDouble { listener.onClick(it) }
    }


}