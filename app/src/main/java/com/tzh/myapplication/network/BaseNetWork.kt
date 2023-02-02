package com.tzh.myapplication.network

import com.google.gson.JsonSyntaxException
import com.tzh.mylibrary.util.LogUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.Proxy
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class BaseNetWork : ObservableTransformer<Any?, Any?> {

    init {
        initRetrofit()
    }

    private fun initRetrofit() {
        val client: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(ParamsInterceptor())

        client.proxy(Proxy.NO_PROXY)

        client.addNetworkInterceptor(HttpLoggingInterceptor { message ->
            LogUtils.e(
                "-Http-body-",
                message
            )
        }.setLevel(HttpLoggingInterceptor.Level.BODY))
        val retrofit = Retrofit.Builder().baseUrl(baseUrl())
            .client(client.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 使用RxJava作为回调适配器
            .addConverterFactory(GsonConverterFactory.create()) //使用Gson作为数据解析
            .build()
    }

    companion object {
        fun baseUrl(): String {
            return "https://w.bangbangce.com/rest.php/"
        }

        fun getRetrofit(baseUrl: String): Retrofit {
            val client: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(ParamsInterceptor())

//            client.proxy(Proxy.NO_PROXY)

            client.addNetworkInterceptor(HttpLoggingInterceptor { message ->
                LogUtils.e(
                    "-Http-body-",
                    message
                )
            }.setLevel(HttpLoggingInterceptor.Level.BODY))

            return Retrofit.Builder().baseUrl(baseUrl)
                .client(client.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .addConverterFactory(GsonConverterFactory.create()) //使用Gson作为数据解析
                .build()
        }


        fun getErrorInfo(e: Throwable): String {
            LogUtils.e("","error:" + e.javaClass.simpleName + ",e.getmessage:" + e.message)
            val info: String = when (e) {
                is ConnectException, is NullPointerException -> {
                    "网络连接失败,请检测您网络连接"
                }
                is SocketTimeoutException -> {
                    "网络连接超时,请检测您网络连接"
                }
                is JsonSyntaxException -> {
                    "数据解析错误"
                }
                else -> {
                    "连接失败"
                }
            }
            return info
        }
    }

    override fun apply(upstream: Observable<Any?>): ObservableSource<Any?> {
        return upstream
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}