package com.example.library.multiple

import android.view.ViewGroup

import com.example.library.BaseViewHolder

/**
 * TYPE_BASE的Delegate
 * Created by 陈健宇 at 2019/7/10
 */
class BaseDelegate<T> : IMultiItemDelegate<T> {

    override fun isForViewType(items: T, position: Int) = false

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder? = null

    override fun onBindView(holder: BaseViewHolder, items: T, position: Int) {}
}
