package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.POJO.DataModel;
import com.idear.move.R;

import java.util.List;


/**
 * Created by user on 2016/12/12.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context mContext;
    private List<DataModel> modelList;

    public MyAdapter(Context context , List<DataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 给ViewHolder设置布局文件
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        View v = mInflater.inflate(R.layout.carview_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        DataModel u = (DataModel) modelList.get(position);
        holder.mTextView.setText(u.Name);
        //设置图像
        holder.mImageView.setImageDrawable(mContext.getResources().getDrawable(u.getImageResourceId(mContext)));
    }

    @Override
    public int getItemCount() {
        // 返回数据总数
        return modelList == null ? 0 : modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return modelList.get(position).type;
    }

    //重写的自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;

        private ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.info_text);
            mImageView = (ImageView)v.findViewById(R.id.pic);
        }
    }

}
