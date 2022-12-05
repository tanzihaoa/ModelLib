package com.tzh.myapplication.adapter.divider

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class XBaseDivider(context: Context) : XDividerItemDecoration(context) {
    protected val defaultDivider: XDivider by lazy {
        XDividerBuilder().create()
    }

    override fun getDivider(adapter: RecyclerView.Adapter<*>?, itemPosition: Int): XDivider {
        adapter ?: return defaultDivider
        return getItemDivider(adapter, itemPosition)
    }

    open fun getItemDivider(adapter: RecyclerView.Adapter<*>, itemPosition: Int): XDivider{
        return defaultDivider
    }
}