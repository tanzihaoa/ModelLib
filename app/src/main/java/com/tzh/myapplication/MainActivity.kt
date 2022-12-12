package com.tzh.myapplication

import android.content.Intent
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.livedata.LoginStateLiveData
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.dialog.MyDialog
import com.tzh.myapplication.ui.service.MediaControllerService
import com.tzh.myapplication.utils.Util

class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mDialog by lazy {
        MyDialog(this)
    }

    override fun initView() {
        binding.v = this

        LoginStateLiveData.instance.observe(this) { isLogin ->

        }
        if (!Util.isNotificationListenerEnabled(this)) {//是否开启通知使用权
            Util.openNotificationListenSettings(this);
        }

    }

    override fun onRestart() {
        super.onRestart()
        if (Util.isNotificationListenerEnabled(this)) {//开启通知使用权后,重启服务再走一下onStartCommand方法,使设置有效
            startService(Intent(this, MediaControllerService::class.java))
        }
    }


    override fun initData() {

    }

    fun toRecycler(){
        ListActivity.start(this)
    }

    fun openDialog(){
        mDialog.show()
    }
}