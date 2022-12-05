package com.tzh.myapplication.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import com.tzh.myapplication.base.BaseApplication

object UtilKtx {
}


inline fun Boolean.isTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

inline fun Boolean.isFalse(block: () -> Unit): Boolean {
    if (!this) block()
    return this
}

inline fun <T> T?.isNull(block: () -> Unit): T? {
    if (this == null) block()
    return this
}

/**
 * 将 中文转Int类型，如果不能转，则返回默认的数字
 */
fun String?.toIntDef(default: Int): Int = this?.toIntOrNull().toDefault(default)

/**
 * 将一个中文转成 Int 类型并且与一个数字相加
 * 如果不能转成中文，则直接不想加，直接返回中文
 */
fun String?.toIntPlusOrString(addNum: Int = 1): String {
    this?.toIntOrNull()?.let {
        return (it + addNum).toString()
    }
    return this.toDefault("")
}


/**
 * sp 转 px
 */
fun Context?.spToPx(spValue: Float): Int {
    val context = this ?: com.tzh.myapplication.base.BaseApplication.sContext
    val fontScale: Float = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

/**
 * dp 转 px
 */
fun Context?.dpToPx(dpValue: Float): Int {
    val context = this ?: com.tzh.myapplication.base.BaseApplication.sContext
    val scale: Float = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun Context?.pxToDp(pxValue: Float): Int {
    val context = this ?: com.tzh.myapplication.base.BaseApplication.sContext
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}


/**
 * 将一个中文转成 Int 类型并且与一个数字相减
 * 如果 相减后 小于0，或者 本身小于0 ，则直接返回0
 * 如果不能转成中文，则直接不想加，直接返回中文
 */
fun String?.toIntReduceOrString(addNum: Int = 1): String {
    this?.toIntOrNull()?.let {
        if (it - addNum > 0) {
            return (it - addNum).toString()
        }
        return "0"
    }
    return this.toDefault("")
}

/**
 * 获取数组 中的某一项，越界则返回null
 */
fun <T> MutableList<T>?.getItem(position: Int): T? {
    this ?: return null
    if (this.size <= position) return null
    return this[position]
}

fun String?.containsKtx(other: String?, ignoreCase: Boolean = false): Boolean {
    if (other.isNullOrEmpty()) return false
    if (this.isNullOrEmpty()) return false
    return this.contains(other, ignoreCase)
}


/**
 * 当数据为空或为null 时返回null
 */
fun <T> MutableList<T>?.emptyToNull(): MutableList<T>? {
    if (isNullOrEmpty()) {
        return null
    }
    return this
}


/**
 * 截取list中 部分数据，处理超过或者不够
 */
fun <T> MutableList<T>?.subListX(fromIndex: Int, toIndex: Int): MutableList<T>? {
    this ?: return null
    return if (this.size > toIndex) {
        subList(fromIndex, toIndex)
    } else {
        subList(fromIndex, this.size)
    }
}


/**
 * 当数据为空或为null 时返回空数组
 */
fun <T> MutableList<T>?.nullToEmpty(): MutableList<T> {
    return this ?: mutableListOf()
}


fun String?.toEmptyString(): String {
    return this ?: ""
}


fun String?.emptyToNull(): String? {
    if (isNullOrEmpty()) {
        return null
    }
    return this
}

/**
 * 对某个对象 做非null处理
 */
fun <T> T?.toDefault(default: T): T = this ?: default

/**
 * view 被点击多次后 再回调
 * @param num 点击次数
 * @param time xx毫秒内 ()
 */
fun View?.setOnClickMoreShow(num: Int = 5, time: Int = 3, block: () -> Unit) {
    this ?: return
    var clickSum = 0
    var clickTime = 0L
    this.setOnClickListener {
        if (clickTime + time > System.currentTimeMillis()) {
            clickSum++
            if (clickSum == num) {
                clickSum = 0
                block()
            }
        } else {
            clickSum = 0
            clickTime = System.currentTimeMillis()
        }
    }
}


fun View?.setOnClickNoDouble(time: Int = 800, block: (view: View) -> Unit) {
    this ?: return
    var oldTime = System.currentTimeMillis()
    this.setOnClickListener {
        if (System.currentTimeMillis() > oldTime + time) {
            oldTime = System.currentTimeMillis()
            block(this)
        }
    }
}

/**
 * 获取状态栏高度
 *
 * return px
 */
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