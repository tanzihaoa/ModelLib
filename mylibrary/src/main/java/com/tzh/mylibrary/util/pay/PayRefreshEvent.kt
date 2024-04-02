package com.tzh.mylibrary.util.pay

import android.content.Context
import android.widget.Toast

/**
 * 支付刷新event
 */

data class PayRefreshEvent(

    /**
     * 是否成功
     */
    var success: Boolean = false,

    /**
     * 支付方式
     *
     */
    var payment: String = "",

    /**
     * 支付宝状态码
     */
    var status: String = "",


    /**
     * 微信返回的result
     */
    var resultTxt: String = ""
) {


    var orderId: String? = null

    /**
     * 支付宝支付8000状态码时，需要提示
     */
    fun onError(context: Context) {
        when (payment) {
            PaymentType.aliPay -> {
                if (status == "8000") {
                    Toast.makeText(context,"因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以支付宝通知为准，请稍候根据您的账户余额信息或订单信息来决定是否继续付款", Toast.LENGTH_LONG).show()
                }
            }
            PaymentType.wechat -> {
                Toast.makeText(context,resultTxt,Toast.LENGTH_LONG).show()
            }
        }
    }
}