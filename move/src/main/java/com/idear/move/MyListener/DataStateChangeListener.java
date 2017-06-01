package com.idear.move.MyListener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by user on 2017/5/17.
 * 继承自RecyclerView.OnScrollListener，一：可以监听到是否滑动到页面最低部。二：滑动时停止加载图片
 */

public class DataStateChangeListener extends RecyclerView.OnScrollListener {

    /**
     * 当前RecyclerView类型
     */
    private  DataStateChangeListener.LayoutManagerType layoutManagerType;
    /**
     * 最后一个的位置
     */
    private  int[] lastPositions;
    /**
     * 最后一个可见的item的位置
     */
    private  int lastVisibleItemPosition;
    private  int firstVisibleItemPosition;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        switch (getLayoutManagerType(recyclerView)) {
            case LinearLayout:
                if(getFirstCompletelyVisibleItemPosition(recyclerView)==0) {
                    onRefreshPage(recyclerView);
                }
                break;
            case GridLayout:
                if(recyclerView.getChildAt(0).getY()==0 && getFirstCompletelyVisibleItemPosition(recyclerView)==0) {
                    onRefreshPage(recyclerView);
                }
                break;
            case StaggeredGridLayout:
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int flag[] = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
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
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        //到达底部的判定
        if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                (getLastVisibleItemPosition(recyclerView)) >= totalItemCount - 1)) {
            onLoadNextPage(recyclerView);
        }
        //到了停止刷新的方法
        if(visibleItemCount > 0 && (newState == RecyclerView.SCROLL_STATE_IDLE ) &&
                        getFirstVisibleItemPosition(recyclerView) == 0&&
                getFirstCompletelyVisibleItemPosition(recyclerView) !=0) {
            onStopRefreshPage(recyclerView);
        }
    }

    public void onLoadNextPage(final View view) {

    }

    public void onRefreshPage(final View view) {

    }

    public void onStopRefreshPage(final View view) {

    }

    private int getFirstVisibleItemPosition(RecyclerView recyclerView) {
        layoutManagerType = getLayoutManagerType(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        switch (layoutManagerType) {
            case LinearLayout:
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case GridLayout:
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                firstVisibleItemPosition = 0;
                break;
        }
        return firstVisibleItemPosition;
    }

    private int getFirstCompletelyVisibleItemPosition(RecyclerView recyclerView) {
        layoutManagerType = getLayoutManagerType(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        switch (layoutManagerType) {
            case LinearLayout:
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                break;
            case GridLayout:
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                firstVisibleItemPosition = 0;
                break;
        }
        return firstVisibleItemPosition;
    }

    public int getLastCompletelyVisibleItemPosition(RecyclerView recyclerView) {
        layoutManagerType = getLayoutManagerType(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        switch (layoutManagerType) {
            case LinearLayout:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                break;
            case GridLayout:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }
        return lastVisibleItemPosition;
    }
    public int getLastVisibleItemPosition(RecyclerView recyclerView) {
        layoutManagerType = getLayoutManagerType(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        switch (layoutManagerType) {
            case LinearLayout:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GridLayout:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }
        return lastVisibleItemPosition;
    }


    private  DataStateChangeListener.LayoutManagerType getLayoutManagerType(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = DataStateChangeListener.LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = DataStateChangeListener.LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = DataStateChangeListener.LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
        return layoutManagerType;
    }

    /**
     * 取数组中最大值
     *
     * @param lastPositions
     * @return
     */
    private  int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

}
