package com.tzh.myapplication.network.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tzh.myapplication.base.MyApplication;
import com.tzh.myapplication.utils.SPUtil;

import java.util.UUID;

/**
 * Created on 2020/11/30.
 */
public class DeviceUuidUtils {
    public static final String PREFS_DEVICE_ID = "device_id";

    @SuppressLint("MissingPermission")
    @SuppressWarnings("deprecation")
    public static String getDeviceId() {
        //如果有deviceId
        String deviceId = SPUtil.getInstance().getString(PREFS_DEVICE_ID, "");
        deviceId.replace(" ", "");

        if (TextUtils.isEmpty(deviceId)) {
            try {
                TelephonyManager manager = (TelephonyManager) MyApplication.mContext.getSystemService(Context.TELEPHONY_SERVICE);
                if (manager.getDeviceId() == null || manager.getDeviceId().equals("")) {
                    if (Build.VERSION.SDK_INT >= 23){
                        deviceId = manager.getDeviceId(0);
                    }
                } else{
                    deviceId = manager.getDeviceId();
                }
                if (TextUtils.isEmpty(deviceId)){
                    deviceId = getUUID();
                }
            } catch (Exception e) {
                deviceId = getUUID();
            }
            SPUtil.getInstance().putString(PREFS_DEVICE_ID, deviceId);
        }
        return deviceId;
    }

    /**
     * 获取 UUID　的值
     * @return
     */
    private static String getUUID(){
        ////获取设备id
        String deviceId;

        ////
        String androidId = Secure.getString(MyApplication.mContext.getContentResolver(), Secure.ANDROID_ID);
        try {
            UUID uuid;
            if ("9774d56d682e549c".equals(androidId)){
                uuid = UUID.randomUUID();
            } else{
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            }

            deviceId = uuid.toString();
        } catch (Exception e) {
            ////是在不行就只能自己生产了
            String brand = Build.BRAND;
            //随机数
            int random = (int) (Math.random() * 100000);
            ////deviceId自己生产
            deviceId = brand.toLowerCase()+"_"+System.currentTimeMillis()+"_"+random;
        }
        return deviceId;
    }
}
