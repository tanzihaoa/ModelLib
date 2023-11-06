package com.tzh.myapplication.network

import android.util.ArrayMap
import androidx.lifecycle.LifecycleOwner
import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.myapplication.ui.dto.SmsDto
import com.tzh.myapplication.utils.ConfigUtil
import com.uber.autodispose.ObservableSubscribeProxy

object NetWorkApi {
    init {
        HttpHelper.onBindingInterface(NetWorkInterface::class.java)
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

    /**
     * 获取收信内容
     */
    fun httpSmsInbox(owner: LifecycleOwner, mobile : String, content : String,sendAt : String): ObservableSubscribeProxy<BaseResDto<Any>> {
        return xHttpRequest<NetWorkInterface>().httpSmsInbox(
            ArrayMap<String, Any>().apply {
                //当前手机号
                put("app_mobile", ConfigUtil.getMobile())
                //发信手机号
                put("mobile", mobile)
                //信息内容
                put("content", content)
                //发信时间
                put("send_at", sendAt)
            }
        ).xWithDefault(owner)
    }
}