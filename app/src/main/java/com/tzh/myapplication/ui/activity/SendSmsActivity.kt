package com.tzh.myapplication.ui.activity

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivitySendSmsBinding
import com.tzh.myapplication.utils.AndroidUtil
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil
import com.tzh.myapplication.utils.SendUtil
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.myapplication.utils.img.CameraUtil
import com.tzh.myapplication.utils.img.ImageDTO
import com.tzh.myapplication.utils.sms.PhoneManager
import com.tzh.mylibrary.util.LoadImageUtil
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.toDefault


class SendSmsActivity : AppBaseActivity<ActivitySendSmsBinding>(R.layout.activity_send_sms) {
    companion object {
        @JvmStatic
        fun start(context: AppCompatActivity) {
            PermissionXUtil.requestAnyPermission(context, mutableListOf<String>().apply {
                add(Manifest.permission.SEND_SMS)
                add(Manifest.permission.CALL_PHONE)
                add(Manifest.permission.READ_SMS)
                add(Manifest.permission.RECEIVE_SMS)
                add(Manifest.permission.RECEIVE_MMS)
                add(Manifest.permission.WRITE_CONTACTS)
                add(Manifest.permission.READ_CONTACTS)
                add(Manifest.permission.READ_PHONE_STATE)
                add(Manifest.permission.CHANGE_NETWORK_STATE)
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            },object : OnPermissionCallBackListener{
                override fun onAgree() {
                    context.startActivity(Intent(context, SendSmsActivity::class.java))
                }

                override fun onDisAgree() {

                }
            })
        }
    }

    var SENT = "SMS_SENT"

    var img : ImageDTO ?= null
    override fun initView() {
        binding.titleBar.setRightTitleTxt(AndroidUtil.getVersionName(this))
        binding.activity = this

        //注册短信发送监听
        registerReceiver(smsSentReceiver, IntentFilter(SENT))
        binding.layoutImg.setOnClickListener {
            CameraUtil.createAlbum(this@SendSmsActivity,1,object : CameraUtil.onSelectCallback{
                override fun onResult(photos: MutableList<ImageDTO>) {
                    if(photos.size > 0){
                        img = photos[0]
                        img?.file?.apply {
                            LoadImageUtil.loadImageUrl(binding.ivImg,this.absolutePath,12f)
                            LogUtils.e("图片大小====", (img?.file?.length().toDefault(0) / 1024).toString()+"kb")
                        }
                    }
                }

                override fun onCancel() {

                }
            })
        }
    }

    override fun initData() {

    }

    private val smsSentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
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

    fun sendSms(){
        if(PhoneManager.isDefaultPhoneApplication(this)){
            val phone = binding.etPhone.text.toString()
            val content = binding.etContent.text.toString()
            val sentIntent = Intent(SENT)
            val sentPendingIntent = PendingIntent.getBroadcast(this, 0, sentIntent, PendingIntent.FLAG_IMMUTABLE)
            img?.apply {
                SendUtil.sendSms(this@SendSmsActivity,phone,content,this.file,sentPendingIntent)
//            val fileUri = FileProvider.getUriForFile(
//                this@SendSmsActivity,
//                "${this@SendSmsActivity.applicationContext.packageName}.fileprovider",
//                this.file
//            )
//            MmsUtil.sendMms(this@SendSmsActivity, Uri.fromFile(this.file),phone,content)
            }
        }else{
            LogUtils.e("=====","设置默认短信")
            // 要设置默认的短信应用程序包名
            val myPackageName = packageName

            // 如果包名已经是默认短信应用，则不需要更改
            if (Telephony.Sms.getDefaultSmsPackage(this) == myPackageName) {
                Log.d(TAG, "This app is already the default SMS app.")
                return
            }

            // 允许用户将此应用程序设置为默认的短信应用程序
            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消注册广播接收器
        unregisterReceiver(smsSentReceiver)
    }
}