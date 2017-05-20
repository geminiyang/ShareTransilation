package com.yqq.materialtest;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by user on 2016/12/12.
 */

public class MyAdapterOne extends RecyclerView.Adapter<MyAdapterOne.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    //为每个数据项的视图提供参考,复杂的数据项可能需要超过一个视图，并且您提供对视图保持器中的数据项的所有视图的访问

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //在这个案例中每一个数据项知识一个字符串
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 提供一个稳定的构造器
    public MyAdapterOne(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    //创建新的视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        //这个方法适用于View就是一个布局
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.my_text_view, parent, false);
//        TextView mTextView = (TextView) v;

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carview_layout, parent, false);

        TextView mTextView = (TextView) v.findViewById(R.id.info_text);

        // set the view's size, margins, paddings and layout parameters
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,120);
//        mTextView.setLayoutParams(params);
//        params.setMargins(16,16,16,16);
        mTextView.setPadding(1,0,0,0);
        mTextView.setBackgroundColor(Color.rgb(87,207,251));
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.argb(98,207,12,23));
        //设置阴影
        //v.setElevation(6.0f);
        mTextView.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);

        ViewHolder vh = new ViewHolder(mTextView);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    //替换视图的内容
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //获取特定位置的数据项，用特定元素取代试图中的内容
        holder.mTextView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    //返回数据集的大小
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
