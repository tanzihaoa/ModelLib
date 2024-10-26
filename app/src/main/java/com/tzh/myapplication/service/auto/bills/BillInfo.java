/*
 * Copyright (C) 2021 dreamn(dream@dreamn.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tzh.myapplication.service.auto.bills;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Set;


/**
 * 数据字段
 */
public class BillInfo {

    public static String TYPE_PAY = "0";//支出
    public static String TYPE_INCOME = "1";//收入
    public static String TYPE_TRANSFER_ACCOUNTS = "2";//转账
    public static String TYPE_CREDIT_CARD_PAYMENT = "3";//信用卡还款
    public static String TYPE_PAYMENT_REFUND_OUT = "5";//支出报销
    public static String TYPE_PAYMENT_REFUND_IN = "998";//收入报销(非官方)
    private String type = "0";//账单类型
    private String money = "0";//大于0

    private String time;//yyyy-MM-dd HH:mm:ss

    private int id = 0;//数据ID
    //以上为必传参数

    private String remark;//备注信息

    private String catename = "NotFound";//分类

    private String reimbursement = "false";//是否报销

    private String billId = "-1";//服务器上的Id
    private String catechoose = "0";//type=0或1有效

    private String bookname = "默认账本";//账本名称，不填写则使用默认账本

    private String accountname = "无账户";//账单所属资产名称(或转账的转出账户）

    private String accountId1 = "-1";

    private String accountname2 = "无账户";//转账或者还款的转入账户

    private String fromApp = "";//来源app

    private String rawAccount = "无账户";//没有替换的资产名
    private String rawAccount2 = "无账户";//没有替换的资产名

    private String shopAccount = "";//识别出来的收款账户

    private String shopRemark = "";//识别出来的备注

    private String extraData = "";//额外数据来源

    private String fee = "0";//手续费

    private boolean isAuto = false;

    private long timeStamp;

    public BillInfo() {
        setTime();
    }

    public static BillInfo parse(String url) {
        Uri mUri;
        try {
            mUri = Uri.parse(url);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i("","数据有误：" + url);
            return new BillInfo();
        }
        BillInfo billInfo = new BillInfo();
        // 获得所有参数 key
        Set<String> params = mUri.getQueryParameterNames();
        for (String param : params) {
            String value = mUri.getQueryParameter(param);
            switch (param) {
                case "type":
                    if (value.equals(BillInfo.TYPE_PAYMENT_REFUND_IN)) {
                        value = BillInfo.TYPE_INCOME;
                        billInfo.setReimbursement(true);
                    } else if (value.equals(BillInfo.TYPE_PAYMENT_REFUND_OUT)) {
                        value = BillInfo.TYPE_PAY;
                        billInfo.setReimbursement(true);
                    }
                    billInfo.setType(value);
                    break;
                case "money":
                    billInfo.setMoney(value);
                    break;
                case "time":
                    billInfo.setTime(value);
                    break;
                case "remark":
                    billInfo.setRemark(value);
                    break;
                case "catename":
                    billInfo.setCateName(value);
                    break;
                case "bookname":
                    billInfo.setBookName(value);
                    break;
                case "accountname":
                    billInfo.setAccountName(value);
                    break;
                case "accountname2":
                    billInfo.setAccountName2(value);
                    break;
                case "shopAccount":
                    billInfo.setShopAccount(value);
                    break;
                case "shopRemark":
                    billInfo.setShopRemark(value);
                    break;
                case "reimbursement":
                    billInfo.setReimbursement(value.equals("true"));
                    break;
                case "fromApp":
                    billInfo.setFromApp(value);
                    break;
                case "rawAccount":
                    billInfo.setRawAccount(value);
                    break;
                case "rawAccount2":
                    billInfo.setRawAccount2(value);
                    break;
                case "extraData":
                    billInfo.setExtraData(value);
                    break;
                case "fee":
                    billInfo.setFee(value);
                    break;

                case "id":
                    billInfo.setId(Integer.parseInt(value));
                    break;
                case "timeStamp":
                    billInfo.setTimeStamp(Long.parseLong(value));
                    break;
                default:
                    break;
            }

        }
        return billInfo;


    }

