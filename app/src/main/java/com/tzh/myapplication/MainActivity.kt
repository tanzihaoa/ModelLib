package com.tzh.myapplication

import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.ui.ListActivity

class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        binding.v = this
    }

    override fun initData() {

    }

    fun toRecycler(){
        ListActivity.start(this)
    }
}