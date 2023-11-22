package com.tzh.myapplication.service

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.mylibrary.util.LogUtils

class MmsSentReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        LogUtils.d("MyBroadcastReceiver", "Received broadcast: " + intent.action)
        LogUtils.d("code", resultCode.toString())
        when (resultCode) {
            Activity.RESULT_OK -> {
                // 短信发送成功
                LogUtils.e("signal", "成功")
                ToastUtil.show("短信发送成功yyyyy")
            }

            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                // 短信发送失败
                LogUtils.e("signal", "失败")
                ToastUtil.show("短信发送失败")
            }

            SmsManager.RESULT_ERROR_NO_SERVICE -> {
                // 手机没有信号，无法发送短信
                LogUtils.e("signal", "失败")
                ToastUtil.show("手机无信号，无法发送短信")
            }

            else->{
                ToastUtil.show(resultCode.toString())
            }
        }
    }
}