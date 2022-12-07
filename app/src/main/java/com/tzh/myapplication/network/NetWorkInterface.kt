package com.tzh.myapplication.network

import android.util.ArrayMap
import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.myapplication.ui.dto.BaseResPageDto
import com.tzh.myapplication.ui.dto.MasterShopListDto
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
}