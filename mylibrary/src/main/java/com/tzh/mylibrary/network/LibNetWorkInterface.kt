package com.tzh.mylibrary.network

import android.util.ArrayMap
import com.tzh.mylibrary.dto.TranslateDto
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface LibNetWorkInterface {

    /**
     * 翻译
     */
    @POST("https://fanyi-api.baidu.com/api/trans/vip/translate")
    fun translate(@QueryMap arrayMap: ArrayMap<String, Any>): Observable<LibBaseResDto<MutableList<TranslateDto>>>
}