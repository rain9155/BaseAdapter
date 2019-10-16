package com.example.library.loadmore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loading.Loading
import com.example.loading.StatusView

/**
 * 管理LoadMoreView的状态
 * Created by 陈健宇 at 2019/10/16
 */
class LoadMoreView(val parent: ViewGroup){

    private var loadMoreView: View
    private var loadingView:View
    private var loadingEndView: View
    private var loadingFailView: View
    private var statusView: StatusView

    var loadMoreHelper = DefaultLoadMoreHelper()

    init {
        loadMoreView = LayoutInflater.from(parent.context).inflate(loadMoreHelper.getLayoutId(), parent, false)

        loadingView = loadMoreView.findViewById(loadMoreHelper.getLoadingViewId())
        loadingEndView = loadMoreView.findViewById(loadMoreHelper.getLoadingEndViewId())
        loadingFailView = loadMoreView.findViewById(loadMoreHelper.getLoadingFailViewId())

        statusView = Loading.beginBuildStatusView(parent.context)
                .warpView(loadMoreView)
                .addLoadingView(loadMoreHelper.getLoadingViewId())
                .addEmptyView(loadMoreHelper.getLoadingFailViewId())
                .addErrorView(loadMoreHelper.getLoadingFailViewId())
                .create()
    }

    fun getLoadMoreView() = statusView.wrappedView

    fun updateLoadMoreView(helper: LoadMoreView){

    }

}
