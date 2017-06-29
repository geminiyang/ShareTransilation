package com.idear.move.Thread;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.idear.move.Adapter.BannerPagerAdapter;
import com.idear.move.Fragment.UserHomeFragment;
import com.idear.move.POJO.HomeInitialItem;
import com.idear.move.R;
import com.idear.move.myWidget.IndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by user on 2017/4/27.
 * 封装Fragment中的初始化操作
 */

public class MyHomeLoadingAsyncTask extends AsyncTask<Void,Void,Void> {

    /**
     * 轮播图
     */
    private ViewPager mViewPager;
    /**
     * 轮播图自动轮播消息
     */
    public static final int AUTO_BANNER_CODE = 0X1001;
    /**
     * 当前轮播位置
     */
    private int mBannerPosition;
    /**
     * 自动轮播计时器
     */
    private Timer timer = new Timer();
    /**
     * 轮播任务
     */
    private BannerTimerTask bannerTimerTask;
    /**
     * 是否点击轮播图
     */
    private boolean isClickBanner = false;
    private static final int CIRCLE_TIME = 20000;//设置循环时间
    /**
     * 定时器handler
     */
    private Handler bannerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //当用户点击时不进行轮播
            if(!isClickBanner){
                //获取当前位置
                mBannerPosition = mViewPager.getCurrentItem();
                //更换轮播图(向后)
                mBannerPosition = (mBannerPosition + 1 )% BannerPagerAdapter.BANNER_MAX_SIZE;
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
    private List<HomeInitialItem> pictureList = new ArrayList<>();

    /**
     * 上下文参数
     */
    private Context context;
    public MyHomeLoadingAsyncTask(Context context,View view,List<HomeInitialItem> list) {
        this.context = context;
        this.pictureList = list;
        initView(view);
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public void initView(View view){
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_banner);
        mIndicatorView = (IndicatorView) view.findViewById(R.id.indicator_view_banner);
        bannerPagerAdapter = new BannerPagerAdapter(context,pictureList);
        mViewPager.setAdapter(bannerPagerAdapter);
        mIndicatorView.setViewPager(pictureList.size(),mViewPager);
        // 设置默认起始位置,使开始可以向左边滑动
        mViewPager.setCurrentItem(pictureList.size() * 100);
        //设置监听器
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

    public void quitBannerTask(){
        if(bannerTimerTask!=null){
            bannerTimerTask.cancel();
            bannerTimerTask=null;
        }
    }

}
