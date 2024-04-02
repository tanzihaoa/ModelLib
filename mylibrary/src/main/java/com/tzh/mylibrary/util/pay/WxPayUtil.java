package com.tzh.mylibrary.util.pay;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信工具
 *
 * @author xz
 */
public class WxPayUtil {
    public static void startWechatPay(Context context, WechatPayDto payDto,CloseDialogLister lister) {
        if (!isWxAppInstalled(context,payDto.getWxAppId())) {
            Toast.makeText(context,"请先安装微信App",Toast.LENGTH_LONG).show();
            if(lister!=null){
                lister.result(true);
            }
            return;
        }
        //这里的appid，替换成自己的即可
        IWXAPI api = WXAPIFactory.createWXAPI(context, payDto.getAppid());
        api.registerApp(payDto.getAppid());

        PayReq payRequest = new PayReq();
        payRequest.appId = payDto.getAppid();
        ////随机字符串,随机字符串，不长于32位。推荐随机数生成算法
        payRequest.nonceStr = payDto.getNoncestr();
        ////微信支付分配的商户号
        payRequest.partnerId = payDto.getPartnerid();
        ////微信返回的支付交易会话ID
        payRequest.prepayId = payDto.getPrepayid();
        //固定值
        payRequest.packageValue = payDto.getPackageX();
        //时间戳，请见接口规则-参数规定
        payRequest.timeStamp = payDto.getTimestamp() + "";
        //签名，详见签名生成算法注意：签名方式一定要与统一下单接口使用的一致
        payRequest.sign = payDto.getSign();
        //发起请求，调起微信前去支付
        api.sendReq(payRequest);
    }

    /**
     * 调支付的方法
     * 注意： 每次调用微信支付的时候都会校验 appid 、包名 和 应用签名的。 这三个必须保持一致才能够成功调起微信
     *
     * @param context context
     * @param payDto  微信支付返回数据
     */
    public static void startWechatPay(Context context, WechatPayDto payDto) {
        startWechatPay(context,payDto,null);
    }

    /**
     * 微信授权
     */
    public static void wxLogin(Context context,String appId) {
        if (!isWxAppInstalled(context,appId)) {
            Toast.makeText(context,"请先安装微信App",Toast.LENGTH_LONG).show();
            return;
        }
        //这里的appid，替换成自己的即可
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(context,"您未安装微信，请先安装微信!",Toast.LENGTH_LONG).show();
        } else {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wallpaper_login";
            api.sendReq(req);
        }
    }

    /**
     * 签名
     *
     * @param req req
     * @return
     */
    private static String getSign(PayReq req) {
        SortedMap<String, Object> signParams = new TreeMap<>();
        signParams.put("appid", req.appId);
        signParams.put("noncestr", req.nonceStr);
        signParams.put("package", req.packageValue);
        signParams.put("partnerid", req.partnerId);
        signParams.put("prepayid", req.prepayId);
        signParams.put("timestamp", req.timeStamp);

        return genPackageSign(signParams).toUpperCase();
    }

    /**
     * 生成签名
     */
    private static String genPackageSign(SortedMap<String, Object> params) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> map : params.entrySet()) {
            sb.append(map.getKey());
            sb.append('=');
            sb.append(map.getValue());
            sb.append('&');
        }
        //最后需要拼接 商户的 key
        sb.append("key=");
        sb.append(params.get("partnerid"));
        Log.d("genPackageSign", sb.toString());
        return MD5EncryptUtils.MD5Encode(sb.toString(), "UTF-8");
    }

    /**
     * 生成时间戳
     *
     * @return
     */
    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 生成随机参数
     *
     * @return
     */
    private static String genNonceStr() {
        Random random = new Random();
        return MD5EncryptUtils.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }

    /**
     * 判断微信是否安装
     *
     * @param context
     * @return true 已安装   false 未安装
     */
    public static boolean isWxAppInstalled(Context context,String WxAPPId) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(WxAPPId);
        return wxApi.isWXAppInstalled();
    }

    public interface CloseDialogLister{
        void result(Boolean isClose);
    }



}
