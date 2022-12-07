package com.tzh.myapplication.adapter.divider

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tzh.myapplication.adapter.XRvConfig

open class XRvVerticalDivider(var context: Context) : XBaseDivider(context) {


    /**
     * 分割线颜色
     */
    @ColorInt
    var dividerColor = 0


    /**
     * 分割线高度(dp)
     */
    var dividerHeight = 1f

    /**
     * 距离开始边距
     */
    var marginStart = 0f

    /**
     * 距离结束边距
     */
    var marginEnd = 0f

    /**
     * 是否显示第一个item上面的 分割线
     */
    var isShowTopLine = false

    /**
     * 是否显示底部分割线
     */
    var isShowBottomLine = false

    /**
     * 是否在只有一个item时，显示底部分割线
     */
    var isSingleItemShowBottomLine = false

    /**
     * 不需要分割线的item下标
     */
    var noDividerItem = mutableListOf<Int>()

    fun setDividerColorRes(@ColorRes color: Int) {
        dividerColor = ContextCompat.getColor(context, color)
    }

    private val divider1 by lazy {
        XDividerBuilder()
            .setTopSideLine(true, dividerColor, dividerHeight, marginStart, marginEnd)
            .setBottomSideLine(true, dividerColor, dividerHeight, marginStart, marginEnd)
            .create()
    }
    private val divider2 by lazy {
        XDividerBuilder()
            .setBottomSideLine(true, dividerColor, dividerHeight, marginStart, marginEnd)
            .create()
    }

    private val divider3 by lazy {
        XDividerBuilder()
            .setTopSideLine(true, dividerColor, dividerHeight, marginStart, marginEnd)
            .create()
    }


    override fun getItemDivider(adapter: RecyclerView.Adapter<*>, itemPosition: Int): XDivider {
        //当有个别的item不需要divider时，根据itemPosition处理
        if (noDividerItem.size > 0) {
            if (noDividerItem.contains(itemPosition)) {
                return defaultDivider
            }
        }
        if (adapter.itemCount <= 0) return defaultDivider
        if (itemPosition < 0) return defaultDivider
        if (adapter.getItemViewType(itemPosition) == XRvConfig.NO_MORE_DATA_TAG) return defaultDivider

        //最底部如果是没有更多item时，分割线只计算到 倒数第二个
        val haveNoMoreData = adapter.getItemViewType(adapter.itemCount - 1) == XRvConfig.NO_MORE_DATA_TAG

        val itemCount = if (haveNoMoreData) adapter.itemCount - 1 else adapter.itemCount

        if (itemCount <= 0) return defaultDivider

        //单个item时
        return if (itemCount == 1) {
            if (isSingleItemShowBottomLine) {
                if (isShowTopLine) {
                    divider1
                } else {
                    divider2
                }
            } else {
                if (isShowTopLine) {
                    divider3
                } else {
                    defaultDivider
                }
            }
        } else if (itemPosition == 0 && isShowTopLine) {
            //是否显示顶部分割线
            divider1
        } else if (!isShowBottomLine && itemCount == itemPosition + 1) {
            //不显示底部分割线
            defaultDivider
        } else {
            divider2
        }

    }



}