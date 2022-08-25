package com.tzh.mylibrary.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static long lastClickTime;

    public static boolean isNoDoubleClick() {
        return isNoDoubleClick(300);
    }

    /**
     * @param times
     * @return
     * @方法说明:防止控件被重复点击，如果点击间隔时间小于指定时间就点击无 @方法名称:isFastDoubleClick
     * @返回 boolean
     */
    public static boolean isNoDoubleClick(long times) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < times) {
            return false;
        }
        lastClickTime = time;
        return true;
    }

    /**
     * 获取mac地址 有线网络的
     */
    public static String GetMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("eth0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 读取assets的文件
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getFromAssets(String fileName, Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到手机宽度 返回px
     */
    public static int getPhoneWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        return width;
    }

    /**
     * 得到手机高度 返回px
     */
    public static int getPhoneHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int height = point.y;
        return height;
    }


    //检测当前的网络状态

    //API版本23以下时调用此方法进行检测
//因为API23后getNetworkInfo(int networkType)方法被弃用
    public static void checkState_21(Context context) {
        //步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
        //NetworkInfo对象包含网络连接的所有信息
        //步骤3：根据需要取出网络连接信息
        //获取WIFI连接的信息
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Boolean isWifiConn = networkInfo.isConnected();

        //获取移动数据连接的信息
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        Boolean isMobileConn = networkInfo.isConnected();
    }


    /**
     * 两个集合是否有不同的
     */
    public static boolean isHaveDifferent(List<String> list1, List<String> list2) {

        boolean different = false;
        if (list1 == null || list2 == null) {
            return true;
        }
        if (list1.size() != list2.size()) {
            return true;
        }
        List<String> diff = new ArrayList<>();
        List<String> maxList = list1;
        List<String> minList = list2;
//        if (list2.size() > list1.size()) {
//            maxList = list2;
//            minList = list1;
//        }
        Map<String, Integer> map = new HashMap<>(maxList.size());
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            if (map.get(string) != null) {
                map.put(string, 2);
                continue;
            }
            diff.add(string);
            different = true;
            break;
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
                different = true;
                break;
            }
        }

        return different;

    }

    /**
     * 用ascii码 判断string 是否是数字
     *
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

}
