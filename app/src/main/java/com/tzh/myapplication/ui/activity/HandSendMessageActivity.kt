package com.tzh.myapplication.ui.activity

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityHandSendMessageBinding
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.mylibrary.util.LogUtils


class HandSendMessageActivity : AppBaseActivity<ActivityHandSendMessageBinding>(R.layout.activity_hand_send_message) {
    companion object {
        @JvmStatic
        fun start(context: AppCompatActivity) {
            context.startActivity(Intent(context, HandSendMessageActivity::class.java))
        }
    }

    var SENT = "SMS_SENT"
    var DELIVERED = "SMS_DELIVERED"

    val sentPI by lazy {
        PendingIntent.getBroadcast(
            this, 0,
            Intent(SENT), PendingIntent.FLAG_IMMUTABLE
        )
    }
    val deliveredPI by lazy {
        PendingIntent.getBroadcast(
            this, 0,
            Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun initView() {
        binding.activity = this
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, arg1: Intent) {
                when (resultCode) {
                    RESULT_OK -> {
                        LogUtils.e(SENT,"RESULT_OK")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        LogUtils.e(SENT,"Generic failure")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        LogUtils.e(SENT, "No service")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU ->{
                        LogUtils.e(SENT, "Null PDU")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        LogUtils.e(SENT, "Radio off")
                    }
                }
            }
        }, IntentFilter(SENT))

        //---when the SMS has been delivered---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, arg1: Intent) {
                when (resultCode) {
                    RESULT_OK -> {
                        LogUtils.e(SENT, "Radio off")
                    }
                    RESULT_CANCELED ->{
                        LogUtils.e(SENT, "Radio off")
                    }
                }
            }
        }, IntentFilter(DELIVERED))
    }

    override fun initData() {

    }

    fun send(){
        PermissionXUtil.requestAnyPermission(this, Manifest.permission.SEND_SMS,object : OnPermissionCallBackListener{
            override fun onAgree() {
                val phone = binding.etPhone.text.toString()
                val content = binding.etContent.text.toString()
                if(phone.length != 11){
                    ToastUtil.show("请输入正确的手机号码")
                }else if(TextUtils.isEmpty(content)){
                    ToastUtil.show("请输入短信内容")
                }else{
                    binding.etContent.post {
                        val manager = SmsManager.getDefault()
                        manager.sendTextMessage(phone, null, content, sentPI, deliveredPI)
                    }
                    ToastUtil.show("发送成功")
                }
            }

            override fun onDisAgree() {

            }
        })
    }
}