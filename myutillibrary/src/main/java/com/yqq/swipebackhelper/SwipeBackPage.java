package com.yqq.swipebackhelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;

/**
 * 每个滑动页面具体类
 */
public class SwipeBackPage {
    //仅为判断是否需要将mSwipeBackLayout注入进去
    private boolean mEnable = true;
    private boolean mRelativeEnable = false;

    public Activity mActivity;
    public SwipeBackLayout mSwipeBackLayout;//滑动返回布局
    public RelateSlider slider;//滑动效果具体实现类
    public SwipeBackPage(Activity activity){
        this.mActivity = activity;
    }

    //页面的回调用于配置滑动效果
    public void onCreate(){
        //设置背景为透明
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        mSwipeBackLayout = new SwipeBackLayout(mActivity);
        mSwipeBackLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        slider = new RelateSlider(this);
    }

    public void onPostCreate(){
        handleLayout();
    }


    @TargetApi(11)
    public SwipeBackPage setSwipeRelateEnable(boolean enable) {
        mRelativeEnable = enable;
        slider.setEnable(enable);
        return this;
    }

    //设置相对位移
    public SwipeBackPage setSwipeRelateOffset(int offset) {
        slider.setOffset(offset);
        return this;
    }

    //设置layout是否可以滑动
    public SwipeBackPage setSwipeBackEnable(boolean enable) {
        mEnable = enable;
        mSwipeBackLayout.setEnableGesture(enable);
        handleLayout();
        return this;
    }

    private void handleLayout(){
        if (mEnable||mRelativeEnable){
            mSwipeBackLayout.attachToActivity(mActivity);
        } else {
            mSwipeBackLayout.removeFromActivity(mActivity);
        }
    }

    //可滑动的范围。百分比。200表示为左边200px的屏幕
    public SwipeBackPage setSwipeEdge(int swipeEdge){
        mSwipeBackLayout.setEdgeSize(swipeEdge);
        return this;
    }

    //可滑动的范围。百分比。0.2表示为左边20%的屏幕
    public SwipeBackPage setSwipeEdgePercent(float swipeEdgePercent){
        mSwipeBackLayout.setEdgeSizePercent(swipeEdgePercent);
        return this;
    }

    //对横向滑动手势的敏感程度。0为迟钝 1为敏感
    public SwipeBackPage setSwipeSensitivity(float sensitivity){
        mSwipeBackLayout.setSensitivity(mActivity, sensitivity);
        return this;
    }

    //底层阴影颜色
    public SwipeBackPage setScrimColor(int color){
        mSwipeBackLayout.setScrimColor(color);
        return this;
    }

    //触发关闭Activity百分比
    public SwipeBackPage setClosePercent(float percent){
        mSwipeBackLayout.setScrollThreshold(percent);
        return this;
    }

    public SwipeBackPage setDisallowInterceptTouchEvent(boolean disallowIntercept){
        mSwipeBackLayout.setDisallowInterceptTouchEvent(disallowIntercept);
        return this;
    }

    public SwipeBackPage addListener(SwipeListener listener){
        mSwipeBackLayout.addSwipeListener(listener);
        return this;
    }

    public SwipeBackPage removeListener(SwipeListener listener){
        mSwipeBackLayout.removeSwipeListener(listener);
        return this;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void scrollToFinishActivity() {
        mSwipeBackLayout.scrollToFinishActivity();
    }

}
