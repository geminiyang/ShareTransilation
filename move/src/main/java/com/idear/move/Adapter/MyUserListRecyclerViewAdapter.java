package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Fragment.UserListFragment.OnListFragmentInteractionListener;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.POJO.ChatContentHolder;
import com.idear.move.R;
import com.idear.move.ViewHolder.FooterHolder;
import com.idear.move.ViewHolder.HeaderHolder;
import com.idear.move.constants.AppConstant;
import com.idear.move.myWidget.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyUserListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<UserList> mValues = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;

    public MyUserListRecyclerViewAdapter(Context context ,OnListFragmentInteractionListener listener,
                                         ArrayList<UserList> mList) {
        this.mContext = context;
        this.mValues = mList;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatContentHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ChatContentHolder mHolder = ((ChatContentHolder)holder);
        mHolder.mItem = mValues.get(position);
        mHolder.mTitle.setText(mValues.get(position).title);
        mHolder.mContent.setText(mValues.get(position).content);
        mHolder.mTime.setText(mValues.get(position).time);
        mHolder.mImg.setBackgroundResource(mValues.get(position).userImgId);
        mHolder.mTipImg.setBackgroundResource(mValues.get(position).tipImgId);

        mHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    //监听函数
                    mListener.onListFragmentInteraction(mHolder.mItem);
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
