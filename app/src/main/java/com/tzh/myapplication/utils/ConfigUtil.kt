package com.tzh.myapplication.utils


object ConfigUtil {
    private const val CONFIG_Mobile = "CONFIG_Mobile"

    /**
     * 是否保存了手机号码
     */
    fun isMobile() : Boolean{
        return SPUtil.getInstance().getString(CONFIG_Mobile).length == 11
    }

    /**
     * 设置手机号码
     */
    fun setMobile(mobile : String){
        SPUtil.getInstance().putString(CONFIG_Mobile,mobile)
    }

    /**
     * 获取设置的手机号码
     */
    fun getMobile() : String{
        return SPUtil.getInstance().getString(CONFIG_Mobile)
    }
}