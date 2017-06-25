package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.idear.move.Adapter.CardLayoutThreeAdapter;
import com.idear.move.Adapter.SearchToAddFriendsAdapter;
import com.idear.move.POJO.CardLayoutThreeDataModel;
import com.idear.move.POJO.SearchToAddFriendsDataModel;
import com.idear.move.R;
import com.idear.move.util.ToastUtil;
import com.yqq.idear.DataStateChangeCheck;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.LinkedList;

import static com.idear.move.Adapter.SearchToAddFriendsAdapter.*;

public class FriendAddActivity extends MyBaseActivity implements View.OnClickListener,OnViewClickListener{

    private EditText editText;//用户搜索输入
    private FrameLayout fl;
    private ImageButton searchClose;
    private Button back;
    private ImageView searchAction;

    //搜索列表相关参数
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<SearchToAddFriendsDataModel> dataModels = new LinkedList<>();
    private static final String picUrl = "http://img0.imgtn.bdimg.com/it/u=1928150351,2755968131&fm=26&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.title_bar_blue));
        setContentView(R.layout.activity_friend_add);

        initView();
        initRecyclerView();
        initEvent();
    }

    private void searchOp() {
        //这里执行提交数据并获取相关数据的异步操作
        for(int i=0;i<2;i++) {
            dataModels.add(new SearchToAddFriendsDataModel("20143501140331",picUrl,"123、快跑"));
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new SearchToAddFriendsAdapter(this, dataModels);
        ((SearchToAddFriendsAdapter)adapter).setOnViewClickListener(this);
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

    private void initView() {
        editText = (EditText) findViewById(R.id.edit_text);

        fl = (FrameLayout) findViewById(R.id.fl);

        searchClose = (ImageButton) findViewById(R.id.ib_close);
        back = (Button) findViewById(R.id.ic_arrow_back);

        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        searchAction = (ImageView) findViewById(R.id.iv_search);
    }

    private void initEvent() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(fl.getVisibility() == View.GONE&&s.toString().trim().length()>0) {
                    fl.setVisibility(View.VISIBLE);
                }
            }
        });
        searchClose.setOnClickListener(this);
        searchAction.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                if(fl.getVisibility() == View.VISIBLE) {
                    fl.setVisibility(View.GONE);
                    editText.setText("");
                    editText.setHint(" 搜索添加或关注、好友、商户");
                }
                break;
            case R.id.ic_arrow_back:
                finish();
                break;
            case R.id.iv_search:
                searchOp();
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewClick(int position, int viewType) {
        switch (viewType) {
            case 1:
                ToastUtil.getInstance().showToast(this,"点击了关注按钮");
                //这里执行相关的关注异步操作
                break;
            default:
                break;
        }
    }
}
