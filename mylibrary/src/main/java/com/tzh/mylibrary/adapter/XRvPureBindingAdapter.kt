package com.tzh.mylibrary.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class XRvPureBindingAdapter : RecyclerView.Adapter<XRvBindingHolder>() {

    /**
     * 设置某些layout不能点击
     */

    protected val noClickLayouts = mutableListOf<@LayoutRes Int>()

    /**
     * 设置某些layout不能长按
     */
    protected val noLongClickLayouts = mutableListOf<@LayoutRes Int>()

    /**
     * 设置某些layout不能选中
     */
    protected val noFocusableLayouts = mutableListOf<@LayoutRes Int>()

    /**
     * 点击事件
     */
    var onItemClickListener: OnItemClickListener? = null

    /**
     * 长按事件
     */
    var onItemLongClickListener: OnItemLongClickListener? = null

    /**
     * Tv端或者键盘手机使用
     * item 焦点 被选中
     */
    var onItemFocusableListener: OnItemFocusableListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XRvBindingHolder {

        val viewHolder = XRvBindingHolder.createHolder(parent, getItemLayout(viewType))

        onItemClickListener?.run {
            //判断点击事件
            if (!noClickLayouts.contains(getItemLayout(viewType))) {
                viewHolder.itemView.setOnClickListener { v ->
                    if (viewHolder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        onItemClick(v, viewHolder, viewHolder.bindingAdapterPosition)
                    }
                }
            }
        }
        onItemLongClickListener?.run {
            if (!noLongClickLayouts.contains(getItemLayout(viewType))) {
                viewHolder.itemView.setOnLongClickListener { v ->
                    if (viewHolder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        onItemLongClick(v, viewHolder, viewHolder.bindingAdapterPosition)
                    } else {
                        false
                    }
                }
            }
        }
        onItemFocusableListener?.run {
            if (!noFocusableLayouts.contains(getItemLayout(viewType))) {
                //todo 焦点事件，会触发两次，一次是item离开的时候，一次是item被进入的时候，
                viewHolder.itemView.setOnFocusChangeListener { v, hasFocus ->
                    if (viewHolder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        onItemFocusableListener!!.onItemFocusable(v, hasFocus, viewHolder, viewHolder.bindingAdapterPosition)
                    }
                }
            }
        }


        return viewHolder
    }


    /**
     * 设置item layout
     *
     * @param viewType 类型
     */
    @LayoutRes
    protected abstract fun getItemLayout(viewType: Int): Int



    /**
     * 添加 没有点击 长按 焦点 事件的layout
     */
    protected open fun addNoListenerLayout(@LayoutRes layout: Int) {
        noClickLayouts.add(layout)
        noLongClickLayouts.add(layout)
        noFocusableLayouts.add(layout)
    }
}


/**
 * 点击接口
 */
interface OnItemClickListener {
    fun onItemClick(view: View, holder: XRvBindingHolder, position: Int)
}


/**
 * 长按接口
 */
interface OnItemLongClickListener {
    fun onItemLongClick(view: View, holder: XRvBindingHolder, position: Int): Boolean
}

/**
 * Tv端或者键盘手机使用
 * 焦点选中接口
 */
interface OnItemFocusableListener {
    /**
     * @param view     被选中的view
     * @param hasFocus 是否有焦点
     * @param holder   holder
     * @param position 下标
     * todo 焦点事件，会触发两次，一次是item离开的时候，一次是item被进入的时候，
     */
    fun onItemFocusable(view: View, hasFocus: Boolean, holder: XRvBindingHolder, position: Int)
}
