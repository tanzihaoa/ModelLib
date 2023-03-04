package com.tzh.mylibrary.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.mylibrary.util.XRvWrapperUtils


/**
 * 带有 底部 没有更多数据 的binding 适配器 ， 只有一个类型的item时，可从构造函数 传入itemlayout
 * 多item 类型 使用 这个函数 [getBindingItemLayout]
 */
abstract class XRvBindingPureDataAdapter<T>(@LayoutRes val itemLayout: Int = 0) : XRvPureBindingAdapter() {

    /**
     * 一般数据
     */
    val listData = mutableListOf<T>()

    /**
     * 是否显示 没有更多多数据 条目
     */
    private var isShowNoMoreData = false

    /**
     * 是否启用 没有更多数据
     */
    var isEnableNoMoreItem = true

    /**
     * 获取 是否显示 没有更多多数据 条目
     */
    fun getShowNoMoreData(): Boolean {
        return isShowNoMoreData
    }

    /**
     * 显示/隐藏 没有更多数据
     */
    fun showNoMoreData(isShow: Boolean = true) {
        if (!isEnableNoMoreItem) {
            return
        }
        if (isShowNoMoreData == isShow) {
            return
        }
        isShowNoMoreData = isShow
        if (isShowNoMoreData) {
            notifyItemRangeInserted(listData.size, 1)
        } else {
            notifyItemRangeRemoved(listData.size, 1)
        }
    }


    /**
     * 当需要显示 没有更多数据条目时，itemCount数量需要+1
     */
    final override fun getItemCount(): Int {
        return if (isShowNoMoreData) {
            listData.size + 1
        } else {
            listData.size
        }
    }

    /**
     * 请使用 getItemViewBindingType 来判断 viewtype
     */
    final override fun getItemViewType(position: Int): Int {
        if (position < 0 || position >= itemCount) {
            return 0
        }
        return if (isShowNoMoreData && position == itemCount - 1) {
            XRvConfig.NO_MORE_DATA_TAG
        } else {
            getBindingItemViewType(position)
        }
    }

    open fun getBindingItemViewType(position: Int): Int {
        return 0
    }

    final override fun getItemLayout(viewType: Int): Int {
        return if (viewType == XRvConfig.NO_MORE_DATA_TAG) {
            //没有更多数据item 不需要点击事件
            addNoListenerLayout(getNoMoreDataLayout())
            getNoMoreDataLayout()
        } else {
            getBindingItemLayout(viewType)
        }
    }

    /**
     *  可从 构造函数传入
     *  如果需要处理多类型的item，就需要使用这个函数
     */
    @LayoutRes
    open fun getBindingItemLayout(viewType: Int): Int {
        return itemLayout
    }

    /**
     * 设置 没有更多数据 Layout
     */
    @LayoutRes
    fun getNoMoreDataLayout(): Int {
        return XRvConfig.noMoreDataLayout
    }

    final override fun onBindViewHolder(holder: XRvBindingHolder, position: Int) {
        if (isShowNoMoreData && position == itemCount - 1) {
            onBindNoMoreDataViewHolder(holder, position)
        } else {
            onBindViewHolder(holder, position, getItem(position))
        }
    }

