package com.yqq.materialtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by user on 2016/12/12.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List mUser;

        private Context mContext;

        public MyAdapter(Context context , List users)
        {
            this.mContext = context;
            this.mUser = users;
        }

        // 重写的自定义ViewHolder
        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView mTextView;

            public ImageView mImageView;

            public ViewHolder(View v)
            {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.info_text);
                mImageView = (ImageView) v.findViewById(R.id.pic);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i )
        {
            // 给ViewHolder设置布局文件
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carview_layout, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // 给ViewHolder设置元素
            User u = (User) mUser.get(position);
            holder.mTextView.setText(u.Name);
            holder.mImageView.setImageDrawable(mContext.getDrawable(u.getImageResourceId(mContext)));
        }


    @Override
    public int getItemCount()
    {
        // 返回数据总数
        return mUser == null ? 0 : mUser.size();
    }

}
