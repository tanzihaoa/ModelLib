package com.tzh.myapplication.network

import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.mylibrary.network.ApiThrowable
import com.tzh.mylibrary.util.toDefault
import io.reactivex.functions.Consumer

/**
 * 异常处理
 */
class DefaultFailure : Consumer<BaseResDto<*>> {

    /**
     * 无错误
     */
    private val REQUEST_SUCCESS = 0

    @Throws(Exception::class)
    override fun accept(responseDto: BaseResDto<*>) {
        when (responseDto.code) {
            REQUEST_SUCCESS -> {
                //如果data，或者 list中data为null，则直接走 异常流程
                responseDto.data.also { data ->
                    if (data == null) {
                        throw ApiThrowable(responseDto.code, "服务器数据异常")
                    }
                }
            }

            else -> throw ApiThrowable(responseDto.code, responseDto.msg.toDefault(""))
        }
    }
}

