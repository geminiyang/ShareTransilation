package com.idear.move.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.idear.move.Adapter.DynamicsCommentAdapter;
import com.idear.move.Adapter.DynamicsPraiseAdapter;
import com.idear.move.POJO.DynamicsCommentDataModel;
import com.idear.move.POJO.DynamicsPraiseDataModel;
import com.idear.move.R;
import com.idear.move.util.ToastUtil;

import java.util.LinkedList;

public class DynamicCommentActivity extends MyBaseActivity implements
        DynamicsCommentAdapter.OnItemClickListener,DynamicsCommentAdapter.OnViewClickListener {

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<DynamicsCommentDataModel> dataModels = new LinkedList<>();
    private static final String picUrl = "http://img0.imgtn.bdimg.com/it/u=1928150351,2755968131&fm=26&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_comment);
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
            dataModels.add(new DynamicsCommentDataModel(picUrl,"123、快跑","","你很不错哦",picUrl,
                    getString(R.string.companySummer)));
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
        adapter = new DynamicsCommentAdapter(this, dataModels);
        ((DynamicsCommentAdapter)adapter).setOnItemClickListener(this);
        ((DynamicsCommentAdapter)adapter).setOnViewClickListener(this);
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

    @Override
    public void onViewClick(int position, int viewType) {
        ToastUtil.getInstance().showToast(this,"点击了回复");
    }
}
