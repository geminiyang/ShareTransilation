package com.yqq.horizonalscrollviewtest;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/4/26.
 * 自定义HorizontalScrollView思想：
 *1、首先根据屏幕的大小和Item的大小，计算可以一个屏幕最多可以加载多少个Item，然后加载该数量Item。
 *2、当用户右滑（从右向左），滑动到一定距离时，加载下一张，删除第一张
 *3、当用户左滑（从左向右），滑动到一定距离时，加载上一张，删除最后一张
 */

public class MyHorizontalScrollView extends HorizontalScrollView implements View.OnClickListener{
    /**
     * 图片滚动时的回调接口
     */
    public interface CurrentImageChangeListener {
        void onCurrentImgChanged(int position, View viewIndicator);
    }

    /**
     * 条目点击时的回调
     */
    public interface OnItemClickListener {
        void onClick(View view, int pos);
    }

    private CurrentImageChangeListener mListener;
    private OnItemClickListener mOnClickListener;

    private static final String TAG = "MyHorizontalScrollView";

    /**
     * HorizontalListView中的LinearLayout
     */
    private LinearLayout mContainer;//HorizontalListView中的LinearLayout
    private int mChildWidth;//子元素的宽度
    private int mChildHeight;//子元素的高度
    private int mCurrentIndex;//当前最后那张图片的index
    private int mFirstIndex=0;//当前第一张图片的index
    private View mFirstView;//当前第一个视图
    /**
     * 数据适配器
     */
    private HorizontalScrollViewAdapter mAdapter;

    private int mCountOneScreen;//每个屏幕最多显示个数
    private int mScreenWidth;//屏幕宽度

    /**
     * 保存View与位置的键值对
     */
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context,attrs);
        // 获得屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
    }

    /**
     * 加载下一张图片
     */
    protected void loadNextImg()
    {
        // 数组边界值计算,最后一张图片没有下一张
        if (mCurrentIndex == mAdapter.getCount() - 1) {
            return;
        }
        //移除第一张图片，且将水平滚动位置置0
        scrollTo(0, 0);
        mViewPos.remove(mContainer.getChildAt(0));//清除k-v表
        mContainer.removeViewAt(0);

        //获取下一张图片，并且设置onclick事件，且加入容器中
        View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
        view.setOnClickListener(this);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);

        //当前第一张图片下标
        mFirstIndex++;
        //如果设置了滚动监听则触发
        if (mListener != null) {
            notifyCurrentImgChanged();
        }

    }
    /**
     * 加载前一张图片
     */
    protected void loadPreImg()
    {
        //如果当前已经是第一张，则返回
        if (mFirstIndex == 0) return;
        //获得当前应该显示为第一张图片的下标
        int index = mCurrentIndex - mCountOneScreen;
        if (index >= 0) {
            //mContainer = (LinearLayout) getChildAt(0);
            //移除最后一张
            int oldViewPos = mContainer.getChildCount() - 1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);

            //将此View放入第一个位置
            View view = mAdapter.getView(index, null, mContainer);
            mViewPos.put(view, index);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);
            //水平滚动位置向左移动view的宽度个像素
            scrollTo(mChildWidth, 0);
            //当前位置--，当前第一个显示的下标--
            mCurrentIndex--;
            mFirstIndex--;
            //监听器，触发设置背景色为蓝色
            if (mListener != null) {
                notifyCurrentImgChanged();
            }
        }
    }

    /**
     * 滑动时的回调
     */
    public void notifyCurrentImgChanged()
    {
        //先清除所有的背景色，点击时会设置为蓝色
        for (int i = 0; i < mContainer.getChildCount(); i++)
        {
            //将该布局下的所有子视图设置背景色
            mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        //调用特例的回调事件，当前第一个标签下对应的View的背景色设置为蓝色
        mListener.onCurrentImgChanged(mFirstIndex, mContainer.getChildAt(0));
    }

    /**
     * 初始化数据，设置数据适配器（为布局添加第一个元素，为了计算出该显示多少个）
     * @param mAdapter
     */
    public void initDatas(HorizontalScrollViewAdapter mAdapter)
    {

        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        // 获得适配器中第一个View
        final View view = mAdapter.getView(0, null, mContainer);
        mContainer.addView(view);

        // 强制计算当前View的宽和高
        if (mChildWidth == 0 && mChildHeight == 0)
        {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = view.getMeasuredWidth();
            Log.e(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());

            // 计算每次加载多少个View item,向下取整再加2
            mCountOneScreen = mScreenWidth / mChildWidth + 2;

            Log.e(TAG, "mCountOneScreen = " + mCountOneScreen + " ,mChildWidth = " + mChildWidth);


        }
        //初始化第一屏幕的元素(添加四个元素)
        initFirstScreenChildren(mCountOneScreen);
    }

    /**
     * 加载第一屏的View
     *
     * @param mCountOneScreen
     */
    public void initFirstScreenChildren(int mCountOneScreen)
    {
        mContainer = (LinearLayout) getChildAt(0);//获取第一层子布局
        mContainer.removeAllViews();//清除已有缓存
        mViewPos.clear();//清除Map中残留的k-v值

        for (int i = 0; i < mCountOneScreen; i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.setOnClickListener(this);//设置监听器
            mContainer.addView(view);
            mViewPos.put(view, i);//k，v对存贮视图和index
            mCurrentIndex = i;//记录当前索引
        }

        if (mListener != null) {
            notifyCurrentImgChanged();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, getScrollX() + "");
                //Toast.makeText(getContext(),getScrollX()+"",Toast.LENGTH_SHORT).show();
                //getScrollX() 就是当前view的左上角相对于母视图的左上角的X轴偏移量
                //这就是我们视图里为什么永远都是滑过第一个图片长度的时候就移动到下一个视图
                //相当于当前屏幕的左上角为（0，0）
                int scrollX = getScrollX();
                // 如果当前scrollX为view的宽度,加载下一张,移除第一张
                if (scrollX >= mChildWidth) {
                    loadNextImg();
                }
                // 如果当前scrollX = 0,往前设置一张,移除最后一张
                if (scrollX == 0) {
                    loadPreImg();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onClick(View v)
    {
        if (mOnClickListener != null)
        {
            //每一次新的点击要将背景改成初始颜色
            for (int i = 0; i < mContainer.getChildCount(); i++)
            {
                mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
            mOnClickListener.onClick(v, mViewPos.get(v));//将特例抽取出来，当前点击的视图的点击
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setCurrentImageChangeListener(CurrentImageChangeListener mListener) {
        this.mListener = mListener;
    }

}
