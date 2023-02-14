package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityImageBinding

class ImageActivity : AppBaseActivity<ActivityImageBinding>(R.layout.activity_image) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, ImageActivity::class.java))
        }
    }

    override fun initView() {
        binding.startView.setStartNum(4.5f)
    }

    override fun initData() {

    }
}