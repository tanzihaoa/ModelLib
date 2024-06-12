package com.tzh.myapplication.ui.activity.main

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.liulishuo.okdownload.OkDownloadProvider.context
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.ui.activity.ContactsActivity
import com.tzh.myapplication.ui.activity.HandSendMessageActivity
import com.tzh.myapplication.ui.activity.ImageActivity
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.activity.SearchActivity
import com.tzh.myapplication.ui.activity.SendMessageActivity
import com.tzh.myapplication.ui.activity.SendSmsActivity
import com.tzh.myapplication.ui.dialog.AddMobileDialog
import com.tzh.myapplication.ui.dialog.MyDialog
import com.tzh.myapplication.utils.ConfigUtil
import com.tzh.myapplication.utils.SkUtil
import com.tzh.myapplication.utils.TimeUtil
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.myapplication.utils.img.PermissionDetectionUtil
import com.tzh.mylibrary.activity.ScanUtilActivity
import com.tzh.mylibrary.activity.TranslateActivity
import com.tzh.mylibrary.activity.WebActivity
import com.tzh.mylibrary.activity.tool.MuYuActivity
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.img.ChoiceImageAdapter
import com.tzh.mylibrary.util.img.ChoiceImageUtil
import com.tzh.mylibrary.util.picture.PictureSelectorHelper
import com.tzh.mylibrary.util.toDefault


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

        val mAdapter = ChoiceImageUtil.setChoiceImage(this,binding.recyclerView,4,9,false, isBack = false,false)
        mAdapter.setListener(object : ChoiceImageAdapter.ImageChangeListener{
            override fun change() {

            }

            override fun getPermission() {
                PermissionDetectionUtil.detection(this@MainActivity,object : PermissionDetectionUtil.DetectionListener{
                    override fun ok() {

                    }
                })
            }
        })
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
        mDialog.show()
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
        ContactsActivity.start(this)
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

    /**
     * 搜索
     */
    fun search(){
        SearchActivity.start(this)
    }

    fun selectImg(){
        PermissionDetectionUtil.getPermission(object : PermissionDetectionUtil.DetectionListener{
            override fun ok() {
                PictureSelectorHelper.onPictureSelector(this@MainActivity,2,object : OnResultCallbackListener<LocalMedia>{
                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        if(result?.size.toDefault(0) > 0){
                            val dto = result?.get(0)
                            ToastUtil.show(dto?.realPath)
                        }
                    }

                    override fun onCancel() {

                    }
                }, SelectMimeType.ofImage())
            }
        })
    }

    /**
     * 木鱼
     */
    fun muYu(){
        MuYuActivity.start(this)
    }

    /**
     * 添加一条短信
     */
    fun addMessage(){
        val smsManager: SmsManager = SmsManager.getDefault()
        val phoneNumber = "15197841559"
        val message = "要发送的短信内容"

        smsManager.sendTextMessage(phoneNumber, null, message, null, null)

        val values = ContentValues()
        values.put("address", phoneNumber)
        values.put("body", message)
        context.contentResolver.insert(Uri.parse("content://sms/sent"), values)

        val notificationManager = NotificationManagerCompat.from(context)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("新短信发送成功")
            .setContentText("短信内容：$message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        notificationManager.notify(1006, builder.build())
    }

    /**
     * 录音
     */
    fun record(){
        ImageActivity.start(this)
    }

    /**
     * 拍照
     */
    fun openCamera(){
        PictureSelectorHelper.openCamera(this,object : OnResultCallbackListener<LocalMedia>{
            override fun onResult(result: ArrayList<LocalMedia>?) {

            }

            override fun onCancel() {

            }
        })
    }
}