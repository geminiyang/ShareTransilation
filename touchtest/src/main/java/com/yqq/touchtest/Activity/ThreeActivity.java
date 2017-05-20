package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.yqq.touchtest.Util.BannerPagerAdapter;
import com.yqq.touchtest.Util.BannerTimerTask;
import com.yqq.touchtest.MyWidget.CircleView;
import com.yqq.touchtest.MyWidget.IndicatorView;
import com.yqq.touchtest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ThreeActivity extends Activity {

    private CircleView circleView=  null;
    /**
     * 绘图handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            circleView.startDraw(msg.what);
            circleView.invalidate();//进行重新绘制
        }
    };
    /**
     * 轮播图
     */
    private ViewPager mViewPager;
    /**
     * 轮播图自动轮播消息
     */
    public static final int AUTOBANNER_CODE = 0X1001;
    /**
     * 当前轮播位置
     */
    public int mBannerPosition;
    /**
     * 自动轮播计时器
     */
    public Timer timer = new Timer();
    /**
     * 轮播任务
     */
    private BannerTimerTask bannerTimerTask;
    /**
     * 是否点击轮播图
     */
    private boolean isClickBanner = false;
    private int CIRCLE_TIME = 5000;//设置循环时间
    /**
     * 定时器handler
     */
    Handler bannerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //当用户点击时不进行轮播
            if(!isClickBanner){
                //获取当前位置
            mBannerPosition = mViewPager.getCurrentItem();
                //更换轮播图(向后)
            mBannerPosition = (mBannerPosition + 1 )%bannerPagerAdapter.BANNER_MAX_SIZE;
                mViewPager.setCurrentItem(mBannerPosition);
            }
            return true;
        }
    });
    /**
     * 指示器
     */
    private IndicatorView mIndicatorView;
    /**
     * 适配器
     */
    private BannerPagerAdapter bannerPagerAdapter;
    /**
     * 图像列表
     */
    private List<Integer> pictureList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        findView();
        initData();
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_banner);
        mIndicatorView = (IndicatorView) findViewById(R.id.indicatorview_banner);
        bannerPagerAdapter = new BannerPagerAdapter(this,pictureList);
        mViewPager.setAdapter(bannerPagerAdapter);
        mIndicatorView.setViewPager(pictureList.size(),mViewPager);
        // 设置默认起始位置,使开始可以向左边滑动
        mViewPager.setCurrentItem(pictureList.size() * 100);
        mIndicatorView.setOnPageChangeListener(new IndicatorView.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||
                        event.getAction()==MotionEvent.ACTION_MOVE){
                    isClickBanner = true;
                } else {
                    isClickBanner = false;
                }
                return false;
            }
        });

        startBannerTimer();
    }

    private void startBannerTimer() {
        if(timer==null){
            timer = new Timer();
        }
        if(bannerTimerTask!=null){
            bannerTimerTask.cancel();
        }
        bannerTimerTask = new BannerTimerTask(bannerHandler);
        if(timer!=null&&bannerTimerTask!=null){
            //设置循环时间,第二个时间为延迟时间，第三个为持续时间
            timer.schedule(bannerTimerTask,CIRCLE_TIME,CIRCLE_TIME);
        }
    }

    private void initData() {
        pictureList.add(R.drawable.pic_one);
        pictureList.add(R.drawable.pic_two);
        pictureList.add(R.drawable.pic_three);
        pictureList.add(R.drawable.pic_four);
        pictureList.add(R.drawable.pic_five);
        pictureList.add(R.drawable.pic_six);
    }

    private void findView() {
        circleView = (CircleView) findViewById(R.id.circleview);
        //该逻辑只为演示效果，作用是得到两个百分比数字，进而得到对应的弧度值
        int radianOne = 72;
        int radianTwo = 252;
        //则
        circleView.setDemarcation(radianOne, radianTwo);
        new Thread(){
            public void run() {
                int i = 0;
                try {
                    sleep(200);
                    while(i<=360){
                        mHandler.sendEmptyMessage(i);
                        i+=1;
                        sleep(3);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 即时销毁，防止异常
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bannerTimerTask!=null){
            bannerTimerTask.cancel();
            bannerTimerTask=null;
        }
    }
}
