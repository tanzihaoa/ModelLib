package com.tzh.myapplication.base

import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class XBaseBindingActivity<B : ViewDataBinding>(@LayoutRes LayoutId: Int = 0) : AppCompatActivity(LayoutId) {

    protected lateinit var binding: B
    private var mInputMethodManager: InputMethodManager? = null

    /**
     * 是否关闭
     */
    private var isClose = false


    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        //Activity管理
        //Activity管理
        XAppActivityManager.getInstance().addActivity(this)
        binding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        setContentView(binding.root)
        init()
    }

    open fun init() {
        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }

    protected abstract fun initView()
    protected abstract fun initData()

    /**
     * 方法说明:手动释放内存
     * 方法名称:releaseMemory
     * 返回void
     */
    protected abstract fun onCloseActivity()

    override fun onPause() {
        if (isFinishing && !isClose) {
            isClose = true
            onCloseActivity()
        }
        super.onPause()
    }

    override fun onStop() {
        if (isFinishing && !isClose) {
            isClose = true
            onCloseActivity()
        }
        super.onStop()
    }

    override fun onDestroy() {
        XAppActivityManager.getInstance().removeActivity(this)
        super.onDestroy()
    }

}