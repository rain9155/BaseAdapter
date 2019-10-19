package com.example.library.loadmore

import com.example.library.R

/**
 * ILoadMoreHelper的默认实现
 * Created by 陈健宇 at 2019/10/16
 */
class DefaultLoadMoreViewProvide : ILoadMoreViewProvide{

    override fun getLoadMoreLayoutId(): Int = R.layout.baseadapter_load_more_layout

    override fun getLoadingViewId(): Int = R.id.baseadapter_loading

    override fun getLoadingEndViewId(): Int = R.id.baseadapter_loading_end

    override fun getLoadingFailViewId(): Int = R.id.baseapadter_loading_fail

}