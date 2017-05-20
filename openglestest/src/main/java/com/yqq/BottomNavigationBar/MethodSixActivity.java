package com.yqq.BottomNavigationBar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yqq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TabLayout+ViewPager
 */
public class MethodSixActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private MyFragment f1,f2,f3,f4;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_six);

        initData();
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);


        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.getTabAt(0).setIcon(R.drawable.tab_menu_time).setText("时间");//自有方法添加icon
        mTabLayout.getTabAt(1).setIcon(R.drawable.tab_menu_comment).setText("评论");
        mTabLayout.getTabAt(2).setIcon(R.drawable.tab_menu_heart).setText("心情");
        mTabLayout.getTabAt(3).setIcon(R.drawable.tab_menu_locate).setText("定位");

    }


    private void initData() {
        if(f1==null) {
            f1 = MyFragment.newInstance("时间");
        }
        if(f2==null){
            f2 = MyFragment.newInstance("评论");
        }
        if(f3==null){
            f3 = MyFragment.newInstance("心情");
        }
        if(f4==null){
            f4 = MyFragment.newInstance("定位");
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
    public void onTabUnselected(TabLayout.Tab tab) {
        setTabUnSelectedState(tab);
    }


    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setTabSelectedState(TabLayout.Tab tab) {

    }

    private void setTabUnSelectedState(TabLayout.Tab tab) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        resetTabIcon();
        setTabSelectedState(tab);
        int position = tab.getPosition();
        switch (position) {
            case 0:
                tab.setIcon(R.mipmap.time_fill);
                break;
            case 1:
                tab.setIcon(R.mipmap.comment_fill);
                break;
            case 2:
                tab.setIcon(R.mipmap.heart_fill);
                break;
            case 3:
                tab.setIcon(R.mipmap.locate_fill);
                break;

        }
    }


    private void resetTabIcon() {
        mTabLayout.getTabAt(0).setIcon(R.mipmap.time);
        mTabLayout.getTabAt(1).setIcon(R.mipmap.comment);
        mTabLayout.getTabAt(2).setIcon(R.mipmap.heart);
        mTabLayout.getTabAt(3).setIcon(R.mipmap.locate);
    }
}
