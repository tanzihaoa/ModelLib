package com.tzh.myapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import com.tzh.myapplication.utils.bindingInflateLayout
import com.tzh.mylibrary.R
import com.tzh.mylibrary.utils.dpToPx
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * 加载
 */
class LoadView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @SuppressLint("CheckResult")
    companion object {

        /**
         * 加载中
         */
        const val STATE_LOADING = 1

        /**
         * 加载成功
         */
        private const val STATE_SUCCESS = 2

        /**
         * 加载失败
         */
        private const val STATE_ERROR = 3

        /**
         * 加载 空
         */
        private const val STATE_EMPTY = 4

        /**
         * 搜索 空
         */
        private const val STATE_SEARCH_EMPTY = 5

    }

    /**
     * 图片/gif 宽
     */
    private var mImgWidth: Int = -1

    /**
     * 图片/gif 高
     */
    private var mImgHeight: Int = -1

    /**
     * gif 宽
     */
    private var mLoadWidth: Int = -1

    /**
     * gif 高
     */
    private var mLoadHeight: Int = -1

    /**
     * 加载 layout 是否居中
     */
    private var mLoadLayoutIsCenter = true

    /**
     * 加载view 在不居中的情况下， 距离顶部多少距离
     */
    private var mLoadLayoutTopMargin = -1

    /**
     * 状态view 是否居中
     */
    private var mStateLayoutIsCenter = false

    /**
     * 状态view 在不居中的情况下， 距离顶部多少距离
     */
    private var mStateLayoutTopMargin = -1

    /**
     * 去 xx 的按钮文字，没有文字则不显示
     */
    private var mGoButtonText: String? = null

    /**
     * 直接显示某个状态
     */
    private var mShowStateDefault: Int = STATE_LOADING

    /**
     * 状态码
     */
    var mStatus = STATE_LOADING

    /**
     * 提示文字大
     */
    var mStateBigTipStr: String? = null

    /**
     * 提示文字小
     */
    var mStateMinTipStr: String? = null


    /**
     * 是否显示重试按钮
     */
    var isShowReload: Boolean = false


    /**
     * 是否是网络错误
     */
    var isNetError: Boolean = false

    /**
     * 状态处理
     */
    var onStateListener: OnStateListener? = null

    /**
     * 页面加载view
     */
    private var mPageLoadView: ImageView? = null


    init {
        attrs?.run {
            initView(this)
        }
    }

    private fun initView(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadView)
        //图片/ 宽
        mImgWidth = typedArray.getDimensionPixelSize(R.styleable.LoadView_lvImgWidth, -1)
        //图片/ 高
        mImgHeight = typedArray.getDimensionPixelSize(R.styleable.LoadView_lvImgHeight, -1)
        //gif 宽
        mLoadWidth = typedArray.getDimensionPixelSize(R.styleable.LoadView_lvLoadWidth, -1)
        //gif 高
        mLoadHeight = typedArray.getDimensionPixelSize(R.styleable.LoadView_lvLoadHeight, -1)

        //加载view 是否居中
        mLoadLayoutIsCenter = typedArray.getBoolean(R.styleable.LoadView_lvLoadLayoutIsCenter, true)
        //加载view 在不居中的情况下， 距离顶部多少距离
        mLoadLayoutTopMargin = typedArray.getDimensionPixelSize(R.styleable.LoadView_lvLoadLayout_top_margin, -1)
        //状态view 是否居中
        mStateLayoutIsCenter = typedArray.getBoolean(R.styleable.LoadView_lvStateLayoutIsCenter, false)
        //状态view 在不居中的情况下， 距离顶部多少距离
        mStateLayoutTopMargin = typedArray.getDimensionPixelSize(R.styleable.LoadView_lvStateLayout_top_margin, -1)
        //去 xx 的按钮文字，没有文字则不显示
        mGoButtonText = typedArray.getString(R.styleable.LoadView_lvGoButtonText)
        //直接显示某个状态
        mShowStateDefault = typedArray.getInt(R.styleable.LoadView_lvShowState, STATE_LOADING)
        typedArray.recycle()

        if (mImgWidth == -1) {
            mImgWidth = context.dpToPx(120f)
        }
        if (mImgHeight == -1) {
            mImgHeight = context.dpToPx(120f)
        }

        if (mLoadWidth == -1) {
            mLoadWidth = context.dpToPx(80f)
        }
        if (mLoadHeight == -1) {
            mLoadHeight = context.dpToPx(80f)
        }

        if (mLoadLayoutTopMargin == -1 && !mLoadLayoutIsCenter) {
            mLoadLayoutTopMargin = context.dpToPx(120f)
        }

        if (mStateLayoutTopMargin == -1 && !mStateLayoutIsCenter) {
            mStateLayoutTopMargin = context.dpToPx(120f)
        }

        isClickable = true
        if (background == null) {
            setBackgroundResource(R.color.color_f4f4f4)
        }

        //默认显示哪个状态
        show(mShowStateDefault)
    }


    private fun show(status: Int) {
        mStatus = status
        mPageLoadView?.setImageDrawable(null)
        mPageLoadView?.setWillNotDraw(true)
        removeAllViews()
        when (status) {
            STATE_LOADING -> {
                //加载中
                bindingInflateLayout<StatusLayoutLoadBinding>(R.layout.status_layout_load).let { binding ->
                    mPageLoadView = binding.loadImg
                    binding.loadImg.layoutParams = binding.loadImg.layoutParams.apply {
                        this.width = mLoadWidth
                        this.height = mLoadHeight
                    }
                    if (mLoadLayoutIsCenter) {
                        binding.loadLayout.gravity = Gravity.CENTER
                        binding.loadLayout.updatePadding(0, 0, 0, 0)
                    } else {
                        binding.loadLayout.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                        binding.loadLayout.updatePadding(0, mLoadLayoutTopMargin, 0, 0)
                    }
                    if (mStateBigTipStr.isNullOrEmpty()) {
                        mStateBigTipStr = "加载中..."
                    }
                    binding.loadImg.setWillNotDraw(false)
                    Glide.with(binding.loadImg.context.applicationContext).clear(binding.loadImg)
                    Glide.with(binding.loadImg.context.applicationContext).asGif().load(R.drawable.page_loading).into(binding.loadImg)
                }
            }

            STATE_SUCCESS, STATE_ERROR, STATE_EMPTY, STATE_SEARCH_EMPTY, ApiThrowable.HTTP_ERROR_NO_EXIT_OR_DELETE -> {
                bindingInflateLayout<StatusLayoutStateBinding>(R.layout.status_layout_state).let { binding ->
                    updateView(binding.stateImg, binding.stateLayout)

                    binding.stateReLoad.visibility = View.GONE

                    when (mStatus) {
                        STATE_SUCCESS -> {
                            //加载成功
                            binding.stateLayout.visibility = View.VISIBLE
                            if (mStateBigTipStr.isNullOrEmpty()) {
                                mStateBigTipStr = "加载成功"
                            }
                            hide()
                        }
                        STATE_ERROR -> {
                            //加载失败
                            if (mStateBigTipStr.isNullOrEmpty()) {
                                mStateBigTipStr = "加载失败"
                            }
                            if (isShowReload) {
                                binding.stateReLoad.visibility = View.VISIBLE
                            }
                            binding.stateLayout.visibility = View.VISIBLE
                            binding.stateImg.setWillNotDraw(false)
                            if (isNetError) {
                                binding.stateImg.setImageResource(R.mipmap.load_state_error_net)
                                mStateBigTipStr = "网络连接失败，请检查网络设置"
                                mStateMinTipStr = "重新连接"
                            } else {
                                binding.stateImg.setImageResource(R.mipmap.load_state_error)
                            }
                        }
                        ApiThrowable.HTTP_ERROR_NO_EXIT_OR_DELETE -> {
                            //数据不存在，或已删除
                            if (mStateBigTipStr.isNullOrEmpty()) {
                                mStateBigTipStr = "不存在或已删除"
                            }
                            binding.stateLayout.visibility = View.VISIBLE
                            if (!mGoButtonText.isNullOrEmpty()) {
                                binding.stateGoLayout.visibility = View.VISIBLE
                            }
                            binding.stateImg.setWillNotDraw(false)
                            binding.stateImg.setImageResource(R.mipmap.load_state_empty)
                        }
                        STATE_EMPTY -> {
                            //加载没有数据
                            if (mStateBigTipStr.isNullOrEmpty()) {
                                mStateBigTipStr = "暂无数据"
                            }
                            binding.stateLayout.visibility = View.VISIBLE
                            if (!mGoButtonText.isNullOrEmpty()) {
                                binding.stateGoLayout.visibility = View.VISIBLE
                            }
                            binding.stateImg.setWillNotDraw(false)
                            binding.stateImg.setImageResource(R.mipmap.load_state_empty)
                        }
                        STATE_SEARCH_EMPTY -> {
                            //搜索为空
                            if (mStateBigTipStr.isNullOrEmpty()) {
                                mStateBigTipStr = "暂无数据"
                            }
                            binding.stateLayout.visibility = View.VISIBLE
                            if (!mGoButtonText.isNullOrEmpty()) {
                                binding.stateGoLayout.visibility = View.VISIBLE
                            }
                            binding.stateImg.setWillNotDraw(false)
                            binding.stateImg.setImageResource(R.mipmap.load_state_empty)
                        }
                    }

                    if (!mGoButtonText.isNullOrEmpty()) {
                        binding.stateGoTxt.text = mGoButtonText
                    }

                    binding.stateBigTip.text = mStateBigTipStr.toDefault("")
                    binding.stateMinTip.text = mStateMinTipStr.toDefault("")

                    //状态页 两个按钮都不显示则 隐藏 占位 view
                    if (binding.stateReLoad.visibility == View.VISIBLE || binding.stateGoLayout.visibility == View.VISIBLE) {
                        binding.stateSpace.visibility = View.VISIBLE
                    } else {
                        binding.stateSpace.visibility = View.GONE
                    }
                    //状态页 提示文字小 没有文字时，则隐藏
                    if (this.mStateMinTipStr.isNullOrEmpty()) {
                        binding.stateMinTip.visibility = View.GONE
                    } else {
                        binding.stateMinTip.visibility = View.VISIBLE
                    }
                    //重试
                    binding.stateReLoad.setOnClickListener {
                        //点击 重试 后，显示加载
                        loadingShow()
                        onStateListener?.onReload()
                    }
                    //去xxxx
                    binding.stateGoLayout.setOnClickListener {
                        onStateListener?.onGoTo()
                    }
                }
            }
        }
    }

    private fun updateView(iv: ImageView, ll: LinearLayout) {
        iv.layoutParams = iv.layoutParams.apply {
            this.width = mImgWidth
            this.height = mImgHeight
        }

        if (mStateLayoutIsCenter) {
            ll.gravity = Gravity.CENTER
            ll.updatePadding(0, 0, 0, 0)
        } else {
            ll.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            ll.updatePadding(0, mStateLayoutTopMargin, 0, 0)
        }
    }

    /**
     * 加载
     */
    @JvmOverloads
    fun loadingShow(tip: String? = null) {
        visibility = View.VISIBLE
        mStateBigTipStr = tip.toDefault("加载中")
        mStateMinTipStr = ""
        show(STATE_LOADING)
    }

    /**
     * 加载数据
     * @param haveData 是否有数据
     * @param bigTip 大字提示
     * @param minTip 小字提示
     */
    @JvmOverloads
    fun loadingForData(haveData: Boolean, bigTip: String? = "暂无数据", minTip: String? = "") {
        visibility = View.VISIBLE
        mStateBigTipStr = bigTip.toDefault("暂无数据")
        mStateMinTipStr = minTip.toDefault("")
        if (haveData) {
            show(STATE_SUCCESS)
        } else {
            show(STATE_EMPTY)
        }
    }

    /**
     *  搜索 加载数据
     * @param list  数据集合
     * @param bigTip 大字提示
     * @param minTip 小字提示
     */
    @JvmOverloads
    fun loadingForSearch(list: MutableList<*>?, bigTip: String? = "暂无数据", minTip: String? = "") {
        loadingForSearch(list?.size.toDefault(0) > 0, bigTip, minTip)
    }


    /**
     *  搜索 加载数据
     * @param haveData 是否有数据
     * @param bigTip 大字提示
     * @param minTip 小字提示
     */
    @JvmOverloads
    fun loadingForSearch(haveData: Boolean, bigTip: String? = "暂无数据", minTip: String? = "") {
        visibility = View.VISIBLE
        mStateBigTipStr = bigTip.toDefault("暂无数据")
        mStateMinTipStr = minTip.toDefault("")
        if (haveData) {
            show(STATE_SUCCESS)
        } else {
            show(STATE_SEARCH_EMPTY)
        }
    }

    @Deprecated("请使用 loadingForSearch")
    @JvmOverloads
    fun loadingForSearchResult(haveResult: Boolean, isToActivity: Boolean = false) {
        visibility = View.VISIBLE
        mStateBigTipStr = "没有搜索结果"
        mStateMinTipStr = "换个关键词再试试吧~"
        if (haveResult) {
            show(STATE_SUCCESS)
        } else {
            show(STATE_SEARCH_EMPTY)
        }
    }

    /**
     * 加载失败
     */
    @JvmOverloads
    fun loadingError(throwable: Throwable?, showReload: Boolean = true) {
        throwable?.printStackTrace()
        if (throwable is ApiThrowable) {
            when (throwable.status) {
                ApiThrowable.HTTP_ERROR_SEARCH_VIOLATION -> {
                    //搜索违规错误
                    loadingError(throwable.message, false)
                    return
                }
                ApiThrowable.HTTP_ERROR_NO_EXIT_OR_DELETE -> {
                    //不存在或已删除
                    visibility = View.VISIBLE
                    mStateBigTipStr = throwable.message
                    show(ApiThrowable.HTTP_ERROR_NO_EXIT_OR_DELETE)
                    return
                }
            }
            if (throwable.message.toDefault("").contains("不存在或已删除")) {
                //返回的api异常中，带有不存在或已删除的 需要显示这个错误
                visibility = View.VISIBLE
                mStateBigTipStr = throwable.message
                show(ApiThrowable.HTTP_ERROR_NO_EXIT_OR_DELETE)
                return
            }
        }
        //用来判断是不是网络导致的请求异常
        isNetError = when {
            throwable is ConnectException ||
                    throwable?.cause is ConnectException ||
                    throwable is NullPointerException ||
                    throwable?.cause is NullPointerException ||
                    throwable is SocketTimeoutException ||
                    throwable?.cause is SocketTimeoutException ||
                    throwable is UnknownHostException ||
                    throwable?.cause is UnknownHostException -> {
                true
            }
            else -> {
                false
            }
        }
        loadingError(throwable?.message, showReload)
    }

    /**
     * 加载失败
     */
    @JvmOverloads
    fun loadingError(error: String? = "加载失败", showReload: Boolean = true) {
        visibility = View.VISIBLE
        mStateBigTipStr = error.toDefault("加载失败")
        isShowReload = showReload
        show(STATE_ERROR)
    }


    /**
     * 隐藏
     */
    @JvmOverloads
    fun hide(state: Int = STATE_SUCCESS) {
        mStatus = state
        mPageLoadView?.setImageDrawable(null)
        mPageLoadView?.setWillNotDraw(true)
        visibility = View.GONE
    }

    /**
     * 加载成功
     */
    fun loadingSuccess() {
        show(STATE_SUCCESS)
    }

    fun loadingForNet(isNetConnected: Boolean) {
        visibility = View.VISIBLE
        isNetError = !isNetConnected
        if (isNetConnected) {
            hide()
        } else {
            show(STATE_ERROR)
        }
    }

    @Deprecated("弃用")
    fun loadingProgressT() {

    }

    @Deprecated("弃用")
    fun loadingProgress() {

    }

    @Deprecated("弃用")
    fun setReloadClickListener(listener: OnClickListener) {

    }

    @Deprecated("弃用")
    fun setToClickListener(listener: OnClickListener) {

    }

    @Deprecated("弃用")
    fun loadingForResultShowTo(b1: Boolean, s1: String, b2: Boolean, s2: String) {

    }

    /**
     * 判断是否有网络连接
     */
    fun Context?.isNetConnected(): Boolean {
        if (this != null) {
            val mConnectivityManager = getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                @Suppress("DEPRECATION")
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**
     * 状态监听
     */
    abstract class OnStateListener {
        /**
         * 重新加载
         */
        open fun onReload() {}

        /**
         * 去登录
         */
        open fun onLogin() {}

        /**
         * 去、。。
         */
        open fun onGoTo() {}

        /**
         * 后退
         */
        open fun onBack() {}
    }
}