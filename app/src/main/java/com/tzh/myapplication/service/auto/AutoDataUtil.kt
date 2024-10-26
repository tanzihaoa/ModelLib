package com.tzh.myapplication.service.auto

import com.tzh.myapplication.utils.SPUtil

object AutoDataUtil {

    /**
     * 设置监听APP包名
     */
    fun setApps(){
        SPUtil.getInstance().putString("apps","com.tencent.mm,com.tencent.mobileqq,com.eg.android.AlipayGphone,com.jingdong.app.mall,com.unionpay,com.synjones.xuepay.sdu")
    }

    /**
     * 获取监听APP包名
     */
    fun getApps() : String{
        return SPUtil.getInstance().getString("apps","com.tencent.mm,com.tencent.mobileqq,com.eg.android.AlipayGphone,com.jingdong.app.mall,com.unionpay,com.synjones.xuepay.sdu")
    }
}