package com.tzh.mylibrary.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 运算方式工具类
 */
public class MathUtil {
    /**
     * 四舍五入保留两位
     * @param number 值
     * @param scale 保留长度
     */
    public static float roundedNumber(float number,int scale) {
        BigDecimal bigDec = new BigDecimal(number);
        return bigDec.setScale(scale, RoundingMode.HALF_UP)
                .floatValue();
    }

    /**
     * 四舍五入保留两位
     * @param number 值
     * @param scale 保留长度
     */
    public static double roundedNumber(double number,int scale) {
        BigDecimal bigDec = new BigDecimal(number);
        return bigDec.setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
