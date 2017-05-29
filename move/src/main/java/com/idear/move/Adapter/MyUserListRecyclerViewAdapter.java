package com.idear.move.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.Fragment.UserListFragment.OnListFragmentInteractionListener;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.R;

import java.util.List;

public class MyUserListRecyclerViewAdapter extends RecyclerView.Adapter<MyUserListRecyclerViewAdapter.ViewHolder> {

    private final List<UserList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyUserListRecyclerViewAdapter(List<UserList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).title);
        holder.mContent.setText(mValues.get(position).content);
        holder.mTime.setText(mValues.get(position).time);
        holder.mImg.setBackgroundResource(mValues.get(position).userImgId);
        holder.mTipImg.setBackgroundResource(mValues.get(position).tipImgId);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    //监听函数
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImg;
        public final ImageView mTipImg;
        public final TextView mTitle;
        public final TextView mContent;
        public final TextView mTime;
        public UserList mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImg = (ImageView) view.findViewById(R.id.userImg);
            mTipImg = (ImageView) view.findViewById(R.id.tip_img);
            mTitle = (TextView) view.findViewById(R.id.title);
            mContent = (TextView) view.findViewById(R.id.content);
            mTime = (TextView) view.findViewById(R.id.time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContent.getText() + "'";
        }
    }
}
