package com.tzh.myapplication.base

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.tzh.myapplication.utils.LogUtils
import com.tzh.mylibrary.base.XBaseBindingActivity

abstract class AppBaseActivity<B : ViewDataBinding>(@LayoutRes LayoutId: Int = 0) : XBaseBindingActivity<B>(LayoutId) {

    protected val TAG = javaClass.simpleName

    private val mInputMethodManager: InputMethodManager by lazy {
        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init()
        super.onCreate(savedInstanceState)
    }

    open fun hideSoftKeyBoard() {
        val localView = currentFocus
        if (localView != null) {
            mInputMethodManager.hideSoftInputFromWindow(localView.windowToken, 2)
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtils.e(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.e(TAG,"onPause")
    }

    override fun onCloseActivity() {
        hideSoftKeyBoard()
        ImmersionBar.with(this).reset()
    }


}