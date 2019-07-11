package com.example.library.multiple;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.library.BaseViewHolder;

import static com.example.library.BaseAdapter.TYPE_BASE;

/**
 * 管理所有的item的AdapterDelegate的Manager
 * Created by 陈健宇 at 2019/5/30
 */

public final class MultiItemDelegateManager<T>{

    private SparseArray<IMultiItemDelegate<T>> mDelegates = new SparseArray<>();

    /**
     * 添加一个item的delegate, 默认以delegates的size作为viewType
     * @param delegate item的delegate
     */
    public MultiItemDelegateManager<T> addDelegte(@NonNull IMultiItemDelegate<T> delegate){
        return addDelegte(mDelegates.size(), delegate);
    }

    /**
     * 根据给定的viewType，添加一个item的delegate
     * @param viewType 指定的viewType
     * @param delegate item的delegate
     */
    public MultiItemDelegateManager<T> addDelegte(int viewType, @NonNull IMultiItemDelegate<T> delegate){
        if(delegate == null) throw new NullPointerException("IMultiItemDelegate can't not be null");
        if(mDelegates.get(viewType) != null) throw new IllegalArgumentException("An IMultiItemDelegate is already added for the viewType =" + viewType);
        mDelegates.put(viewType, delegate);
        return this;
    }

    /**
     * 根据给定的viewType，获取一个delegate
     * @param viewType 指定的viewType
     * @return item的delegate
     */
    public IMultiItemDelegate<T> getDelegte(int viewType){
        IMultiItemDelegate<T> delegate = mDelegates.get(viewType);
        if(delegate == null) throw new NullPointerException("No IMultiItemDelegate added that matches viewType = " + viewType + " in delegates");
        return delegate;
    }

    /**
     * 根据给定的delegate，从Manager中移除它
     * @param delegate 给定的delegate
     */
    public MultiItemDelegateManager<T> removeDelegate(@NonNull IMultiItemDelegate<T> delegate) {
        if (delegate == null) throw new NullPointerException("IMultiItemDelegate is null");
        int indexToRemove = mDelegates.indexOfValue(delegate);
        if (indexToRemove >= 0) {
            mDelegates.removeAt(indexToRemove);
        }
        return this;

    }

    /**
     * 根据给定的viewType，从Manager中移除viewType对应的elegate
     * @param viewType 给定的viewType
     */
    public MultiItemDelegateManager<T> removeDelegate(int viewType) {
        mDelegates.remove(viewType);
        return this;
    }

    /**
     * 根据给定的item得到对应的viewType
     * @param item position对应的item
     * @param position item在数据源中的索引
     * @return item对应的viewType
     */
    public int getItemViewType(@NonNull T item, int position) {
        if (item == null) throw new NullPointerException("Item is null!");
        int delegatesCount = mDelegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            IMultiItemDelegate<T> delegate = mDelegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return mDelegates.keyAt(i);
            }
        }
         return TYPE_BASE;
    }


    /**
     * 获得设置delegate的数量
     */
    public int getItemDelegateCount(){
        return mDelegates.size();
    }

    @NonNull
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IMultiItemDelegate<T> delegate = mDelegates.get(viewType);
        if(delegate == null) throw new NullPointerException("No IMultiItemDelegate added that matches viewType = "+ viewType + " in delegates");
        BaseViewHolder holder = delegate.onCreateViewHolder(parent);
        if(holder == null) throw new NullPointerException("IMultiItemDelegate' viewHolder can't be null!");
        return holder;
    }

    public void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull T items, int position) {
        IMultiItemDelegate<T> delegate = mDelegates.get(holder.getItemViewType());
        delegate.onBindView(holder, items, position);
    }

}
