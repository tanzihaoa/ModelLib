package com.tzh.myapplication.utils

import android.os.Build
import java.util.*

object SystemUtil {

    // 华为
    const val PHONE_HUAWEI = "Huawei"

    // 荣耀
    const val PHONE_HONOR = "HONOR"

    // 华为 NOVA
    const val PHONE_NOVA = "nova"

    // 小米
    const val PHONE_XIAOMI = "xiaomi"

    // vivo
    const val PHONE_VIVO = "vivo"

    // 魅族
    const val PHONE_MEIZU = "Meizu"

    // 索尼
    const val PHONE_SONY = "sony"

    // 三星
    const val PHONE_SAMSUNG = "samsung"

    // OPPO
    const val PHONE_OPPO = "OPPO"

    // 乐视
    const val PHONE_Letv = "letv"

    // 一加
    const val PHONE_OnePlus = "OnePlus"

    // 锤子
    const val PHONE_SMARTISAN = "smartisan"

    // 联想
    const val PHONE_LENOVO = "lenovo"

    // LG
    const val PHONE_LG = "lg"

    // HTC
    const val PHONE_HTC = "htc"

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    fun getSystemLanguage(): String? {
        return Locale.getDefault().language
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    open fun getSystemLanguageList(): Array<Locale?>? {
        return Locale.getAvailableLocales()
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    fun getSystemVersion(): String? {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun getSystemModel(): String? {
        return Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    fun getDeviceBrand(): String? {
        return Build.BRAND
    }

    /**
     * 判断是否是小米手机
     */
    fun isXiaomi() : Boolean{
        if(getDeviceBrand().equals(PHONE_XIAOMI, ignoreCase = true)){
            return true
        }

        return false
    }

    /**
     * 判断是否是华为手机
     */
    fun isHuaWei() : Boolean{
        if(getDeviceBrand().equals(PHONE_HUAWEI, ignoreCase = true)){
            return true
        }

        if(getDeviceBrand().equals(PHONE_HONOR, ignoreCase = true)){
            return true
        }

        if(getDeviceBrand().equals(PHONE_NOVA, ignoreCase = true)){
            return true
        }

        return false
    }
}
