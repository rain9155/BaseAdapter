package com.example.baseadapter.multiItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baseadapter.R;
import com.example.library.BaseViewHolder;
import com.example.library.multiple.IMultiItemDelegate;

/**
 * Created by 陈健宇 at 2019/7/10
 */
public class MutipleItem2 implements IMultiItemDelegate<String> {

    @Override
    public boolean isForViewType(String items, int position) {
        return position == 5 || position == 30;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi2, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindView(BaseViewHolder holder, String items, int position) {
        holder.setText(R.id.tv_text, items);
    }
}
