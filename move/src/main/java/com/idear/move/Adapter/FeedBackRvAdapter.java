package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idear.move.POJO.FeedBackDataModel;
import com.idear.move.POJO.SpreadDataModel;
import com.idear.move.R;
import com.idear.move.myWidget.RoundImageViewByXfermode;
import com.idear.move.myWidget.UpdateTimeTextView;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ScreenUtil;

import java.util.List;


public class FeedBackRvAdapter extends RecyclerView.Adapter<FeedBackRvAdapter.ViewHolder> {

    private Context mContext;
    private List<FeedBackDataModel> modelList;
    private LayoutInflater mInflater;
    public OnItemClickListener onItemClickListener;//item点击事件
    public OnItemLongClickListener onItemLongClickListener;//item长按事件
    private int smallPicWidth,smallPicHeight;

    public FeedBackRvAdapter(Context context , List<FeedBackDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
        smallPicWidth = ScreenUtil.dip2px(mContext,140);
        smallPicHeight = ScreenUtil.dip2px(mContext,80);
    }

    @Override
    public FeedBackRvAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.feedback_cardview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedBackRvAdapter.ViewHolder holder, int position) {
        final int tempPosition = position;
        // 给ViewHolder设置元素
        FeedBackDataModel u = modelList.get(position);
        holder.mTitle.setText(u.title);
        holder.mHisPublishTime.setText(u.publishTime);
        holder.mVisitNum.setText(u.visitNum);
        //设置图像
        Glide.with(mContext).load(u.smallImage).override(smallPicWidth,smallPicHeight).
                diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(false).
                centerCrop().into(holder.mSmallPic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {//item点击事件
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(tempPosition);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {//item长按事件
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(tempPosition);
                }
                return true;
            }
        });
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
        private UpdateTimeTextView mHisPublishTime;
        private RoundImageViewByXfermode mSmallPic;

        private ViewHolder(View v) {
            super(v);
            mTitle = (TextView)v.findViewById(R.id.info_title);
            mVisitNum = (TextView)v.findViewById(R.id.info_visit_num);
            mSmallPic = (RoundImageViewByXfermode) v.findViewById(R.id.smallPic);
            mHisPublishTime = (UpdateTimeTextView) v.findViewById(R.id.hisPublishTime);
        }
    }

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * item长按事件
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    /**
     * 设置item点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

}
