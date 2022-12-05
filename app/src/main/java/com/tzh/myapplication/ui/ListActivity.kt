package com.tzh.myapplication.ui

import android.content.Context
import android.content.Intent
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityListBinding
import com.tzh.myapplication.network.DefaultError
import com.tzh.myapplication.network.NetWorkApi
import com.tzh.myapplication.ui.adapter.ListAdapter
import com.tzh.myapplication.view.LoadView
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.myapplication.utils.initAdapter
import com.tzh.myapplication.utils.linear
import com.tzh.myapplication.utils.verDivider

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
            requestData()
        }
        binding.loadView.onStateListener = object : LoadView.OnStateListener(){
            override fun onReload() {
                requestData()
            }
        }
        requestData()
    }

    override fun initData() {

    }
    /**
     * 获取列表数据
     */
    private fun requestData() {
        NetWorkApi.masterShopList(this, binding.smartLayout.pageIndex, "", "", "")
            .subscribe({
                binding.smartLayout.pageCount = it.getDataDto().maxPage
                if ( binding.smartLayout.isRefresh) {
                    mAdapter.setDatas(it.getDataDto().getListDto(), true)
                } else {
                    mAdapter.addDatas(it.getDataDto().getListDto(), true)
                }
                ToastUtil.show("hhhh")
                binding.smartLayout.loadSuccess(mAdapter,binding.loadView)
            }, DefaultError(binding.loadView, binding.smartLayout))
    }

}