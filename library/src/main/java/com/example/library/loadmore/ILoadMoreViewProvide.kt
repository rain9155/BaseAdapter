package com.example.library.loadmore

/**
 * 提供加载更多布局的接口
 * Created by 陈健宇 at 2019/10/16
 */
interface ILoadMoreViewProvide{

    fun getLayoutId(): Int
    fun getLoadingViewId(): Int
    fun getLoadingEndViewId(): Int
    fun getLoadingFailViewId(): Int

}
