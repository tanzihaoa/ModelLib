package com.tzh.mylibrary.util.pay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tzh.mylibrary.util.stringToObj
import com.tzh.mylibrary.util.toDefault
import com.tzh.mylibrary.util.toJsonString

/**
 * 微信支付 帮助类
 * 不能提成全局使用，每次都需要 new
 */
object WxPayHelper {
    /**
     * 微信返回的参数 支付实体
     */
    private const val WX_PAY_RESP = "wx_pay_resp"

    private const val BROADCAST_PAY = "WxPayHelper"

    /**
     * 微信支付成功，通过发广播来回调给 发起支付的页面
     */
    @JvmStatic
    fun sendWeiXinPayResp(context: Context?, resp: BaseResp?) {
        context ?: return
        resp ?: return
        //这里只处理微信支付的回调
        if (resp.type != ConstantsAPI.COMMAND_PAY_BY_WX) {
            return
        }
        //将微信的baseResp 转成 payResp 能拿到 预订单id,
        //通过预订单id 发送对应的广播，保证发起支付对象的 预订单id，和 支付结果的 预订单id一致
        val payResp = (resp as? PayResp)
        //toDefault 使用来判空的
        val intent = Intent(BROADCAST_PAY + payResp?.prepayId.toDefault(""))
        //转成json 字符串 来发送
        intent.putExtra(WX_PAY_RESP, payResp.toJsonString())
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    @JvmStatic
    fun get(activity: AppCompatActivity): WxPayRequestHelper {
        val helper = WxPayRequestHelper(activity)
        activity.lifecycle.addObserver(helper)
        return helper
    }

    @JvmStatic
    fun get(fragment: Fragment): WxPayRequestHelper {
        val helper = WxPayRequestHelper(fragment.context)
        fragment.lifecycle.addObserver(helper)
        return helper
    }

    @JvmStatic
    fun get(owner: LifecycleOwner, context: Context): WxPayRequestHelper {
        val helper = WxPayRequestHelper(context)
        owner.lifecycle.addObserver(helper)
        return helper
    }


    class WxPayRequestHelper(private val context: Context?) : LifecycleEventObserver {

        /**
         *  支付回调
         */
        private var mPayCallback: OnPayCallback? = null

        /**
         * 广播通知
         */
        private var mBroadcastReceiver: BroadcastReceiver? = null


        private val mLocalBroadcastManager by lazy { context?.let { LocalBroadcastManager.getInstance(it) } }


        /**
         * 正在执行支付的 订单id/预订单号 /支付交易会话ID
         */
        private var mPrepayId: String? = null

        /**
         * 生命周期发送变化时
         */
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                mBroadcastReceiver?.let {
                    mLocalBroadcastManager?.unregisterReceiver(it)
                }
                mBroadcastReceiver = null
                mPayCallback = null
            }
        }

