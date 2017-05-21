package com.idear.move.Activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.idear.move.Fragment.MyFragment;
import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ActivityDetailActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private MyFragment f1,f2;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this,getResources().getColor(R.color.title_bar_blue));
        setContentView(R.layout.activity_activity_detail);

        initToolBar();
        initData();
        initView();
    }

    private void initToolBar() {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final CollapsingToolbarLayout collapsing_toolbar_layout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        collapsing_toolbar_layout.setTitle("");
        collapsing_toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsing_toolbar_layout.setExpandedTitleColor(getResources().getColor(R.color.blue_light));

        AppBarLayout app_bar_layout = (AppBarLayout)findViewById(R.id.app_bar);

        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e("APP","appBarLayoutHeight:"+appBarLayout.getHeight()+" getTotalScrollRange:"+appBarLayout.getTotalScrollRange()+" offSet:"+verticalOffset);
                if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //当向下滑动超过一定距离时(达到collapsed状态）
                    //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                    collapsing_toolbar_layout.setTitle("具体活动(collapsed)");
                }else{
                    collapsing_toolbar_layout.setTitle("具体活动(expanded)");
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void initData() {
        if(f1==null) {
            f1 = MyFragment.newInstance("详情");
        }
        if(f2==null){
            f2 = MyFragment.newInstance("反馈");
        }
        mTabs.add(f1);
        mTabs.add(f2);

        //要关注如何更新其中的数据
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
    }
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
        //TabLayout.MODE_SCROLLABLE和TabLayout.MODE_FIXED分别表示当tab的内容超过屏幕宽度是否支持横向水平滑动
        //第一种支持滑动，第二种不支持，默认不支持水平滑动。
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);

        mTabLayout.getTabAt(0).setText("详情");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("反馈");

        //设置下划线的长度
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                //setIndicator(mTabLayout, 50, 50);
            }
        });
    }

    /**
     * 利用反射的方法来设置 每一个tab的大小
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //将px转换成dp
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip,
                Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip,
                Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                mViewPager.setCurrentItem(0,false);
                break;
            case 1:
                mViewPager.setCurrentItem(1,false);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
