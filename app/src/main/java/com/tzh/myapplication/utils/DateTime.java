package com.tzh.myapplication.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @类名:DateTime
 * @类描述:日期时间工具类
 * @作者:Administrator
 * @修改人:
 * @修改时间:
 * @修改备注:
 * @版本:
 */
@SuppressLint("SimpleDateFormat")
public class DateTime {

    public static final String GROUP_BY_EACH_DAY = "yyyyMMdd";
    public static final String GROUP_BY_EACH_DAYSM = "yyyyMMddHHmmss";
    public static final String GROUP_BY_EACH_SECOND = "yyyyMMddHHmm";
    public static final String TIMEsubordinate = "0.000";
    public final static String TIME_PATTERN = "HH:mm:ss";// 定义标准时间格式
    public final static String DATE_PATTERN_1 = "yyyy/MM/dd";// 定义标准日期格式1
    public final static String DATE_PATTERN_2 = "yyyy-MM-dd";// 定义标准日期格式2
    public final static String DATE_PATTERN_3 = "yyyy/MM/dd HH:mm:ss";// 定义标准日期格式3，带有时间
    public final static String DATE_PATTERN_4 = "yyyy/MM/dd HH:mm:ss E";// 定义标准日期格式4，带有时间和星期
    public final static String DATE_PATTERN_5 = "yyyy年MM月dd日 HH:mm:ss E";// 定义标准日期格式5，带有时间和星期
    public final static String DATE_PATTERN_6 = "yyyy-MM-dd HH-mm-ss";// 定义标准日期格式6，带有时间
    public final static String DATE_PATTERN_7 = "yyyy年MM月dd日";// 定义标准日期格式7
    public final static String DATE_PATTERN_8 = "yyyy-MM-dd HH:mm";// 定义标准日期格式8，带有时间
    public final static String DATE_PATTERN_9 = "yy-MM-dd HH时";// 定义标准日期格式9，带有时间
    public final static String DATE_PATTERN_10 = "yy/MM/dd HH:mm";// 定义标准日期格式10，带有时间
    public final static String DATE_PATTERN_11 = "yy.MM.dd";// 定义标准日期格式11，带有时间
    public final static String DATE_PATTERN_12 = "yyyyMMdd";// 定义标准日期格式11，带有时间
    public final static String DATE_PATTERN_13 = "yyyy-MM-dd HH:mm:ss";// 定义标准日期格式13，带有时间
    public final static String DATE_PATTERN_14 = "MM-dd HH:mm";// 定义标准日期格式13，带有时间
    public final static String DATE_PATTERN_15 = "mm_ss";//
    public final static String DATE_PATTERN_16 = "HH:mm";//
    public final static String DATE_PATTERN_17 = "HH时mm分";//
    /**
     * 定义时间类型常量
     */
    public final static int SECOND = 1;
    public final static int MINUTE = 2;
    public final static int HOUR = 3;
    public final static int DAY = 4;
    public final static int YEAR = 5;

    public final static long SECOND_TIME_LONG = 1000;
    public final static long MINUTE_TIME_LONG = 60 * 1000;
    public final static long HOUR_TIME_LONG = 60 * 60 * 1000;
    public final static long DAY_TIME_LONG = 24 * 60 * 60 * 1000;


