package com.idear.move.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.idear.move.Fragment.MyFragment;
import com.idear.move.R;

import java.util.ArrayList;
import java.util.List;

public class MyFavoritesActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private ImageView iv_back;
    private TabLayout mTabLayout;

    //分页列表
    private ViewPager mViewPager;
    private MyFragment f1,f2,f3,f4,f5;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        initData();
        initView();
        initEvent();
    }

    private void initData() {
        if (f1 == null) {
            f1 = MyFragment.newInstance("界面1");
        }
        if (f2 == null) {
            f2 = MyFragment.newInstance("界面2");
        }
        if (f3 == null) {
            f3 = MyFragment.newInstance("界面3");
        }
        if (f4 == null) {
            f4 = MyFragment.newInstance("界面4");
        }
        if (f5 == null) {
            f5 = MyFragment.newInstance("界面5");
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
    }

    private void initEvent() {
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
        mTabLayout.getTabAt(2).setText("待反馈");
        mTabLayout.getTabAt(2).setText("已结束");
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
}