    public int getId() {
        return id;
    }

    public void setId(int value) {
        id = value;
    }

    public static String getTypeId(String type) {
        switch (type) {
            case "收入":
                return BillInfo.TYPE_INCOME;
            case "转账":
                return BillInfo.TYPE_TRANSFER_ACCOUNTS;
            case "信用还款":
                return BillInfo.TYPE_CREDIT_CARD_PAYMENT;
            case "支出报销":
                return BillInfo.TYPE_PAYMENT_REFUND_OUT;
            case "收入报销":
                return BillInfo.TYPE_PAYMENT_REFUND_IN;
            default:
                return BillInfo.TYPE_PAY;
        }
    }

    public static String getTypeName(String type) {
        if (type == null) return "支出";
        if (type.equals(TYPE_PAY)) return "支出";
        if (type.equals(TYPE_CREDIT_CARD_PAYMENT)) return "信用还款";
        if (type.equals(TYPE_INCOME)) return "收入";
        if (type.equals(TYPE_TRANSFER_ACCOUNTS)) return "转账";
        if (type.equals(TYPE_PAYMENT_REFUND_OUT)) return "支出报销";
        if (type.equals(TYPE_PAYMENT_REFUND_IN)) return "收入报销";
        return "支出";
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean value) {
        isAuto = value;
    }

    public void setFee(String value) {
        fee = value;
    }

    public void setExtraData(String value) {
        extraData = value;
    }

    public String getRawAccount() {
        if (rawAccount.equals(""))
            rawAccount = accountname;
        return rawAccount;
    }

    public void setRawAccount(String value) {
        rawAccount = value;
    }

    public void setFromApp(String value) {
        fromApp = value;
    }

    public String getFee() {
        return fee;
    }

    public String getExtraData() {
        return extraData;
    }

    public String getRawAccount2() {
        if (rawAccount2.equals(""))
            rawAccount2 = accountname2;
        return rawAccount2;
    }

    public void setRawAccount2(String value) {
        rawAccount2 = value;
    }

    public String getFromApp() {
        return fromApp;
    }

    public String getAccountId1() {
        return accountId1;
    }

    public void setAccountId1(String value) {
        accountId1 = value;
    }

    public boolean getReimbursement() {
        return reimbursement != null && reimbursement.equals("true");
    }

    public void setReimbursement(boolean state) {
        reimbursement = (state ? "true" : "false");
    }

    public String getType() {
        return type;
    }

    public int getTypeInt() {
        return Integer.parseInt(type);
    }

    public String getType(Boolean real) {
        if (real) {
            String t = type;
            if (getReimbursement() && type.equals(TYPE_PAY)) t = TYPE_PAYMENT_REFUND_OUT;
            if (getReimbursement() && type.equals(TYPE_INCOME)) t = TYPE_PAYMENT_REFUND_IN;
            return t;
        } else return type;
    }

