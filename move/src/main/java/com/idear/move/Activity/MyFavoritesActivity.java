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

import com.idear.move.Fragment.MyFavoritesActivityFragment;
import com.idear.move.Fragment.MyFavoritesDynamicsFragment;
import com.idear.move.Fragment.MyFavoritesSpreadFragment;
import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MyFavoritesActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
                                            MyFavoritesActivityFragment.OnFragmentInteractionListener,
                                            MyFavoritesDynamicsFragment.OnFragmentInteractionListener,
                                            MyFavoritesSpreadFragment.OnFragmentInteractionListener {

    private ImageView iv_back;
    private TabLayout mTabLayout;

    //分页列表
    private ViewPager mViewPager;
    private MyFavoritesActivityFragment f1;
    private MyFavoritesDynamicsFragment f2;
    private MyFavoritesSpreadFragment f3;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_my_favorites);

        initData();
        initView();
        initEvent();
    }

    private void initData() {
        if (f1 == null) {
            f1 = MyFavoritesActivityFragment.newInstance("活动");
        }
        if (f2 == null) {
            f2 = MyFavoritesDynamicsFragment.newInstance("动态");
        }
        if (f3 == null) {
            f3 = MyFavoritesSpreadFragment.newInstance("推广");
        }

        mTabs.add(f1);
        mTabs.add(f2);
        mTabs.add(f3);

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
        mViewPager.setOffscreenPageLimit(0);//设置当前ViewPager显示item,旁边左右跨度预先加载范围

        mTabLayout.setupWithViewPager(mViewPager);
        //TabLayout.MODE_SCROLLABLE和TabLayout.MODE_FIXED分别表示当tab的内容超过屏幕宽度是否支持横向水平滑动
        //第一种支持滑动，第二种不支持，默认不支持水平滑动。
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);

        mTabLayout.getTabAt(0).setText("活动");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("动态");
        mTabLayout.getTabAt(2).setText("推广");
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
            default:
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
    public void onFavoritesSpreadFragmentInteraction(Uri uri) {
        //与我的收藏里面的推广关联
    }

    @Override
    public void onFavoritesDynamicsFragmentInteraction(Uri uri) {
        //与我的收藏里面的动态关联
    }

    @Override
    public void onFavoritesActivityFragmentInteraction(Uri uri) {
        //与我的收藏里面的活动关联
    }
}
