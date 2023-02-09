package com.tzh.mylibrary.divider

import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.tzh.mylibrary.util.gradDivider
import com.tzh.mylibrary.util.grid
import com.tzh.mylibrary.util.linear
import com.tzh.mylibrary.util.verDivider

object DividerUtil {
    /**
     * 设置RecyclerView为线性布局  并设置间隔高度
     */
    @JvmStatic
    fun setVerDivider(recyclerView: RecyclerView,height: Float = 1f){
        recyclerView.linear().verDivider(height)
    }

    /**
     * 设置RecyclerView为线性布局  并设置间隔高度与颜色
     */
    @JvmStatic
    fun setVerDivider(recyclerView: RecyclerView,height: Float = 1f, @ColorRes color: Int = android.R.color.transparent){
        recyclerView.linear().verDivider(height,color)
    }


    /**
     * 设置RecyclerView为表格布局  并设置一行数量与间隔
     */
    @JvmStatic
    fun setGradDivider(recyclerView: RecyclerView,space: Float = 1f, num: Int){
        recyclerView.grid(num).gradDivider(space,num,space)
    }
}