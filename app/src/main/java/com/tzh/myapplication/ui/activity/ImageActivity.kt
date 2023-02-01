package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityImageBinding
import com.tzh.myapplication.view.GyroscopeManager

class ImageActivity : AppBaseActivity<ActivityImageBinding>(R.layout.activity_image) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, ImageActivity::class.java))
        }
    }

    private val gyroscopeManager by lazy {
        GyroscopeManager()
    }
    override fun initView() {
        binding.xImage.setGyroscopeManager(gyroscopeManager)
    }

    override fun initData() {

    }

    override fun onResume() {
        gyroscopeManager.register(this);
        super.onResume()
    }

    override fun onPause() {
        gyroscopeManager.unregister();
        super.onPause()
    }
}