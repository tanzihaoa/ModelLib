package com.tzh.myapplication.service.auto.Helper;



import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tzh.myapplication.livedata.AutoDataLiveData;
import com.tzh.myapplication.service.SkAccessibilityService;
import com.tzh.myapplication.service.auto.AutoDataDto;
import com.tzh.myapplication.service.auto.AutoDataGetUtil;
import com.tzh.myapplication.service.auto.Helper.pkg.alipay;
import com.tzh.myapplication.service.auto.Helper.pkg.mt;
import com.tzh.myapplication.service.auto.Helper.pkg.baseHelper;
import com.tzh.myapplication.service.auto.Helper.pkg.unionOrjd;
import com.tzh.myapplication.service.auto.Helper.pkg.wechat;
import com.tzh.mylibrary.util.GsonUtil;
import com.tzh.mylibrary.util.LogUtils;
import java.util.ArrayList;
import java.util.List;


public class Analyze implements Runnable {

    public static final int FLAG_WECHAT_PAY_UI = 1;
    //微信支付界面
    public static final int FLAG_ALIPAY_PAY_UI = 2;
    //支付宝支付界面
    public static final int FLAG_WECHAT_PAY_MONEY_UI = 3;
    //微信账单详情
    public static final int FLAG_UNION_PAY_UI = 4;
    //云闪付支付界面
    public static final int FLAG_UNION_PAY_DETAIL_UI = 5;
    //云闪付账单详情
    public static final int FLAG_ALIPAY_PAY_DETAIL_UI = 6;
    //支付宝账单详情
    public static final int FLAG_MT_PAY_DETAIL_UI = 7;
    //美团账单详情
    public static final int FLAG_JD_PAY_DETAIL_UI = 8;
    //京东账单详情
    public static final int FLAG_JD_PAY_UI = 9;
    //京东支付界面
    public static final int FLAG_PDD_DETAIL_UI = 10;
    //拼多多账单详情
    public static final int FLAG_WECHAT_DETAIL_UI_2 = 11;
    //新版微信账单详情

    public static long time;
    public final String packageName;
    public final String className;
    public final AccessibilityNodeInfo nodeInfo;
    public final SkAccessibilityService autoService;

    public Analyze(SkAccessibilityService autoService, String packageName, String className, AccessibilityNodeInfo nodeInfo) {
        this.autoService = autoService;
        this.packageName = packageName;
        this.className = className;
        this.nodeInfo = nodeInfo;

    }

