package com.tzh.myapplication

import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.ui.activity.ImageActivity
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.dialog.MyDialog

class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mDialog by lazy {
        MyDialog(this)
    }

    override fun initView() {
        binding.v = this
        binding.bmiView.setBmi(28f)

        // 体重标尺
        binding.mrvRuler.mVlaueListener = {
            binding.tvTz.text = it.toString()
        }
        binding.mrvRuler.setValue(60f, 0f, 120f, 0.1f)
    }

    override fun initData() {

    }

    fun toRecycler(){
        ListActivity.start(this)
    }

    fun openDialog(){
        mDialog.show()
    }

    fun toImage(){
        ImageActivity.start(this)
    }

    fun start(){

    }


}