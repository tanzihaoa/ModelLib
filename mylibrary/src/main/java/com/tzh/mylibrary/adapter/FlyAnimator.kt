package com.tzh.mylibrary.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator


/**
 * @author : Dumplings
 * @version : v1.0.0
 * @ClassName : FlyAnimator.java
 * @Function ： 移除飞行效果动画
 * @Description ：
 * @Idea :
 * []
 * @Encourage ：And the more we try to understand one another, the more exceptional each of us will be.
 * 我们越是努力理解他人，自身也越发变得优秀。
 * @date : 2021/6/3
 */
class FlyAnimator : SimpleItemAnimator() {
    var removeHolders: MutableList<RecyclerView.ViewHolder> = ArrayList()
    var removeAnimators: MutableList<RecyclerView.ViewHolder> = ArrayList()
    var moveHolders: MutableList<RecyclerView.ViewHolder> = ArrayList()
    var moveAnimators: MutableList<RecyclerView.ViewHolder> = ArrayList()
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        removeHolders.add(holder)
        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        holder.itemView.translationY = (fromY - toY).toFloat()
        moveHolders.add(holder)
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        return false
    }

    override fun runPendingAnimations() {
        if (!removeHolders.isEmpty()) {
            for (holder in removeHolders) {
                remove(holder)
            }
            removeHolders.clear()
        }
        if (!moveHolders.isEmpty()) {
            for (holder in moveHolders) {
                move(holder)
            }
            moveHolders.clear()
        }
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {}
    override fun endAnimations() {}
    override fun isRunning(): Boolean {
        return !(removeHolders.isEmpty() && removeAnimators.isEmpty() && moveHolders.isEmpty() && moveAnimators.isEmpty())
    }

    private fun remove(holder: RecyclerView.ViewHolder) {
        removeAnimators.add(holder)
        val animation = TranslateAnimation(0f, 1000f, 0f, 0f)
        animation.duration = 500
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                dispatchRemoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animation) {
                removeAnimators.remove(holder)
                dispatchRemoveFinished(holder)
                if (!isRunning) {
                    dispatchAnimationsFinished()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        holder.itemView.startAnimation(animation)
    }

    private fun move(holder: RecyclerView.ViewHolder) {
        moveAnimators.add(holder)
        val animator = ObjectAnimator.ofFloat(
            holder.itemView,
            "translationY", holder.itemView.translationY, 0f
        )
        animator.duration = 500
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                dispatchMoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchMoveFinished(holder)
                moveAnimators.remove(holder)
                if (!isRunning) {
                    dispatchAnimationsFinished()
                }
            }
        })
        animator.start()
    }
}

