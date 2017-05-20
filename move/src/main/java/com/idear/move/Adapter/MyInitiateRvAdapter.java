package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.POJO.MyInitiateDataModel;
import com.idear.move.R;

import java.util.List;


/**
 * Created by user on 2016/12/12.
 */
public class MyInitiateRvAdapter extends RecyclerView.Adapter<MyInitiateRvAdapter.ViewHolder> {

    private Context mContext;
    private List<MyInitiateDataModel> modelList;
    private LayoutInflater mInflater;

    public MyInitiateRvAdapter(Context context , List<MyInitiateDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public MyInitiateRvAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.myinitiate_cardview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyInitiateRvAdapter.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        MyInitiateDataModel u = (MyInitiateDataModel) modelList.get(position);
        holder.mState.setText(u.state);
        //设置图像
        holder.mImageDetail.setImageDrawable(mContext.getResources().getDrawable(u.getImageResourceId(mContext)));
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
        private TextView mState;
        private ImageView mImageDetail;

        private ViewHolder(View v) {
            super(v);
            mState= (TextView)v.findViewById(R.id.info_state);
            mImageDetail = (ImageView)v.findViewById(R.id.pic);
        }
    }

}
