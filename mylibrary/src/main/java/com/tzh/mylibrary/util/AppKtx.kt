package com.tzh.mylibrary.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Looper
import android.os.MessageQueue
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.util.regex.Pattern

object AppKtx {

    /**
     * 获取状态栏高度
     *
     * return px
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 24
        val resId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                result.toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

    /*底部导航栏*/
    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 复制到剪贴板
     *
     * @param context context
     * @param tip     标识语
     * @param text    内容
     */
    fun putTextIntoClip(
        context: Context,
        tip: String? = null,
        text: String? = null
    ) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //创建ClipData对象
        val clipData = ClipData.newPlainText(tip, text)
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData)
    }

    /**
     * 触摸点 是否在 rect范围内
     */
    fun isInRect(event: MotionEvent, rect: Rect): Boolean {
        return event.x >= rect.left && event.x <= rect.right && event.y >= rect.top && event.y <= rect.bottom
    }


}

/**
 *  databinding 通过 inflate 绑定  layout
 */
fun <T : ViewDataBinding> Context.bindingInflateLayout(@LayoutRes layoutId: Int): T {
    return DataBindingUtil.inflate(
        LayoutInflater.from(this),
        layoutId, null, false
    )
}


/**
 * 对某个对象 做非null处理
 */
//fun <T> T?.toDefault(default: T): T = this ?: default


fun <T> T?.toJsonString(): String? {
    this ?: return null
    return GsonUtil.GsonString(this)
}


/**
 * string 转列表
 */
inline fun <reified T> String?.stringToList(): MutableList<T> {
    if (this.isNullOrEmpty()) {
        return mutableListOf()
    }
    return GsonUtil.GsonToList(this, T::class.java).nullToEmpty()
}

/**
 * string 转对象
 */
inline fun <reified T> String?.stringToObj(): T? {
    if (this.isNullOrEmpty()) {
        return null
    }
    return try {
        GsonUtil.getGSON().fromJson(this, T::class.java)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 如果最后一位是某个字符，则删除
 */
fun String.deleteLastChar(default: Char): String {
    if (this.isEmpty()) {
        return this
    }
    if (this.lastIndexOf(default) == this.length - 1) {
        return this.substring(0, this.length - 1)
    }
    return this
}


fun String?.toFloat(default: Float): Float {
    this ?: return default
    return if (this.toFloatOrNull() == null) {
        default
    } else {
        this.toFloat()
    }
}

/**
 * 判断当前对象，是不是 null
 */
fun Any?.isNotNullKtx(): Boolean = this != null


/**
 * 主线程闲置后再处理新的
 *
 * @param handler 返回false代表在这个IdleHandler被回调一次后就会被销毁。true代表可以一直被回调。
 */
fun showMainIdle(handler: MessageQueue.IdleHandler) {
    Looper.myQueue().addIdleHandler(handler)
}


/**
 *  databinding 通过 inflate 绑定  layout
 */
fun <T : ViewDataBinding> ViewGroup.bindingInflateLayout(@LayoutRes layoutId: Int): T {
    return DataBindingUtil.inflate(
        LayoutInflater.from(this.context),
        layoutId, this, true
    )
}

/**
 * int 类型数据 前面增加+0
 * @param length 保证长度
 */
fun Int.supplement(length: Int, default: String = "0"): String {
    var thisStr = this.toString()
    if (thisStr.length < length) {
        for (i in 0 until length - thisStr.length) {
            thisStr = default + thisStr
        }
    }
    return thisStr
}

/**
 * long 类型数据 前面增加+0
 * @param length 保证长度
 */
fun Long.supplement(length: Int, default: String = "0"): String {
    var thisStr = this.toString()
    if (thisStr.length < length) {
        for (i in 0 until length - thisStr.length) {
            thisStr = default + thisStr
        }
    }
    return thisStr
}

/**
 * 判断是否是手机号
 */
fun String.isMobile() : Boolean{
    if(this.length != 11){
        return false
    }
    val pattern = "^(\\+?\\d{1,4}[-.\\s]?)?\\(?\\d{1,3}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$"
    val regex = Pattern.compile(pattern)
    val matcher = regex.matcher(this)
    return matcher.matches()
}