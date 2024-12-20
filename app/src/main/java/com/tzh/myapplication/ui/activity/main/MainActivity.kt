package com.tzh.myapplication.ui.activity.main

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.liulishuo.okdownload.OkDownloadProvider.context
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.molihuan.pathselector.PathSelector
import com.molihuan.pathselector.entity.FileBean
import com.molihuan.pathselector.fragment.BasePathSelectFragment
import com.molihuan.pathselector.listener.CommonItemListener
import com.molihuan.pathselector.utils.MConstants
import com.molihuan.pathselector.utils.Mtools
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.livedata.AutoDataLiveData
import com.tzh.myapplication.ui.activity.ContactsActivity
import com.tzh.myapplication.ui.activity.HandSendMessageActivity
import com.tzh.myapplication.ui.activity.ImageActivity
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.activity.SearchActivity
import com.tzh.myapplication.ui.activity.SendMessageActivity
import com.tzh.myapplication.ui.activity.SendSmsActivity
import com.tzh.myapplication.ui.activity.wallper.WallPaperActivity
import com.tzh.myapplication.ui.dialog.AddMobileDialog
import com.tzh.myapplication.ui.dialog.MyDialog
import com.tzh.myapplication.utils.ConfigUtil
import com.tzh.myapplication.utils.SkUtil
import com.tzh.myapplication.utils.TimeUtil
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.myapplication.utils.img.PermissionDetectionUtil
import com.tzh.myapplication.utils.window.WindowUtil
import com.tzh.myapplication.utils.xls.MyBean
import com.tzh.myapplication.utils.xls.XlsxUtil
import com.tzh.mylibrary.activity.tool.MuYuActivity
import com.tzh.mylibrary.activity.tool.ScanUtilActivity
import com.tzh.mylibrary.activity.tool.TranslateActivity
import com.tzh.mylibrary.livedata.observeForeverNoBack
import com.tzh.mylibrary.util.GsonUtil
import com.tzh.mylibrary.util.permission.PermissionLauncher
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

    val mWindowUtil by lazy {
        WindowUtil(this)
    }

    override fun initView() {
        binding.v = this

        curSelDate = TimeUtil.getCurrentDate()

        binding.tvWza.setOnClickListener {
            SkUtil.start(this)
        }


        AutoDataLiveData.instance.observeForeverNoBack {
            it?.apply {
                binding.root.post {
                    mWindowUtil.showAsFloatingWindow()
                }
            }
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

    /**
     * 缩小为悬浮窗
     */
    fun window(){
        mWindowUtil.showAsFloatingWindow()
    }

    /**
     * 导出数据为xlsx表
     */
    fun toXls(){
        XlsxUtil.toXlsx(this, mutableListOf<MyBean>().apply {
            add(MyBean(1,"20","2024-07-27","2024-07-27"))
            add(MyBean(2,"20","2024-07-27","2024-07-27"))
            add(MyBean(3,"20","2024-07-27","2024-07-27"))
            add(MyBean(4,"20","2024-07-27","2024-07-27"))
            add(MyBean(5,"20","2024-07-27","2024-07-27"))
            add(MyBean(6,"20","2024-07-27","2024-07-27"))
            add(MyBean(7,"20","2024-07-27","2024-07-27"))
        })
    }

    /**
     * 壁纸
     */
    fun toWall(){
        WallPaperActivity.start(this)
    }

    /**
     * 选择文件
     */
    fun selectFile(){
        //Activity构建方式
        //Activity构建方式
        val selector = PathSelector.build(this, MConstants.BUILD_ACTIVITY)
            .setRequestCode(635)
            .setShowFileTypes("xml", "cvs", "xls")
            .setSelectFileTypes("xml", "cvs", "xls")
            .setMaxCount(1)
            .setShowSelectStorageBtn(false)//设置是否显示内部存储选择按钮
            .setTitlebarBG(ContextCompat.getColor(context,R.color.colorPrimary))
            .setMorePopupItemListeners(
                object : CommonItemListener("确定",true) {
                    override fun onClick(
                        v: View,
                        tv: TextView,
                        selectedFiles: List<FileBean>,
                        currentPath: String,
                        pathSelectFragment: BasePathSelectFragment
                    ): Boolean {
                        val builder = StringBuilder()
                        builder.append("you selected:\n")
                        for (fileBean in selectedFiles) {
                            builder.append(fileBean.path + "\n")
                        }
                        Mtools.toast(builder.toString())
                        return true
                    }
                }
            )
            .show()
    }

    fun toSelectFile(){
        SelectFileActivity.start(this)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun permission(){
        PermissionLauncher()
            .with(this) //this可以是androidx.fragment.app.Fragment或者androidx.fragment.app.FragmentActivity
            .granted {
                Log.e("======","已经获得权限")
            }
            .denied { rejectPermissionList: List<String> ->
                for(permission in rejectPermissionList){
                    if(permission == Manifest.permission.MANAGE_EXTERNAL_STORAGE && !Environment.isExternalStorageManager()){
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.data = Uri.parse("package:$packageName")
                        startActivity(intent)
                    }
                }
                Log.e("======",GsonUtil.GsonString(rejectPermissionList))
            }
            .request(getFilePermissions().toTypedArray(), arrayOf("文件选择"), arrayOf("该功能需要获取手机存储读取权限用于选择本地文件"))
    }


    /**
     * 选择文件所需权限
     */
    fun getFilePermissions(): MutableList<String> {
        return mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}