package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityListBinding
import com.tzh.myapplication.service.MessageService
import com.tzh.myapplication.ui.adapter.ListAdapter
import com.tzh.mylibrary.util.initAdapter
import com.tzh.mylibrary.util.linear
import com.tzh.mylibrary.util.verDivider

class ListActivity : AppBaseActivity<ActivityListBinding>(R.layout.activity_list) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, ListActivity::class.java))
        }
    }

    private val mAdapter by lazy {
        ListAdapter()
    }

    val mIntent by lazy {
        Intent(this, MessageService::class.java)
    }

    override fun initView() {
        binding.recyclerView.linear().initAdapter(mAdapter).verDivider(1f)
    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(mIntent)
    }
}