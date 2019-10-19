package com.example.library.multiple

import android.util.SparseArray
import android.view.ViewGroup

import com.example.library.BaseViewHolder

import com.example.library.BaseAdapter.Companion.TYPE_BASE

/**
 * 管理所有的item的AdapterDelegate的Manager
 * Created by 陈健宇 at 2019/5/30
 */

class MultiItemDelegateManager<T> {

    private val mDelegates = SparseArray<IMultiItemDelegate<T>>()

    /**
     * 获得设置delegate的数量
     */
    val itemDelegateCount: Int
        get() = mDelegates.size()

    /**
     * 添加一个item的delegate, 默认以delegates的size作为viewType
     * @param delegate item的delegate
     */
    fun addDelegate(delegate: IMultiItemDelegate<T>): MultiItemDelegateManager<T> {
        return addDelegate(mDelegates.size(), delegate)
    }

    /**
     * 根据给定的viewType，添加一个item的delegate
     * @param viewType 指定的viewType
     * @param delegate item的delegate
     */
    fun addDelegate(viewType: Int, delegate: IMultiItemDelegate<T>): MultiItemDelegateManager<T> {
        if (delegate == null) throw NullPointerException("IMultiItemDelegate can't not be null")
        if (mDelegates.get(viewType) != null) throw IllegalArgumentException("An IMultiItemDelegate is already added for the viewType =$viewType")
        mDelegates.put(viewType, delegate)
        return this
    }

    /**
     * 根据给定的viewType，获取一个delegate
     * @param viewType 指定的viewType
     * @return item的delegate
     */
    fun getDelegte(viewType: Int): IMultiItemDelegate<T> {
        return mDelegates.get(viewType)
                ?: throw NullPointerException("No IMultiItemDelegate added that matches viewType = $viewType in delegates")
    }

    /**
     * 根据给定的delegate，从Manager中移除它
     * @param delegate 给定的delegate
     */
    fun removeDelegate(delegate: IMultiItemDelegate<T>): MultiItemDelegateManager<T> {
        if (delegate == null) throw NullPointerException("IMultiItemDelegate is null")
        val indexToRemove = mDelegates.indexOfValue(delegate)
        if (indexToRemove >= 0) {
            mDelegates.removeAt(indexToRemove)
        }
        return this

    }

    /**
     * 根据给定的viewType，从Manager中移除viewType对应的elegate
     * @param viewType 给定的viewType
     */
    fun removeDelegate(viewType: Int): MultiItemDelegateManager<T> {
        mDelegates.remove(viewType)
        return this
    }

    /**
     * 根据给定的item得到对应的viewType
     * @param item position对应的item
     * @param position item在数据源中的索引
     * @return item对应的viewType
     */
    fun getItemViewType(item: T, position: Int): Int {
        if (item == null) throw NullPointerException("Item is null!")
        val delegatesCount = mDelegates.size()
        for (i in 0 until delegatesCount) {
            val delegate = mDelegates.valueAt(i)
            if (delegate.isForViewType(item, position)) {
                return mDelegates.keyAt(i)
            }
        }
        return TYPE_BASE
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val delegate = mDelegates.get(viewType)
                ?: throw NullPointerException("No IMultiItemDelegate added that matches viewType = $viewType in delegates")
        return delegate.onCreateViewHolder(parent)
                ?: throw NullPointerException("IMultiItemDelegate' viewHolder can't be null!")
    }

    fun onBindViewHolder(holder: BaseViewHolder, items: T, position: Int) {
        val delegate = mDelegates.get(holder.itemViewType)
        delegate.onBindView(holder, items, position)
    }

}
