package com.tzh.mylibrary.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tzh.mylibrary.R
import com.tzh.mylibrary.util.AppKtx
import com.tzh.mylibrary.util.Util
import com.tzh.mylibrary.util.dpToPx

abstract class BaseBindingDialog<B : ViewDataBinding> @JvmOverloads constructor(
    context: Context,
    @LayoutRes LayoutId: Int,
    @StyleRes themeResId: Int = R.style.dialog_float_translucent
) : Dialog(context, themeResId) {
    companion object {
        /**
         * 内置动画 透明浮现动画
         */
        val ANIM_TRANSLUCENT = R.style.dialog_float_translucent

        /**
         * 内置动画——》底部出现动画
         */
        val ANIM_BOTTOM = R.style.BottomAnimation
    }

    /**
     * 顶部view 高度
     */
    protected var topViewHeight = 120f

    /**
     * dialog 宽
     */
    var windowWidth = (Util.getPhoneWidth(context) * 0.85f).toInt()

    /**
     * dialog 高
     */
    var windowHeight = ViewGroup.LayoutParams.WRAP_CONTENT

    /**
     * dialog 位置
     */
    var windowGravity = Gravity.CENTER

    /**
     * dialog 动画
     */
    var windowAnim: Int = ANIM_TRANSLUCENT

    /**
     * 是否需要输入法
     */
    var isNeedInput: Boolean = false

    /**
     * 点击外部关闭弹框
     */
    var isCanceledOnTouchOutsideDialog = true

    /**
     * 返回键关闭弹框
     */
    var isCancelableDialog = true

    /**
     * 高度是否占满屏幕
     */
    var isMatchHeight = false

    /**
     * 从底部出现的dialog 的配置
     */
    @JvmOverloads
    fun initBottomDialog(matchHeight: Boolean = false) {
        isMatchHeight = matchHeight
        windowWidth = ViewGroup.LayoutParams.MATCH_PARENT
        windowHeight = if (matchHeight) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        windowGravity = Gravity.BOTTOM
        windowAnim = ANIM_BOTTOM
    }

    protected var binding: B = DataBindingUtil.inflate(layoutInflater, LayoutId, null, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        super.onCreate(savedInstanceState)

        //如果从底部出来的 dialog，需要至少距离顶部 120的高度
        if (windowAnim == ANIM_BOTTOM) {
            setContentView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, if (isMatchHeight) LinearLayout.LayoutParams.MATCH_PARENT else LinearLayout.LayoutParams.WRAP_CONTENT)
                addView(View(context).also {
                    it.setOnClickListener {
                        if (isCanceledOnTouchOutsideDialog) {
                            dismiss()
                        }
                    }
                }, Util.getPhoneWidth(context), context.dpToPx(topViewHeight))
                if (isNeedInput) {
                    //如果弹框有输入法，则 高度不能用填充，必须是计算的高度，
//                    addView(binding.root, windowWidth, Util.getPhoneHeight(context) - context.dpToPx(121f))
                    addView(binding.root, windowWidth, LinearLayout.LayoutParams.MATCH_PARENT)
                } else {
                    addView(binding.root, windowWidth, windowHeight)
                }
            })
        } else {
            setContentView(binding.root)
        }

        setCanceledOnTouchOutside(isCanceledOnTouchOutsideDialog)
        setCancelable(isCancelableDialog)
        window?.run {
            if (isNeedInput) {
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            }
            attributes = attributes.also { params ->
                params.width = windowWidth
                params.height = windowHeight
                params.gravity = windowGravity
            }
            setWindowAnimations(windowAnim)
        }
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

    private fun getMetricsHeight(): Int {
//        Util.getPhoneHeight()
        val outMetrics = DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(outMetrics)
//        val widthPixels = outMetrics.widthPixels
//        val heightPixels = outMetrics.heightPixels
        if (outMetrics.heightPixels == Util.getPhoneHeight(context)) {
            return outMetrics.heightPixels - AppKtx.getStatusBarHeight(context) - AppKtx.getNavigationBarHeight(context)
        }
        return outMetrics.heightPixels
    }

}