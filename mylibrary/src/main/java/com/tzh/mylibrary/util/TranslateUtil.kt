package com.tzh.mylibrary.util

import kotlin.random.Random


class TranslateUtil {
    companion object{
        const val appId = "20231213001909212"

        const val key = "5cnWoa6Rj1YccYMQFL1_"

        //数字
        const val num = "0123456789"
        //小写字母
        const val smallLetter = "abcdefghijklmnopqrstuvwxyz"
        //大写字母
        const val bigLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }

    fun getText() : String{
        //字符
        var password = ""
        val passwordText = num + smallLetter + bigLetter
        val length = 16
        //循环取
        for(index in 1..length){
            val num : Int = Random.nextInt(passwordText.length - 1)
            password += passwordText.substring(num,num+1)
        }
        return password
    }

    fun translateMD5(text : String,salt : String) : String{
        return SignUtil.encryption(appId+text+salt+key)
    }
}