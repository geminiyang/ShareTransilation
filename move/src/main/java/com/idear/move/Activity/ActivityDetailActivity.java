package com.idear.move.Activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.util.ToastUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;


public class ActivityDetailActivity extends BaseActivity {

    private static final String TAG = "info";

    private Toolbar toolbar;
    private ImageView iv_back;
    private Button takePartInActivity;

    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this,getResources().getColor(R.color.transparent));
        setContentView(R.layout.activity_activity_detail);

        initToolBar();
        initView();
        initEvent();
    }

    private void initToolBar() {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsing_toolbar_layout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar_layout);
        collapsing_toolbar_layout.setTitle("具体活动");
        collapsing_toolbar_layout.setExpandedTitleGravity(Gravity.CENTER);
        collapsing_toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsing_toolbar_layout.setExpandedTitleColor(getResources().getColor(R.color.blue_light));

        AppBarLayout app_bar_layout = (AppBarLayout)findViewById(R.id.app_bar);

        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        //(expanded)
                        if(takePartInActivity.getVisibility()==View.GONE) {
                            takePartInActivity.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (Math.abs(verticalOffset) >= scrollRange) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                    //当向下滑动超过一定距离时(达到collapsed状态）
                    if(takePartInActivity.getVisibility()==View.VISIBLE) {
                        takePartInActivity.setVisibility(View.GONE);
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){

                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "你已将该活动标记为喜欢", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void initView() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.my_nested_scrollview);
        takePartInActivity = (Button) findViewById(R.id.takePartInActivity);
    }

    private void initEvent() {

        takePartInActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.getInstance().showToastTest(ActivityDetailActivity.this);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");

                }

            }
        });
    }
}
