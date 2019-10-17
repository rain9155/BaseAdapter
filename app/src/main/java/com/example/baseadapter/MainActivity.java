package com.example.baseadapter;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.baseadapter.adapter.DataAdapter;
import com.example.library.BaseAdapter;
import com.example.library.loadmore.LoadMoreHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DataAdapter mDataAdapter;
    private boolean isAlwaysAnim;
    private boolean isEnableLoadMore = true;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int curLoadMoreStatus = LoadMoreHelper.STATUS_LOADING_COMPLETE;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataAdapter = new DataAdapter(R.layout.item_data);
        mRecyclerView = findViewById(R.id.recycler_view);
        mDataAdapter.openItemAnim();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mDataAdapter);
        mDataAdapter.setOnLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore");
                mHandler.postDelayed(() -> {
                    if(curLoadMoreStatus == LoadMoreHelper.STATUS_LOADING_END){
                        mDataAdapter.loadingEnd();
                        Log.d(TAG, "loadingEnd");
                    }else if(curLoadMoreStatus == LoadMoreHelper.STATUS_LOADING_FAIL){
                        mDataAdapter.loadingFail();
                        Log.d(TAG, "loadingFail");
                    }else {
                        List<String> newDatas = new ArrayList<>();
                        getDatas(newDatas, 10);
                        mDataAdapter.addDatas(newDatas);
                        mDataAdapter.loadingComplete();
                        Log.d(TAG, "loadingComplete");
                    }

                }, 2000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_add_datas:
                List<String> datas = new ArrayList<>();
                getDatas(datas, 30);
                mDataAdapter.setDatas(datas);
                mDataAdapter.disableLoadMoreIfNotFill(mRecyclerView);
                break;
            case R.id.item_load_more:
                isEnableLoadMore = !isEnableLoadMore;
                if(isEnableLoadMore){
                    mDataAdapter.setLoadMoreEnable(true);
                    item.setTitle("Disable Load More");
                }else {
                    mDataAdapter.setLoadMoreEnable(false);
                    item.setTitle("Enable Load More");
                }
                break;
            case R.id.item_load_more_status:
                if(curLoadMoreStatus == LoadMoreHelper.STATUS_LOADING_COMPLETE){
                    curLoadMoreStatus = LoadMoreHelper.STATUS_LOADING_END;
                    item.setTitle("Load More Status(End)");
                }else if(curLoadMoreStatus == LoadMoreHelper.STATUS_LOADING_END){
                    curLoadMoreStatus = LoadMoreHelper.STATUS_LOADING_FAIL;
                    item.setTitle("Load More Status(Fail)");
                }else {
                    curLoadMoreStatus = LoadMoreHelper.STATUS_LOADING_COMPLETE;
                    item.setTitle("Load More Status(Complete)");
                }
                break;
            case R.id.item_add_header:
                View headerView = LayoutInflater.from(this).inflate(R.layout.header_view, null);
                mDataAdapter.addHeaderView(headerView);
                break;
            case R.id.item_remove_header:
                mDataAdapter.removeHeaderView();
                break;
            case R.id.item_alpha_anim:
                mDataAdapter.changeItemAnim(BaseAdapter.ANIM_ALPHA);
                break;
            case R.id.item_slide_anim:
                mDataAdapter.changeItemAnim(BaseAdapter.ANIM_SLIDE_FROM_LEFT);
                break;
            case R.id.item_scale_anim:
                mDataAdapter.changeItemAnim(BaseAdapter.ANIM_SCALE);
                break;
            case R.id.item_close_anim:
                mDataAdapter.closeItemAnim();
                break;
            case R.id.item_open_anim:
                mDataAdapter.openItemAnim();
                break;
            case R.id.item_always_anim:
                isAlwaysAnim = !isAlwaysAnim;
                mDataAdapter.setAlwaysItemAnim(isAlwaysAnim);
                item.setTitle("Always Anim" + "(" + isAlwaysAnim + ")");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDatas(List<String> datas, int count){
        for(int i = 0; i < count; i++){
            datas.add(String.valueOf(i));
        }
    }

}
