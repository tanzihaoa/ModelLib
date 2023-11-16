package com.tzh.myapplication.utils.sms;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.TELECOM_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;

/**
 * <pre>
 *     author : dejinzhang
 *     time : 2021/09/09
 *     desc : 电话管理器。该类中包含了对电话操作的各类方法：
 *            接听、挂断，以及弹出设置应用为系统默认电话程序（会弹出切换系统电话程序的系统弹框）的方法。
 * </pre>
 */
public class PhoneManager {
    /**
     * 设置应用为系统默认电话程序（会弹出切换系统默认电话程序的系统弹框）
     * 注意：
     * Android 10以上使用RoleManager
     * Android 6 ~ Android 9以上使用TelecomManager
     * Android 6以下不支持（因为Android 6以下call.answer call.disconnect等api不能用）
     * @param context  上下文
     * @param packageName  需要设置为系统默认电话程序的应用包名
     */
    public static void setDefaultDialerSetWindow(Context context, String packageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = context.getSystemService(RoleManager.class);
            if (roleManager != null) {
                Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                ((Activity)context).startActivityForResult(roleRequestIntent, 999);
            }
        } else {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
            if (telecomManager != null) {
                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
                intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 设置应用为系统默认短信程序（会弹出切换系统默认电话程序的系统弹框）
     * 注意：
     * Android 10以上使用RoleManager
     * Android 6 ~ Android 9以上使用TelecomManager
     * Android 6以下不支持（因为Android 6以下call.answer call.disconnect等api不能用）
     * @param context  上下文
     * @param packageName  需要设置为系统默认电话程序的应用包名
     */
    public static void setDefaultSmsSetWindow(Context context, String packageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = context.getSystemService(RoleManager.class);
            if (roleManager != null) {
                Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
                ((Activity)context).startActivityForResult(roleRequestIntent, 999);
            }
        } else {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
            if (telecomManager != null) {
                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
                intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 接听电话
     * @param call  call对象
     */
    public static void answer(Call call) {
        if (call != null) {
            call.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    /**
     * 断开电话，包括来电时的拒接以及接听后的挂断
     * @param call  call对象
     */
    public static void hangup(Call call) {
        if (call != null) {
            call.disconnect();
        }
    }

    /**
     * 打电话
     * @param context  上下文
     * @param phoneNumber  所要拨打的号码
     */
    public static void call(Context context, String phoneNumber) {
        if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }
    }

    /**
     * 挂起
     * @param call  call对象
     */
    public static void hold(Call call) {
        if (call != null) {
            call.hold();
        }
    }

    /**
     * 取消挂起
     * @param call  call对象
     */
    public static void unHold(Call call) {
        if (call != null) {
            call.unhold();
        }
    }

    /**
     * 指示此 {@code Call} 播放双音多频信号 (DTMF) 音调。
     * @param call  call对象
     * @param digit  输入的指令数字
     */
    public static void playDtmfTone(Call call, char digit) {
        if (call != null) {
            call.playDtmfTone(digit);
            call.stopDtmfTone();
        }
    }

    /**
     * 打开/关闭扬声器
     * @param context  上下文
     * @param on  true 打开扬声器  false 关闭扬声器
     */
    public static void switchSpeaker(Context context, boolean on) {
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(on);
        }
    }


    public static boolean isDefaultPhoneApplication(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
        if (telecomManager.getDefaultDialerPackage() != null) {
            String defaultDialerPackage = telecomManager.getDefaultDialerPackage();
            if (defaultDialerPackage.equals(context.getPackageName())) {
                return true;
            } else {
//                    appInfoSPEditor.putString("defaultDialerPackage", defaultDialerPackage).commit();
//                    return false;
            }
        }
        return false;
    }
}
