package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idear.move.POJO.ActivityDetailContentDataModel;
import com.idear.move.R;
import com.idear.move.myWidget.MySpannableTextView;
import com.idear.move.util.ScreenUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.List;

public class ActivityDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ActivityDetailContentDataModel> modelList;
    private LayoutInflater mInflater;
    private int picWidth,picHeight;
    public OnViewClickListener onViewClickListener;//item子view点击事件
    public OnItemClickListener onItemClickListener;//item点击事件
    public OnItemLongClickListener onItemLongClickListener;//item长按事件

    public ActivityDetailAdapter(Context context , List<ActivityDetailContentDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
        picWidth = ScreenUtil.dip2px(mContext,200);
        picHeight = ScreenUtil.dip2px(mContext,120);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.item_activity_detail_content, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int tempPosition = position;
        // 给ViewHolder设置元素
        ActivityDetailContentDataModel u = modelList.get(position);
        ViewHolder mHolder = (ViewHolder) holder;
        //设置活动状态
        mHolder.mActivityState.setText(u.getActivityState());
        //设置活动内容
        mHolder.mActivityContent.setText(u.getActivityContent());
        //设置活动发起人
        mHolder.mPublishUsername.setText(u.getPublishUsername());
        //设置活动时间
        mHolder.mActivityTime.setText(u.getActivityTime());
        //设置活动地点
        mHolder.mActivityLocation.setText(u.getActivityLocation());
        //设置活动意义
        mHolder.mActivityMeaning.setText(u.getActivityMeaning());
        //设置活动招募人数
        mHolder.mActivityPersonNum.setText(u.getActivityPersonNum());
        //设置活动筹资金额
        mHolder.mActivityMoney.setText(u.getActivityMoney());
        //设置活动招募期限至
        mHolder.mActivityRExpireTime.setText(u.getActivityRExpireTime());
        //设置活动筹资期限至
        mHolder.mActivityFExpireTime.setText(u.getActivityFExpireTime());

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
        private TextView mActivityState,mPublishUsername,mActivityTime,mActivityLocation,mActivityMeaning,
        mActivityPersonNum,mActivityMoney,mActivityRExpireTime,mActivityFExpireTime;
        private MySpannableTextView mActivityContent;

        private ViewHolder(View v) {
            super(v);
            mActivityState = (TextView) v.findViewById(R.id.activityState);
            mPublishUsername = (TextView) v.findViewById(R.id.publishUsername);
            mActivityTime = (TextView) v.findViewById(R.id.activityTime);
            mActivityLocation = (TextView) v.findViewById(R.id.activityLocation);
            mActivityMeaning = (TextView) v.findViewById(R.id.activityMeaning);
            mActivityPersonNum = (TextView) v.findViewById(R.id.activityPersonNum);
            mActivityMoney = (TextView) v.findViewById(R.id.activityMoney);
            mActivityRExpireTime = (TextView) v.findViewById(R.id.activityRExpireTime);
            mActivityFExpireTime = (TextView) v.findViewById(R.id.activityFExpireTime);
            mActivityContent = (MySpannableTextView) v.findViewById(R.id.activityContent);
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