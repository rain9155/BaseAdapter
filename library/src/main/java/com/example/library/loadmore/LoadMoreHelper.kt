package com.example.library.loadmore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.library.BaseViewHolder

/**
 * 管理LoadMoreView的状态
 * Created by 陈健宇 at 2019/10/16
 */
class LoadMoreHelper{

    companion object {
        const val STATUS_LOADING = 0x000
        const val STATUS_LOADING_END = 0x001
        const val STATUS_LOADING_FAIL = 0x002
        const val STATUS_LOADING_COMPLETE = 0x003
    }

    private var loadMoreViewProvide: ILoadMoreViewProvide
    lateinit var loadMoreView: View
    var curStatus = STATUS_LOADING_COMPLETE

    init {
        loadMoreViewProvide = DefaultLoadMoreViewProvide()
    }

    fun show(holder: BaseViewHolder){
        when(curStatus){
            STATUS_LOADING ->{
                holder.setVisibility(loadMoreViewProvide.getLoadingViewId(), View.VISIBLE)
                holder.setVisibility(loadMoreViewProvide.getLoadingEndViewId(), View.INVISIBLE)
                holder.setVisibility(loadMoreViewProvide.getLoadingFailViewId(), View.INVISIBLE)
            }
            STATUS_LOADING_END ->{
                holder.setVisibility(loadMoreViewProvide.getLoadingEndViewId(), View.VISIBLE)
                holder.setVisibility(loadMoreViewProvide.getLoadingViewId(), View.INVISIBLE)
                holder.setVisibility(loadMoreViewProvide.getLoadingFailViewId(), View.INVISIBLE)
            }
            STATUS_LOADING_FAIL ->{
                holder.setVisibility(loadMoreViewProvide.getLoadingFailViewId(), View.VISIBLE)
                holder.setVisibility(loadMoreViewProvide.getLoadingEndViewId(), View.INVISIBLE)
                holder.setVisibility(loadMoreViewProvide.getLoadingViewId(), View.INVISIBLE)
            }
            else -> {
                holder.setVisibility(loadMoreViewProvide.getLoadingFailViewId(), View.GONE)
                holder.setVisibility(loadMoreViewProvide.getLoadingEndViewId(), View.GONE)
                holder.setVisibility(loadMoreViewProvide.getLoadingViewId(), View.GONE)
            }
        }
    }

    fun updateLoadMoreView(parent: ViewGroup){
        loadMoreView = LayoutInflater.from(parent.context).inflate(loadMoreViewProvide.getLoadMoreLayoutId(), parent, false)
    }

    fun updateLoadMoreProvide(provide: ILoadMoreViewProvide){
        loadMoreViewProvide = provide
    }

}
