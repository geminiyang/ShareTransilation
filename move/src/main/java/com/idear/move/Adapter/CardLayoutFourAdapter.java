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
import com.idear.move.POJO.CardLayoutFourDataModel;
import com.idear.move.R;
import com.idear.move.myWidget.RoundImageViewByXfermode;
import com.idear.move.myWidget.UpdateTimeTextView;
import com.idear.move.util.ScreenUtil;

import java.util.List;


public class CardLayoutFourAdapter extends RecyclerView.Adapter<CardLayoutFourAdapter.ViewHolder> {

    private Context mContext;
    private List<CardLayoutFourDataModel> modelList;
    private LayoutInflater mInflater;
    public OnItemClickListener onItemClickListener;//item点击事件
    public OnItemLongClickListener onItemLongClickListener;//item长按事件
    private int picWidth,picHeight;

    public CardLayoutFourAdapter(Context context , List<CardLayoutFourDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
        picWidth = ScreenUtil.dip2px(mContext,340);
        picHeight = ScreenUtil.dip2px(mContext,120);
    }

    @Override
    public CardLayoutFourAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.card_layout_four, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardLayoutFourAdapter.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        final int tempPosition = position;
        CardLayoutFourDataModel u = modelList.get(position);
        holder.mActivityName.setText(u.activityName);
        holder.mPublishTime.setText(u.publishTime);
        holder.mPersonNum.setText(u.personNum);
        holder.mMoneyNum.setText(u.moneyNum);
        holder.mVisitNum.setText(u.visitNum);
        holder.mFavoriteNum.setText(u.favoriteNum);
        holder.mActivityState.setText(u.activityState);
        Glide.with(mContext).load(u.activityPic).override(picWidth,picHeight).diskCacheStrategy(DiskCacheStrategy.RESULT).
                skipMemoryCache(false).into(holder.mActivityPic);
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
        private TextView mActivityName,mFavoriteNum,mVisitNum,mPersonNum,mMoneyNum,mActivityState;
        private UpdateTimeTextView mPublishTime;
        private RoundImageViewByXfermode mActivityPic;

        private ViewHolder(View v) {
            super(v);
            mActivityName = (TextView) v.findViewById(R.id.activityName);
            mPublishTime = (UpdateTimeTextView) v.findViewById(R.id.publishTime);
            mPersonNum = (TextView) v.findViewById(R.id.personNum);
            mMoneyNum = (TextView) v.findViewById(R.id.moneyNum);
            mActivityPic = (RoundImageViewByXfermode) v.findViewById(R.id.activityPic);
            mActivityState = (TextView) v.findViewById(R.id.activityState);
            mFavoriteNum = (TextView) v.findViewById(R.id.favoriteNum);
            mVisitNum = (TextView) v.findViewById(R.id.visitNum);
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
