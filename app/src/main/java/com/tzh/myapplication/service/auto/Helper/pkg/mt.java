//
// Decompiled by Procyon - 2200ms
//
package com.tzh.myapplication.service.auto.Helper.pkg;

import com.tzh.myapplication.service.auto.Helper.Analyze;

import java.util.List;

public class mt extends baseHelper {
    public static boolean l(final String s, final String s2) {
        return "com.sankuai.waimai.business.knb.KNBWebViewActivity".equals(s2) || "com.meituan.android.hybridcashier.HybridCashierActivity".equals(s2) || "com.sankuai.eh.framework.EHContainerActivity".equals(s2) || "com.meituan.android.pay.activity.PayActivity".equals(s2) || (("com.sankuai.meituan.takeoutnew".equals(s) || "com.sankuai.meituan".equals(s)) && "android.webkit.WebView".equals(s2));
    }

    public static boolean m(final List<String> list) {
        final int size = list.size();
        final boolean b = true;
        if (size >= 3) {
            boolean b2 = b;
            if (Analyze.checkNode(list, "交易详情", true)) {
                return b2;
            }
            b2 = b;
            if (Analyze.checkNode(list, "交易类型", true)) {
                return b2;
            }
            if (Analyze.checkNode(list, "支付成功", true)) {
                b2 = b;
                return b2;
            }
        }
        return false;
    }

}

