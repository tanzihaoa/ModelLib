package com.tzh.mylibrary.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tzh.mylibrary.util.pay.PayRefreshEvent


/**
 * Activity 关闭 处理
 */
class ActivityCloseLiveData : MutableLiveData<Boolean>() {
    companion object {
        val instance by lazy { ActivityCloseLiveData() }
    }
}

/**
 * 支付结果监听
 */
class PayRefreshData : MutableLiveData<PayRefreshEvent>() {
    companion object {
        val instance by lazy { PayRefreshData() }
    }
}

/**
 * 拦截 liveData 不向刚新建的观察者发送数据
 */
fun <T> LiveData<T>.observeNoBack(owner: LifecycleOwner, action: (data: T?) -> Unit): Observer<T> {
    val result: Observer<T>
    var isFirst = value != null
    observe(owner, Observer<T> {
        if (!isFirst) {
            action(it)
        }
        isFirst = false
    }.apply {
        result = this
    })
    return result
}


/**
 * 拦截 liveData 不向刚新建的观察者发送数据
 *
 * 不放生命周期的话，就是全局响应，但是需要注意销毁
 */
fun <T> LiveData<T>.observeForeverNoBack(action: (data: T?) -> Unit): Observer<T> {
    val result: Observer<T>
    var isFirst = value != null
    observeForever(Observer<T> {
        if (!isFirst) {
            action(it)
        }
        isFirst = false
    }.apply {
        result = this
    })
    return result
}