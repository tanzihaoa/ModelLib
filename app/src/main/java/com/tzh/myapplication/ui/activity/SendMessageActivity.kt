package com.tzh.myapplication.ui.activity

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import androidx.appcompat.app.AppCompatActivity
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivitySendMessageBinding
import com.tzh.myapplication.network.NetWorkApi
import com.tzh.myapplication.ui.adapter.SmsListAdapter
import com.tzh.myapplication.ui.dialog.AddMobileDialog
import com.tzh.myapplication.ui.dto.SmsDto
import com.tzh.myapplication.utils.ConfigUtil
import com.tzh.myapplication.utils.ObservableUtil
import com.tzh.myapplication.utils.OnPermissionCallBackListener
import com.tzh.myapplication.utils.PermissionXUtil
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.initAdapter
import com.tzh.mylibrary.util.linear
import com.tzh.mylibrary.util.toDefault
import com.tzh.mylibrary.util.verDivider


class SendMessageActivity : AppBaseActivity<ActivitySendMessageBinding>(R.layout.activity_send_message) {
    companion object {
        @JvmStatic
        fun start(context: AppCompatActivity) {
            if(!ConfigUtil.isMobile()){
                AddMobileDialog(context,object : AddMobileDialog.AddMobileListener{
                    override fun mobile(mobile: String) {
                        ConfigUtil.setMobile(mobile)
                        start(context)
                    }
                }).show()
            }else{
                PermissionXUtil.requestAnyPermission(context, Manifest.permission.SEND_SMS,object : OnPermissionCallBackListener{
                    override fun onAgree() {
                        context.startActivity(Intent(context, SendMessageActivity::class.java))
                    }

                    override fun onDisAgree() {

                    }
                })
            }
        }
    }

    val mAdapter by lazy {
        SmsListAdapter()
    }

    var SENT = "SMS_SENT"

    override fun initView() {
        binding.activity = this
        binding.recyclerView.linear().initAdapter(mAdapter).verDivider(10f)
        registerReceiver(smsSentReceiver, IntentFilter(SENT))
        getTask()
    }

    override fun initData() {

    }

    /**
     * 当前发送的短信
     */
    var mDto : SmsDto ?= null

    /**
     * 获取短信任务
     * 15988301228
     */
    fun getTask(){
        NetWorkApi.httpSmsTaskGet(this,ConfigUtil.getMobile()).subscribe({
            mDto = it.getDataDto()
            send()
        },{
            ToastUtil.show(it.message)
            ObservableUtil.startTimer(10*1000,SENT){
                ObservableUtil.stopTimer(SENT)
                getTask()
            }
        })
    }

    fun send(){
        if(mDto == null){
            ToastUtil.show("当前没有发送短信")
        }else{
            val sentIntent = Intent(SENT)
            val sentPendingIntent = PendingIntent.getBroadcast(this@SendMessageActivity, 0, sentIntent, PendingIntent.FLAG_IMMUTABLE)
            binding.root.post {
                val manager = SmsManager.getDefault()
                manager.sendTextMessage(mDto?.mobile.toDefault(""), null, mDto?.content.toDefault(""), sentPendingIntent, null)
            }
        }
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
                    taskRet(1)
                }

                SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                    // 短信发送失败
                    LogUtils.e("signal", "失败")
                    ToastUtil.show("短信发送失败")
                    taskRet(2)
                }

                SmsManager.RESULT_ERROR_NO_SERVICE -> {
                    // 手机没有信号，无法发送短信
                    LogUtils.e("signal", "失败")
                    ToastUtil.show("手机无信号，无法发送短信")
                    taskRet(2)
                }

                else->{
                    taskRet(2)
                }
            }
        }
    }

    /**
     * 反馈发送任务结果
     * @param status 1表示发送成功，2表示发送失败
     */
    fun taskRet(status : Int){
        mDto?.apply {
            mAdapter.addData(this)
        }

        NetWorkApi.httpSmsTaskRet(this,mDto?.id.toDefault(""),status).subscribe({
            mDto = null
            getTask()
        },{
            mDto = null
            ToastUtil.show(it.message)
            getTask()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消注册广播接收器
        unregisterReceiver(smsSentReceiver)
        ObservableUtil.stopTimer(SENT)
    }
}