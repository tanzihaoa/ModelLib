package com.tzh.myapplication.network

import android.util.ArrayMap
import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.myapplication.ui.dto.BaseResPageDto
import com.tzh.myapplication.ui.dto.MasterShopListDto
import com.tzh.myapplication.ui.dto.SmsDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface NetWorkInterface {
    /**
     * 大师-好物推荐-列表数据
     */
    @POST("bdc/content/topic_list")
    fun masterShopList(@QueryMap arrayMap: ArrayMap<String, Any>): Observable<BaseResDto<BaseResPageDto<MasterShopListDto>>>

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
}