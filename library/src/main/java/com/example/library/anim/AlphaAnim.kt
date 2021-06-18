package com.example.library.anim

import android.animation.ObjectAnimator
import android.view.View

import com.example.library.config.Constant

/**
 * 渐变动画
 * Created by 陈健宇 at 2019/7/10
 */
class AlphaAnim : IAnim {

    override fun applyAnimation(itemView: View) {
        val alphaAnimation = ObjectAnimator.ofFloat(itemView, "alpha", 0.5f, 1f)
        alphaAnimation.run {
            duration = Constant.ITEM_ANIM_TIME.toLong()
            start()
        }
    }

}
