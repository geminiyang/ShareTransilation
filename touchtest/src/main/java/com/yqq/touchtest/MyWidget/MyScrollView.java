package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * ScrollView并没有实现滚动监听，所以我们必须自行实现对ScrollView的监听，
 * 我们很自然的想到在onTouchEvent()方法中实现对滚动Y轴进行监听
 * ScrollView的滚动Y值进行监听
 * Created by user on 2017/2/5.
 */
public class MyScrollView extends ScrollView {
    //视图滑动接口
    private OnScrollListener onScrollListener;

    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    public MyScrollView(Context context) {
        super(context, null);
    }
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyScrollView.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                //handler循环更新当前Y值
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            //刷新当前的ScrollY值
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }

        };

    };
    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，
     * 抬手那一瞬间需要20ms防止处理速度跟不上导致视图变形
     * 在handler处理，MyScrollView滑动的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(onScrollListener != null){
            //获取当前视图的ScrollY值然后赋值给lastScrollY
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }

        switch(ev.getAction()){
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动的回调接口，暴露外面供外部调用
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        void onScroll(int scrollY);
    }
}
