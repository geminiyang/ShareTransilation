package com.yqq.BottomNavigationBar;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yqq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager+RadioGroup+RadioButton
 */
public class MethodTwoActivity extends AppCompatActivity implements
                                            RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener {

    private RadioButton heart,time,locate,comment;
    private TipButton locate_tip_bt;
    private MyFragment f1,f2,f3,f4,f5;

    private RadioGroup radioGroup;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_two);
        
        initData();
        initView();
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
        if(f5==null){
            f5 = MyFragment.newInstance("定位");
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

    //UI组件初始化与事件绑定
    private void initView() {
        heart = (RadioButton) findViewById(R.id.heart);
        time = (RadioButton) findViewById(R.id.time);
        locate = (RadioButton) findViewById(R.id.locate);
        comment = (RadioButton) findViewById(R.id.comment);
        locate_tip_bt = (TipButton) findViewById(R.id.locate_tip);

        radioGroup = (RadioGroup) findViewById(R.id.rd_group);
        radioGroup.setOnCheckedChangeListener(this);

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        setDefaultFragment();

    }

    private void setDefaultFragment() {
        time.setChecked(true);
        mViewPager.setCurrentItem(0,false);
        locate_tip_bt.setTipOn(true);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch(checkedId){
            case R.id.time:
                mViewPager.setCurrentItem(0, false);
                break;

            case R.id.comment:
                mViewPager.setCurrentItem(1, false);
                break;

            case R.id.heart:
                mViewPager.setCurrentItem(2, false);
                break;

            case R.id.locate:
                mViewPager.setCurrentItem(3, false);
                break;
            case R.id.locate_tip:
                mViewPager.setCurrentItem(4, false);
                locate_tip_bt.setTipOn(false);
                break;
        }
    }

    /*
        ViewPager的活动监听
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mViewPager.setCurrentItem(0, false);
                time.setChecked(true);
                break;
            case 1:
                mViewPager.setCurrentItem(1, false);
                comment.setChecked(true);
                break;
            case 2:
                mViewPager.setCurrentItem(2, false);
                heart.setChecked(true);
                break;
            case 3:
                mViewPager.setCurrentItem(3, false);
                locate.setChecked(true);
                break;
            case 4:
                mViewPager.setCurrentItem(4, false);
                locate_tip_bt.setChecked(true);
                locate_tip_bt.setTipOn(false);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
