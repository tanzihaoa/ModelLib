package com.tzh.mylibrary.activity.tool

import android.content.Context
import android.content.Intent
import com.gyf.immersionbar.ImmersionBar
import com.tzh.mylibrary.R
import com.tzh.mylibrary.base.XBaseBindingActivity
import com.tzh.mylibrary.databinding.ActivityMuYuBinding
import com.tzh.mylibrary.util.LoadImageUtil
import com.tzh.mylibrary.util.general.AnimatorMediumUtil
import com.tzh.mylibrary.util.sound.SoundPlayer

class MuYuActivity : XBaseBindingActivity<ActivityMuYuBinding>(R.layout.activity_mu_yu)  {

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, MuYuActivity::class.java))
        }
    }

    var num = 0

    //木鱼播放器
    private val muYuSoundPlayer by lazy {
        SoundPlayer(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(false).init()
        binding.ivMuYu.setOnClickListener {
            clickMuYu()
        }

        binding.ivMuGun.setOnClickListener {
            clickMuYu()
        }
    }

    override fun initData() {

    }

    override fun onCloseActivity() {

    }

    /**
     * 敲击木鱼
     */
    private fun clickMuYu(){
        num++
        muYuSoundPlayer.play("https://uubook.oss-cn-shanghai.aliyuncs.com/static/mp3/fish001.mp3",false)
        AnimatorMediumUtil.stick(binding.ivMuYu,binding.ivMuGun,binding.layoutText)
        binding.tvNowNum.text = num.toString()
    }
}