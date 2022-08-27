package com.tzh.myapplication.view.load

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.util.SmartUtil
import com.tzh.myapplication.R
import com.tzh.myapplication.utils.loadGif
import pl.droidsonroids.gif.GifDrawable

/**
 * smartlayout 自定义刷新
 */
@SuppressLint("RestrictedApi")
class AppRefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), RefreshHeader {

    private var mStateTv: TextView? = null
    private var mStateGif: ImageView? = null


    init {
        View.inflate(context, R.layout.layout_smart_refresh_header, this)
        mStateTv = findViewById(R.id.refresh_status_tv)
        mStateGif = findViewById(R.id.refresh_gif)
        gravity = Gravity.CENTER
        minimumHeight = SmartUtil.dp2px(80f)
        setBackgroundResource(R.color.color_f4f4f4)
    }


    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
//        Log.e("onStateChanged-refresh",newState.toString())
        when (newState) {
            RefreshState.PullDownToRefresh -> {
                mStateTv?.text = "下拉刷新"
                mStateGif?.setWillNotDraw(false)
                mStateGif?.loadGif(R.drawable.refresh_loading)
            }
            RefreshState.None -> {
                mStateTv?.text = "下拉刷新"
                mStateGif?.setWillNotDraw(true)
                (mStateGif?.drawable as? GifDrawable)?.recycle()
                mStateGif?.setImageDrawable(null)
            }
            RefreshState.Refreshing -> {
                mStateTv?.text = "正在刷新..."
//                mStateGif?.setImageResource(R.mipmap.loading_green)
            }
            RefreshState.ReleaseToRefresh -> {
                mStateTv?.text = "释放立即刷新"
//                mStateGif?.setImageResource(R.mipmap.loading_green)
            }
        }
    }

    /**
     * 开始动画（开始刷新或者开始加载动画）
     * @param layout RefreshLayout
     * @param height HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    /**
     * 动画结束
     * @param layout RefreshLayout
     * @param success 数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        if (success) {
            mStateTv?.text = "刷新完成";
        } else {
            mStateTv?.text = "刷新失败";
        }
        return 200
    }

    /**
     * 获取真实视图（必须返回，不能为null）
     */
    override fun getView(): View {
        return this
    }

    /**
     * 获取变换方式（必须指定一个：平移、拉伸、固定、全屏）
     */
    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    /**
     * 设置主题颜色 （如果自定义的Header没有注意颜色，本方法可以什么都不处理）
     * @param colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
     */
    override fun setPrimaryColors(vararg colors: Int) {

    }

    /**
     * 尺寸定义初始化完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     * @param kernel RefreshKernel 核心接口（用于完成高级Header功能）
     * @param height HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        kernel.requestDrawBackgroundFor(this, ContextCompat.getColor(context, R.color.color_f4f4f4))
    }

    /**
     * 手指拖动下拉（会连续多次调用，用于实时控制动画关键帧）
     * @param percent 下拉的百分比 值 = offset/headerHeight (0 - percent - (headerHeight+maxDragHeight) / headerHeight )
     * @param offset 下拉的像素偏移量  0 - offset - (headerHeight+maxDragHeight)
     * @param headerHeight Header的高度
     * @param maxDragHeight 最大拖动高度
     */
    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {

    }

    /**
     * 手指释放之后的持续动画（会连续多次调用，用于实时控制动画关键帧）
     * @param percent 下拉的百分比 值 = offset/headerHeight (0 - percent - (headerHeight+maxDragHeight) / headerHeight )
     * @param offset 下拉的像素偏移量  0 - offset - (headerHeight+maxDragHeight)
     * @param headerHeight Header的高度
     * @param maxDragHeight 最大拖动高度
     */
    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        // onStartAnimator(refreshLayout, height, maxDragHeight)
    }


    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }
}