package com.tzh.myapplication.network

import android.util.ArrayMap
import androidx.lifecycle.LifecycleOwner
import com.tzh.myapplication.ui.dto.BaseResDto
import com.tzh.myapplication.ui.dto.BaseResPageDto
import com.tzh.myapplication.ui.dto.MasterShopListDto
import com.uber.autodispose.ObservableSubscribeProxy

object NetWorkApi {
    init {
        HttpHelper.onBindingInterface(NetWorkInterface::class.java)
    }

    /**
     * 热议列表
     */
    fun masterShopList(owner: LifecycleOwner, p: Int):
            ObservableSubscribeProxy<BaseResDto<BaseResPageDto<MasterShopListDto>>> {
        return xHttpRequest<NetWorkInterface>().masterShopList(
            ArrayMap<String, Any>().apply {
                //当前页数
                put("p", p)
                //每页数量
                put("num", HttpHelper.PAGE_LIMIT_10)

                put("type", 1)
            }
        ).xWithDefault(owner)
    }
}