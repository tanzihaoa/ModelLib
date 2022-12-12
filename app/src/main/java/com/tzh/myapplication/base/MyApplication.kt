package com.tzh.myapplication.base

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.multidex.MultiDex
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tzh.myapplication.ui.service.MediaControllerService
import com.tzh.myapplication.utils.Util
import com.tzh.myapplication.view.load.AppLoadLayout
import com.tzh.myapplication.view.load.AppRefreshLayout

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
        startService()
    }

    private fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0以上系统启动为前台服务, 否则在后台, 测试中发现过几分钟后MediaController监听不到音乐信息
            context.startForegroundService(Intent(context, MediaControllerService::class.java))
        } else {
            context.startService(Intent(context, MediaControllerService::class.java))
        }
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