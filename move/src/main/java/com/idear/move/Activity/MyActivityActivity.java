package com.idear.move.Activity;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.idear.move.Fragment.ActivityDoingFragment;
import com.idear.move.Fragment.ActivityFinishFragment;
import com.idear.move.Fragment.ActivityInVerifyFragment;
import com.idear.move.Fragment.ActivityNoPassFragment;
import com.idear.move.Fragment.ActivityWaitForFeedBackFragment;
import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MyActivityActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        ActivityNoPassFragment.OnFragmentInteractionListener,ActivityInVerifyFragment.OnFragmentInteractionListener,
        ActivityDoingFragment.OnFragmentInteractionListener,ActivityFinishFragment.OnFragmentInteractionListener,
        ActivityWaitForFeedBackFragment.OnFragmentInteractionListener{

    private ImageView iv_back;
    private TabLayout mTabLayout;

    //分页列表
    private ViewPager mViewPager;
    private ActivityNoPassFragment f1;
    private ActivityInVerifyFragment f2;
    private ActivityDoingFragment f3;
    private ActivityWaitForFeedBackFragment f4;
    private ActivityFinishFragment f5;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_my_activity);

        initData();
        initView();
        initEvent();
        selectTab();
    }

    private void selectTab() {
        String str = getIntent().getExtras().getString("select_tab");
        if(str!= null) {
           if(str.contentEquals("未通过")) {
               mViewPager.setCurrentItem(0, false);
           } else if(str.contentEquals("审核中")) {
               mViewPager.setCurrentItem(1, false);
           } else if(str.contentEquals("进行中")) {
               mViewPager.setCurrentItem(2, false);
           } else if(str.contentEquals("待反馈")) {
               mViewPager.setCurrentItem(3, false);
           } else if(str.contentEquals("已结束")) {
               mViewPager.setCurrentItem(4, false);
           } else {
               //提示出错
           }
        }
    }

    private void initData() {
        if (f1 == null) {
            f1 = ActivityNoPassFragment.newInstance("未通过");
        }
        if (f2 == null) {
            f2 = ActivityInVerifyFragment.newInstance("审核中");
        }
        if (f3 == null) {
            f3 = ActivityDoingFragment.newInstance("进行中");
        }
        if (f4 == null) {
            f4 = ActivityWaitForFeedBackFragment.newInstance("待反馈");
        }
        if (f5 == null) {
            f5 = ActivityFinishFragment.newInstance("已结束");
        }
        mTabs.add(f1);
        mTabs.add(f2);
        mTabs.add(f3);
        mTabs.add(f4);
        mTabs.add(f5);

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
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
        //TabLayout.MODE_SCROLLABLE和TabLayout.MODE_FIXED分别表示当tab的内容超过屏幕宽度是否支持横向水平滑动
        //第一种支持滑动，第二种不支持，默认不支持水平滑动。
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);

        mTabLayout.getTabAt(0).setText("未通过");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("审核中");
        mTabLayout.getTabAt(2).setText("进行中");
        mTabLayout.getTabAt(3).setText("待反馈");
        mTabLayout.getTabAt(4).setText("已结束");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                mViewPager.setCurrentItem(0, false);
                break;
            case 1:
                mViewPager.setCurrentItem(1, false);
                break;
            case 2:
                mViewPager.setCurrentItem(2, false);
                break;
            case 3:
                mViewPager.setCurrentItem(3, false);
                break;
            case 4:
                mViewPager.setCurrentItem(4, false);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onActivityNoPassFragmentInteraction(Uri uri) {
        //未通过与主Activity的关联
    }

    @Override
    public void onActivityInVerifyFragmentInteraction(Uri uri) {
        //审核中与主Activity的关联
    }

    @Override
    public void onActivityDoingFragmentInteraction(Uri uri) {
        //进行中与主Activity的关联
    }

    @Override
    public void onActivityFinishFragmentInteraction(Uri uri) {
        //已完成与主Activity的关联
    }

    @Override
    public void onActivityWaitForFeedBackFragmentInteraction(Uri uri) {
        //待反馈与主Activity的关联
    }

}

