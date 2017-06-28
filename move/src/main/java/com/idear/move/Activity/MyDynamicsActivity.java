package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.idear.move.Adapter.MyDynamicsRvAdapter;
import com.idear.move.POJO.MyDynamicsDataModel;
import com.idear.move.R;
import com.idear.move.Thread.LoadMoreThread;
import com.idear.move.Thread.RefreshThread;
import com.idear.move.network.DataGetInterface;
import com.yqq.idear.CustomRecyclerView;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyDynamicsActivity extends BaseActivity implements CustomRecyclerView.DataOperation {

    private CustomRecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<MyDynamicsDataModel> dataModels = new LinkedList<>();
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_my_dynamics);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        //获取RecyclerV
        myRecyclerView = (CustomRecyclerView) findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new MyDynamicsRvAdapter(this, dataModels);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);

        myRecyclerView.addHeaderView(this);
        myRecyclerView.addFooterView(this);
        myRecyclerView.setTotalCount(TOTAL_COUNT);
        myRecyclerView.setRequestCount(REQUEST_COUNT);
        myRecyclerView.setDataOperation(this);
        myRecyclerView.Initialize();
        //网格布局管理器
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        //网格布局管理器
        myRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initView() {
        findViewById(R.id.ic_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 上拉加载数据请求操作
     */
    @Override
    public void onLoadMore() {
        new LoadMoreThread(new DataGetInterface() {
            @Override
            public void finishWork(Object obj) {
            }

            @Override
            public void interrupt(Exception e) {

            }
        }, dataModels).start();
    }

    /**
     * 下拉刷新数据请求操作
     */
    @Override
    public void onRefresh() {
        new RefreshThread(new DataGetInterface() {
            @Override
            public void finishWork(Object obj) {
                myRecyclerView.finishComplete();
            }

            @Override
            public void interrupt(Exception e) {

            }
        },dataModels).start();
    }
}
