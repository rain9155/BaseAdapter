package com.example.library.multiple;

import android.view.ViewGroup;

import com.example.library.BaseViewHolder;

/**
 * Created by 陈健宇 at 2019/7/10
 */
public final class BaseMultiItemDelegate<T> implements IMultiItemDelegate<T> {
    @Override
    public boolean isForViewType(T items, int position) {
        return false ;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindView(BaseViewHolder holder, T items, int position) {
    }
}
