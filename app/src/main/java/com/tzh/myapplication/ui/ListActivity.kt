package com.tzh.myapplication.ui

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityListBinding
import com.tzh.myapplication.network.DefaultError
import com.tzh.myapplication.network.NetWorkApi
import com.tzh.myapplication.ui.adapter.ListAdapter
import com.tzh.myapplication.ui.dto.ListDTO
import com.tzh.mylibrary.utils.initAdapter
import com.tzh.mylibrary.utils.linear
import com.tzh.mylibrary.utils.verDivider
import io.reactivex.functions.Consumer

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
        requestData()
    }
    /**
     * 获取列表数据
     */
    private fun requestData() {
        NetWorkApi.masterShopList(this, binding.smartLayout.pageIndex, "", "", "")
            .subscribe(Consumer {
                binding.smartLayout.pageCount = it.getDataDto().maxPage
                if ( binding.smartLayout.isRefresh) {
//                    mAdapter.setDatas(it.getDataDto().getListDto(), true)
                } else {
//                    mAdapter.addDatas(it.getDataDto().getListDto(), true)
                }
                binding.smartLayout.loadSuccess(mAdapter)
            }, DefaultError(binding.loadView, binding.smartLayout))
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