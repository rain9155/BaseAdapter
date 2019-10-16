package com.example.library.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View

import com.example.library.config.Constant

/**
 * 缩放动画
 * Created by 陈健宇 at 2019/7/10
 */
class ScaleAnim : IAnim {
    override fun applyAnimation(itemView: View) {
        val scaleAnimationX = ObjectAnimator.ofFloat(itemView, "scaleX", 0.5f, 1f)
        val scaleAnimationY = ObjectAnimator.ofFloat(itemView, "scaleY", 0.5f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.run {
            duration = Constant.ITEM_ANIM_TIME.toLong()
            interpolator = LinearOutSlowInInterpolator()
            playTogether(scaleAnimationX, scaleAnimationY)
            start()
        }
    }
}
