package com.idear.move.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.idear.move.MyListener.DataStateChangeListener;

/**
 * 作者:geminiyang on 2017/5/31.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class RecyclerViewUtil {

    /**
     * 当前RecyclerView类型
     */
    private static DataStateChangeListener.LayoutManagerType layoutManagerType;
    /**
     * 最后一个的位置
     */
    private static int[] lastPositions;
    /**
     * 最后一个可见的item的位置
     */
    private static int lastVisibleItemPosition;
    /**
     * 当前滑动的状态
     */
    private static int currentScrollState = 0;

    public static int getLastVisibleItemPosition(RecyclerView recyclerView) {
        layoutManagerType =getLayoutManagerType(recyclerView);

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

    public static DataStateChangeListener.LayoutManagerType getLayoutManagerType(RecyclerView recyclerView) {
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
    private static int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
