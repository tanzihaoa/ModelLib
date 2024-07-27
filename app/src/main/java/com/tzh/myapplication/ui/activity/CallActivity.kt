package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.view.KeyEvent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.base.MyApplication
import com.tzh.myapplication.databinding.ActivityCallBinding
import com.tzh.myapplication.utils.window.CallFloatWindow
import com.tzh.mylibrary.dialog.HintDialog

open class CallActivity : AppBaseActivity<ActivityCallBinding>(R.layout.activity_call){
    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,CallActivity::class.java))
        }
    }
    override fun initView() {
        binding.titleBar.setBackListener{
            showFloatWindow()
        }
        CallFloatWindow.getInstance(this)
        startCount()
    }

    override fun initData() {

    }

    fun isFloatWindowShowing(): Boolean {
        return CallFloatWindow.getInstance().isShowing
    }

    private fun startCount() {
        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.chronometer.start()
    }
    override fun onStart() {
        super.onStart()
        checkFloatIntent(intent)

    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkFloatIntent(intent)
    }


    override fun onStop() {
        super.onStop()
        if(!isFinish){
            showFloatWindow()
        }

    }

    private fun checkFloatIntent(intent: Intent) {
        // 防止activity在后台被start至前台导致window还存在
        if (isFloatWindowShowing()) {
            val totalCostSeconds = CallFloatWindow.getInstance().totalCostSeconds
            binding.chronometer.base = SystemClock.elapsedRealtime() - totalCostSeconds * 1000
            binding.chronometer.start()
        }
        CallFloatWindow.getInstance().dismiss()
    }

    /**
     * 显示悬浮窗
     */
    fun doShowFloatWindow() {
        CallFloatWindow.getInstance().setCostSeconds(binding.chronometer.costSeconds)
        CallFloatWindow.getInstance(MyApplication.mContext).setmBase(binding.chronometer.base)
        CallFloatWindow.getInstance().show()
        var surface = true
        CallFloatWindow.getInstance().update(false, 0, 0, surface)
        CallFloatWindow.getInstance().setCameraDirection(true, true)
        moveTaskToBack(false)
    }

    protected var requestOverlayPermission = false
    protected val REQUEST_CODE_OVERLAY_PERMISSION = 1002
    fun showFloatWindow() {
        if (Settings.canDrawOverlays(this)) {
            doShowFloatWindow()
        } else { // To reqire the window permission.
            if (!requestOverlayPermission) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    // Add this to open the management GUI specific to this app.
                    intent.data = Uri.parse("package:$packageName")
                    startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
                    requestOverlayPermission = true
                    // Handle the permission require result in #onActivityResult();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 是否触发按键为back键
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed()
            true
        } else {
            // 如果不是back键正常响应
            super.onKeyDown(keyCode, event)
        }
    }

    var isFinish = false
    override fun onBackPressed() {
        // 也可以处理成悬浮窗
        HintDialog(this,object : HintDialog.HintDialogListener{
            override fun cancel() {
                isFinish = true
                finish()
            }

            override fun ok() {
                isFinish = false
                showFloatWindow()
                MyApplication.mLifecycleCallbacks?.makeMainTaskToFront(this@CallActivity)
            }
        }).show("返回还是结束?","返回","结束")
    }
}