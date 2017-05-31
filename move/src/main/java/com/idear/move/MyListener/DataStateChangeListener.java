package com.idear.move.MyListener;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.idear.move.util.RecyclerViewUtil;

/**
 * Created by user on 2017/5/17.
 * 继承自RecyclerView.OnScrollListener，一：可以监听到是否滑动到页面最低部。二：滑动时停止加载图片
 */

public class DataStateChangeListener extends RecyclerView.OnScrollListener {

    private int currentScrollState = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        switch (RecyclerViewUtil.getLayoutManagerType(recyclerView)) {
            case LinearLayout:
                if(RecyclerViewUtil.getLastVisibleItemPosition(recyclerView)==0) {
                    onRefreshPage(recyclerView);
                }
                break;
            case GridLayout:
                if(recyclerView.getChildAt(0).getY()==0 && RecyclerViewUtil.getLastVisibleItemPosition(recyclerView)==0) {
                    onRefreshPage(recyclerView);
                }
                break;
            case StaggeredGridLayout:
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int flag[] = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
                //达到这个条件就说明滑到了顶部
                if(recyclerView.getChildAt(0).getY()==0f && flag[0]==0) {
                    onRefreshPage(recyclerView);
                }
                break;
        }


    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        currentScrollState = newState;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        //到达底部的判定
        if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                (RecyclerViewUtil.getLastVisibleItemPosition(recyclerView)) >= totalItemCount - 1)) {
            onLoadNextPage(recyclerView);
        }
    }

    public void onLoadNextPage(final View view) {

    }

    public void onRefreshPage(final View view) {

    }

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

}
