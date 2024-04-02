package com.tzh.mylibrary.util.pay

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tzh.mylibrary.util.stringToObj
import com.tzh.mylibrary.util.toDefault
import com.tzh.mylibrary.util.toJsonString


/**
 * 统一支付处理
 */
class PayHelper(val context: Context?) {

    companion object {
        private const val WX_RES_BEAN = "wx_res_bean"

        const val BROADCAST_PAY = "com.bangbangce.com.play"

        @JvmStatic
        fun sendPay(context: Context?, resp: BaseResp?, result: String) {
            context ?: return
            resp ?: return
            val intent = Intent(BROADCAST_PAY)
            intent.putExtra(WX_RES_BEAN, PayResBean().also { bean ->
                bean.success = resp.errCode == BaseResp.ErrCode.ERR_OK
                bean.payment = PaymentType.wechat
                if (resp is PayResp) {
                    bean.orderId = resp.prepayId
                }
                bean.wxResult = result
            }.toJsonString())

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        /**
         * 支付宝支付8000状态码时，需要提示
         */
        @JvmStatic
        fun onPayError(context: Context, bean: PayResBean) {
            when (bean.payment) {
                PaymentType.aliPay -> {
                    if (bean.aliState == "8000") {

                    }
                }
                PaymentType.wechat -> {
                    Toast.makeText(context, bean.wxResult, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val mLocalBroadcastManager by lazy {
        context?.let {
            LocalBroadcastManager.getInstance(it)
        }
    }

    private val mPayBroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    it.getStringExtra(WX_RES_BEAN).stringToObj<PayResBean>()?.let { bean ->
                        //当 请求支付的 订单id 与 支付完成的 订单id一致时才能有回调
                        if (bean.orderId != null && mPayOrderId != null && TextUtils.equals(bean.orderId, mPayOrderId)) {
                            payListener?.onPayRes(bean)
                        }
                    }
                }
            }
        }
    }

    /**
     * 正在执行支付的 订单id/预订单号 /支付交易会话ID
     */
    private var mPayOrderId: String? = null

    /**
     * 支付回调监听
     */
    var payListener: OnPayListener? = null

    /**
     * 注册
     */
    fun register() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BROADCAST_PAY)
        mLocalBroadcastManager?.registerReceiver(mPayBroadcastReceiver, intentFilter)
    }

    /**
     * 接触 注册
     */
    fun unRegister() {
        mLocalBroadcastManager?.unregisterReceiver(mPayBroadcastReceiver)
        payListener = null
    }

    /**
     * 开始微信支付
     */
    fun onWxPay(payDto: WechatPayDto) {
        context ?: return
        mPayOrderId = payDto.prepayid
        WxPayUtil.startWechatPay(context, payDto)
    }

    /**
     * 开始支付支付
     * @param payContent 支付宝支付需要的支付串
     * @param payOrder 正在执行支付的 订单id/预订单号 /支付交易会话ID
     */
    fun onAliPay(activity: Activity?, payContent: String?, payOrder: String?) {
        activity ?: return
        payContent ?: return
        payOrder ?: return
        AliPayTask(activity, payListener).execute(PayReqBean(payContent, payOrder))
    }

    interface OnPayListener {
        fun onPayRes(res: PayResBean)
    }


    data class PayReqBean(
        var aliPayContent: String? = null,
        var orderId: String? = null
    )

    data class PayResBean(
        /**
         * 正在执行支付的 订单id/预订单号 /支付交易会话ID
         */
        var orderId: String? = null,
        /**
         * 支付方式
         */
        var payment: String? = null,
        /**
         * 是否成功
         */
        var success: Boolean = false,
        /**
         * 微信返回的支付信息
         */
        var wxResult: String? = null,
        /**
         * 支付宝返回的支付信息
         */
        var aliState: String? = null,
    )

    private class AliPayTask(val activity: Activity, var payListener: OnPayListener? = null) : AsyncTask<PayReqBean, Void, PayResBean>() {

        override fun doInBackground(vararg params: PayReqBean?): PayResBean {
            val payResBean = PayResBean("-1")
            params[0]?.let {
                payResBean.orderId = it.orderId
                // 调用支付接口，获取支付结果
                val result = PayTask(activity).pay(it.aliPayContent.toDefault(""), false)
                Log.d("alipay result: ", result)
                if (StringUtils.isValid(result)) {
                    var resultStatus: String? = null
                    val resultParams = result.split(";").toTypedArray()
                    for (resultParam in resultParams) {
                        if (resultParam.startsWith("resultStatus")) {
                            val prefix = "resultStatus={"
                            resultStatus = resultParam.substring(resultParam.indexOf(prefix) + prefix.length, resultParam.lastIndexOf("}"))
                            break
                        }
                    }
                    payResBean.aliState = resultStatus
                }
            }
            return payResBean
        }


        override fun onPostExecute(bean: PayResBean) {
            bean.payment = PaymentType.aliPay
            when (bean.aliState) {
                "9000" -> {
                    bean.success = true
                }
                "8000" -> {
                    bean.success = false
                }
                "6001" -> {
                    bean.success = false
                    Toast.makeText(activity,"已取消支付", Toast.LENGTH_LONG).show()
                }
                else -> {
                    bean.success = false
                    Toast.makeText(activity,String.format("支付失败，错误码：%s", bean.aliState), Toast.LENGTH_LONG).show()
                }
            }
            payListener?.onPayRes(bean)
        }

    }
}