        /**
         * 注册广播
         */
        private fun onRegisterBroadcast() {
            if (mBroadcastReceiver != null) {
                return
            }
            mBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent == null) {
                        mPayCallback?.onError(mPrepayId.toDefault(""), "-1", Throwable("未返回支付结果"))
                        return
                    }
                    val payResp = intent.getStringExtra(WX_PAY_RESP).stringToObj<PayResp>()
                    if (payResp == null) {
                        mPayCallback?.onError(mPrepayId.toDefault(""), "-1", Throwable("未返回支付结果"))
                        return
                    }
                    if (mPrepayId == null || payResp.prepayId != mPrepayId) {
                        mPayCallback?.onError(mPrepayId.toDefault(""), "-1", Throwable("订单号错误"))
                        return
                    }
                    //当 请求支付的 订单id 与 支付完成的 订单id一致时才能有回调
                    when (payResp.errCode) {
                        BaseResp.ErrCode.ERR_OK -> mPayCallback?.onSuccess(mPrepayId.toDefault(""))
                        BaseResp.ErrCode.ERR_USER_CANCEL -> mPayCallback?.onCancel(mPrepayId.toDefault(""))
                        else -> mPayCallback?.onError(
                            mPrepayId.toDefault(""),
                            payResp.errCode.toString(),
                            Throwable(String.format("%s，错误码：%d", payResp.errStr.toDefault(""), payResp.errCode))
                        )
                    }
                    //收到广播通知后，及时解出注册
                    mBroadcastReceiver?.let {
                        mLocalBroadcastManager?.unregisterReceiver(it)
                    }
                    mBroadcastReceiver = null
                }
            }
            mBroadcastReceiver?.let { broadcastReceiver ->
                val intentFilter = IntentFilter()
                //这里用预订单id 做成action，保证唯一性
                intentFilter.addAction(BROADCAST_PAY + mPrepayId.toDefault(""))
                mLocalBroadcastManager?.registerReceiver(broadcastReceiver, intentFilter)
            }
        }

        /**
         *  设置回调
         */
        fun setCallback(callback: OnPayCallback): WxPayRequestHelper {
            mPayCallback = callback
            return this
        }

        /**
         * 微信支付
         */
        fun onPay(wxDto: WxPayRequestData?) {
            if (wxDto == null) {
                mPayCallback?.onError("", "-1", Throwable("订单生成错误:无支付参数"))
                return
            }
            onPay(wxDto.getWxAppId(), wxDto.getWxNonceStr(), wxDto.getWxPartnerId(), wxDto.getWxPrepayId(), wxDto.getWxPck(), wxDto.getWxTimestamp(), wxDto.getWxSign())
        }


        /**
         * 微信支付
         * @param appId 微信开放平台 appId
         * @param nonceStr 随机字符串,随机字符串，不长于32位。推荐随机数生成算法
         * @param partnerId 微信支付分配的商户号
         * @param prepayId 微信返回的支付交易会话ID
         * @param pck 固定值
         * @param timestamp 时间戳，请见接口规则-参数规定
         * @param sign  签名，详见签名生成算法注意：签名方式一定要与统一下单接口使用的一致
         */
        fun onPay(appId: String?, nonceStr: String?, partnerId: String?, prepayId: String?, pck: String?, timestamp: String?, sign: String?) {
            if (context == null) {
                mPayCallback?.onError(prepayId.toDefault(""), "-1", Throwable("订单生成错误:context不存在"))
                return
            }
            if (appId.isNullOrEmpty()) {
                mPayCallback?.onError(prepayId.toDefault(""), "-1", Throwable("订单生成错误:appId"))
                return
            }
            if (nonceStr.isNullOrEmpty()) {
                mPayCallback?.onError(prepayId.toDefault(""), "-1", Throwable("订单生成错误:nonceStr"))
                return
            }
            if (partnerId.isNullOrEmpty()) {
                mPayCallback?.onError(prepayId.toDefault(""), "-1", Throwable("订单生成错误:partnerId"))
                return
            }
            if (prepayId.isNullOrEmpty()) {
                mPayCallback?.onError("", "-1", Throwable("订单生成错误:prepayId"))
                return
            }
            if (pck.isNullOrEmpty()) {
                mPayCallback?.onError(prepayId.toDefault(""), "-1", Throwable("订单生成错误:pck"))
                return
            }
            if (timestamp.isNullOrEmpty()) {
                mPayCallback?.onError(prepayId.toDefault(""), "-1", Throwable("订单生成错误:timestamp"))
                return
            }
            if (sign.isNullOrEmpty()) {
                mPayCallback?.onError(prepayId.toDefault(""), "-1", Throwable("订单生成错误:sign"))
                return
            }
            mPrepayId = prepayId

            //这里的appid，替换成自己的即可
            val wxApi = WXAPIFactory.createWXAPI(context, appId)
            wxApi.registerApp(appId)

            if (!wxApi.isWXAppInstalled) {
                mPayCallback?.onError(prepayId, "-1", Throwable("请先安装微信App"))
                return
            }

            val payRequest = PayReq()
            payRequest.appId = appId
            ////随机字符串,随机字符串，不长于32位。推荐随机数生成算法
            payRequest.nonceStr = nonceStr
            ////微信支付分配的商户号
            payRequest.partnerId = partnerId
            ////微信返回的支付交易会话ID
            payRequest.prepayId = prepayId
            //固定值
            payRequest.packageValue = pck
            //时间戳，请见接口规则-参数规定
            payRequest.timeStamp = timestamp
            //签名，详见签名生成算法注意：签名方式一定要与统一下单接口使用的一致
            payRequest.sign = sign
            //发起请求，调起微信前去支付
            val isSendReq = wxApi.sendReq(payRequest)
            if (!isSendReq) {
                mPayCallback?.onError(prepayId, "-1", Throwable("微信支付请求发送失败"))
            }
            onRegisterBroadcast()
        }

    }

    /**
     * 选择回调
     */
    abstract class OnPayCallback() {
        /**
         * 成功
         * @param prepayId 预定单id/订单id
         */
        abstract fun onSuccess(prepayId: String)

        /**
         * @param prepayId 预定单id/订单id
         */
        open fun onCancel(prepayId: String) {
            onFailure(prepayId, Throwable("订单已取消"))
        }

        /**
         * @param prepayId 预定单id/订单id
         * @param code 错误码
         * @param throwable 错误信息
         */
        open fun onError(prepayId: String, code: String, throwable: Throwable) {
            onFailure(prepayId, throwable)
        }

        /**
         * 取消支付和支付错误 都是属于 支付失败，所以统一了出口
         * @param prepayId 预定单id/订单id
         */
        open fun onFailure(prepayId: String, throwable: Throwable) {}
    }

    /**
     *  实体类继承这个 可以直接使用
     */
    interface WxPayRequestData {
        /**
         * 微信AppId
         */
        fun getWxAppId(): String?

        /**
         * 微信随机字符串
         */
        fun getWxNonceStr(): String?

        /**
         * 商户id
         */
        fun getWxPartnerId(): String?

        /**
         * 预订单id
         */
        fun getWxPrepayId(): String?

        /**
         * 固定字符串
         */
        fun getWxPck(): String?

        /**
         * 时间戳
         */
        fun getWxTimestamp(): String?

        /**
         * 支付签名
         */
        fun getWxSign(): String?
    }
}

