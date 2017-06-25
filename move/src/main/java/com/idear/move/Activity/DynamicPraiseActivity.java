package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.idear.move.Adapter.DynamicsPraiseAdapter;
import com.idear.move.Adapter.FansAndAttentionAdapter;
import com.idear.move.POJO.DynamicsPraiseDataModel;
import com.idear.move.POJO.FansAndAttentionDataModel;
import com.idear.move.R;
import com.idear.move.util.ToastUtil;

import java.util.LinkedList;

public class DynamicPraiseActivity extends MyBaseActivity implements DynamicsPraiseAdapter.OnItemClickListener{

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<DynamicsPraiseDataModel> dataModels = new LinkedList<>();
    private static final String picUrl = "http://img0.imgtn.bdimg.com/it/u=1928150351,2755968131&fm=26&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_praise);
        init();
        initRecyclerView();
        addData();
    }

    /**
     * 初始化界面
     */
    private void init() {
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
    }

    private void addData() {
        //这里执行提交数据并获取相关数据的异步操作
        for(int i=0;i<2;i++) {
            dataModels.add(new DynamicsPraiseDataModel(picUrl,"123、快跑","",picUrl,getString(R.string.companySummer)));
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        //获取RecyclerV
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new DynamicsPraiseAdapter(this, dataModels);
        ((DynamicsPraiseAdapter)adapter).setOnItemClickListener(this);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);

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

    @Override
    public void onItemClick(int position) {
        ToastUtil.getInstance().showToast(this,"click");
    }
}
