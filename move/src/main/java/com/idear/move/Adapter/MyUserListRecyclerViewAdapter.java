package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idear.move.Fragment.UserListFragment.OnListFragmentInteractionListener;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.POJO.UserListHolder;
import com.idear.move.R;
import com.idear.move.util.ScreenUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.List;

public class MyUserListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<UserList> mValues;
    private final OnListFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    private int iconWidth,iconHeight;

    public MyUserListRecyclerViewAdapter(Context context ,OnListFragmentInteractionListener listener,
                                         List<UserList> mList) {
        this.mContext = context;
        this.mValues = mList;
        this.mListener = listener;
        mInflater = LayoutInflater.from(this.mContext);
        iconWidth = ScreenUtil.dip2px(mContext,55);
        iconHeight = ScreenUtil.dip2px(mContext,55);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserListHolder(mInflater.inflate(R.layout.item_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final UserListHolder mHolder = ((UserListHolder)holder);
        mHolder.mItem = mValues.get(position);//具体哪一个位置的数据项
        mHolder.mTitle.setText(mValues.get(position).title);
        mHolder.mContent.setText(mValues.get(position).content);
        mHolder.mTime.setText(mValues.get(position).time);
        Glide.with(mContext).load(mValues.get(position).userImgId).override(iconWidth,iconHeight).diskCacheStrategy(
                DiskCacheStrategy.RESULT).skipMemoryCache(false).fitCenter().into(mHolder.mImg);
        mHolder.mTipImg.setBackgroundResource(mValues.get(position).tipImgId);

        mHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    //监听函数
                    mListener.onUserListFragmentInteraction(mHolder.mItem);//将这个位置的数据传递出去
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

}
