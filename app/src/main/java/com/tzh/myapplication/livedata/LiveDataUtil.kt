package com.tzh.myapplication.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 登陆状态处理
 */
class LoginStateLiveData : MutableLiveData<Boolean>() {
    companion object {
        val instance by lazy { LoginStateLiveData() }
    }
}