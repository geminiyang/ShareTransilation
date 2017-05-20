package com.idear.move.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.idear.move.Fragment.AllActivityFinancingFragment;
import com.idear.move.Fragment.AllActivityFragment;
import com.idear.move.Fragment.AllActivityRecruitAndFinancingFragment;
import com.idear.move.Fragment.AllActivityRecruitFragment;
import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class AllActivityActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar mToolBar;
    private ImageView iv_back;

    private AllActivityFragment f1;
    private AllActivityRecruitFragment f2;
    private AllActivityFinancingFragment f3;
    private AllActivityRecruitAndFinancingFragment f4;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_all_activity);

        initData();
        initView();
    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.view_pager_all_activity);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_all_activity);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
        //TabLayout.MODE_SCROLLABLE和TabLayout.MODE_FIXED分别表示当tab的内容超过屏幕宽度是否支持横向水平滑动
        //第一种支持滑动，第二种不支持，默认不支持水平滑动。
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);

        mTabLayout.getTabAt(0).setText("全部活动");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("招募");
        mTabLayout.getTabAt(2).setText("筹资");
        mTabLayout.getTabAt(3).setText("招募筹资");

        initToolBar(mToolBar);

        //设置分割线
        LinearLayout linearLayout = (LinearLayout) mTabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.template_divider_vertical));
    }

    private void initToolBar(Toolbar toolbar) {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        if (f1 == null) {
            f1 = AllActivityFragment.newInstance("全部活动");
        }
        if (f2 == null) {
            f2 = AllActivityRecruitFragment.newInstance("招募");
        }
        if (f3 == null) {
            f3 = AllActivityFinancingFragment.newInstance("筹资");
        }
        if (f4 == null) {
            f4 = AllActivityRecruitAndFinancingFragment.newInstance("招募筹资");
        }
        mTabs.add(f1);
        mTabs.add(f2);
        mTabs.add(f3);
        mTabs.add(f4);

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
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
