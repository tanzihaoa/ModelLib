package com.tzh.myapplication.network

import android.util.ArrayMap
import androidx.lifecycle.LifecycleOwner
import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.myapplication.ui.dto.BaseResPageDto
import com.tzh.myapplication.ui.dto.MasterShopListDto
import com.tzh.myapplication.ui.dto.SmsDto
import com.uber.autodispose.ObservableSubscribeProxy

object NetWorkApi {
    init {
        HttpHelper.onBindingInterface(NetWorkInterface::class.java)
    }

    /**
     * 热议列表
     */
    fun masterShopList(owner: LifecycleOwner, p: Int): ObservableSubscribeProxy<BaseResDto<BaseResPageDto<MasterShopListDto>>> {
        return xHttpRequest<NetWorkInterface>().masterShopList(
            ArrayMap<String, Any>().apply {
                //当前页数
                put("p", p)
                //每页数量
                put("num", HttpHelper.PAGE_LIMIT_10)

                put("type", 1)
            }
        ).xWithDefault(owner)
    }

    /**
     * 获取发送任务
     */
    fun httpSmsTaskGet(owner: LifecycleOwner, appMobile : String): ObservableSubscribeProxy<BaseResDto<SmsDto>> {
        return xHttpRequest<NetWorkInterface>().httpSmsTaskGet(
            ArrayMap<String, Any>().apply {
                //当前手机号码
                put("app_mobile", appMobile)
            }
        ).xWithDefault(owner)
    }

    /**
     * 反馈发送任务结果
     */
    fun httpSmsTaskRet(owner: LifecycleOwner, id : String, status : Int,statusNote : String): ObservableSubscribeProxy<BaseResDto<Any>> {
        return xHttpRequest<NetWorkInterface>().httpSmsTaskRet(
            ArrayMap<String, Any>().apply {
                //短信ID
                put("id", id)
                //1表示发送成功，2表示发送失败
                put("status", status)
                //错误描述
                put("status_note", statusNote)
            }
        ).xWithDefault(owner)
    }
}