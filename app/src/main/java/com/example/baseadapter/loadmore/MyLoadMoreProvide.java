package com.example.baseadapter.loadmore;

import com.example.baseadapter.R;
import com.example.library.loadmore.ILoadMoreViewProvide;

/**
 * Created by 陈健宇 at 2019/10/18
 */
public class MyLoadMoreProvide implements ILoadMoreViewProvide {

    @Override
    public int getLoadMoreLayoutId() {
        return R.layout.load_more;
    }

    @Override
    public int getLoadingViewId() {
        return R.id.loading;
    }

    @Override
    public int getLoadingEndViewId() {
        return R.id.loading_end;
    }

    @Override
    public int getLoadingFailViewId() {
        return R.id.loading_fail;
    }
}
