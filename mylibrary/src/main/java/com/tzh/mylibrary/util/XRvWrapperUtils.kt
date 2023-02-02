package com.tzh.mylibrary.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 *
 */
object XRvWrapperUtils {
    @JvmStatic
    fun onAttachedToRecyclerView(recyclerView: RecyclerView, callback: SpanSizeCallback) {
        (recyclerView.layoutManager as? GridLayoutManager)?.let { gridLayoutManager ->
            val spanSizeLookup = gridLayoutManager.spanSizeLookup
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position)
                }
            }
            gridLayoutManager.spanCount = gridLayoutManager.spanCount
        }
    }

    @JvmStatic
    fun setFullSpan(holder: RecyclerView.ViewHolder) {
        (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.let { layoutParam ->
            layoutParam.isFullSpan = true
        }

    }

    //设置一个接口 用来 判断是不是GridLayout
    interface SpanSizeCallback {
        fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int
    }
}