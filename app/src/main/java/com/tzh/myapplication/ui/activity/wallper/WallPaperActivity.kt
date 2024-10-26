package com.tzh.myapplication.ui.activity.wallper

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.angcyo.tablayout.delegate.ViewPager1Delegate
import com.sunshine.retrofit.HttpUtil.SingletonBuilder
import com.sunshine.retrofit.cacahe.CacheProvide
import com.sunshine.retrofit.interceptor.CacheInterceptor
import com.sunshine.retrofit.interceptor.DownLoadInterceptor
import com.sunshine.retrofit.interfaces.ParamsInterceptor
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityWallPaperBinding
import com.tzh.myapplication.network.http.InterfaceSet
import com.tzh.myapplication.network.http.NetGo
import com.tzh.myapplication.network.http.NetGoBase
import com.tzh.mylibrary.util.DpToUtil
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.toDefault
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class WallPaperActivity : AppBaseActivity<ActivityWallPaperBinding>(R.layout.activity_wall_paper){
    companion object {
        @JvmStatic
        fun start(context : Context) {
            context.startActivity(Intent(context, WallPaperActivity::class.java))
        }
    }

    private val mParamsInterceptor =
        ParamsInterceptor { params -> params }
    override fun initView() {

        /**
         * 当前选择的服务器
         */
        /**
         * 当前选择的服务器
         */
        val curren_Base_url: String = NetGoBase.getBaseUrl()
        ///网络接口请求需要的配置
        ///网络接口请求需要的配置
        val singletonBuilder = SingletonBuilder(this, curren_Base_url)
            .addServerUrl(NetGoBase.BASE_URL_LIST.get(0))
            .addServerUrl(NetGoBase.BASE_URL_LIST.get(1))
            .addServerUrl(NetGoBase.BASE_URL_LIST.get(2))
            .addServerUrl(NetGoBase.BASE_URL_LIST.get(3))
            .addServerUrl(NetGoBase.BASE_URL_LIST.get(4))
            .addServerUrl(NetGoBase.BASE_URL_LIST.get(5))
        val cacheProvide = CacheProvide(this)
        singletonBuilder.client(
            OkHttpClient.Builder()
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cacheProvide.provideCache())
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(DownLoadInterceptor(curren_Base_url))
                .addNetworkInterceptor(CacheInterceptor()).build()
        )
        singletonBuilder.paramsInterceptor(mParamsInterceptor).build() // 不传不进行参数统一处理

    }

    override fun initData() {
        binding.root.postDelayed({
            NetGo.getLastVersion(object : InterfaceSet.NetGoCallback{
                override fun done(api: String, result: String) {

                }

                override fun error(api: String, response: String) {

                }
            })
            NetGo.getCateList(object : InterfaceSet.NetGoCallback{
                override fun done(api: String, result: String) {
                    runOnUiThread {
                        LogUtils.e("结果====",result)
                        try {
                            val resJo = JSONObject.parseObject(result)
                            if (resJo.containsKey("cateList")) {
                                //获取主题分类数据
                                val cateInfoList = JSONArray.parseArray(
                                    resJo.getString("cateList"),
                                    WallCate::class.java
                                )
                                setList(cateInfoList)
                            }

                        } catch (e: Exception) {

                        }
                    }

                }

                override fun error(api: String, response: String) {}
            })
        },200)
    }

    fun setList(list : MutableList<WallCate>){
        val fragmentList = mutableListOf<Fragment>()
        for(dto in list){
            val tv = TextView(this)
            tv.gravity = Gravity.CENTER
            tv.setPadding(DpToUtil.dip2px(tv.context, 20f), 0, DpToUtil.dip2px(tv.context, 20f), 0)
            tv.text = dto.name
            binding.tabLayout.addView(tv)
            fragmentList.add(SortFragment.getInstance(dto.cateId.toDefault("")))
        }

        ViewPager1Delegate.install(binding.viewPager, binding.tabLayout, true)
//        binding.viewPager.offscreenPageLimit = fragmentList.size
        binding.viewPager.adapter = ViewPageAdapter(supportFragmentManager,fragmentList)
    }
}