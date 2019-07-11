package com.example.library.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

import com.example.library.config.Constant;

/**
 * 从左边滑进来的动画
 * Created by 陈健宇 at 2019/7/10
 */
public class SlideFormLeftAnim implements IAnim {

    @Override
    public void applyAnimation(View itemView) {
        Animator translationFromLeft = ObjectAnimator.ofFloat(itemView, "translationX", -itemView.getRootView().getWidth(), 0);
        translationFromLeft.setInterpolator(new LinearOutSlowInInterpolator());
        translationFromLeft.setDuration(Constant.ITEM_ANIM_TIME);
        translationFromLeft.start();
    }

}
