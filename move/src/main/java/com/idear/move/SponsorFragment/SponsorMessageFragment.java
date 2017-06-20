package com.idear.move.SponsorFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Fragment.GroupListFragment;
import com.idear.move.Fragment.UserListFragment;
import com.idear.move.R;
import com.idear.move.myWidget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/5/3.
 */

public class SponsorMessageFragment extends Fragment implements
        TabLayout.OnTabSelectedListener {

    private static final String ARG = "arg";

    public static SponsorMessageFragment newInstance(String arg){
        SponsorMessageFragment fragment = new SponsorMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }


    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;

    private GroupListFragment f1;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_sponsor_message,container,false);
            initData();
            init(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void init(View view) {
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.view_pager_message);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout_message);

        mViewPager.setUnableScroll(true);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);//设置默认项

        mTabLayout.setupWithViewPager(mViewPager);
        //TabLayout.MODE_SCROLLABLE和TabLayout.MODE_FIXED分别表示当tab的内容超过屏幕宽度是否支持横向水平滑动
        //第一种支持滑动，第二种不支持，默认不支持水平滑动。
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);

        mTabLayout.getTabAt(0).setText("消息");//自有方法添加icon
    }

    private void initData() {
        if (f1 == null) {
            f1 = GroupListFragment.newInstance(1);
        }
        mTabs.add(f1);

        //要关注如何更新其中的数据
        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
