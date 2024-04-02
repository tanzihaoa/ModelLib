package com.tzh.mylibrary.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.youth.banner.adapter.BannerAdapter

abstract class BaseBannerAdapter<T>(@LayoutRes val itemLayout: Int = 0, data : List<T>) : BannerAdapter<T, XRvBindingHolder>(data)  {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): XRvBindingHolder {
        return XRvBindingHolder.createHolder(parent, getBindingItemLayout(viewType))
    }

    @LayoutRes
    open fun getBindingItemLayout(viewType: Int): Int {
        return itemLayout
    }
}