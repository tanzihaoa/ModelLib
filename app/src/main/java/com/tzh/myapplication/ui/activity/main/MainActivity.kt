package com.tzh.myapplication.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import com.google.zxing.integration.android.IntentIntegrator
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.ui.activity.HandSendMessageActivity
import com.tzh.myapplication.ui.activity.ImageActivity
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.activity.SendMessageActivity
import com.tzh.myapplication.ui.activity.SendSmsActivity
import com.tzh.myapplication.ui.dialog.AddMobileDialog
import com.tzh.myapplication.ui.dialog.MyDialog
import com.tzh.myapplication.utils.ConfigUtil
import com.tzh.myapplication.utils.SkUtil
import com.tzh.myapplication.utils.TimeUtil
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.mylibrary.activity.ScanUtilActivity
import com.tzh.mylibrary.activity.TranslateActivity
import com.tzh.mylibrary.activity.WebActivity
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.divideMessage


class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,MainActivity::class.java))
        }
    }

    private var curSelDate: String = ""


    private val mDialog by lazy {
        MyDialog(this)
    }

    override fun initView() {
        binding.v = this

        curSelDate = TimeUtil.getCurrentDate()

        binding.tvWza.setOnClickListener {
            SkUtil.start(this)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tvWza.text = if(SkUtil.isAccessibilitySettingsOn(this)) "已开启无障碍模式" else "未开启无障碍"
    }

    override fun initData() {

    }

    fun toRecycler(){
        ListActivity.start(this)
    }

    fun openDialog(){
        val manager = SmsManager.getDefault()
        val str = "刘导您好，我是郑诚在襄阳联保学习的同学，10月13号10月14号晚上，郑在分队长屋里喝酒，喝到后半夜，吐的哪儿都是，喝酒相关的情况我已拍照留存，另外，郑诚在学习期间，手机从未上交，每晚玩到很晚，严重影响我们学习。发信息没别的意思，希望您能劝他最后半个月低调一点，如不效，我会把相关证据材料转发负责纪委工作的副政委，也方便你们调查"
        val list = str.divideMessage()
        LogUtils.e("",GsonUtil.GsonString(list))
    }

    fun toImage(){
        ImageActivity.start(this)
    }

    fun start(){
        AddMobileDialog(this,object : AddMobileDialog.AddMobileListener{
            override fun mobile(mobile: String) {
                ConfigUtil.setMobile(mobile)
                ToastUtil.show("修改成功")
            }
        }).show(ConfigUtil.getMobile())
    }

    fun sendMessage(){
        SendMessageActivity.start(this)
    }

    fun handSendMessage(){
        HandSendMessageActivity.start(this)
    }

    fun sendSms(){
        SendSmsActivity.start(this)
    }

    fun toWebView(){
        WebActivity.start(this,getUrl())
    }

    fun getUrl(): String {
        val list: MutableMap<String, String> = HashMap()
        list["用户名"] = "15197841559"
        list["APP名称"] = getString(R.string.app_name)
        list["手机"] = Build.BRAND
        val mUrl = "https://article.uubook.cn/chat.html??channelId=oF21cA"
        return mUrl + "&customer=" + GsonUtil.GsonString(list)
    }

    /**
     * 扫码
     */
    fun scannerCode(){
        ScanUtilActivity.start(this@MainActivity,object : ScanUtilActivity.ScanListener{
            override fun sure(text: String) {
                ToastUtil.show(text)
            }

            override fun cancel() {
                ToastUtil.show("取消")
            }
        })
    }

    /**
     * 翻译
     */
    fun translate(){
        TranslateActivity.start(this)
    }
}