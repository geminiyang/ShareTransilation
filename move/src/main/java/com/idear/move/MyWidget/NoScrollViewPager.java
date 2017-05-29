package com.idear.move.MyWidget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不带滑动功能的ViewPager
 * Created by user on 2017/5/3.
 */

public class NoScrollViewPager extends ViewPager{

    private boolean unableScroll = false;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUnableScroll(boolean unableScroll) {
        this.unableScroll = unableScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(unableScroll) return false;
        else return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(unableScroll) return false;
        else return super.onInterceptTouchEvent(ev);
    }
}
