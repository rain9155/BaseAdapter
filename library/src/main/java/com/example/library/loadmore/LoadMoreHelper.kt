package com.example.library.loadmore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 管理LoadMoreView的状态
 * Created by 陈健宇 at 2019/10/16
 */
class LoadMoreHelper(val parent: ViewGroup){

    companion object {
        const val STATUS_LOADING = 0x000
        const val STATUS_LOADING_END = 0x001
        const val STATUS_LOADING_FAIL = 0x002
        const val STATUS_LOADING_COMPLETE = 0x003
    }

    private var loadingView:View
    private var loadingEndView: View
    private var loadingFailView: View

    var loadMoreView: View
    var loadMoreViewProvide: ILoadMoreViewProvide = DefaultLoadMoreViewProvide()
    var curStatus = STATUS_LOADING_COMPLETE

    init {
        loadMoreView = LayoutInflater.from(parent.context).inflate(loadMoreViewProvide.getLayoutId(), parent, false)
        loadingView = loadMoreView.findViewById(loadMoreViewProvide.getLoadingViewId())
        loadingEndView = loadMoreView.findViewById(loadMoreViewProvide.getLoadingEndViewId())
        loadingFailView = loadMoreView.findViewById(loadMoreViewProvide.getLoadingFailViewId())
    }

    fun show(){
        when(curStatus){
            STATUS_LOADING ->{
                loadingView.visibility = View.VISIBLE
                loadingEndView.visibility = View.INVISIBLE
                loadingFailView.visibility = View.INVISIBLE
            }
            STATUS_LOADING_END ->{
                loadingEndView.visibility = View.VISIBLE
                loadingView.visibility = View.INVISIBLE
                loadingFailView.visibility = View.INVISIBLE
            }
            STATUS_LOADING_FAIL ->{
                loadingFailView.visibility = View.VISIBLE
                loadingEndView.visibility = View.INVISIBLE
                loadingView.visibility = View.INVISIBLE
            }
            else -> {
                loadingFailView.visibility = View.GONE
                loadingEndView.visibility = View.GONE
                loadingView.visibility = View.GONE
            }
        }
    }

    fun updateLoadMoreView(parent: ViewGroup){
        loadMoreView = LayoutInflater.from(parent.context).inflate(loadMoreViewProvide.getLayoutId(), parent, false)
        loadingView = loadMoreView.findViewById(loadMoreViewProvide.getLoadingViewId())
        loadingEndView = loadMoreView.findViewById(loadMoreViewProvide.getLoadingEndViewId())
        loadingFailView = loadMoreView.findViewById(loadMoreViewProvide.getLoadingFailViewId())
    }

    fun updateLoadMoreProvide(provide: ILoadMoreViewProvide){
        loadMoreViewProvide = provide
    }

}
