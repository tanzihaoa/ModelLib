package com.tzh.myapplication.ui

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityListBinding
import com.tzh.myapplication.ui.adapter.ListAdapter
import com.tzh.myapplication.ui.dto.ListDTO
import com.tzh.mylibrary.utils.initAdapter
import com.tzh.mylibrary.utils.linear
import com.tzh.mylibrary.utils.verDivider

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
        binding.recyclerView.linear().initAdapter(mAdapter).verDivider(8f)
        binding.smartLayout.setOnRefreshLoadMoreListener {
            getData()
        }
    }

    override fun initData() {
        getData()
    }

    private fun getData(){
        binding.smartLayout.pageCount = 4
        if(binding.smartLayout.isRefresh){
            mAdapter.setDatas(ListDTO().getList())
        }else{
            mAdapter.addDatas(ListDTO().getList())
        }
        binding.smartLayout.loadSuccess(mAdapter,binding.loadView)
    }
}