package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.POJO.AllActivityDataModel;
import com.idear.move.R;
import com.idear.move.myWidget.UpdateTimeTextView;

import java.util.List;


/**
 * Created by user on 2016/12/12.
 */
public class AllActivityRvAdapter extends RecyclerView.Adapter<AllActivityRvAdapter.ViewHolder> {

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;

    private Context mContext;
    private List<AllActivityDataModel> modelList;
    private LayoutInflater mInflater;

    public AllActivityRvAdapter(Context context , List<AllActivityDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public AllActivityRvAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.allactivity_cardview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AllActivityRvAdapter.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        AllActivityDataModel u = (AllActivityDataModel) modelList.get(position);
        holder.mTitle.setText(u.title);
        holder.mDescription.setText(u.description);
        if(u.type == AllActivityRvAdapter.TYPE_ONE) {
            holder.mPersonNum.setText(u.personNum);
            holder.mMoney.setVisibility(View.GONE);
        } else if(u.type == AllActivityRvAdapter.TYPE_TWO) {
            holder.mPersonNum.setVisibility(View.GONE);
            holder.mMoney.setText(u.money);
        } else if(u.type == AllActivityRvAdapter.TYPE_THREE){
            holder.mPersonNum.setText(u.personNum);
            holder.mMoney.setText(u.money);
        }
        holder.mTime.setText(u.time);
        holder.mUsername.setText(u.username);
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
        private TextView mTitle,mUsername,mVisitNum,mDescription,mPersonNum,mMoney;
        private UpdateTimeTextView mTime;
        private ImageView mImageDetail;

        private ViewHolder(View v) {
            super(v);
            mTitle = (TextView)v.findViewById(R.id.info_title);
            mDescription = (TextView)v.findViewById(R.id.info_description);
            mPersonNum = (TextView)v.findViewById(R.id.info_person_num);
            mMoney = (TextView)v.findViewById(R.id.info_money);
            mUsername = (TextView)v.findViewById(R.id.info_username);
            mVisitNum = (TextView)v.findViewById(R.id.info_visit_num);
            mImageDetail = (ImageView)v.findViewById(R.id.pic);
            mTime = (UpdateTimeTextView) v.findViewById(R.id.info_time);
        }
    }

}
