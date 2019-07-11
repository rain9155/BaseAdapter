package com.example.library.anim;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.library.config.AnimType.*;

/**
 * 内置动画类型
 * Created by 陈健宇 at 2019/7/11
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ALPHA, SCALE, SLIDE_FROM_LEFT})
public @interface Type {}
