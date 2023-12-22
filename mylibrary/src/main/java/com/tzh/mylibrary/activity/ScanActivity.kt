package com.tzh.mylibrary.activity

import android.app.Activity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.tzh.mylibrary.R
import com.tzh.mylibrary.base.XBaseBindingActivity
import com.tzh.mylibrary.databinding.ActivityScanBinding
import com.tzh.mylibrary.livedata.ActivityCloseLiveData
import com.tzh.mylibrary.util.toDefault

class ScanActivity : XBaseBindingActivity<ActivityScanBinding>(R.layout.activity_scan) {
    companion object {
        @JvmStatic
        fun start(context: Activity) {
            val intentIntegrator = IntentIntegrator(context)
            intentIntegrator.setBeepEnabled(true)
            /*设置启动我们自定义的扫描活动，若不设置，将启动默认活动*/
            intentIntegrator.captureActivity = ScanActivity::class.java
            intentIntegrator.initiateScan()
        }
    }

    private val mTitle by lazy {
        intent.getStringExtra("title")
    }

    var capture : CaptureManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        capture = CaptureManager(this, binding.dbv)
        capture?.initializeFromIntent(intent,savedInstanceState)
        capture?.decode()
    }
    override fun initView() {
        binding.titleBar.setTitleTxt(mTitle.toDefault("扫码"))
        binding.dbv.setTorchListener(object : DecoratedBarcodeView.TorchListener{
            override fun onTorchOn() {

            }

            override fun onTorchOff() {

            }
        })

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause();
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy();
    }


    override fun onCloseActivity() {
        ActivityCloseLiveData.instance.value = true
    }
}