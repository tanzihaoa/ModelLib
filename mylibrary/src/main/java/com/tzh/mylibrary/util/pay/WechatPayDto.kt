package com.tzh.mylibrary.util.pay

import com.google.gson.annotations.SerializedName

/**
 * 微信支付dto
 */
data class WechatPayDto(
    var appid: String? = null,
    val partnerid: String? = null,
    val timestamp: String? = null,
    val noncestr: String? = null,
    @SerializedName("package")
    val packageX: String? = null,
    val prepayid: String? = null,
    val signtype: String? = null,
    val sign: String? = null
) : WxPayHelper.WxPayRequestData {
    override fun getWxAppId(): String? = appid
    override fun getWxNonceStr(): String? = noncestr
    override fun getWxPartnerId(): String? = partnerid
    override fun getWxPrepayId(): String? = prepayid
    override fun getWxPck(): String? = packageX
    override fun getWxTimestamp(): String? = timestamp
    override fun getWxSign(): String? = sign
}
