package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idear.move.POJO.FansAndAttentionDataModel;
import com.idear.move.R;
import com.idear.move.myWidget.RoundImageViewByXfermode;
import com.idear.move.util.ScreenUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.List;

public class FansAndAttentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int BTN = 1;

    private Context mContext;
    private List<FansAndAttentionDataModel> modelList;
    private LayoutInflater mInflater;
    private int picWidth,picHeight;
    public OnViewClickListener onViewClickListener;//item子view点击事件
    public OnItemClickListener onItemClickListener;//item点击事件
    public OnItemLongClickListener onItemLongClickListener;//item长按事件

    public FansAndAttentionAdapter(Context context , List<FansAndAttentionDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
        picWidth = ScreenUtil.dip2px(mContext,50);
        picHeight = ScreenUtil.dip2px(mContext,50);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.card_layout_fans_and_attention, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int tempPosition = position;
        // 给ViewHolder设置元素
        FansAndAttentionDataModel u = modelList.get(position);
        final ViewHolder mHolder = (ViewHolder) holder;
        //设置用户昵称
        mHolder.mNickname.setText(u.nickname);
        //设置用户ID
        mHolder.mId.setText(u.id);
        //设置图标
        Glide.with(mContext).load(u.picUrl).override(picWidth,picHeight).diskCacheStrategy(DiskCacheStrategy.RESULT).
                skipMemoryCache(false).centerCrop().into(mHolder.mUserAvatar);
        mHolder.mConcernOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onViewClickListener != null) {
                    onViewClickListener.onViewClick(tempPosition,BTN,mHolder.mConcernOther);
                }
            }
        });

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
        private TextView mId,mNickname;
        private RoundImageViewByXfermode mUserAvatar;
        private Button mConcernOther;

        private ViewHolder(View v) {
            super(v);
            mId = (TextView) v.findViewById(R.id.id);
            mNickname = (TextView) v.findViewById(R.id.nickname);
            mUserAvatar = (RoundImageViewByXfermode) v.findViewById(R.id.avatar);
            mConcernOther = (Button) v.findViewById(R.id.concernOtherBtn);
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
        void onViewClick(int position, int viewType,View view);
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

    /**
     * 设置子View的点击事件
     * @param onViewClickListener
     */
    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }
}