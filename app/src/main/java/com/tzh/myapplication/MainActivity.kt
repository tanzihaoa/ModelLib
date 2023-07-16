package com.tzh.myapplication

import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityMainBinding
import com.tzh.myapplication.ui.activity.CardActivity
import com.tzh.myapplication.ui.activity.ImageActivity
import com.tzh.myapplication.ui.activity.ListActivity
import com.tzh.myapplication.ui.activity.SendMessageActivity
import com.tzh.myapplication.ui.dialog.MyDialog
import com.tzh.myapplication.utils.TimeUtil


class MainActivity : AppBaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var curSelDate: String = ""


    private val mDialog by lazy {
        MyDialog(this)
    }

    override fun initView() {
        binding.v = this

        curSelDate = TimeUtil.getCurrentDate()
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
        CardActivity.start(this)
    }

    fun sendMessage(){
        SendMessageActivity.start(this)
    }
}