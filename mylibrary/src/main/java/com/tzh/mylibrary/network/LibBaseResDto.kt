package com.tzh.mylibrary.network

open class LibBaseResDto<T>() {
    /**
     * 状态码
     */
    var error_code: Int = 0

    /**
     * 请使用 getDataDto 获取数据
     */
    var trans_result: T? = null

    /**
     * 消息提示
     */
    var error_msg: String? = null


    fun getDataDto(): T {
        return trans_result ?: throw  RuntimeException("服务器数据异常")
    }
}