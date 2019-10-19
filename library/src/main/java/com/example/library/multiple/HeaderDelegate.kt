package com.example.library.multiple

import android.view.ViewGroup
import com.example.library.BaseViewHolder

/**
 * TYPE_HEADER_VIEW的Delegate
 * Created by 陈健宇 at 2019/10/16
 */
class HeaderDelegate<T> : IMultiItemDelegate<T>{

    override fun isForViewType(items: T, position: Int): Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder? = null

    override fun onBindView(holder: BaseViewHolder, items: T, position: Int) {}

}