package com.tzh.myapplication.network

import android.util.ArrayMap
import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.myapplication.ui.dto.SmsDto
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface NetWorkInterface {

    /**
     * 获取发送任务
     */
    @POST("/sms/httpSmsTaskGet")
    fun httpSmsTaskGet(@QueryMap arrayMap: ArrayMap<String, Any>): Observable<BaseResDto<SmsDto>>

    /**
     * 反馈发送任务结果
     */
    @POST("/sms/httpSmsTaskRet")
    fun httpSmsTaskRet(@QueryMap arrayMap: ArrayMap<String, Any>): Observable<BaseResDto<Any>>

    /**
     * 获取收信内容
     */
    @POST("/sms/httpSmsInbox")
    fun httpSmsInbox(@QueryMap arrayMap: ArrayMap<String, Any>): Observable<BaseResDto<Any>>
}