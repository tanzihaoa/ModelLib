package com.tzh.myapplication.ui.activity

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivitySendSmsBinding
import com.tzh.myapplication.service.SmsSentReceiver
import com.tzh.myapplication.utils.AndroidUtil
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil
import com.tzh.myapplication.utils.SendUtil
import com.tzh.myapplication.utils.img.CameraUtil
import com.tzh.myapplication.utils.img.ImageDTO
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

    val receiver by lazy {
        SmsSentReceiver()
    }

    override fun initView() {
        binding.titleBar.setRightTitleTxt(AndroidUtil.getVersionName(this))
        binding.activity = this

        //注册短信发送监听
        registerReceiver(receiver, IntentFilter(SENT))
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

    fun sendSms(){
        val phone = binding.etPhone.text.toString()
        val content = binding.etContent.text.toString()
        val sentIntent = Intent(SENT)
        val sentPendingIntent = PendingIntent.getBroadcast(this, 0, sentIntent, PendingIntent.FLAG_IMMUTABLE)
        img?.apply {
            SendUtil.sendSms(this@SendSmsActivity,phone,content,this.file,sentPendingIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消注册广播接收器
        unregisterReceiver(receiver)
    }
}