package com.tzh.mylibrary.util.pay

import androidx.annotation.StringDef

/**
 * 支付方式
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@StringDef(PaymentType.wechat, PaymentType.aliPay, PaymentType.credit)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class PaymentType {
    companion object{
        /**
         * 支付方式-微信支付
         */
        const val wechat = "Wechat"

        /**
         * 支付方式-支付宝支付
         */
        const val aliPay = "Aliwap"

        /**
         * 支付方式-余额支付
         */
        const val credit = "Credit"

    }
}