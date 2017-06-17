package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idear.move.Dummy.GroupListContent.GroupList;
import com.idear.move.Fragment.GroupListFragment.OnListFragmentInteractionListener;
import com.idear.move.POJO.GroupListHolder;
import com.idear.move.R;
import com.idear.move.util.ScreenUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.List;

public class MyGroupListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<GroupList> mValues;
    private LayoutInflater mInflater;
    private int iconWidth,iconHeight;
    public OnItemClickListener onItemClickListener;//item点击事件
    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MyGroupListRecyclerViewAdapter(Context context ,List<GroupList> mList) {
        this.mContext = context;
        this.mValues = mList;
        mInflater = LayoutInflater.from(this.mContext);
        iconWidth = ScreenUtil.dip2px(mContext,55);
        iconHeight = ScreenUtil.dip2px(mContext,55);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupListHolder(mInflater.inflate(R.layout.item_group_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int tempPosition = position;
        final GroupListHolder mHolder = ((GroupListHolder) holder);
        mHolder.mItem = mValues.get(position);//代表哪个位置的数据项
        mHolder.mTitle.setText(mValues.get(position).title);
        mHolder.mContent.setText(mValues.get(position).content);
        mHolder.mTime.setText(mValues.get(position).time);
        Glide.with(mContext).load(mValues.get(position).GroupImgId).override(iconWidth,iconHeight).diskCacheStrategy(
                DiskCacheStrategy.RESULT).skipMemoryCache(false).fitCenter().into(mHolder.mGroupImg);
        mHolder.mTipImg.setBackgroundResource(mValues.get(position).tipImgId);//闹铃按钮图片

        mHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    //监听函数
                    onItemClickListener.onItemClick(tempPosition);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return CustomRecyclerView.TYPE_NORMAL;//配对方法1
    }

    @Override
    public int getItemCount() {
        return mValues==null ? 0 : mValues.size();//配对方法1
    }

    /**
     * 设置item点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
