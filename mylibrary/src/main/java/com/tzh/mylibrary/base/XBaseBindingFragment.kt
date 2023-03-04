package com.tzh.mylibrary.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class XBaseBindingFragment<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    lateinit var binding: B

    /**
     * 数据加载方式是否被调用
     */
    protected var isLoadData = false

    /**
     * 是否是暂停状态
     */
    protected var isPause = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = DataBindingUtil.bind(view)!!
        super.onViewCreated(view, savedInstanceState)
        try {
            onInitView()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        isPause = false
        if (!isLoadData) {
            // 将数据加载逻辑放到onResume()方法中
            isLoadData = true
            onLoadData()
        }
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onStop() {
        super.onStop()
        isPause = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (binding.root.parent as ViewGroup?)?.removeView(binding.root)
    }

    override fun onDestroy() {
        isLoadData = false
        onCloseFragment()
        super.onDestroy()
    }


    /**
     * 初始化视图
     */
    protected abstract fun onInitView()

    /**
     * 初始化数据
     */
    protected abstract fun onLoadData()


    /**
     * 关闭fragment
     */
    protected abstract fun onCloseFragment()

}