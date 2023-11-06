package com.tzh.myapplication.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tzh.myapplication.ui.dto.SmsDto

/**
 * 登陆状态处理
 */
class LoginStateLiveData : MutableLiveData<Boolean>() {
    companion object {
        val instance by lazy { LoginStateLiveData() }
    }
}

/**
 * 收到的短信监听
 */
class SmsLiveData : MutableLiveData<SmsDto>() {
    companion object {
        val instance by lazy { SmsLiveData() }
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