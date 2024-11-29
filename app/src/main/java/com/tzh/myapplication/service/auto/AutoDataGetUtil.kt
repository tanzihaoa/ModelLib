package com.tzh.myapplication.service.auto

import com.tzh.myapplication.utils.DateTime
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.LogUtils

/**
 * 根据页面数据获取数据
 */
object AutoDataGetUtil {

    /**
     * 获取微信账单页面数据
     */
    fun getData(dataInfo : MutableList<String>) : AutoDataDto{
        LogUtils.e("数据=====", GsonUtil.GsonString(dataInfo))
        val dto = AutoDataDto()
        try {
            if(dataInfo[1].replace(" ","").toFloat() != 0f){
                dto.name = dataInfo[0]
                dto.money = dataInfo[1].replace(" ","").toFloat()

                if(dataInfo.indexOf("当前状态") > 0){
                    dto.type = dataInfo[dataInfo.indexOf("当前状态") + 1].replace(" ","")
                }

                if(dataInfo.indexOf("支付时间") > 0){
                    dto.time = dataInfo[dataInfo.indexOf("支付时间") + 1].replace(" ","")
                }
            }
            return dto
        }catch (e : Exception){
            LogUtils.e("失败=====", GsonUtil.GsonString(e))
            return dto
        }
    }

    /**
     * 获取微信支付页面数据
     */
    fun getWxPlay(dataInfo : MutableList<String>) : AutoDataDto{
        LogUtils.e("数据=====", GsonUtil.GsonString(dataInfo))
        val dto = AutoDataDto()

        for(info in dataInfo){
            if(info.contains("￥")){
                dto.money = -info.replace("￥","").toFloat()
            }

            if(info.contains("待") && info.contains("确认收款")){
                //转账类型
                dto.remark = "向"+info.replace("待","").replace("确认收款","")+"转账"
            }

        }

        dto.type = "1"
        dto.time = DateTime.getNowTime()


        return dto
    }

    /**
     * 支付宝账单详情解析
     */
    fun getAliPayData(dataInfo : MutableList<String>) : AutoDataDto{
        LogUtils.e("数据=====", GsonUtil.GsonString(dataInfo))
        val dto = AutoDataDto()
        try {
            if(dataInfo[1].replace(" ","").replace("元","").replace("支出","").toFloat() != 0f){
                dto.name = dataInfo[0]

                val money = dataInfo[1].replace(" ","").replace("元","")
                if(money.contains("支出")){
                    //支出
                    dto.money = -money.replace("支出","").toFloat()
                    dto.type = "支出"
                }else{
                    //收入
                    dto.money = money.toFloat()
                    dto.type = "收入"
                }

                if(dataInfo.indexOf("支付时间") > 0){
                    dto.time = dataInfo[dataInfo.indexOf("支付时间") + 1].replace(" ","")
                }

                if(dataInfo.indexOf("收款方全称") > 0){
                    dto.remark = dataInfo[dataInfo.indexOf("收款方全称") + 1].replace(" ","")
                }
            }
            return dto
        }catch (e : Exception){
            LogUtils.e("失败=====", GsonUtil.GsonString(e))
            return dto
        }
    }
}