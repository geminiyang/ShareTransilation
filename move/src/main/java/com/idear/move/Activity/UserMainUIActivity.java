package com.idear.move.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.idear.move.Dummy.GroupListContent;
import com.idear.move.Dummy.UserListContent;
import com.idear.move.Fragment.DynamicsFragment;
import com.idear.move.Fragment.GroupListFragment;
import com.idear.move.Fragment.MessageFragment;
import com.idear.move.Fragment.MyHomeFragment;
import com.idear.move.Fragment.UserInformationFragment;
import com.idear.move.Fragment.UserListFragment;
import com.idear.move.R;
import com.idear.move.Service.ActivityManager;
import com.idear.move.myWidget.TipButton;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ObjectAnimatorUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;
import com.yqq.swipebackhelper.SwipeBackHelper;

import java.util.ArrayList;
import java.util.List;

public class UserMainUIActivity extends BaseActivity implements
        RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener,
        UserListFragment.OnListFragmentInteractionListener,
        GroupListFragment.OnListFragmentInteractionListener {
    private RadioButton dynamic,home,locate,msg;
    private TipButton my;
    private DynamicsFragment f3;
    private MessageFragment f2;
    private UserInformationFragment f4;
    private MyHomeFragment f1;

    private RadioGroup radioGroup;

    private FrameLayout fm;
    private ImageView img;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    private PopupWindow popup;
    private FrameLayout fl_bt1,fl_bt2,fl_bt3;
    private TextView tv1,tv2,tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first_main_ui);

        initData();
        initView();
        initEvent();

        //设置当前Activity不能够滑动返回
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);
    }

    private void initData() {
        if(f1==null) {
            f1 = MyHomeFragment.newInstance("首页");
        }
        if(f2==null){
            f2 = MessageFragment.newInstance("消息");
        }
        if(f3==null){
            f3 = DynamicsFragment.newInstance("动态");
        }
        if(f4==null) {
            f4 = UserInformationFragment.newInstance("我的");
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
        dynamic = (RadioButton) findViewById(R.id.dynamic);
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
        mViewPager.addOnPageChangeListener(this);

        //初始化默认页面
        setDefaultFragment();

        //发布按钮点击事件
        fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                fm.post(new Runnable() {
                    @Override
                    public void run() {
                        showWindow(view);
                    }
                });
            }
        });

    }

    /**
     * 点击+号按钮后的触发事件 自动逸动画
     * @param parent
     */
    private void showWindow(View parent) {

        ObjectAnimatorUtil.rotateAnimation(img,0,315f);

        if(popup == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = layoutInflater.inflate(R.layout.popup_layout, null);

            fl_bt1 = (FrameLayout) view.findViewById(R.id.fl_bt1);
            fl_bt2 = (FrameLayout) view.findViewById(R.id.fl_bt2);
            fl_bt3 = (FrameLayout) view.findViewById(R.id.fl_bt3);
            tv1 = (TextView) view.findViewById(R.id.tv1);
            tv2 = (TextView) view.findViewById(R.id.tv2);
            tv3 = (TextView) view.findViewById(R.id.tv3);

            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IntentSkipUtil.skipToNextActivity(view.getRootView().getContext(),PublishRActivity.class);
                        }
                    },1000);

                    popup.dismiss();
                    //添加旋转效果
                    ObjectAnimatorUtil.rotateAnimation(img,315f,0);
                }
            });
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IntentSkipUtil.skipToNextActivity(view.getRootView().getContext(),PublishRFActivity.class);
                        }
                    },1000);

                    popup.dismiss();
                    //添加旋转效果
                    ObjectAnimatorUtil.rotateAnimation(img,315f,0);
                }
            });
            tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv3.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IntentSkipUtil.skipToNextActivity(view.getRootView().getContext(),PublishFActivity.class);
                        }
                    },1000);

                    popup.dismiss();
                    //添加旋转效果
                    ObjectAnimatorUtil.rotateAnimation(img,315f,0);
                }
            });


            // 创建一个PopupWidow对象
            popup = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,130);
            if(Build.VERSION.SDK_INT>=21){
                popup.setElevation(10);
            }

//            popup.setEnterTransition();
//            popup.setExitTransition();

            popup.setAnimationStyle(R.style.PopupAnimation);

            // 设置焦点在弹窗上
            popup.setFocusable(true);
            // 设置允许在外点击消失
            popup.setOutsideTouchable(true);
            // 设置弹窗消失事件监听
            popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //设置按钮为正常加号
                    //img.setImageResource(R.mipmap.plus);
                    //添加旋转效果
                    ObjectAnimatorUtil.rotateAnimation(img,315f,0);
                }
            });

            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popup.dismiss();
                        //添加旋转效果
                        ObjectAnimatorUtil.rotateAnimation(img,315f,0);
                        return true;
                    }
                    return false;
                }
            });
        }

        //设置显示位置
        if(!popup.isShowing()) {
            //相对于屏幕
            popup.showAsDropDown(parent, 0,0,Gravity.BOTTOM);
        }
    }
    /**
     * 对应更多操作
     * @param context
     * @param parent
     * @param layoutId
     */
    private void showPopupWindow(final Context context, View parent,int layoutId) {
        View contentView = LayoutInflater.from(context).inflate(layoutId,null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        contentView.findViewById(R.id.rl_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(UserMainUIActivity.this,FriendAddActivity.class);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.rl_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(UserMainUIActivity.this,DynamicPublishingActivity.class);
                popupWindow.dismiss();
            }
        });

        //设置入场动画
        popupWindow.setAnimationStyle(R.style.PopupSlideFromRightAnimation);
        // 设置焦点在弹窗上
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”,还是点击外部区域，也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        //设置显示位置
        if(!popupWindow.isShowing()) {
            //相对于一个参照物
            popupWindow.showAsDropDown(parent);
            //相对屏幕，显示在指定位置
            //popupWindow.showAtLocation(parent,Gravity.CENTER,0,0);
        }
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
            case R.id.dynamic:
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
                dynamic.setChecked(true);
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
                            ActivityManager.getInstance().finishAllActivities();
                            finish();
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
    public void onUserListFragmentInteraction(UserListContent.UserList item) {
        IntentSkipUtil.skipToNextActivity(this,UserChatActivity.class);
    }

    @Override
    public void onGroupListFragmentInteraction(GroupListContent.GroupList item) {
        IntentSkipUtil.skipToNextActivity(this,UserChatActivity.class);
    }
}

