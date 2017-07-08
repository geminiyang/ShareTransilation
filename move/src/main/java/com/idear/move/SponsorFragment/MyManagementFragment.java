package com.idear.move.SponsorFragment;

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

import com.idear.move.Fragment.GroupListFragment;
import com.idear.move.Fragment.MyFragment;
import com.idear.move.Fragment.UserListFragment;
import com.idear.move.R;
import com.idear.move.myWidget.NoScrollViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/5/3.
 */

public class MyManagementFragment extends Fragment implements
        TabLayout.OnTabSelectedListener {

    private static final String ARG = "arg";

    public static MyManagementFragment newInstance(String arg){
        MyManagementFragment fragment = new MyManagementFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }


    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;

    private MyFragment f1;
    private MyFragment f2;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_my_management,container,false);
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

        mTabLayout.getTabAt(0).setText("赞助");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("推广");

        //设置下划线的长度
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTabLayout, 10, 10);
            }
        });
    }

    private void initData() {
        if (f1 == null) {
            f1 = MyFragment.newInstance("赞助");
        }
        if (f2 == null) {
            f2 = MyFragment.newInstance("推广");
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
            params.bottomMargin = 0;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }

}
