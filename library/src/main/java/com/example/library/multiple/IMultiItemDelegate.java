package com.example.library.multiple;

import android.view.ViewGroup;

import com.example.library.BaseViewHolder;

/**
 * 每个item的Delegate
 * Created by 陈健宇 at 2019/5/30
 */
public interface IMultiItemDelegate<T>{


    /**
     * 判断给定的item是否要使用position位置的ViewType
     * @param items position对应的item
     * @param position item在数据源中的索引
     * @return true表示item要使用该position位置处的ViewType
     */
    boolean isForViewType(T items, int position);

    BaseViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindView(BaseViewHolder holder, T items, int position);
}
