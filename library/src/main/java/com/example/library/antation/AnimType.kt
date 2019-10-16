package com.example.library.antation

import android.support.annotation.IntDef
import com.example.library.BaseAdapter.Companion.ANIM_ALPHA
import com.example.library.BaseAdapter.Companion.ANIM_SCALE
import com.example.library.BaseAdapter.Companion.ANIM_SLIDE_FROM_LEFT
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * 内置动画类型
 * Created by 陈健宇 at 2019/7/11
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(ANIM_ALPHA, ANIM_SCALE, ANIM_SLIDE_FROM_LEFT)
annotation class AnimType
