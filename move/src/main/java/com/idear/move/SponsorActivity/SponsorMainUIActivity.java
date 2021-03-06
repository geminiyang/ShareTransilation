package com.idear.move.SponsorActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.idear.move.Fragment.GroupListFragment;
import com.idear.move.R;
import com.idear.move.Service.ActivityManager;
import com.idear.move.SponsorFragment.AdministrateSpreadFragment;
import com.idear.move.SponsorFragment.SponsorManagementFragment;
import com.idear.move.SponsorFragment.SponsorHomeFragment;
import com.idear.move.SponsorFragment.SponsorInformationFragment;
import com.idear.move.SponsorFragment.SponsorMessageFragment;
import com.idear.move.Thread.LogoutThread;
import com.idear.move.myWidget.TipButton;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;
import com.yqq.swipebackhelper.SwipeBackHelper;

import java.util.ArrayList;
import java.util.List;

public class SponsorMainUIActivity extends BaseActivity implements
        RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener,
        GroupListFragment.OnListFragmentInteractionListener ,
        AdministrateSpreadFragment.OnFragmentInteractionListener{
    private RadioButton administrate,home,locate,msg;
    private TipButton my;
    private SponsorHomeFragment f1;
    private SponsorMessageFragment f2;
    private SponsorManagementFragment f3;
    private SponsorInformationFragment f4;

    private RadioGroup radioGroup;

    private FrameLayout fm;
    private ImageView img;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_sponsor_main_ui);

        initData();
        initView();
        initEvent();

        //设置当前Activity不能够滑动返回
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);
    }

    private void initData() {
        if(f1==null) {
            f1 = SponsorHomeFragment.newInstance("首页");
        }
        if(f2==null){
            f2 = SponsorMessageFragment.newInstance("消息");
        }
        if(f3==null){
            f3 = SponsorManagementFragment.newInstance("管理");
        }
        if(f4==null) {
            f4 = SponsorInformationFragment.newInstance("我的");
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

    //UI组件初始化与事件绑定
    private void initView() {
        administrate = (RadioButton) findViewById(R.id.administrate);
        home = (RadioButton) findViewById(R.id.home);
        locate = (RadioButton) findViewById(R.id.locate);
        msg = (RadioButton) findViewById(R.id.msg);
        my = (TipButton) findViewById(R.id.my);

        fm = (FrameLayout) findViewById(R.id.pan_bg);
        img = (ImageView) findViewById(R.id.img);

        radioGroup = (RadioGroup) findViewById(R.id.rd_group);

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
    }

    private void initEvent() {
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(this);

        //初始化默认页面
        setDefaultFragment();

        //发布按钮点击事件
        fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        IntentSkipUtil.skipToNextActivity(view.getRootView().getContext(),PublishSActivity.class);
                    }
                }).start();
            }
        });

    }

    /**
     * 设置默认选择项
     */
    private void setDefaultFragment() {
        msg.setChecked(true);
        mViewPager.setCurrentItem(0,false);
        my.setTipOn(true);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch(checkedId){
            case R.id.home:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.msg:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.administrate:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.my:
                mViewPager.setCurrentItem(3, false);
                my.setTipOn(false);
                break;
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mViewPager.setCurrentItem(0, false);
                home.setChecked(true);
                break;
            case 1:
                mViewPager.setCurrentItem(1, false);
                msg.setChecked(true);
                break;
            case 2:
                mViewPager.setCurrentItem(2, false);
                administrate.setChecked(true);
                break;
            case 3:
                mViewPager.setCurrentItem(3, false);
                my.setChecked(true);
                my.setTipOn(false);
                break;
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 重写退出返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.ACTION_DOWN) {
            // 需要处理
            //return true;//屏蔽back键(不响应back键)
            //show()方法决定是否display

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setInverseBackgroundForced(true);
            dialog.setTitle("注销");
            dialog.setMessage("你确定要退出当前程序？");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LogoutThread logoutThread = new LogoutThread(SponsorMainUIActivity.this,
                                    HttpPath.getUserLogOutPath());
                            logoutThread.setDataGetListener(new DataGetInterface() {
                                @Override
                                public void finishWork(Object obj) {
                                    if(obj instanceof ResultType) {
                                        ResultType result = (ResultType) obj;
                                        if(Integer.parseInt(result.getStatus()) == 1) {
                                            ActivityManager.getInstance().finishAllActivities();
                                            finish();
                                        }
                                        ToastUtil.getInstance().showToastInThread(SponsorMainUIActivity.this,
                                                result.getMessage());
                                    } else {
                                        ToastUtil.getInstance().showToastInThread(SponsorMainUIActivity.this,
                                                obj.toString());
                                    }
                                }

                                @Override
                                public void interrupt(Exception e) {
                                    //添加网络错误处理
                                    ToastUtil.getInstance().showToastInThread(SponsorMainUIActivity.this,
                                            ErrorHandleUtil.ExceptionToStr(e,SponsorMainUIActivity.this));
                                }
                            });
                            logoutThread.start();
                        }
                    });
            dialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            //可以如此，也可以直接 用dialog 来执行show()
            AlertDialog apk = dialog.create();
            apk.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onGroupListFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAdministrateSpreadFragmentInteraction(Uri uri) {

    }
}

