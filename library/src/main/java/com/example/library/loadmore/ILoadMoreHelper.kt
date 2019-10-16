package com.example.library.loadmore

/**
 * 加载更多布局接口
 * Created by 陈健宇 at 2019/10/16
 */
interface ILoadMoreHelper{

    fun getLayoutId(): Int
    fun getLoadingViewId(): Int
    fun getLoadingEndViewId(): Int
    fun getLoadingFailViewId(): Int

}
