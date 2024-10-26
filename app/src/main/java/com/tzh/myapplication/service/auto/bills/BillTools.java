package com.tzh.myapplication.service.auto.bills;

import android.content.Context;

import com.tzh.myapplication.R;


public class BillTools {
    public static String getMoney(String s) {
        return getDoubleValue(s);
    }


    private static String getDoubleValue(String str) {


        double d = 0;

        if (str != null && str.length() != 0) {
            str = str.replace(",", "").replace("¥", "");
            StringBuilder bf = new StringBuilder();

            char[] chars = str.toCharArray();
            for (char c : chars) {
                if (c >= '0' && c <= '9') {
                    bf.append(c);
                } else if (c == '.') {
                    if (bf.indexOf(".") != -1) {
                        break;
                    } else {
                        bf.append(c);
                    }
                } else {
                    if (bf.length() != 0) {
                        break;
                    }
                }
            }
            try {
                d = Double.parseDouble(bf.toString());
            } catch (Exception ignored) {
            }
        }

        return String.valueOf(d);
    }

    public static int getColor(Context context, BillInfo billInfo) {
        if (billInfo.getType().equals(BillInfo.TYPE_INCOME)) {
            return context.getColor(R.color.float_3);
        } else if (billInfo.getType().equals(BillInfo.TYPE_PAY)) {
            return context.getColor(R.color.float_4);
        } else if (billInfo.getType().equals(BillInfo.TYPE_TRANSFER_ACCOUNTS)) {
            return context.getColor(R.color.float_2);
        } else {
            return context.getColor(R.color.float_1);
        }
    }

    public static String getCustomBill(BillInfo billInfo) {
        if (billInfo.getType().equals(BillInfo.TYPE_INCOME)) {
            return "+ ￥" + billInfo.getMoney();
        } else if (billInfo.getType().equals(BillInfo.TYPE_PAY)) {
            return "- ￥" + billInfo.getMoney();
        } else if (billInfo.getType().equals(BillInfo.TYPE_TRANSFER_ACCOUNTS)) {
            return "-> ￥" + billInfo.getMoney();
        } else {
            return "- ￥" + billInfo.getMoney();
        }
    }

    public static boolean isMoney(String money) {
        return money.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$") || money.matches("^(-?[1-9]\\d*)|0$");
    }

    public static boolean  hasMoney(String input) {

        return input.matches(".*[0-9]+(?:,[0-9]{3})*(\\.[0-9]{2})?.*");
    }


}
