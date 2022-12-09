package com.tzh.myapplication.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.LinkedHashMap

/**
 * update by xz
 * 因 CommonInterceptor 有请求并发冲突，
 * 修改为 ParamsInterceptor
 */
class ParamsInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()

        // 添加新的参数
        val httUrlBuilder = oldRequest.url
            .newBuilder()
            .scheme(oldRequest.url.scheme)
            .host(oldRequest.url.host)

        //参数处理,以后再加入签名验证
        httUrlBuilder.build()
        httUrlBuilder.addQueryParameter("siteid", "3")
        val httpUrlSign = httUrlBuilder.build()

        val map = LinkedHashMap<String, Any?>()

        httpUrlSign.queryParameterNames.forEach {
            var param: String? = null
            httpUrlSign.queryParameter(it)?.also { value ->
                if (value.contains(" ") || value.contains("\n")) {
                    //去掉请求中 空格，回车
                    param = value.trim().replace("\n", "")
                    httUrlBuilder.removeAllQueryParameters(it)
                    httUrlBuilder.addQueryParameter(it, param)
                } else {
                    param = value
                }
            }
            map[it] = param
        }

        val newRequestBuild = oldRequest.newBuilder()
            .method(oldRequest.method, oldRequest.body)
            .url(httUrlBuilder.build())

        val newRequest = newRequestBuild
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", "zh")
            .build()
        val response = chain.proceed(newRequest)
        return response.newBuilder()
            .build()

    }


}