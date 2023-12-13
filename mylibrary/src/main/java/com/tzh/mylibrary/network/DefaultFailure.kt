package com.tzh.mylibrary.network

import com.tzh.mylibrary.util.toDefault
import io.reactivex.functions.Consumer

/**
 * 异常处理
 */
class DefaultFailure : Consumer<LibBaseResDto<*>> {

    /**
     * 无错误
     */
    private val REQUEST_SUCCESS = 0

    @Throws(Exception::class)
    override fun accept(responseDto: LibBaseResDto<*>) {
        when (responseDto.error_code) {
            REQUEST_SUCCESS -> {
                //如果data，或者 list中data为null，则直接走 异常流程
                responseDto.trans_result.also { data ->
                    if (data == null) {
                        throw ApiThrowable(responseDto.error_code, "服务器数据异常")
                    }
                }
            }

            else -> throw ApiThrowable(responseDto.error_code, responseDto.error_msg.toDefault(""))
        }
    }
}

