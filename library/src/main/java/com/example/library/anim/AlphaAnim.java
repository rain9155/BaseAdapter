package com.example.library.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

import com.example.library.config.Constant;

/**
 * 渐变动画
 * Created by 陈健宇 at 2019/7/10
 */
public class AlphaAnim implements IAnim {

    @Override
    public void applyAnimation(View itemView) {
        Animator alphaAnimation = ObjectAnimator.ofFloat(itemView, "alpha", 0.5f, 1f);
        alphaAnimation.setInterpolator(new LinearOutSlowInInterpolator());
        alphaAnimation.setDuration(Constant.ITEM_ANIM_TIME);
        alphaAnimation.start();
    }

}
