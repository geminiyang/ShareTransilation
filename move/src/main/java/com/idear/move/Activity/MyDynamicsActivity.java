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
import com.yqq.idear.CustomRecyclerView;
import com.yqq.idear.DataStateChangeCheck;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyDynamicsActivity extends BaseActivity implements CustomRecyclerView.DataOperation {

    private CustomRecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<MyDynamicsDataModel> dataModels = new LinkedList<>();
    private String[] states ={"[审核中]","[进行中]","[筹资中]","[已结束]"};
    private int[] pics ={R.mipmap.family,R.mipmap.family,R.mipmap.family,R.mipmap.family};
    private int[] userIcons ={R.mipmap.paintbox,R.mipmap.paintbox,
            R.mipmap.paintbox,R.mipmap.paintbox};
    private String[] userNames = {"大丸子","二丸子","三丸子","四丸子"};
    private String[] times = {"2017.6.11","2017.6.12","2017.6.13","2017.6.14"};
    private String[] commentOne = {"大丸子","一级棒"};
    private List<String[]> lists = new ArrayList<>();

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
        lists.add(commentOne);
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
        //数据状态监听器
        DataStateChangeCheck mDataEndListener = new DataStateChangeCheck(myRecyclerView);
        //实现了数据操作监听
        mDataEndListener.setLoadDataListener(myRecyclerView);
        myRecyclerView.addDataChangeListener(mDataEndListener);
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
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%4;
            Log.d("info","loadMode------"+index+"LIST SIZE : " + dataModels.size()+1);
            dataModels.add(new MyDynamicsDataModel(userIcons[index],userNames[index],times[index],
                    pics[index], states[index],lists.get(0)));
        }
    }

    /**
     * 下拉刷新数据请求操作
     */
    @Override
    public void onRefresh() {
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%4;
            Log.d("info","refreshMode------"+index + "LIST SIZE : " + dataModels.size()+1);
            dataModels.addFirst(new MyDynamicsDataModel(userIcons[index],userNames[index],times[index],
                    pics[index], states[index],lists.get(0)));
        }
    }
}
