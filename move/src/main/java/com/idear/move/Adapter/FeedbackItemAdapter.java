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
import com.idear.move.POJO.CardLayoutOneDataModel;
import com.idear.move.POJO.FeedbackItemDataModel;
import com.idear.move.R;
import com.idear.move.util.DateUtil;
import com.idear.move.util.ScreenUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.List;

public class FeedbackItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FeedbackItemDataModel> modelList;
    private LayoutInflater mInflater;
    private int picWidth,picHeight;
    public OnViewClickListener onViewClickListener;//item子view点击事件
    public OnItemClickListener onItemClickListener;//item点击事件
    public OnItemLongClickListener onItemLongClickListener;//item长按事件

    public FeedbackItemAdapter(Context context , List<FeedbackItemDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
        picWidth = ScreenUtil.dip2px(mContext,200);
        picHeight = ScreenUtil.dip2px(mContext,120);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int tempPosition = position;
        // 给ViewHolder设置元素
        FeedbackItemDataModel u = modelList.get(position);
        ViewHolder mHolder = (ViewHolder) holder;
        //设置活动标题名
        mHolder.mTitle.setText(u.title);
        //设置当前时间
        mHolder.mTime.setText(DateUtil.receiveDate(u.receiverTime));
        //设置图像
        Glide.with(mContext).load(u.pic).override(picWidth,picHeight).diskCacheStrategy(DiskCacheStrategy.RESULT).
                skipMemoryCache(false).centerCrop().into(mHolder.mPic);
        //设置状态
        mHolder.mScore.setText(u.score);

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
        return CustomRecyclerView.TYPE_NORMAL;
    }

    //重写的自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mScore,mTitle,mTime;
        private ImageView mPic;

        private ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.title);
            mScore= (TextView)v.findViewById(R.id.score);
            mTime = (TextView) v.findViewById(R.id.receiverTime);
            mPic = (ImageView) v.findViewById(R.id.pic);
        }
    }

    /**
     * item中子view的点击事件（回调）
     */
    public interface OnViewClickListener {
        /**
         * @param position item position
         * @param viewType 点击的view的类型，调用时根据不同的view传入不同的值加以区分
         */
        void onViewClick(int position, int viewType);
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