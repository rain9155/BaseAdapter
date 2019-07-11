package com.example.library.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

import com.example.library.config.Constant;

/**
 * 缩放动画
 * Created by 陈健宇 at 2019/7/10
 */
public class ScaleAnim implements IAnim {
    @Override
    public void applyAnimation(View itemView) {
        Animator scaleAnimationX = ObjectAnimator.ofFloat(itemView, "scaleX", 0.5f, 1f);
        Animator scaleAnimationY = ObjectAnimator.ofFloat(itemView, "scaleY", 0.5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(Constant.ITEM_ANIM_TIME)
                .setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.playTogether(scaleAnimationX, scaleAnimationY);
        animatorSet.start();
    }
}
