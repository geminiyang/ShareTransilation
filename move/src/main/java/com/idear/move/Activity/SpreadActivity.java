package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.idear.move.Adapter.SpreadRvAdapter;
import com.idear.move.POJO.SpreadDataModel;
import com.idear.move.R;
import com.idear.move.MyWidget.RecyclerViewDivider;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SpreadActivity extends BaseActivity {

    private Button add,remove;

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;

    private List<SpreadDataModel> dataModels = new ArrayList<SpreadDataModel>();
    private String[] titles ={"a公司","b公司","c公司","d公司"};
    private String[] pics ={"a","b","c","d"};
    private String[] visitNum ={"88","88","88","88"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_spread);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {

        dataModels.add(new SpreadDataModel(titles[0],"",pics[0],visitNum[0],1));
        dataModels.add(new SpreadDataModel(titles[1],"",pics[1],visitNum[1],1));
        //获取RecyclerV
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new SpreadRvAdapter(this, dataModels);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);
        //网格布局管理器
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        //网格布局管理器
        myRecyclerView.setLayoutManager(gridLayoutManager);

        RecyclerViewDivider divider = new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color._grey));
        divider.setMyGridLayoutManager(gridLayoutManager);
        divider.setItemMargin(2);
        myRecyclerView.addItemDecoration(divider);
    }

    private void initView() {
        add = (Button) findViewById(R.id.add);
        remove = (Button) findViewById(R.id.remove);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() !=pics.length) {
                    dataModels.add(new SpreadDataModel(titles[adapter.getItemCount()],
                            "", pics[adapter.getItemCount()],
                            visitNum[adapter.getItemCount()], 1));
                    //更新视图(添加数据项)
                    myRecyclerView.scrollToPosition(0);
                    adapter.notifyDataSetChanged();
                }
                // 当点击actionbar上的删除按钮时，向adapter中移除最后一个数据并通知刷新
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() != 0) {
                    dataModels.remove(adapter.getItemCount() - 1);
                    myRecyclerView.scrollToPosition(0);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        findViewById(R.id.ic_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
