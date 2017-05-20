package com.yqq.swipebackhelper;

import android.os.Build;

/**
 * 统一设置activity的相对平移效果的类
 */
public class RelateSlider implements SwipeListener {
    public SwipeBackPage curPage;
    private static final int DEFAULT_OFFSET = 40;
    private int offset = 500;

    public RelateSlider(SwipeBackPage curActivity) {
        this.curPage = curActivity;
        //如需添加监听器，请在此构造器实现相关操作
        //curPage.addListener(this);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setEnable(boolean enable){
        if (enable)curPage.addListener(this);
        else curPage.removeListener(this);
    }

    @Override
    public void onScroll(float percent, int px) {
        //这一步是如何实现的
        if (Build.VERSION.SDK_INT>11) {
            SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
            if (page != null) {
                //动态设置前一个page的位置,offset是一个初始位置
                page.getSwipeBackLayout().setX(Math.min(-offset * Math.max(1 - percent,0) + DEFAULT_OFFSET,0));
                if (percent == 0 ) {
                    page.getSwipeBackLayout().setX(0);
                }
            }
        }
    }

    @Override
    public void onEdgeTouch() {

    }

    @Override
    public void onScrollToClose() {
        //获取到栈中的前一个page
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (Build.VERSION.SDK_INT>11) {
            if (page != null) page.getSwipeBackLayout().setX(0);
        }
    }
}
