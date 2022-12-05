package com.tzh.myapplication.network

import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.myapplication.utils.LogUtils
import com.tzh.mylibrary.utils.GsonUtil
import com.tzh.mylibrary.utils.ToastUtil
import com.tzh.mylibrary.utils.toDefault
import io.reactivex.functions.Consumer

/**
 * 异常处理
 */
class DefaultFailure : Consumer<BaseResDto<*>> {

    /**
     * 无错误
     */
    private val REQUEST_SUCCESS = 1


    @Throws(Exception::class)
    override fun accept(responseDto: BaseResDto<*>) {
        LogUtils.e("accept==",GsonUtil.GsonString(responseDto))
        when (responseDto.status) {
            REQUEST_SUCCESS -> {
                //如果data，或者 list中data为null，则直接走 异常流程
                responseDto.data.also { data ->
                    if (data == null) {
                        throw ApiThrowable(responseDto.status, "服务器数据异常")
                    }
                }
            }
            ApiThrowable.HTTP_ERROR_ROLES_CHANGED -> {
                //老师用户转为 普通用户 时，需要清除登录信息，并回到首页
                ToastUtil.showToast(responseDto.info.toDefault(""))

                throw ApiThrowable(responseDto.status, responseDto.info.toDefault(""))
            }
            ApiThrowable.HTTP_ERROR_USER_RESET_LOGIN -> {
                //用户数据出现错误，需要重新登录
                ToastUtil.showToast(responseDto.info.toDefault(""))
                throw ApiThrowable(responseDto.status, responseDto.info.toDefault(""))
            }

            else -> throw ApiThrowable(responseDto.status, responseDto.info.toDefault(""))
        }
    }
}

