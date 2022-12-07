package com.tzh.myapplication.network

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tzh.myapplication.adapter.XRvBindingPureDataAdapter
import com.tzh.myapplication.utils.emptyToNull
import com.tzh.myapplication.utils.loadError
import com.tzh.myapplication.view.LoadView
import com.tzh.myapplication.view.XSmartRefreshLayout
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.myapplication.utils.toDefault
import com.tzh.myapplication.utils.toEmptyString
import io.reactivex.exceptions.CompositeException
import io.reactivex.functions.Consumer
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 错误收集处理
 */
class DefaultError : Consumer<Throwable> {
    /**
     * 是否允许显示错误toast
     */
    var isShowToast: Boolean = true

    var listener: OnDefaultErrorListener? = null

    var mLoadView: LoadView? = null

    /**
     * 刷新加载view
     */
    var mSmartLayout: SmartRefreshLayout? = null

    /**
     * 是否在loadview中 显示 重新加载按钮
     */
    var isReload = true

    fun setErrorListener(l: OnDefaultErrorListener): DefaultError {
        listener = l
        return this
    }

    constructor() {}

    constructor(showToast: Boolean) {
        isShowToast = showToast
    }

    constructor(l: OnDefaultErrorListener) {
        this.listener = l
    }


    constructor(view: LoadView? = null, l: OnDefaultErrorListener) {
        this.mLoadView = view
        this.listener = l
    }

    constructor(
        view: LoadView? = null,
        smartLayout: SmartRefreshLayout? = null,
        l: OnDefaultErrorListener? = null
    ) {
        this.mLoadView = view
        this.mSmartLayout = smartLayout
        this.listener = l
    }

    /**
     * 是否需要重新加载按钮
     */
    constructor(
        view: LoadView? = null,
        smartLayout: SmartRefreshLayout? = null,
        reload: Boolean = true,
        l: OnDefaultErrorListener? = null
    ) {
        this.mLoadView = view
        this.mSmartLayout = smartLayout
        this.isReload = reload
        this.listener = l
    }


    open override fun accept(t: Throwable?) {
        Log.e("DefaultError", t.toString())

        //这个错误属于 rxjava 内部传递错误，需要取出这个错误
        val throwable = if (t is CompositeException) {
            t.exceptions.emptyToNull()?.get(0).toDefault(t)
        } else {
            t
        }

        if (mSmartLayout != null) {
            if (mSmartLayout is XSmartRefreshLayout) {
                (mSmartLayout as? XSmartRefreshLayout)?.loadError(mLoadView, throwable, isReload)
            } else {
                mSmartLayout?.loadError(mLoadView, throwable, isReload)
            }
        } else {
            mLoadView?.loadingError(throwable, isReload)
        }

        listener?.run {
            isShowToast = if (throwable is ApiThrowable) {
                !this.onError(throwable)
            } else {
                !this.onError(ApiThrowable(ApiThrowable.HTTP_REQUEST_ERROR, getErrorMessage(throwable), throwable))
            }
        }
        if (isShowToast) {
            ToastUtil.show(getErrorMessage(throwable))
        }

        listener?.run {
            if (throwable is ApiThrowable) {
                this.onError(throwable)
            } else {
                this.onError(ApiThrowable(-99, getErrorMessage(throwable), throwable))
            }
        }
        if (isShowToast) {
            ToastUtil.show(getErrorMessage(throwable))
        }
    }

    companion object {

        fun getErrorMessage(throwable: Throwable?): String {
            if (throwable == null) {
                return "未知异常"
            }
            Log.e(
                "DefaultError",
                "error:" + throwable.javaClass.simpleName + ",e.getmessage:" + throwable.message
            )
            return if (throwable is ApiThrowable) {
                throwable.message.toEmptyString()
            } else if (throwable is ConnectException || throwable is NullPointerException) {
                "网络连接失败,请检测您网络连接"
            } else if (throwable is SocketTimeoutException) {
                "网络连接超时,请检测您网络连接"
            } else if (throwable is JsonSyntaxException) {
                "数据解析错误"
            } else if (throwable is UnknownHostException) {
                "请确认已连接网络"
            } else {
                throwable.toString()
            }
        }
    }
}

/**
 * 提供额外的处理
 */
interface OnDefaultErrorListener {
    fun onError(throwable: ApiThrowable): Boolean
}

/**
 * 属于刷新加载 请求 加载错误使用
 */
fun Throwable?.loadListError(
    smart: SmartRefreshLayout?,
    adapter: XRvBindingPureDataAdapter<*>? = null,
    loadView: LoadView? = null,
    isShowToast: Boolean = true,
    isReLoad: Boolean = true
) {
    if (isShowToast) {
        ToastUtil.show(DefaultError.getErrorMessage(this))
    }
    // smart.loadError(adapter, loadView, this, isReLoad)
}

/**
 * 不属于刷新加载 请求 加载错误使用
 */
fun Throwable?.loadError(
    loadView: LoadView?,
    isShowToast: Boolean = true,
    isReLoad: Boolean = true
) {
    if (isShowToast) {
        ToastUtil.show(DefaultError.getErrorMessage(this))
    }
    loadView?.loadingError(DefaultError.getErrorMessage(this), isReLoad)
}

class ApiThrowable(val status: Int, message: String? = null, case: Throwable? = null) : Throwable(message, case) {
    companion object {
        /**
         * 默认错误
         */
        const val HTTP_ERROR_DEFAULT = -1

        /**
         * 请求错误
         */
        const val HTTP_REQUEST_ERROR = -99

        /**
         * 用户端- 预约大师时间 错误
         */
        const val APPOINTMENT_TIME_ERROR = -2

        /**
         * 搜索违规
         */
        const val HTTP_ERROR_SEARCH_VIOLATION = -1002

        /**
         * xx 不存在或已删除
         */
        const val HTTP_ERROR_NO_EXIT_OR_DELETE = -1003

        /**
         * 用户需要重新登录
         */
        const val HTTP_ERROR_USER_RESET_LOGIN = -1004


        /**
         * 老师用户 转为 普通用户时，需要重新登录
         */
        const val HTTP_ERROR_ROLES_CHANGED = 4001 //角色切换


        const val HTTP_ERROR_SYSTEM_PROBLEM = 4002 //角色切换

        @JvmStatic
        fun newThrowable(throwable: Throwable? = null): ApiThrowable {
            return ApiThrowable(HTTP_ERROR_DEFAULT, DefaultError.getErrorMessage(throwable), throwable)
        }

        @JvmStatic
        fun newThrowable(content: String): ApiThrowable {
            return ApiThrowable(HTTP_ERROR_DEFAULT, content)
        }
    }
}