    //验证节点
    public static boolean checkNode(List<String> nodeList, String checkStr, boolean equals) {
        //从最后的数据开始查找
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            String nodeName = nodeList.get(i);
            if (equals) {
                if (!nodeName.equals(checkStr)) {
                    continue;
                }
                return true;
            }
            if (!nodeName.contains(checkStr)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(List<String> list) {
        return list == null || list.size() == 0;
    }

    @Override
    public void run() {
        Log.i("=====","开始分析页面" + packageName + "===" + GsonUtil.GsonString(SkAccessibilityService.ergodicList(autoService, nodeInfo)));
        try {
            if (wechat.findWechatPayUi(packageName, className, nodeInfo)) {
                Log.i("=====","[auto]微信支付界面");
                autoService.useful = true;
                autoService.flag = FLAG_WECHAT_PAY_UI;
            } else if (wechat.findWechatPayUi2(className)) {
                Log.i("=====","[auto]微信零钱通_WX_PAY");
                autoService.useful = true;
                autoService.flag = FLAG_WECHAT_PAY_UI;
            } else if (wechat.findWechatPayBill(className)) {
                Log.i("=====","[auto]微信零钱通_WX_BILL");
                autoService.useful = true;
                autoService.flag = FLAG_WECHAT_PAY_MONEY_UI;
            } else if (wechat.findWechatPayDetail(packageName, nodeInfo)) {
                Log.i("=====","[auto]微信账单详情");
                autoService.useful = true;
                autoService.flag = FLAG_WECHAT_PAY_MONEY_UI;
            } else if (wechat.findWechatPayDetail2(packageName, className)) {
                Log.i("=====","[auto]微信新版账单详情");
                autoService.useful = true;
                autoService.flag = FLAG_WECHAT_DETAIL_UI_2;
            } else if (alipay.o(packageName, className, nodeInfo)) {
                Log.i("=====","[auto]支付宝支付界面");
                autoService.useful = true;
                autoService.flag = FLAG_ALIPAY_PAY_UI;
            } else if (alipay.l(packageName, nodeInfo)) {
                Log.i("=====","[auto]支付宝账单详情");
                autoService.useful = true;
                autoService.flag = FLAG_ALIPAY_PAY_DETAIL_UI;
            } else if (unionOrjd.o(packageName, className, nodeInfo)) {
                Log.i("=====","[auto]云闪付支付界面");
                autoService.useful = true;
                autoService.flag = FLAG_UNION_PAY_UI;
            } else if (unionOrjd.m(packageName, className, nodeInfo)) {
                Log.i("=====","[auto]云闪付账单详情");
                autoService.useful = true;
                autoService.flag = FLAG_UNION_PAY_DETAIL_UI;
            } else if (mt.l(packageName, className)) {
                Log.i("=====","[auto]美团账单详情");
                autoService.useful = true;
                autoService.flag = FLAG_MT_PAY_DETAIL_UI;
            } else if (unionOrjd.l(packageName, nodeInfo)) {
                Log.i("=====","[auto]京东账单详情");
                autoService.useful = true;
                autoService.flag = FLAG_JD_PAY_DETAIL_UI;
            } else if (unionOrjd.n(className)) {
                Log.i("=====","[auto]京东支付界面");
                autoService.useful = true;
                autoService.flag = FLAG_JD_PAY_UI;
            } else if (alipay.m(packageName, className)) {
                Log.i("=====","[auto]拼多多账单详情");
                autoService.useful = true;
                autoService.flag = FLAG_PDD_DETAIL_UI;
            } else if (baseHelper.isInApp(className)) {
                autoService.useful = false;
                Log.i("=====","[auto]退出");
                SkAccessibilityService.clear(autoService);
            }

            if (!autoService.useful) {
                Log.i("=====","识别结束,淘汰");
                return;
            }
            List<String> nodeListInfo = SkAccessibilityService.ergodicList(autoService, nodeInfo);
            SkAccessibilityService.nodeList = new ArrayList<>();
            if (!isNullOrEmpty(nodeListInfo)) {

                if (autoService.flag == FLAG_WECHAT_DETAIL_UI_2 && !wechat.isUseful(nodeListInfo)) {
                    Log.i("=====","[auto]无效账单详情");
                    SkAccessibilityService.clear(autoService);
                    return;
                }

                if (autoService.flag == FLAG_MT_PAY_DETAIL_UI && !mt.m(nodeListInfo)) {
                    Log.i("=====","[auto]无效账单详情");
                    SkAccessibilityService.clear(autoService);
                    return;
                }

                if (autoService.flag == FLAG_PDD_DETAIL_UI && !alipay.n(nodeListInfo)) {
                    Log.i("=====","[auto]无效账单详情");
                    SkAccessibilityService.clear(autoService);
                    return;
                }

                if (autoService.flag == FLAG_ALIPAY_PAY_DETAIL_UI && (checkNode(nodeListInfo, "添加照片", true))) {
                    Log.i("=====","[auto]无效账单详情");
                    SkAccessibilityService.clear(autoService);
                    return;
                }

                if(autoService.flag == FLAG_WECHAT_PAY_MONEY_UI || autoService.flag == FLAG_WECHAT_DETAIL_UI_2){
                    //微信账单详情
                    AutoDataDto dto = AutoDataGetUtil.INSTANCE.getData(nodeListInfo);
                    LogUtils.e("微信账单解析结果=====",GsonUtil.GsonString(dto));
                    goApp(dto);
                }else if(autoService.flag == FLAG_WECHAT_PAY_UI){
                    //微信支付页面
                    AutoDataDto dto = AutoDataGetUtil.INSTANCE.getWxPlay(nodeListInfo);
                    LogUtils.e("微信支付账单解析结果=====",GsonUtil.GsonString(dto));
                    goApp(dto);
                }else if(autoService.flag == FLAG_ALIPAY_PAY_DETAIL_UI){
                    //支付宝账单详情
                    AutoDataDto dto = AutoDataGetUtil.INSTANCE.getAliPayData(nodeListInfo);
                    LogUtils.e("支付宝账单解析结果=====",GsonUtil.GsonString(dto));
                    goApp(dto);
                }

            }
        } catch (Throwable v0) {
            Log.i("=====","出现异常");
            v0.printStackTrace();
        }


    }

    public static void goApp(AutoDataDto autoDataDto) {
        if (autoDataDto == null) {
            Log.i("","autoDataDto数据为空");
            return;
        }
        //防止出现多次识别
        if (System.currentTimeMillis() - time > 1000L) {
            time = System.currentTimeMillis();
//            SendDataToApp.call(context, billInfo);
            //进行记账
            AutoDataLiveData.Companion.getInstance().postValue(autoDataDto);
        }
    }
}