    final override fun onBindViewHolder(holder: XRvBindingHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (!(isShowNoMoreData && position == itemCount - 1)) {
                onBindViewHolder(holder, position, getItem(position), payloads)
            }
        }
    }

    /**
     *
     */
    protected open fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: T, payloads: MutableList<Any>) {

    }

    /**
     *
     */
    abstract fun onBindViewHolder(holder: XRvBindingHolder, position: Int, data: T)

    /**
     *  对 没有更多数据 layout 处理
     */
    protected open fun onBindNoMoreDataViewHolder(holder: XRvBindingHolder, position: Int) {

    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        XRvWrapperUtils.onAttachedToRecyclerView(recyclerView, object :
            XRvWrapperUtils.SpanSizeCallback {
            override fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int {
                if (isShowNoMoreData && position == itemCount - 1) {
                    return layoutManager.spanCount
                }
                return oldLookup.getSpanSize(position)
            }
        })

    }

    override fun onViewAttachedToWindow(holder: XRvBindingHolder) {
        super.onViewAttachedToWindow(holder)
        if (isShowNoMoreData && holder.bindingAdapterPosition == itemCount - 1) {
            XRvWrapperUtils.setFullSpan(holder)
        }
    }


    /**
     * 数据替换 ，带刷新
     *
     * @param list     数据
     * @param isNotify 是否刷新
     */
    @JvmOverloads
    open fun setData(data: T, isNotify: Boolean = true) {
        listData.clear()
        listData.add(data)
        if (isNotify) {
            notifyDataSetChanged()
        }
    }


    /**
     * 数据替换 ，带刷新
     *
     * @param isRefresh  是否替换数据
     * @param list 数据
     * @param isNotify 适配器
     */
    @JvmOverloads
    open fun setDatas(isRefresh: Boolean, list: MutableList<T>, isNotify: Boolean = true) {
        if (isRefresh) {
            setDatas(list, isNotify)
        } else {
            addDatas(list, isNotify)
        }
    }

    /**
     * 数据替换 ，带刷新
     *
     * @param list     数据
     * @param isNotify 是否刷新
     */
    @JvmOverloads
    open fun setDatas(list: MutableList<T>, isNotify: Boolean = true) {
        listData.clear()
        listData.addAll(list)
        if (isNotify) {
            notifyDataSetChanged()
        }
    }

    /**
     * 增加一个data
     *
     * @param data      数据
     * @param isNotify 是否刷新
     */
    @JvmOverloads
    open fun addData(data: T, isNotify: Boolean = true) {
        listData.add(data)
        if (isNotify) {
            notifyItemRangeInserted(listData.size, 1)
            if (listData.size > 2) {
                notifyItemChanged(listData.size - 2)
            }
        }
    }


    /**
     * 增加一个data
     *
     * @param data      数据
     * @param isNotify 是否刷新
     */
    @JvmOverloads
    open fun addData(position: Int, data: T, isNotify: Boolean = true) {
        listData.add(position, data)
        if (isNotify) {
            notifyItemRangeInserted(position, 1)
            notifyItemChanged(position, listData.size - position + 1)
        }
    }


    /**
     * 数据集合增加数据 带刷新
     *
     * @param data      数据
     * @param isNotify 是否刷新
     */
    @JvmOverloads
    open fun addDatas(datas: MutableList<T>, isNotify: Boolean = true) {
        listData.addAll(datas)
        if (isNotify) {
            notifyItemRangeInserted(listData.size - datas.size, datas.size)
            if (listData.size - datas.size > 1) {
                notifyItemChanged(listData.size - datas.size - 1)
            }
        }
    }

    /**
     * 数据集合增加数据 带刷新
     *
     * @param data      数据
     * @param isNotify 是否刷新
     */
    @JvmOverloads
    open fun addDatas(position: Int, datas: MutableList<T>, isNotify: Boolean = true) {
        listData.addAll(position, datas)
        if (isNotify) {
            notifyItemRangeInserted(position, datas.size)
            notifyItemRangeChanged(position, listData.size - position)
            //当rv不绘制最后一个item 的 分割线时，如果新增了数据，不刷新上次最后一个item，那么上次最后一个item的分割线会缺少
            if (listData.size - datas.size > 1 && position > 0) {
                notifyItemChanged(position - 1)
            }
        }
    }

    /**
     * 移除data
     *
     * @param data 对象
     * @return 是否移除成功
     */
    open fun removeData(data: T): Boolean {
        return removeData(listData.indexOf(data))
    }


    /**
     * 移除data
     *
     * @param index 下标
     * @return
     */
    open fun removeData(index: Int): Boolean {
        return if (index >= 0) {
            if (index < listData.size) {
                listData.removeAt(index)
                notifyItemRangeRemoved(index,  1)
                notifyItemRangeChanged(index, listData.size - index + 1)
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    /**
     * 移除所有数据
     */
    open fun removeDataAll() {
        listData.clear()
        notifyDataSetChanged()
    }

    /**
     * 得到单个item
     *
     * @param position item的下标
     * @return 集合中的某个对象
     */
    open fun getItem(position: Int): T {
        return listData[position]
    }

    /**
     * 得到单个item
     * 处理掉 下标越界但是可能为null
     *
     * @param position item的下标
     * @return 集合中的某个对象
     */
    open fun getItemOrNUll(position: Int): T? {
        if (position < 0) return null
        if (listData.size <= position) return null
        return listData[position]
    }
}