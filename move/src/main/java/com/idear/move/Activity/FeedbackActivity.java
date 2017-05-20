package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.idear.move.Adapter.FeedBackRvAdapter;
import com.idear.move.POJO.FeedBackDataModel;
import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends BaseActivity {

    private Toolbar mToolBar;

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;

    private List<FeedBackDataModel> dataModels = new ArrayList<FeedBackDataModel>();
    private String[] usernames ={"yangqiqi","liguancan","liaoyalin","luoyuezhi","chenjiamin"};
    private String[] titles ={"活动1","活动2","活动3","活动4","活动5"};
    private String[] pics ={"a","b","c","d","e"};
    private String[] visitNum ={"浏览 88 次","浏览 88 次","浏览 88 次","浏览 88 次","浏览 88 次"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_feedback);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(mToolBar);
        initRecyclerView();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add:
                if (adapter.getItemCount() !=usernames.length) {
                    dataModels.add(new FeedBackDataModel(titles[adapter.getItemCount()],
                            "", usernames[adapter.getItemCount()],
                            pics[adapter.getItemCount()], visitNum[adapter.getItemCount()],1));
                    //更新视图(添加数据项)
                    myRecyclerView.scrollToPosition(0);
                    adapter.notifyDataSetChanged();
                }
                break;
            // 当点击actionbar上的删除按钮时，向adapter中移除最后一个数据并通知刷新
            case R.id.remove:
                if (adapter.getItemCount() != 0) {
                    dataModels.remove(adapter.getItemCount() - 1);
                    myRecyclerView.scrollToPosition(0);
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    private void initRecyclerView() {
        dataModels.add(new FeedBackDataModel(titles[0],"",usernames[0],pics[0],visitNum[0],1));
        dataModels.add(new FeedBackDataModel(titles[1],"",usernames[1],pics[1],visitNum[1],1));
        //获取RecyclerV
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //使用垂直布局管理器(单次使用)默认垂直
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        myRecyclerView.setLayoutManager(layoutManager);
        //初始化自定义适配器
        adapter = new FeedBackRvAdapter(this, dataModels);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);
    }

    private void initToolBar(Toolbar toolbar) {
        findViewById(R.id.ic_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
