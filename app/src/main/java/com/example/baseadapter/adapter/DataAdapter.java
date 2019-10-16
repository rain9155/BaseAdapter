package com.example.baseadapter.adapter;

import com.example.baseadapter.R;
import com.example.baseadapter.multiItem.MultipleItem1;
import com.example.baseadapter.multiItem.MutipleItem2;
import com.example.library.BaseAdapter;
import com.example.library.BaseViewHolder;

/**
 * Created by 陈健宇 at 2019/7/9
 */
public class DataAdapter extends BaseAdapter<String>{

    public DataAdapter(int layoutId) {
        super(layoutId);
        addItemDelegate(new MutipleItem2()).addItemDelegate(new MultipleItem1());
    }

    @Override
    protected void onBindView(BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_text, item);
    }


}
