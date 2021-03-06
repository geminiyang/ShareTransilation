package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.idear.move.Adapter.FeedBackRvAdapter;
import com.idear.move.POJO.FeedBackDataModel;
import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.idear.CustomRecyclerView;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.LinkedList;

public class FeedbackActivity extends BaseActivity implements CustomRecyclerView.DataOperation,
        FeedBackRvAdapter.OnItemClickListener{

    //标题栏相关控件
    private ImageView back;
    //RecyclerView相关参数
    private CustomRecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 2;

    private LinkedList<FeedBackDataModel> dataModels = new LinkedList<>();
    private String[] titles ={"aa活动","bb活动","cc活动",
            "d活动"};
    private int[] picUrl ={R.drawable.rina,R.drawable.rina,R.drawable.family,R.drawable.family};
    private String[] Num ={"88","12","188","788"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_feedback);
        initView();
        initEvent();
        initRecyclerView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.ic_arrow_back);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        //获取RecyclerV
        myRecyclerView = (CustomRecyclerView) findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new FeedBackRvAdapter(this, dataModels);
        ((FeedBackRvAdapter)adapter).setOnItemClickListener(this);
        // specify(指定)an adapter (see also next example)
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

//        //设置分割线
//        RecyclerViewDivider divider = new RecyclerViewDivider(
//                this, LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color._grey));
//        divider.setMyGridLayoutManager(gridLayoutManager);
//        divider.setItemMargin(2);
//        myRecyclerView.addItemDecoration(divider);
    }

    @Override
    public void onLoadMore() {
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%4;
            Log.d("info","loadMode------" + "LIST SIZE : " + dataModels.size()+1);
            dataModels.add(new FeedBackDataModel(titles[index],"",picUrl[index],Num[index],
                    CustomRecyclerView.TYPE_NORMAL));
        }
    }

    @Override
    public void onRefresh() {
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%4;
            Log.d("info","refreshMode------" + "LIST SIZE : " + dataModels.size()+1);
            dataModels.addFirst(new FeedBackDataModel(titles[index],"",picUrl[index],Num[index],
                    CustomRecyclerView.TYPE_NORMAL));
        }
    }

    @Override
    public void onItemClick(int position) {
        IntentSkipUtil.skipToNextActivity(this,FeedBackDetailActivity.class);
    }
}
