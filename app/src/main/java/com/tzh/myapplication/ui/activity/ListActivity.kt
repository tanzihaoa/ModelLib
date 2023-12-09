package com.tzh.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.base.DataUtil
import com.tzh.myapplication.databinding.ActivityListBinding
import com.tzh.myapplication.service.MessageService
import com.tzh.myapplication.ui.adapter.ListAdapter
import com.tzh.mylibrary.util.initAdapter
import com.tzh.mylibrary.util.linear
import com.tzh.mylibrary.util.setAnimator
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

    override fun initView() {
        binding.recyclerView.linear().initAdapter(mAdapter).verDivider(1f)
        binding.recyclerView.setAnimator()
        binding.smartLayout.pageCount = 3
        binding.smartLayout.setOnRefreshLoadMoreListener {
            requestData()
        }
        requestData()
    }

    override fun initData() {

    }


    /**
     * 获取列表数据
     */
    private fun requestData() {
        val list = DataUtil.getData()
        if(binding.smartLayout.isRefresh){
            mAdapter.setDatas(list)
        }else{
            mAdapter.addDatas(list)
        }
        binding.smartLayout.loadSuccess(mAdapter)
    }
}