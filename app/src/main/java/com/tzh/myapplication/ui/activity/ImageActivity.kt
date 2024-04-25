package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityImageBinding
import com.tzh.mylibrary.util.voice.RecordView
import java.io.File


class ImageActivity : AppBaseActivity<ActivityImageBinding>(R.layout.activity_image) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, ImageActivity::class.java))
        }
    }

    override fun initView() {
        binding.recordView.init(this)
        binding.recordView.callback = object : RecordView.RecordCallback{
            override fun recordEnd(outputFile: File, second: Long) {

            }
        }
    }

    override fun initData() {

    }

}