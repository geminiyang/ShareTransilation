package com.idear.move.Fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.idear.move.Activity.UserChatActivity;
import com.idear.move.Dummy.UserListContent;
import com.idear.move.R;
import com.idear.move.myWidget.NoScrollViewPager;
import com.idear.move.util.IntentSkipUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/5/3.
 */

public class MessageFragment extends Fragment implements
        TabLayout.OnTabSelectedListener {

    private static final String ARG = "arg";

    public static MessageFragment newInstance(String arg){
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }


    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;

    private UserListFragment f1;
    private GroupListFragment f2;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_message,container,false);
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

        mTabLayout.getTabAt(0).setText("私聊");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("房间");

        //设置下划线的长度
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                //setIndicator(mTabLayout, 50, 50);
            }
        });
    }

    private void initData() {
        if (f1 == null) {
            f1 = UserListFragment.newInstance(1);
        }
        if (f2 == null) {
            f2 = GroupListFragment.newInstance(1);
        }
        mTabs.add(f1);
        mTabs.add(f2);

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

}
