package com.tzh.myapplication.ui.dto

open class BaseResDto<T>() {
    /**
     * 状态码
     */
    var code: Int = 0

    /**
     * 请使用 getDataDto 获取数据
     */
    var data: T? = null

    /**
     * 消息提示
     */
    var msg: String? = null


    fun getDataDto(): T {
        return data ?: throw  RuntimeException("服务器数据异常")
    }
}