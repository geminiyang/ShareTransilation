package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.idear.move.Adapter.DynamicsDetailAdapter;
import com.idear.move.POJO.AbstractDataModel;
import com.idear.move.POJO.DynamicDetailItemOneDataModel;
import com.idear.move.POJO.DynamicDetailItemThreeDataModel;
import com.idear.move.POJO.DynamicDetailItemTwoDataModel;
import com.idear.move.R;
import com.idear.move.util.DateUtil;
import com.idear.move.util.ToastUtil;

import java.util.LinkedList;
import java.util.Random;

import static com.idear.move.Adapter.DynamicsDetailAdapter.ATTENTION_BTN;
import static com.idear.move.Adapter.DynamicsDetailAdapter.COMMENT_ICON;
import static com.idear.move.Adapter.DynamicsDetailAdapter.FAVORITE_ICON;
import static com.idear.move.Adapter.DynamicsDetailAdapter.THUMB_UP_ICON_ONE;
import static com.idear.move.Adapter.DynamicsDetailAdapter.THUMB_UP_ICON_THREE;

public class DynamicsDetailActivity extends MyBaseActivity implements
        DynamicsDetailAdapter.OnItemClickListener, DynamicsDetailAdapter.OnViewClickListener {
    //UI相关
    private ImageView iv_back;

    //RecyclerView相关
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<AbstractDataModel> dataModels = new LinkedList<>();
    private static final String picUrl = "http://img0.imgtn.bdimg.com/it/u=1928150351,2755968131&fm=26&gp=0.jpg";
    private long TimeStamp = 1497626094;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamics_detail);
        initView();
        initEvent();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initRecyclerView();
    }

    //模拟数据初始化
    private void addData() {
        //这里执行提交数据并获取相关数据的异步操作
        for(int i=0;i<10;i++) {
            if(i==0) {
                dataModels.add(new DynamicDetailItemOneDataModel(picUrl,picUrl,"123、快跑",
                        DateUtil.receiveDate(TimeStamp),getString(R.string.testString)) {
                });
            } else if(i==1) {
                dataModels.add(new DynamicDetailItemTwoDataModel("全部评论(共8条)") {
                });
            } else {
                Random r = new Random();
                String num1 = r.nextInt(100)+"";
                String num2 = r.nextInt(100)+"";
                dataModels.add(new DynamicDetailItemThreeDataModel( "第"+(i-1)+"名用户",
                        "抢到楼主沙发啦",DateUtil.receiveDate(TimeStamp),num1,num2,picUrl) {
                });
            }
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
        adapter = new DynamicsDetailAdapter(this, dataModels);
        //填充数据
        addData();
        ((DynamicsDetailAdapter)adapter).setOnItemClickListener(this);
        ((DynamicsDetailAdapter)adapter).setOnViewClickListener(this);
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
        ToastUtil.getInstance().showToast(this,"clickItem");
    }

    @Override
    public void onViewClick(int position, int viewType) {
        switch (viewType) {
            case ATTENTION_BTN :
                ToastUtil.getInstance().showToast(this,"点击了关注按钮");
                break;
            case THUMB_UP_ICON_ONE:
                ToastUtil.getInstance().showToast(this,"点击动态布局点赞按钮");
                break;
            case FAVORITE_ICON:
                ToastUtil.getInstance().showToast(this,"点击收藏按钮");
                break;
            case THUMB_UP_ICON_THREE:
                ToastUtil.getInstance().showToast(this,"点击评论布局点赞按钮");
                break;
            case COMMENT_ICON:
                ToastUtil.getInstance().showToast(this,"点击评论按钮");
                break;
            default:
                break;
        }
    }
}
