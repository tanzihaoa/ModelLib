package com.tzh.myapplication

import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.livedata.LoginStateLiveData
import com.tzh.myapplication.ui.ListActivity
import com.tzh.myapplication.ui.dialog.MyDialog

class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mDialog by lazy {
        MyDialog(this)
    }

    override fun initView() {
        binding.v = this

        LoginStateLiveData.instance.observe(this) { isLogin ->

        }
    }

    override fun initData() {

    }

    fun toRecycler(){
        ListActivity.start(this)
    }

    fun openDialog(){
        mDialog.show()
    }
}