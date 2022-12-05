package com.tzh.myapplication.base

import android.content.Context
import android.net.ConnectivityManager
import androidx.multidex.MultiDex
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tzh.myapplication.view.load.AppLoadLayout
import com.tzh.myapplication.view.load.AppRefreshLayout
import com.tzh.mylibrary.base.BaseApplication

class MyApplication : BaseApplication() {

    val context by lazy {
        applicationContext
    }

    companion object {
        init {
            //关闭彩蛋
            SmartRefreshLayout.setDefaultRefreshInitializer { _, layout -> layout.layout.tag = "close egg" }
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> //全局设置主题颜色
                //  layout.setEnableHeaderTranslationContent(true);
                AppRefreshLayout(context)
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> //  layout.setEnableFooterTranslationContent(false);
                //指定为经典Footer，默认是 BallPulseFooter
                AppLoadLayout(context)
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        isNetwork()
    }

    /**
     * 这里会在onCreate之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun isNetwork(): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isAvailable ?: false
    }


}