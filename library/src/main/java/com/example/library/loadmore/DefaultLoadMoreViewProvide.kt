package com.example.library.loadmore

import com.example.library.R

/**
 * ILoadMoreHelper的默认实现
 * Created by 陈健宇 at 2019/10/16
 */
class DefaultLoadMoreViewProvide : ILoadMoreViewProvide{

    override fun getLayoutId(): Int = R.layout.load_more_layout

    override fun getLoadingViewId(): Int = R.id.loading

    override fun getLoadingEndViewId(): Int = R.id.loading_end

    override fun getLoadingFailViewId(): Int = R.id.loading_fail

}