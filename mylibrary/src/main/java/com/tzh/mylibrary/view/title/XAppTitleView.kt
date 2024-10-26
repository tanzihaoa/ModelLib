package com.tzh.mylibrary.view.title

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tzh.mylibrary.util.AppKtx.getStatusBarHeight


/**
 * 填充顶部的
 * @author xz
 */
class XAppTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec,
            getStatusBarHeight(context)
        )
    }

}