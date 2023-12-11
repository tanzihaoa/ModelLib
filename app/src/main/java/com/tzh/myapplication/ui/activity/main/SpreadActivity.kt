package com.tzh.myapplication.ui.activity.main

import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivitySpreadBinding
import com.tzh.mylibrary.util.LoadImageUtil


/**
 * 开屏页
 */
class SpreadActivity : AppBaseActivity<ActivitySpreadBinding>(R.layout.activity_spread) {
    override fun initView() {
        LoadImageUtil.loadImageUrl(binding.ivLogo, R.mipmap.ic_launcher,8f)
        binding.root.postDelayed({
            toHome()
        },1000)
    }

    override fun initData() {

    }

    private fun toHome(){
        //处理首次安装点击打开切到后台,点击桌面图标再回来重启的问题及通过应用宝唤起在特定条件下重走逻辑的问题
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        if (!isTaskRoot) {
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        MainActivity.start(this@SpreadActivity)
        finish()
    }

}