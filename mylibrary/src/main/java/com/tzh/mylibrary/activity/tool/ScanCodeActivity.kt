package com.tzh.mylibrary.activity.tool

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.tzh.mylibrary.R
import com.tzh.mylibrary.activity.ScanUtilActivity
import com.tzh.mylibrary.base.XBaseBindingActivity
import com.tzh.mylibrary.databinding.ActivityScanCodeBinding
import com.tzh.mylibrary.dialog.HintDialog
import com.tzh.mylibrary.util.general.AndroidUtil


/**
 * 扫码工具
 */
class ScanCodeActivity : XBaseBindingActivity<ActivityScanCodeBinding>(R.layout.activity_scan_code) {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, ScanCodeActivity::class.java))
        }
    }

    private val mDialog by lazy {
        HintDialog(this,object : HintDialog.HintDialogListener{
            override fun cancel() {

            }

            override fun ok() {
                ScanUtilActivity.start(this@ScanCodeActivity,object : ScanUtilActivity.ScanListener{
                    override fun cancel() {

                    }

                    override fun sure(text: String) {
                        binding.tvReply.text = text
                    }
                })
            }
        })
    }

    override fun initView() {
        binding.activity = this

    }

    override fun initData() {

    }

    override fun onCloseActivity() {

    }

    fun scanCode(){
        if(PackageManager.PERMISSION_GRANTED == this.packageManager.checkPermission(Manifest.permission.CAMERA,this.packageName)){
            ScanUtilActivity.start(this@ScanCodeActivity,object : ScanUtilActivity.ScanListener{
                override fun cancel() {

                }

                override fun sure(text: String) {
                    binding.tvReply.text = text
                }
            })
        }else{
            mDialog.show("将申请相机权限用于扫码功能","获取","取消")
        }
    }

    fun copy(){
        val text = binding.tvReply.text.toString()
        if(text.isEmpty()){
            Toast.makeText(this,"暂无扫码结果",Toast.LENGTH_LONG).show()
        }else{
            AndroidUtil.copy(this,text)
        }
    }
}