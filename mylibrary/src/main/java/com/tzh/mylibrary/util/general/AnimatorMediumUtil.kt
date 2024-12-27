package com.tzh.mylibrary.util.general

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import com.tzh.mylibrary.R
import com.tzh.mylibrary.type.FontType
import com.tzh.mylibrary.util.DpToUtil
import java.util.Random


object AnimatorMediumUtil {
    var objectAnimator : ObjectAnimator ?= null
    /**
     * View的旋转动画
     * @param duration 转一圈的时长
     * @param isForever 是否一直转
     */
    fun rotating(view : View,duration : Long,isForever : Boolean){
        objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        objectAnimator?.duration = duration
        if(isForever){
            objectAnimator?.repeatCount = ObjectAnimator.INFINITE
        }else{
            objectAnimator?.repeatCount = 1
        }
        objectAnimator?.interpolator = LinearInterpolator()//让旋转动画一直转，不停顿的重点（实际上是添加动画插值器）
        objectAnimator?.repeatMode = ObjectAnimator.RESTART//匀速
        objectAnimator?.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        objectAnimator?.start()
    }


    /**
     * 敲击动画
     */
    fun stick(view : View,view2 : View,layout : RelativeLayout){
        val rotateAnimation1 = RotateAnimation(0f, -40f, view2.width.toFloat(),view2.height.toFloat())
        rotateAnimation1.duration = 100
        view2.startAnimation(rotateAnimation1)
        rotateAnimation1.setAnimationListener(object  : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                val anim = AnimatorInflater.loadAnimator(view.context, R.animator.stick_animation_2) as AnimatorSet
                anim.setTarget(view)
                anim.start()
                randomAnimation(view,layout)
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })
    }


    /**
     * 取消动画
     */
    fun remove(){
        objectAnimator?.removeAllListeners()
        objectAnimator?.cancel()
        objectAnimator?.reverse()
        objectAnimator = null
    }

    private fun randomAnimation(view : View,layout : RelativeLayout) {
        val random = Random() //随机数函数
        val state = random.nextInt(10)
        when (state) {
            0 -> PlayAnimation(0f,view,layout)
            1 -> PlayAnimation(50f,view,layout)
            2 -> PlayAnimation(100f,view,layout)
            3 -> PlayAnimation(200f,view,layout)
            4 -> PlayAnimation(600f,view,layout)
            5 -> PlayAnimation(650f,view,layout)
            6 -> PlayAnimation(400f,view,layout)
            7 -> PlayAnimation(450f,view,layout)
            8 -> PlayAnimation(550f,view,layout)
            9 -> PlayAnimation(700f,view,layout)
            10 -> PlayAnimation(880f,view,layout)
        }
    }

    /**
     * 根据对应的很坐标执行动画
     *
     * @param state
     */
    private fun PlayAnimation(state: Float,view : View,layout : RelativeLayout) {
        val textView = TextView(layout.context)
        // 设置text2在父容器中的属性
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 让text2在父容器中靠右对齐
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        layout.addView(textView) //动态添加view
        setTextTitle(textView)
        textView.textSize = 34f
        val index = Random().nextInt(2)
        if(index == 1){
            textView.setTextColor(Color.parseColor("#EC8812"))
        }else{
            textView.setTextColor(Color.parseColor("#EC8812"))
        }
//        FontUtil.loadFont(textView, FontType.SJXZ)
        val animSet = AnimatorSet()
        val anim1 = ObjectAnimator.ofFloat(textView, "translationX", view.x + view.width / 2 - DpToUtil.dip2px(view.context,textView.text.length.toFloat()*8), state) //移动到屏幕中间
        val anim2 = ObjectAnimator.ofFloat(textView, "translationY", view.y, 0f) //从木鱼移动到上端
        val anim3 = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f) //透明度变化
        val builder = animSet.play(anim2)
        builder.with(anim1).with(anim2).with(anim3)
        animSet.duration = 1200 //持续时间
        animSet.start() // 开始播放属性动画

    }

    private fun setTextTitle(textView: TextView){
        val list = mutableListOf<String>().apply {
            add("家和")
            add("富贵")
            add("健康")
            add("幸福")
            add("平安")
            add("如意")
            add("吉祥")
            add("安康")
            add("发财")
        }
        val index = Random().nextInt(list.size)
        textView.text = list[index]
    }
}