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
     * 大师-好物推荐-列表数据
     */
    fun masterShopList(owner: LifecycleOwner, p: Int, keywords: String?, attrid: String?, modelid: String):
            ObservableSubscribeProxy<BaseResDto<BaseResPageDto<MasterShopListDto>>> {
        return xHttpRequest<NetWorkInterface>().masterShopList(
            ArrayMap<String, Any>().apply {
                //当前页数
                put("p", p)
                //每页数量
                put("num", HttpHelper.PAGE_LIMIT_20)
                //关键词搜索
                if (!keywords.isNullOrEmpty()) {
                    put("keywords", keywords)
                }
                //商品属性id
                if (!attrid.isNullOrEmpty()) {
                    put("attrid", attrid)
                }
                //模型[Product详测卖货|Quick闪测卖货|Topic话题卖货]
                put("modelid", modelid)
            }
        ).xWithDefault(owner)
    }
}