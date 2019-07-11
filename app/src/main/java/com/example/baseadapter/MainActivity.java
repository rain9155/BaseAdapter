package com.example.baseadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.baseadapter.adapter.DataAdapter;
import com.example.library.anim.ScaleAnim;
import com.example.library.config.AnimType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DataAdapter dataAdapter;
    boolean isAlways;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataAdapter = new DataAdapter(R.layout.item_data);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        dataAdapter.openItemAnimation();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);
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
                getDatas(datas);
                dataAdapter.setDatas(datas);
                dataAdapter.notifyDataSetChanged();
                break;
            case R.id.item_add_header:
                View headerView = LayoutInflater.from(this).inflate(R.layout.header_view, null);
                dataAdapter.addHeaderView(headerView);
                break;
            case R.id.item_remove_header:
                dataAdapter.removeHeaderView();
                break;
            case R.id.item_alpha_anim:
                dataAdapter.changeItemAnimation(AnimType.ALPHA);
                break;
            case R.id.item_slide_anim:
                dataAdapter.changeItemAnimation(AnimType.SLIDE_FROM_LEFT);
                break;
            case R.id.item_scale_anim:
                dataAdapter.changeItemAnimation(AnimType.SCALE);
                break;
            case R.id.item_close_anim:
                dataAdapter.closeItemAnimation();
                break;
            case R.id.item_open_anim:
                dataAdapter.openItemAnimation();
                break;
            case R.id.item_always_anim:
                isAlways = !isAlways;
                dataAdapter.setAlwaysItemAnim(isAlways);
                item.setTitle("Always Anim" + "(" + isAlways + ")");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDatas(List<String> datas){
        int count = 50;
        for(int i = 0; i < count; i++){
            datas.add(String.valueOf(i));
        }
    }

}
