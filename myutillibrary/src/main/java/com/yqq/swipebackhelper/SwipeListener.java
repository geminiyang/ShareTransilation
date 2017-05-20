package com.yqq.swipebackhelper;

/**
 * 滑动时监听类的接口
 */
public interface SwipeListener {
        void onScroll(float percent, int px);
        void onEdgeTouch();
        /**
         * Invoke when scroll percent over the threshold for the first time
         * 第一次超过滚动百分比的阈值时候调用
         */
        void onScrollToClose();
    }