    /**
     * 获取当前的年月日
     */
    public static String getNowTimeDay() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }


    /**
     * 时间字符转时间戳
     * @param time 时间字符
     * @param type      字符类型
     */
    public static Long getLongTime(String time,String type) {
        if (TextUtils.isEmpty(time)) {
            return 0L;
        } else {
            Date date = new DateTime().formatString(time, type);
            if (null == date) {
                return 0L;
            } else {
                return date.getTime();
            }
        }
    }

    /**
     * 时间戳转时间字符
     * @param timeStamp 时间戳
     * @param type      字符类型
     */
    public static String timeStampToTime(long timeStamp, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(new Date(timeStamp));
    }

    /**
     * 将一种时间字符转换成另一种时间字符
     * @param time 需要转换的时间字符
     * @param type 时间字符类型
     * @param outType 输出的时间字符类型
     */
    public static String timeStampToTimeStamp(String time,String type,String outType){
        long mTime = getLongTime(time,type);
        if(mTime<=0){
            return time;
        }
        return timeStampToTime(mTime,outType);
    }



    /**
     * @param standardTime 标准格式yyyy
     *                     -MM-dd HH:mm:ss时间字符串
     * @return
     * @方法说明:获取定制的时间格式
     * @方法名称:getCustomizedTime
     * @返回值:String
     */
    public String getCustomizedTime(String standardTime) {
        Date now = new Date();
        Date chatTime;
        chatTime = formatString(standardTime, DATE_PATTERN_6);
        long minuties = (now.getTime() - chatTime.getTime()) / 60000; // 当前时间和聊天时间的间隔分钟
        if (minuties < 1) {
            return "刚刚";
        } else if (minuties < 60 && minuties >= 1) {
            return minuties + "分钟前";
        } else if (minuties < 1440 && minuties >= 60) {
            if (now.getDate() == chatTime.getDate()) {
                return minuties / 60 + "小时前";
            } else {
                if (now.getDate() - chatTime.getDate() == 1) {
                    SimpleDateFormat dfss = new SimpleDateFormat("HH:mm");
                    return "昨天" + dfss.format(chatTime);
                } else if (now.getYear() == chatTime.getYear()) {
                    SimpleDateFormat dfss = new SimpleDateFormat("MM-dd");
                    return dfss.format(chatTime);
                } else {
                    SimpleDateFormat dfss = new SimpleDateFormat("yyyy-MM-dd");
                    return dfss.format(chatTime);
                }
            }
        } else {
            if (now.getYear() == chatTime.getYear()) {
                SimpleDateFormat dfsyers = new SimpleDateFormat("yyyy-MM-dd");
                if (now.getDate() - chatTime.getDate() == 1) {
                    SimpleDateFormat dfss = new SimpleDateFormat("HH:mm");
                    return "昨天" + dfss.format(chatTime);
                }
                return dfsyers.format(chatTime);
            } else {
                SimpleDateFormat dfsyers = new SimpleDateFormat("yyyy-MM-dd");
                return dfsyers.format(chatTime);
            }
        }
    }

    /**
     * @param date    日期对象
     * @param pattern 格式模型
     * @return
     * @方法说明:把一个日期，按照某种格式 格式化输出
     * @方法名称:formatDate
     * @返回值:String
     */
    public static String formatDate(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
        return "";
    }

    /**
     * @param str     时间字符串
     * @param pattern 格式
     * @return
     * @方法说明:将字符串类型的时间转换为Date类型
     * @方法名称:formatString
     * @返回值:Date
     */
    public Date formatString(String str, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date time = null;
        // 需要捕获ParseException异常，如不要捕获，可以直接抛出异常，或者抛出到上层
        try {
            time = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * @param num  时间差数值
     *             ,支持小数
     * @param type 时间类型
     *             ：1->秒,2->分钟,3->小时,4->天
     * @return
     * @方法说明:将一个表示时间段的数转换为毫秒数
     * @方法名称:intToTimeMillis
     * @返回值:intlong类型时间差毫秒数，当为-1时表示参数有错
     */
    public int intToTimeMillis(int num, int type) {
        if (num <= 0)
            return 0;
        switch (type) {
            case SECOND:
                return num * 1000;
            case MINUTE:
                return num * 60 * 1000;
            case HOUR:
                return num * 60 * 60 * 1000;
            case DAY:
                return num;
            default:
                return -1;
        }
    }

    /**
     * @param num  时间差数值
     *             ,支持小数
     * @param type 时间类型
     *             ：1->秒,2->分钟,3->小时,4->天
     * @return
     * @方法说明:将一个表示时间段的数转换为毫秒数
     * @方法名称:formatToTimeMillis
     * @返回值:longlong类型时间差毫秒数，当为-1时表示参数有错
     */
    public long formatToTimeMillis(double num, int type) {
        if (num <= 0)
            return 0;
        switch (type) {
            case SECOND:
                return (long) (num * 1000);
            case MINUTE:
                return (long) (num * 60 * 1000);
            case HOUR:
                return (long) (num * 60 * 60 * 1000);
            case DAY:
                return (long) (num * 24 * 60 * 60 * 1000);
            case YEAR:
                return (long) (num * 24 * 60 * 60 * 365);
            default:
                return -1;
        }
    }


    /**
     * 根据两个秒数 获取两个时间差
     * 返回 时分秒
     */
    public static String getDatePoor(long endDate, long nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的秒时间差异
        long diff = (endDate * 1000L) - (nowDate * 1000L);
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        String res = "";
        if (day != 0) {
            res += supplement(day) + "天  ";
        }
        res += supplement(hour) + ":" + supplement(min) + ":" + supplement(sec);
        return res;
    }

    /**
     * 根据两个秒数 获取两个时间差
     * 返回 时分秒
     */
    public static String getDatePoorTime(long endDate, long nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的秒时间差异
        long diff = (endDate * 1000L) - (nowDate * 1000L);
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        String res = "";
        if (day != 0) {
            res += supplement(day) + "天  ";
        }
        res += supplement(hour) + "时" + supplement(min) + "分" + supplement(sec)+"秒";
        return res;
    }

    private static String supplement(long number) {
        String n = String.valueOf(number);
        if (n.length() < 2) {
            n = "0" + n;
        }
        return n;
    }


    /**
     * @param date
     * @return
     * @方法说明:
     * @方法名称:getDay
     * @返回值:int
     */
    public int getDay(Date date) {
        String day = formatDate(date, "dd");// 只输出时间
        if (TextUtils.isEmpty(day)) {
            return 00;
        }
        return Integer.parseInt(day);
    }

    /**
     * @param date
     * @return
     * @方法说明:只输出一个时间中的月份
     * @方法名称:getMonth
     * @返回值:int
     */
    public int getMonth(Date date) {
        String month = formatDate(date, "MM");// 只输出时间
        if (TextUtils.isEmpty(month)) {
            return 00;
        }
        return Integer.parseInt(month);
    }

    /**
     * @param date
     * @return
     * @方法说明:只输出一个时间中的年份
     * @方法名称:getYear
     * @返回值:int
     */
    public int getYear(Date date) {
        String year = formatDate(date, "yyyy");// 只输出年份
        if (TextUtils.isEmpty(year)) {
            return 00;
        }
        return Integer.parseInt(year);
    }

    /**
     * @param date
     * @return
     * @方法说明:得到一个日期函数的格式化时间
     * @方法名称:getTimeByDate
     * @返回值:String
     */
    public String getTimeByDate(Date date) {
        return formatDate(date, TIME_PATTERN);
    }

    /**
     * @return
     * @方法说明:获取当前的时间，new Date()获取当前的日期
     * @方法名称:getNowTime
     * @返回值:String
     */
    public String getNowTime() {
        return formatDate(new Date(), DATE_PATTERN_2);
    }

    /**
     * @return
     * @方法说明:获取当前的时间，new Date()获取当前的日期
     * @方法名称:getNowTime
     * @返回值:String
     */
    public long getNowTime_Long() {
        String date = formatDate(new Date(), DATE_PATTERN_2);
        Date time = formatString(date,DATE_PATTERN_2);
        return time.getTime();
    }

    /**
     * @return
     * @方法说明:获取当前的时间，new Date()获取当前的日期
     * @方法名称:getNowTime
     * @返回值:long
     */
    public long getNowTime_Long(String type) {
        String date = formatDate(new Date(), type);
        Date time = formatString(date,type);
        return time.getTime();
    }

    /**
     * @param num  时间差数值
     * @param type 时间差类型
     *             ：1->秒,2->分钟,3->小时,4->天
     * @param date 参考时间
     * @return
     * @方法说明:获取某一指定时间的前一段时间
     * @方法名称:getPreTimeStr
     * @返回值:String
     */
    public String getPreTimeStr(double num, int type, Date date) {
        long nowLong = date.getTime();// 将参考日期转换为毫秒时间
        Date time = new Date(nowLong - formatToTimeMillis(num, type));// 减去时间差毫秒数
        return getTimeByDate(time);
    }

    /**
     * @param num  时间差数值
     * @param type 时间差类型
     *             ：1->秒,2->分钟,3->小时,4->天
     * @param date 参考时间
     * @return
     * @方法说明:获取某一指定时间的前一段时间
     * @方法名称:getPreTime
     * @返回值:Date
     */
    public Date getPreTime(double num, int type, Date date) {
        long nowLong = date.getTime();// 将参考日期转换为毫秒时间
        Date time = new Date(nowLong - formatToTimeMillis(num, type));// 减去时间差毫秒数
        return time;
    }

    /**
     * @param num  时间差数值
     * @param type 时间差类型
     *             ：1->秒,2->分钟,3->小时,4->天
     * @param date 参考时间
     * @return
     * @方法说明:获取某一指定时间的后一段时间
     * @方法名称:getNextTimeStr
     * @返回值:String
     */
    public String getNextTimeStr(double num, int type, Date date) {
        long nowLong = date.getTime();// 将参考日期转换为毫秒时间
        Date time = new Date(nowLong + formatToTimeMillis(num, type));// 加上时间差毫秒数
        return getTimeByDate(time);
    }

    /**
     * @param num  时间差数值
     * @param type 时间差类型
     *             ：1->秒,2->分钟,3->小时,4->天
     * @param date 参考时间
     * @return
     * @方法说明:获取某一指定时间的后一段时间
     * @方法名称:getNextTime
     * @返回值:Date
     */
    public Date getNextTime(double num, int type, Date date) {
        long nowLong = date.getTime();// 将参考日期转换为毫秒时间
        Date time = new Date(nowLong + formatToTimeMillis(num, type));// 加上时间差毫秒数
        return time;
    }

    /**
     * @param num
     * @param date
     * @return
     * @方法说明:得到前几个月的现在 利用GregorianCalendar类的set方法来实现
     * @方法名称:getPreMonthTime
     * @返回值:Date
     */
    public Date getPreMonthTime(int num, Date date) {
        GregorianCalendar gregorianCal = new GregorianCalendar();
        gregorianCal
                .set(Calendar.MONTH, gregorianCal.get(Calendar.MONTH) - num);
        return gregorianCal.getTime();
    }

    /**
     * @param num
     * @param date
     * @return
     * @方法说明:得到后几个月的现在时间 利用GregorianCalendar类的set方法来实现
     * @方法名称:getNextMonthTime
     * @返回值:Date
     */
    public Date getNextMonthTime(int num, Date date) {
        GregorianCalendar gregorianCal = new GregorianCalendar();
        gregorianCal
                .set(Calendar.MONTH, gregorianCal.get(Calendar.MONTH) + num);
        return gregorianCal.getTime();
    }

    /**
     * @param num
     * @param type
     * @param dataSoure
     * @param dataCompare
     * @return
     * @方法说明:判断时间是否在一定的区间内
     * @方法名称:judgeData
     * @返回值:boolean
     */
    public boolean judgeData(int num, int type, Date dataSoure, Date dataCompare) {
        long nowLong = dataSoure == null ? 0L : dataSoure.getTime();
        long beforeLong = dataCompare == null ? 0L : dataCompare.getTime();
        long gapLong = formatToTimeMillis(num, type);
        if (type == DateTime.YEAR) {
            if (Math.abs(nowLong / 1000 - beforeLong / 1000) < gapLong) {
                return true;
            }
        }
        if (Math.abs(nowLong - beforeLong) < gapLong) {
            return true;
        }
        return false;
    }

    public boolean isNowTime(String chatTime, int Num) {
        if (!TextUtils.isEmpty(chatTime)) {
            DateTime dt = new DateTime();
            Date date = dt.formatString(chatTime, DateTime.DATE_PATTERN_6);
            Date nowdate = new Date();
            int nowDayNum = getDay(nowdate);
            int nowMonthNum = getMonth(nowdate);
            int nowYearNum = getYear(nowdate);
            int chatDayNum = getDay(date);
            int chatMonthNum = getMonth(date);
            int chatYearNum = getYear(date);
            if (nowYearNum >= chatYearNum) {
                if (nowMonthNum >= chatMonthNum) {
                    int count = nowDayNum - chatDayNum;
                    if (count == Num) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param num
     * @param type
     * @param dataSoure
     * @param dataCompare
     * @return
     * @方法说明:判断活动开始和结束时间是否在一定的区间内
     * @方法名称:actTimejudgeData
     * @返回值:boolean
     */
    public boolean actTimejudgeData(int num, int type, Date dataSoure,
                                    Date dataCompare) {
        long nowLong = dataSoure.getTime();
        long beforeLong = dataCompare.getTime();
        long gapLong = formatToTimeMillis(num, type);
        if (Math.abs(beforeLong - nowLong) <= gapLong) {
            return true;
        }
        return false;
    }

    /**
     * @说明:返回时间对象
     * @名称:instance
     * @类型:DateTime
     */
    private static DateTime instance = null;

    public static DateTime getInstance() {
        if (instance == null) {
            instance = new DateTime();
        }
        return instance;
    }


    /**
     * @param timelong
     * @return
     * @方法说明:计算秒
     * @方法名称:subSecond
     * @返回值:String
     */
    public String subSecond(long timelong) {
        long num = timelong / SECOND_TIME_LONG;
        String numStr = String.valueOf(num);
        if (num > 9) {
            numStr = String.valueOf(num);
        } else {
            numStr = "0" + String.valueOf(num);
        }

        return numStr + "''";

    }


    @SuppressWarnings("unused")
    public String subMinute(long timelong) {
        long num = timelong / MINUTE_TIME_LONG;
        String numStr = String.valueOf(num);
        if (num > 9) {
            numStr = String.valueOf(num);
        } else {
            numStr = "0" + String.valueOf(num);
        }

        return num + "'" + subSecond(timelong % MINUTE_TIME_LONG);
    }

    /**
     * @param timelong
     * @return
     * @方法说明:计算小时
     * @方法名称:subHour
     * @返回值:String
     */
    @SuppressWarnings("static-access")
    public String subHour(float timelong) {
        DecimalFormat dt = new DecimalFormat(this.TIMEsubordinate);
        float d = 60 * 60 * 1000;
        String num = dt.format((timelong / d));
        //
        // String numStr = String.valueOf(num);
        // if (num > 9) {
        // numStr = String.valueOf(num);
        // } else {
        // numStr = "0" + String.valueOf(num);
        // }
        return num;
    }

    /**
     * @param timeLong
     * @return
     * @方法说明:计算天
     * @方法名称:subDay
     * @返回值:String
     */
    @SuppressWarnings("unused")
    public String subDay(long timeLong) {
        long num = timeLong / DAY_TIME_LONG;

        String numStr = String.valueOf(num);
        if (num > 9) {
            numStr = String.valueOf(num);
        } else {
            numStr = "0" + String.valueOf(num);
        }
        return num + "'" + subHour(timeLong % DAY_TIME_LONG);
    }

    /**
     * @param date 目标时间
     * @return
     * @方法说明:计算目标时间与当前时间之间的范畴
     * @方法名称:getTime
     * @返回值:String
     */
    public String getTime(String date) {
        String result = "";
        SimpleDateFormat f = new SimpleDateFormat(DATE_PATTERN_6);
        try {
            Date da = (Date) f.parseObject(date);
            long ltime = System.currentTimeMillis() - da.getTime();
            float ftime = ltime / 1000;
            int itime = (int) ftime;
            if (itime <= 60) {
                result = "1分钟内";
            } else if (itime > 60 && itime <= 3600) {
                itime = (int) (ftime / 60.0f);
                result = itime + "分钟前";
            } else if (itime > 3600 && itime <= 86400) {
                itime = (int) (ftime / 3600.0f);
                result = itime + "小时前";
            } else if (itime > 86400 && itime <= 31536000) {
                itime = (int) (ftime / 86400.0f);
                result = itime + "天前";
            } else if (itime > 31536000) {
                itime = (int) (ftime / 31536000.0f);
                result = itime + "年前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 获取当前星期几
     */
    public static int getWeek(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前年份
     */
    public static int getYear(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     */
    public static int getMonth(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH)+1;
    }

    /**
     * 获取当前多少号
     */
    public static int getDay(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
