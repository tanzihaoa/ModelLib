package com.tzh.mylibrary.util

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.tzh.mylibrary.adapter.XRvBindingPureDataAdapter

/**
 * 加载数据 成功 使用这个
 */
fun SmartRefreshLayout?.loadSuccess(adapter: XRvBindingPureDataAdapter<*>? = null,
                                    pageIndex: Int = 0,
                                    pageCount: Int = 0
) {
    this ?: return
    if (this.isRefreshing) {
        finishRefresh()
    }
    if (this.isLoading) {
        finishLoadMore()
    }
    if (pageIndex > 0 && pageCount > 0) {
        adapter?.run {
            if (pageIndex >= pageCount) {
                adapter.showNoMoreData(true)
                setEnableLoadMore(false)
            } else {
                adapter.showNoMoreData(false)
                setEnableLoadMore(true)
            }

        }
    }
}


/**
 * 加载失败
 */
fun SmartRefreshLayout?.loadError(
    throwable: Throwable? = null,
    isReLoad: Boolean = true
) {
    loadError(false, throwable, isReLoad)
}

/**
 * 加载失败
 * @param isRefresh 这个 判断 是否刷新状态，主要用于 手动执行了刷新状态，而不是通过smartrefreshlayout 调用的刷新状态
 */
fun SmartRefreshLayout?.loadError(
    isRefresh: Boolean = false,
    throwable: Throwable? = null,
    isReLoad: Boolean = false
) {
    this ?: return
    if (this.isLoading) {
        finishLoadMore(300, false, false)
    }
    if (this.isRefreshing) {
        finishRefresh(300, false, false)
        //处理数据加载错误的缺省状态
        //  loadView?.loadingError(throwable, isReLoad)
    }
}

/**
 * 没有更多数据
 */
fun SmartRefreshLayout?.loadNoData(adapter: XRvBindingPureDataAdapter<*>? = null) {
    this ?: return
    adapter?.run {
        adapter.showNoMoreData(true)
        setEnableLoadMore(false)
    }
    finishLoadMore()
}


/**
 * 刷新时，允许加载更多
 */
fun SmartRefreshLayout?.onRefreshStatus(adapter: XRvBindingPureDataAdapter<*>? = null) {
    this ?: return
    adapter?.showNoMoreData(false)
    setNoMoreData(false)
    setEnableLoadMore(true)
}


/**
 * 立即停止刷新加载
 */
fun SmartRefreshLayout?.stopRefreshLoad(
    refreshDelayed: Int = 300,
    loadDelayed: Int = 300
) {
    this ?: return

    if (state == RefreshState.Refreshing) {
        finishRefresh(refreshDelayed)
    }
    if (state == RefreshState.Loading) {
        finishLoadMore(loadDelayed)
    }
}

/**
 * 禁用刷新加载 带阻尼效果
 */
fun SmartRefreshLayout?.dampingStyle() {
    this ?: return
    this.setEnableLoadMore(false)
    this.setEnableRefresh(false)
    this.setEnableOverScrollBounce(true)
    this.setEnableOverScrollDrag(true)
    this.setEnablePureScrollMode(true)
}

/**
 * 禁用加载更多 带阻尼效果
 */
fun SmartRefreshLayout?.dampingRefreshStyle() {
    this ?: return
    this.setEnableLoadMore(false)
    this.setEnableRefresh(true)
    this.setEnableOverScrollBounce(true)
    this.setEnableOverScrollDrag(true)
}

/**
 * 禁用加载更多 带阻尼效果
 */
fun SmartRefreshLayout?.dampingRefreshLondingStyle() {
    this ?: return
    this.setEnableLoadMore(true)
    this.setEnableRefresh(true)
    this.setEnableOverScrollBounce(true)
    this.setEnableOverScrollDrag(false)
    this.setEnablePureScrollMode(false)
}