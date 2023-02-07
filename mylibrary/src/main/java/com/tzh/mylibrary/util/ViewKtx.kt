package com.tzh.mylibrary.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzh.mylibrary.divider.GradDivider
import com.tzh.mylibrary.divider.XRvVerticalDivider

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
 * RecyclerView linear 初始化
 */
fun RecyclerView.linear(
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView {
    this.layoutManager = LinearLayoutManager(context, orientation, reverseLayout)
    return this
}

/**
 * RecyclerView linear 初始化
 */
fun RecyclerView.grid(
    spanCount: Int, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView {
    this.layoutManager = GridLayoutManager(context, spanCount, orientation, reverseLayout)
    return this
}


/**
 * RecyclerView adapter 初始化
 * 方便直接使用
 */
fun <T : RecyclerView.Adapter<*>> RecyclerView.initAdapter(ada: T): RecyclerView {
    this.adapter = ada
    return this
}

/**
 * RecyclerView adapter 初始化
 * 方便直接使用
 */
fun RecyclerView.verDivider(height: Float = 1f, @ColorRes color: Int = android.R.color.transparent): XRvVerticalDivider {
    val vDivider = XRvVerticalDivider(context).apply {
        dividerHeight = height
        setDividerColorRes(color)
    }
    this.addItemDecoration(vDivider)
    return vDivider
}

/**
 * RecyclerView adapter 初始化
 * 方便直接使用
 */
fun RecyclerView.gradDivider(height: Float = 1f, space: Float, num: Int, heightSpace: Float = space ): GradDivider {
    val gDivider = GradDivider(context,space, num, heightSpace)
    this.addItemDecoration(gDivider)
    return gDivider
}

/**
 *
 */
@ColorInt
fun Context?.getColorInt(@ColorRes colorRes: Int): Int {
    this ?: return Color.BLACK
    return ContextCompat.getColor(this, colorRes)
}

/**
 *
 */
fun Context?.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable? {
    this ?: return null
    return ContextCompat.getDrawable(this, drawableRes)
}

/**
 *
 */
@ColorInt
fun View?.getColorInt(@ColorRes colorRes: Int): Int {
    this ?: return Color.BLACK
    return ContextCompat.getColor(this.context, colorRes)
}

/**
 *
 */
@ColorInt
fun Fragment?.getColorInt(@ColorRes colorRes: Int): Int {
    this ?: return Color.BLACK
    context ?: return Color.BLACK
    return ContextCompat.getColor(requireContext(), colorRes)
}

/**
 * 设置View 背景色 染色
 */
fun View?.setBackgroundTint(@ColorRes colorRes: Int) {
    this ?: return
    ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

/**
 * 设置图片 染色
 */
fun AppCompatImageView?.setImageTint(@ColorRes colorRes: Int) {
    this ?: return
    if (colorRes == -1) {
        ImageViewCompat.setImageTintList(this, null)
    } else {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
    }
}

/**
 * 设置图片 宽高
 * @param  width 宽 dp
 * @param height 高  dp
 */
fun AppCompatImageView?.setImageWidthHeight(width: Float, height: Float) {
    this ?: return
    this.layoutParams = this.layoutParams.apply {
        this.width = context.dpToPx(width)
        this.height = context.dpToPx(height)
    }
}

/**
 * 设置图片 和 染色
 */
fun AppCompatImageView?.setImageResourceAndTint(@DrawableRes resId: Int, @ColorRes colorRes: Int) {
    this ?: return
    this.setImageResource(resId)
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

/**
 * 设置图片 染色
 */
fun AppCompatImageView?.setImageTintColor(@ColorInt color: Int) {
    this ?: return
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

/**
 * 设置图片 染色
 */
fun AppCompatImageView?.setImageTintColorRes(@ColorRes colorRes: Int) {
    this ?: return
    if(colorRes == -1){
        return
    }
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

/**
 * 设置 text  资源Color
 */
fun TextView?.setTextColorRes(@ColorRes colorRes: Int) {
    this ?: return
    setTextColor(ContextCompat.getColor(context, colorRes))
}


/**
 * 设置 text  大小
 */
fun <T : TextView> T?.setTextSizeDip(dpSize: Float) {
    this ?: return
    setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpSize)
}

/**
 * 设置 text  资源Color
 */
fun TextView?.setTextStyle(isBold: Boolean) {
    this ?: return
    paint.isFakeBoldText = isBold
    if (isBold) {
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    } else {
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }
}


/**
 *将View从父控件中移除
 */
fun View?.removeViewFormParent() {
    this ?: return
    val parent: ViewParent? = parent
    parent ?: return
    if (parent is FrameLayout) {
        parent.removeView(this)
    }
}

/**
 *将ViewGroup 的 子view清空
 */
fun ViewGroup?.removeChildAllView() {
    this ?: return
    this.removeAllViews()
}

fun View?.setOnNoDoubleClick(listener: View.OnClickListener) {
    this.setOnNoDoubleClick(800, listener)
}

fun View?.setOnNoDoubleClick(time: Int = 800, listener: View.OnClickListener) {
    this ?: return
    var oldTime = System.currentTimeMillis()
    this.setOnClickListener {
        if (System.currentTimeMillis() > oldTime + time) {
            oldTime = System.currentTimeMillis()
            listener.onClick(this)
        }
    }
}

fun <T : View> T.onVisibility(visibility: Int): T {
    this.visibility = visibility
    return this
}

/**
 * 直接设置 view 是否隐藏
 */
fun <T : View> T.onVisibility(isVisibility: Boolean): T {
    if (isVisibility) {
        if (this.visibility != View.VISIBLE) {
            this.visibility = View.VISIBLE
        }
    } else {
        if (this.visibility != View.GONE) {
            this.visibility = View.GONE
        }
    }
    return this
}

/**
 * 直接设置 view 是否隐藏
 * 但是位置还是在
 */
fun <T : View> T.onInVisibility(isVisibility: Boolean): T {
    if (isVisibility) {
        if (this.visibility != View.VISIBLE) {
            this.visibility = View.VISIBLE
        }
    } else {
        if (this.visibility != View.INVISIBLE) {
            this.visibility = View.INVISIBLE
        }
    }
    return this
}


fun <T : View> T.isVisibility(): Boolean = this.visibility == View.VISIBLE


fun TextView.setTextKtx(txt: CharSequence?): TextView {
    text = txt ?: ""
    return this
}


/**
 * @param width view width
 * @param height view height
 */
fun <T : View> T.newLinearParams(
    width: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F,
    height: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F
): LinearLayout.LayoutParams {
    return LinearLayout.LayoutParams(
        if (width < 0) {
            width.toInt()
        } else {
            context.dpToPx(width)
        },
        if (height < 0) {
            height.toInt()
        } else {
            context.dpToPx(height)
        }

    )
}


/**
 * @param width view width
 * @param height view height
 * @param layoutClass layoutParams 类型
 */
fun <T : View> T.updateViewWH(
    layoutClass: Class<out ViewGroup.LayoutParams> = ViewGroup.LayoutParams::class.java,
    width: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F,
    height: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F
): T {
    return updateViewWH(width, height, layoutClass)
}

/**
 * @param width view width
 * @param height view height
 * @param layoutClass layoutParams 类型
 */
fun <T : View> T.updateViewWH(
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    layoutClass: Class<out ViewGroup.LayoutParams> = ViewGroup.LayoutParams::class.java
): T {
    return updateViewWH(width * 1f, height * 1f, layoutClass)
}

/**
 * @param width view width
 * @param height view height
 * @param layoutClass layoutParams 类型
 */
fun <T : View> T.updateViewWH(
    width: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F,
    height: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F,
    layoutClass: Class<out ViewGroup.LayoutParams> = ViewGroup.LayoutParams::class.java
): T {
    //如果LayoutParams 为null，则view 是new出来的，需要先增加对应的 layoutParams
    if (this.layoutParams == null) {
        val groupParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.layoutParams = when (layoutClass) {
            LinearLayout.LayoutParams::class.java -> {
                LinearLayout.LayoutParams(groupParams)
            }
            FrameLayout.LayoutParams::class.java -> {
                FrameLayout.LayoutParams(groupParams)
            }
            RelativeLayout.LayoutParams::class.java -> {
                RelativeLayout.LayoutParams(groupParams)
            }
            ConstraintLayout.LayoutParams::class.java -> {
                ConstraintLayout.LayoutParams(groupParams)
            }
            else -> {
                groupParams
            }
        }
    }
    this.layoutParams = this.layoutParams.also { params ->
        params.width = if (width < 0) {
            width.toInt()
        } else {
            context.dpToPx(width)
        }
        params.height = if (height < 0) {
            height.toInt()
        } else {
            context.dpToPx(height)
        }
    }
    return this
}


/**
 * @param width view width
 * @param height view height
 * @param layoutParams layoutParams 类型
 */
fun <T : View> T.updateViewWH(
    width: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F,
    height: Float = ViewGroup.LayoutParams.WRAP_CONTENT * 1F,
    layoutParams: ViewGroup.LayoutParams
): T {
    //如果LayoutParams 为null，则view 是new出来的，需要先增加对应的 layoutParams
    if (this.layoutParams == null) {
        this.layoutParams = layoutParams
        this.layoutParams = this.layoutParams.also { params ->
            params.width = if (width < 0) {
                width.toInt()
            } else {
                context.dpToPx(width)
            }
            params.height = if (height < 0) {
                height.toInt()
            } else {
                context.dpToPx(height)
            }
        }
    }
    return this
}

inline fun View?.updatePaddingKtx(
    start: Float = -1f,
    top: Float = -1f,
    end: Float = -1f,
    bottom: Float = -1f
) {
    this ?: return
    val startF = if (start == -1f) paddingStart else context.dpToPx(start)
    val topF = if (top == -1f) paddingTop else context.dpToPx(top)
    val endF = if (end == -1f) paddingEnd else context.dpToPx(end)
    val bottomF = if (bottom == -1f) paddingBottom else context.dpToPx(bottom)

    setPaddingRelative(startF, topF, endF, bottomF)
}

inline fun View?.updatePaddingKtxToPx(
    start: Float = -1f,
    top: Float = -1f,
    end: Float = -1f,
    bottom: Float = -1f
) {
    this ?: return
    val startF = if (start == -1f) paddingStart else start
    val topF = if (top == -1f) paddingTop else top
    val endF = if (end == -1f) paddingEnd else end
    val bottomF = if (bottom == -1f) paddingBottom else bottom

    setPaddingRelative(startF.toInt(), topF.toInt(), endF.toInt(), bottomF.toInt())
}

inline fun <T : ViewGroup.LayoutParams> View?.updateMarginKtx(
    start: Float = -1f,
    top: Float = -1f,
    end: Float = -1f,
    bottom: Float = -1f,
    defaultParams: T? = null
) {
    this ?: return
    var params = layoutParams

    if (params == null && defaultParams != null) {
        params = defaultParams
    }
    (params as? ViewGroup.MarginLayoutParams)?.let { param ->
        val startF = if (start == -1f) paddingStart else context.dpToPx(start)
        val topF = if (top == -1f) paddingTop else context.dpToPx(top)
        val endF = if (end == -1f) paddingEnd else context.dpToPx(end)
        val bottomF = if (bottom == -1f) paddingBottom else context.dpToPx(bottom)

        param.setMargins(startF, topF, endF, bottomF)
        param.marginStart = startF
        param.marginEnd = endF
        layoutParams = param
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.getLayoutParamsOrNew(): T {
    if (layoutParams == null) {
        val groupParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.layoutParams = when (T::class.java) {
            LinearLayout.LayoutParams::class.java -> {
                LinearLayout.LayoutParams(groupParams)
            }
            FrameLayout.LayoutParams::class.java -> {
                FrameLayout.LayoutParams(groupParams)
            }
            RelativeLayout.LayoutParams::class.java -> {
                RelativeLayout.LayoutParams(groupParams)
            }
            ConstraintLayout.LayoutParams::class.java -> {
                ConstraintLayout.LayoutParams(groupParams)
            }
            ViewGroup.MarginLayoutParams::class.java -> {
                ViewGroup.MarginLayoutParams(groupParams)
            }
            else -> {
                groupParams
            }
        }
    }
    return layoutParams as T
}

fun <T : View> T.setHeight(height: Int): T {
    if (layoutParams != null) {
        val params = layoutParams
        params.height = this.context.dpToPx(height.toFloat())
        layoutParams = params
    }
    return this
}

/**
 * 设置/取消分割线
 */
fun TextView.setStrikeThru(show: Boolean = true): TextView {
    if (show) {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
    return this
}

/**
 * 初始化webview 设置
 */
fun WebView.initSetting() {
    settings.apply {
        loadWithOverviewMode = true
        blockNetworkImage = false
        domStorageEnabled = true
        allowFileAccess = true
        javaScriptEnabled = true
        //   mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        useWideViewPort = true
        loadsImagesAutomatically = true
        setSupportZoom(true)

        //解除阻塞
        blockNetworkImage = false
        //判断webview是否加载了，图片资源
        if (!loadsImagesAutomatically) {
            //设置wenView加载图片资源
            loadsImagesAutomatically = true
        }
    }


    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                binding.progressBar.progress = newProgress
//                if (newProgress == 100) {
//                    binding.progressBar.onVisibility(false)
//                } else {
//                    binding.progressBar.onVisibility(true)
//                }
            super.onProgressChanged(view, newProgress)
        }
    }
    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageFinished(p0: WebView?, p1: String?) {

        }
    }
}

