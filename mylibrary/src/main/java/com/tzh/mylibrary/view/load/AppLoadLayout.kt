package com.tzh.mylibrary.view.load

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.util.SmartUtil
import com.tzh.mylibrary.R


@SuppressLint("RestrictedApi")
class AppLoadLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), RefreshFooter {

    private var mStateTv: TextView? = null

    protected var mNoMoreData = false

    init {
        View.inflate(context, R.layout.layout_smart_footer_loading, this)
        mStateTv = findViewById(R.id.refresh_status_tv)
        gravity = Gravity.CENTER
        minimumHeight = SmartUtil.dp2px(60f)
        setBackgroundResource(R.color.color_f4f4f4)
    }


    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        if (!mNoMoreData) {
            when (newState) {
                RefreshState.None -> {
                }
                RefreshState.PullUpToLoad -> {
                    mStateTv?.text = "上拉加载更多"
                }
                RefreshState.Loading, RefreshState.LoadReleased -> {
                    mStateTv?.text = "正在加载中..."
                }
                RefreshState.ReleaseToLoad -> {
                    mStateTv?.text = "释放立即加载"
                }
                RefreshState.Refreshing -> {
                }
                else -> {}
            }
        }
    }


    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        if (!mNoMoreData) {
            if (success) {
                mStateTv?.text = "加载完成";
            } else {
                mStateTv?.text = "加载失败";
            }
            return 200
        }
        return 0
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            if (noMoreData) {
                mStateTv?.text = "———————————  你已经把我看光啦  ———————————"
            } else {
                mStateTv?.text = "上滑继续加载更多"
            }
        }
        return true
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        kernel.requestDrawBackgroundFor(this, ContextCompat.getColor(context, R.color.color_f4f4f4))
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }


    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun autoOpen(duration: Int, dragRate: Float, animationOnly: Boolean): Boolean {

        return false
    }


}