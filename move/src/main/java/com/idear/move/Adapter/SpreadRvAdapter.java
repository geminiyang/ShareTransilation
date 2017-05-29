package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.POJO.SpreadDataModel;
import com.idear.move.R;
import com.idear.move.MyWidget.UpdateTimeTextView;

import java.util.List;


/**
 * Created by user on 2016/12/12.
 */
public class SpreadRvAdapter extends RecyclerView.Adapter<SpreadRvAdapter.ViewHolder> {

    private Context mContext;
    private List<SpreadDataModel> modelList;
    private LayoutInflater mInflater;

    public SpreadRvAdapter(Context context , List<SpreadDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
       mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public SpreadRvAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.spread_cardview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SpreadRvAdapter.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        SpreadDataModel u = (SpreadDataModel) modelList.get(position);
        holder.mTitle.setText(u.title);
        holder.mTime.setText(u.time);
        holder.mVisitNum.setText(u.visitNum);
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
        private TextView mTitle,mVisitNum;
        private UpdateTimeTextView mTime;
        private ImageView mImageDetail;

        private ViewHolder(View v) {
            super(v);
            mTitle = (TextView)v.findViewById(R.id.info_title);
            mVisitNum = (TextView)v.findViewById(R.id.info_visit_num);
            mImageDetail = (ImageView)v.findViewById(R.id.pic);
            mTime = (UpdateTimeTextView) v.findViewById(R.id.info_time);
        }
    }

}
