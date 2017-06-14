package com.yqq.idear;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by user on 2017/5/17.
 * 继承自RecyclerView.OnScrollListener，一：可以监听到是否滑动到页面最低部。二：滑动时停止加载图片
 */

public class DataStateChangeCheck extends RecyclerView.OnScrollListener {

    public interface LoadDataListener {
        void onLoadNextPage(final View view);
        void onRefreshPage(final View view);
    }
    /**
     * 当前RecyclerView类型
     */
    private  DataStateChangeCheck.LayoutManagerType layoutManagerType;
    /**
     * 最后一个的位置
     */
    private  int[] lastPositions;
    /**
     * 最后一个可见的item的位置
     */
    private  int lastVisibleItemPosition;
    /**
     * 最前一个可见的item的位置
     */
    private  int firstVisibleItemPosition;
    private boolean isTop = false;
    /**
     * 监听器接口
     */
    private DataStateChangeCheck.LoadDataListener mListener;
    private RecyclerView mRecyclerView;

    public DataStateChangeCheck(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        switch (getLayoutManagerType()) {
            case LinearLayout:
                if(getFirstCompletelyVisibleItemPosition()==0) {
                    if(recyclerView.canScrollVertically(-1)) {
                        //到达顶部判定
                        mListener.onRefreshPage(recyclerView);
                    }
                }
                break;
            case GridLayout:
                if(recyclerView.getChildAt(0).getY()==0 && getFirstCompletelyVisibleItemPosition()==0) {
                    if(recyclerView.canScrollVertically(-1)) {
                        //到达顶部判定
                        mListener.onRefreshPage(recyclerView);
                    }
                }
                break;
            case StaggeredGridLayout:
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int flag[] = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
                //达到这个条件就说明滑到了顶部
                if(recyclerView.getChildAt(0).getY()==0f && flag[0]==0&&
                        recyclerView.canScrollVertically(-1)) {
                    mListener.onRefreshPage(recyclerView);
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
                (getLastVisibleItemPosition()) >= totalItemCount - 1)) {
            mListener.onLoadNextPage(recyclerView);
        }

//        //在到达顶部的途中
//        if(visibleItemCount > 0 && (newState == RecyclerView.SCROLL_STATE_IDLE ) &&
//                        getFirstVisibleItemPosition() == 0&&
//                getFirstCompletelyVisibleItemPosition() !=0) {
//            mListener.onDragToTopPage(recyclerView);
//        }
    }

    /**
     * 必须设置加载数据监听器
     * @param listener  Activity中赋予特定行为的监听器
     */
    public void setLoadDataListener(DataStateChangeCheck.LoadDataListener listener) {
        this.mListener = listener;
    }

    /**
     * @return  获取到对应的position
     */
    public int getFirstVisibleItemPosition() {
        layoutManagerType = getLayoutManagerType();

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
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

    /**
     * 获取最前一个完全可见的Item位置
     * @return  获取到对应的position
     */
    public int getFirstCompletelyVisibleItemPosition() {
        layoutManagerType = getLayoutManagerType();

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
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

    /**
     * 获取最后一个完全可见的Item位置
     * @return  获取到对应的position
     */
    public int getLastCompletelyVisibleItemPosition() {
        layoutManagerType = getLayoutManagerType();

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
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

    /**
     * 获取最后一个可见的Item位置
     * @return  获取到对应的position
     */
    public int getLastVisibleItemPosition() {
        layoutManagerType = getLayoutManagerType();

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
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


    /**
     * 获取布局管理器类型
     * @return  获取到对应的类型
     */
    private  DataStateChangeCheck.LayoutManagerType getLayoutManagerType() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = DataStateChangeCheck.LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = DataStateChangeCheck.LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = DataStateChangeCheck.LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
        return layoutManagerType;
    }

    /**
     * 取数组中最大值
     * @param lastPositions
     * @return  获取到对应的position
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

    /**
     * 布局管理器枚举类
     */
    private enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }
}
