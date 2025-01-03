package com.tzh.myapplication.ui.activity.encipher

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.base.DataUtil
import com.tzh.myapplication.databinding.ActivityEncipherBinding
import com.tzh.myapplication.utils.AndroidUtil
import com.tzh.mylibrary.util.GsonUtil

class EncipherActivity : AppBaseActivity<ActivityEncipherBinding>(R.layout.activity_encipher){

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context,EncipherActivity::class.java))
        }
    }

    override fun initView() {
        binding.activity = this

        binding.tvText.setOnClickListener {
            AndroidUtil.copy(binding.tvText.text.toString())
        }
    }

    override fun initData() {

    }

    fun encode(){
        val message = mutableMapOf<String,String>().apply {
            put("a","1")
            put("b","2")
        }
        EncipherUtil.main()
    }

    fun getCode(){
        binding.tvText.text = GsonUtil.GsonString(DataUtil.getPhoneCodeS())
    }
}