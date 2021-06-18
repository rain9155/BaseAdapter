package com.example.library.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

import com.example.library.config.Constant

/**
 * 从左边滑进来的动画
 * Created by 陈健宇 at 2019/7/10
 */
class SlideFormLeftAnim : IAnim {

    override fun applyAnimation(itemView: View) {
        val translationFromLeft = ObjectAnimator.ofFloat(itemView, "translationX", -itemView.rootView.width.toFloat(), 0f)
        translationFromLeft.run{
            duration = Constant.ITEM_ANIM_TIME.toLong()
            start()
        }
    }

}
