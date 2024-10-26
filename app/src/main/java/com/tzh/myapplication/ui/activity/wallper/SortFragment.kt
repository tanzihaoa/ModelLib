package com.tzh.myapplication.ui.activity.wallper

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.tzh.myapplication.R
import com.tzh.myapplication.databinding.FragmentSortBinding
import com.tzh.myapplication.network.http.InterfaceSet
import com.tzh.myapplication.network.http.NetGo
import com.tzh.mylibrary.base.XBaseBindingFragment
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.gradDivider
import com.tzh.mylibrary.util.grid
import com.tzh.mylibrary.util.initAdapter
import com.tzh.mylibrary.util.toDefault

/**
 * 分类 fragment
 */
class SortFragment : XBaseBindingFragment<FragmentSortBinding>(R.layout.fragment_sort){

    companion object{
        fun getInstance(id : String) : SortFragment{
           val fragment = SortFragment()
            fragment.setData(id)
           return fragment
        }
    }

    val mAdapter by lazy {
        WallpaperListAdapter()
    }

    /**
     * 分类ID
     */
    var mId : String ?= null

    fun setData(id : String){
        mId = id
    }

    override fun onInitView() {
        binding.fragment = this
        binding.recycleView.grid(3).initAdapter(mAdapter).gradDivider(6f,3)
        binding.smartLayout.setOnRefreshLoadMoreListener {
            getData()
        }

        getData()
    }

    override fun onLoadData() {

    }

    override fun onCloseFragment() {

    }

    private fun getData(){

        ///获取主题类表
        NetGo.getWallpaperListByCateId(mId, binding.smartLayout.pageIndex, object : InterfaceSet.NetGoCallback {
            override fun done(api: String, result: String) {
                LogUtils.e("222结果====",result)
                ///获取墙纸列表
                var wallpaperInfoList_get = mutableListOf<WallpaperInfo>()
                //获取正确的信息返回
                try {
                    val resultJo = JSONObject.parseObject(result)
                    if (resultJo != null) {
                        //获取用户列表
                        if (resultJo.containsKey("picList")) {
                            wallpaperInfoList_get = JSONArray.parseArray(
                                resultJo.getString("picList"),
                                WallpaperInfo::class.java
                            )
                        }

                        if(binding.smartLayout.pageCount == 0){
                            binding.smartLayout.pageCount = 1
                        }
                        binding.smartLayout.pageCount = if(wallpaperInfoList_get.size >= 20) binding.smartLayout.pageCount + 1 else binding.smartLayout.pageCount
                        if ( binding.smartLayout.isRefresh) {
                            mAdapter.setDatas(wallpaperInfoList_get)
                        } else {
                            mAdapter.addDatas(wallpaperInfoList_get)
                        }
                    }
                } catch (e: Exception) {
                    println(e)
                }

                binding.smartLayout.loadSuccess(mAdapter)
            }

            override fun error(api: String, response: String) {

            }
        })
    }
}