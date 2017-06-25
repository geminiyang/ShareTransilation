package com.idear.move.Activity;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.idear.move.Fragment.AttentionFragment;
import com.idear.move.Fragment.FansFragment;
import com.idear.move.Fragment.GroupListFragment;
import com.idear.move.Fragment.MyFragment;
import com.idear.move.Fragment.UserListFragment;
import com.idear.move.R;
import com.idear.move.myWidget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class FansAndAttentionActivity extends MyBaseActivity implements
        TabLayout.OnTabSelectedListener,FansFragment.OnFragmentInteractionListener,
        AttentionFragment.OnFragmentInteractionListener{

    private ImageView iv_back;
    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;

    private FansFragment f1;
    private AttentionFragment f2;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_and_attention);
        initData();
        initView();
        initEvent();
    }

    private void initView() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.view_pager_message);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_message);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewPager.setUnableScroll(true);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);//设置默认项

        mTabLayout.setupWithViewPager(mViewPager);
        //TabLayout.MODE_SCROLLABLE和TabLayout.MODE_FIXED分别表示当tab的内容超过屏幕宽度是否支持横向水平滑动
        //第一种支持滑动，第二种不支持，默认不支持水平滑动。
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);

        mTabLayout.getTabAt(0).setText("粉丝");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("关注");

    }

    private void initData() {
        if (f1 == null) {
            f1 = FansFragment.newInstance("粉丝");
        }
        if (f2 == null) {
            f2 = AttentionFragment.newInstance("关注");
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
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                mViewPager.setCurrentItem(0,true);
                break;
            case 1:
                mViewPager.setCurrentItem(1,true);
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
    public void onFansFragmentInteraction(Uri uri) {
        //Activity和Fragment的消息交互，可以用作消息传递
    }

    @Override
    public void onAttentionFragmentInteraction(Uri uri) {

    }
}
