package com.tzh.mylibrary.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.tzh.mylibrary.R
import com.tzh.mylibrary.shapeview.ShapeLinearLayout
import com.tzh.mylibrary.util.*

/**
 * 图片和文字view
 * @author tzh
 */
class ImageTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeLinearLayout(
    context,
    attrs,
    defStyleAttr
) {
    companion object {
        /**
         * 图片在文字的上面
         */
        const val TO_TEXT_TOP = 0

        /**
         * 图片在文字的下面
         */
        const val TO_TEXT_BOTTOM = 1

        /**
         * 图片在文字的左边
         */
        const val TO_TEXT_LEFT = 2

        /**
         * 图片在文字的右边
         */
        const val TO_TEXT_RIGHT = 3
    }

    val mImageView by lazy { AppCompatImageView(getContext()) }
    val mTextview by lazy {
        TextView(getContext()).apply {
            gravity = Gravity.CENTER
//            includeFontPadding = false
        }
    }

    /**
     * 图片选中状态
     */
    private var mImageSelectSrc: Drawable? = null

    /**
     * 图片未选中状态
     */
    private var mImageUnSelectSrc: Drawable? = null

    /**
     * 图片未选中时的染色
     */
    private var mImageUnSelectColor: Int = -1

    /**
     * 图片选中时的染色
     */
    private var mImageSelectColor: Int = -1

    /**
     * 文字选中状态颜色
     */
    private var mTextSelectColor: Int = R.color.color_000

    /**
     * 文字未选中状态颜色
     */
    private var mTextUnSelectColor: Int = R.color.color_000

    /**
     * 图片文字间距
     */
    private var mSpaceDp = 0f

    /**
     * 图片宽
     */
    private var mImgWidth = 24

    /**
     * 图片高
     */
    private var mImgHeight = 24

    /**
     * 选中事件处理
     */
    var onSelectChangeListener: OnSelectChangeListener? = null

    init {
        attrs?.run {
            initImageTextView(this)
        }
    }

    private fun initImageTextView(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView)
        mImgWidth = typedArray.getDimensionPixelOffset(R.styleable.ImageTextView_itvImgWidth, DpToUtil.dip2px(context, 24f))
        mImgHeight = typedArray.getDimensionPixelOffset(R.styleable.ImageTextView_itvImgHeight, DpToUtil.dip2px(context, 24f))

        mImageUnSelectSrc = typedArray.getDrawable(R.styleable.ImageTextView_itvImgUnSelectSrc)
        mImageUnSelectColor = typedArray.getResourceId(R.styleable.ImageTextView_itvImgUnSelectColor, -1)
        mImageSelectSrc = typedArray.getDrawable(R.styleable.ImageTextView_itvImgSelectSrc)
        mImageSelectColor = typedArray.getResourceId(R.styleable.ImageTextView_itvImgSelectColor, -1)

        val space = typedArray.getDimensionPixelOffset(R.styleable.ImageTextView_itvSpace, DpToUtil.sp2px(context, 5f))
        val textSize = typedArray.getDimension(R.styleable.ImageTextView_itvTextSize, DpToUtil.sp2px(context, 14f).toFloat())
        mTextUnSelectColor = typedArray.getResourceId(R.styleable.ImageTextView_itvTextUnSelectColor, R.color.color_000)
        mTextSelectColor = typedArray.getResourceId(R.styleable.ImageTextView_itvTextSelectColor, -1)
        if (mTextSelectColor == -1) {
            mTextSelectColor = mTextUnSelectColor
        }
        val text = typedArray.getString(R.styleable.ImageTextView_itvText)

        val textIsBold = typedArray.getBoolean(R.styleable.ImageTextView_itvTextIsBold, false)

        //显示图片的位置
        val rightShowView = typedArray.getInt(R.styleable.ImageTextView_itvShowLocal, TO_TEXT_RIGHT)
        typedArray.recycle()

        mImageUnSelectSrc?.let { mImageView.setImageDrawable(it) }
        mImageView.setImageTintColorRes(mImageUnSelectColor)

        mTextview.text = text
        mTextview.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        mTextview.setTextColorRes(mTextUnSelectColor)

        mTextview.setTextStyle(textIsBold)

        mSpaceDp = DpToUtil.px2dip(context, space.toFloat()).toFloat()
        gravity = Gravity.CENTER


        isSelected = false

        setShowLocal(rightShowView)

        setOnClickListener {
            isSelected = !isSelected
        }
    }

    /**
     * 设置位置
     */
    fun setShowLocal(viewLocal: Int) {
        removeAllViews()
        when (viewLocal) {
            0 -> {
                //图片显示在文字的上面
                orientation = VERTICAL
                addView(mImageView, mImgWidth, mImgHeight)
                addView(mTextview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                mTextview.updateMarginKtx(0f, mSpaceDp, 0f, 0f, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

            }
            1 -> {
                //图片显示在文字的下面
                orientation = VERTICAL
                addView(mTextview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                addView(mImageView, mImgWidth, mImgHeight)
                mTextview.updateMarginKtx(0f, 0f, 0f, mSpaceDp, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            }
            2 -> {
                //图片显示在文字的左边
                orientation = HORIZONTAL
                addView(mImageView, mImgWidth, mImgHeight)
                addView(mTextview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                mTextview.updateMarginKtx(mSpaceDp, 0f, 0f, 0f, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            }
            3 -> {
                //图片显示在文字的右边
                orientation = HORIZONTAL
                addView(mTextview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                addView(mImageView, mImgWidth, mImgHeight)
                mTextview.updateMarginKtx(0f, 0f, mSpaceDp, 0f, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            }
        }
    }


    /**
     *@param isTrigger 是否触发回调
     */
    fun setSelected(selected: Boolean, isTrigger: Boolean = true) {
        if (isTrigger) {
            if (onSelectChangeListener?.onSelect(selected).toDefault(false)) return
        }
        super.setSelected(selected)
        if (selected) {
            mTextview.setTextColorRes(mTextSelectColor)
            mImageSelectSrc?.let { mImageView.setImageDrawable(it) }
            mImageView.setImageTint(mImageSelectColor)
        } else {
            mTextview.setTextColorRes(mTextUnSelectColor)
            mImageUnSelectSrc?.let { mImageView.setImageDrawable(it) }
            mImageView.setImageTint(mImageUnSelectColor)
        }
    }

    fun setText(str: String?) {
        mTextview.text = str.toDefault("")
    }

    override fun setSelected(selected: Boolean) {
        setSelected(selected, true)
    }


    interface OnSelectChangeListener {
        fun onSelect(selected: Boolean): Boolean
    }

}