    public void setType(String type) {
        this.type = type;

    }

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money) {
        this.money = BillTools.getMoney(money);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        this.timeStamp = DateUtils.dateToStamp(this.time, "yyyy-MM-dd HH:mm:ss");
    }

    public void setTimeStamp() {
        this.time = DateUtils.getTime("yyyy-MM-dd HH:mm:ss");
        this.timeStamp = DateUtils.dateToStamp(this.time, "yyyy-MM-dd HH:mm:ss");

    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long time) {
        this.timeStamp = time;
        this.time = DateUtils.stampToDate(time, "yyyy-MM-dd HH:mm:ss");
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCateName() {
        return catename;
    }

    public void setCateName(String name) {
        this.catename = name;
    }

    public String getCateChoose() {
        return catechoose;
    }

    public void setCateChoose(Boolean Open) {
        if (Open) {
            this.catechoose = "1";
        } else {
            this.catechoose = "0";
        }

    }

    public String getBookName() {
        return bookname;
    }

    public void setBookName(String name) {
        this.bookname = name;
    }

    public String getAccountName() {
        return accountname;
    }

    public void setAccountName(String name) {
        this.accountname = name;
    }

    public String getAccountName2() {
        return accountname2;
    }

    public void setAccountName2(String name) {
        this.accountname2 = name;
    }

    public String getShopAccount() {
        return this.shopAccount;
    }

    public void setShopAccount(String name) {
        this.shopAccount = name;
    }

    public String getShopRemark() {
        return this.shopRemark == null ? "" : shopRemark;
    }

    public void setShopRemark(String name) {
        this.shopRemark = name;
    }



    @SuppressLint("SimpleDateFormat")
    public void setTime() {
        this.time = DateUtils.getTime("yyyy-MM-dd HH:mm:ss");
    }

    @NonNull
    public String toString() {

        String url = "qianji://publicapi/addbill?&type=" + getType(true) + "&money=" + money;

        if (time != null) {
            url += "&time=" + time;
        }
        if (remark != null) {
            url += "&remark=" + remark;
        }
        if (catename != null) {
            url += "&catename=" + catename;
        }
        url += "&catechoose=" + catechoose;

        url += "&catetheme=auto";

        if (this.bookname != null && !this.bookname.equals("默认账本")) {
            url += "&bookname=" + bookname;
        }

        if (accountname != null && !accountname.equals("")) {
            url += "&accountname=" + accountname;
        }
        if (accountname2 != null && !accountname2.equals("")) {
            url += "&accountname2=" + accountname2;
        }
        if (shopAccount != null) {
            url += "&shopAccount=" + shopAccount;
        }
        if (shopRemark != null) {
            url += "&shopRemark=" + shopRemark;
        }
        if (reimbursement != null) {
            url += "&reimbursement=" + reimbursement;
        }
        if (fromApp != null) {
            url += "&fromApp=" + fromApp;
        }
        if (rawAccount != null) {
            url += "&rawAccount=" + rawAccount;
        }
        if (rawAccount2 != null) {
            url += "&rawAccount2=" + rawAccount2;
        }
        if (extraData != null) {
            url += "&extraData=" + extraData;
        }
        if (fee != null) {
            url += "&fee=" + fee;
        }

        if (id != 0) {
            url += "&id=" + id;
        }
        if (timeStamp != 0) {
            url += "&timeStamp=" + timeStamp;
        }
        return url;
    }

    public boolean isAvaiable() {
        if (this.fee != null && this.fee.equals(""))
            this.fee = "0";
        if (accountname.equals("")) {
            accountname = fromApp;
        }
        try {
            //检查手续费
            if (this.fee != null && Float.parseFloat(this.fee) > Float.parseFloat(this.money)) {
                Log.i("记账流程", "手续费错误" + this.time);
                return false;
            }
        } catch (Throwable e) {
            this.fee = "0";
        }
        //检查时间
        if (this.time == null || this.time.equals("")) {
            Log.i("记账流程", "时间错误" + this.time);
            return false;
        }
        //检查金额
        if (this.money == null || BillTools.getMoney(this.money).equals("0") || BillTools.getMoney(this.money).equals("0.0") || BillTools.getMoney(this.money).equals("0.00")) {
            Log.i("记账流程", "金额 :" + this.money);
            return false;
        }
        //检查分类
        if (this.type == null) {
            Log.i("记账流程", "分类错误 -> null");
            return false;
        }
        return true;
    }

    public String dump() {
        String output = "";
        output += String.format("消费类型=%s\n", getTypeName(type));
        output += String.format("金额=%s\n", money);
        output += String.format("时间=%s\n", time);
        output += String.format("备注=%s\n", remark);
        output += String.format("分类=%s\n", catename);
        output += String.format("分类选择=%s\n", catechoose);
        output += String.format("账本名=%s\n", bookname);
        output += String.format("资产名1=%s\n", accountname);
        output += String.format("资产名2=%s\n", accountname2);
        output += String.format("商户名=%s\n", shopAccount);
        output += String.format("商户备注=%s\n", shopRemark);
        output += "是否有效？" + (isAvaiable() ? "有效" : "无效");
        return output;

    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String id) {
        billId = id;
    }
}
