package com.tzh.mylibrary.network

import android.util.ArrayMap
import androidx.lifecycle.LifecycleOwner
import com.tzh.mylibrary.dto.TranslateDto
import com.tzh.mylibrary.util.TranslateUtil
import com.uber.autodispose.ObservableSubscribeProxy

object LibNetWorkApi {
    init {
        HttpHelper.onBindingInterface(LibNetWorkInterface::class.java)
    }


    /**
     * 翻译
     */
    fun translate(owner: LifecycleOwner,text : String,from : String,to : String): ObservableSubscribeProxy<LibBaseResDto<MutableList<TranslateDto>>> {
        return xHttpRequest<LibNetWorkInterface>().translate(
            ArrayMap<String, Any>().apply {
                //翻译内容
                put("q",text)
                //翻译源语言
                put("from",from)
                //翻译目标语言
                put("to",to)
                //APPID
                put("appid", TranslateUtil.appId)
                val salt = TranslateUtil().getText()
                //随机数
                put("salt",salt)
                //签名
                put("sign", TranslateUtil().translateMD5(text,salt))
            }
        ).xWithDefault(owner)
    }

}