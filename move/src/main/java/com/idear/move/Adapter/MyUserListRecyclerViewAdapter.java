package com.idear.move.Adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.idear.move.Dummy.UserListContent;
import com.idear.move.Fragment.UserListFragment.OnListFragmentInteractionListener;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.POJO.ChatContentHolder;
import com.idear.move.R;
import com.idear.move.ViewHolder.FooterHolder;
import com.idear.move.ViewHolder.HeaderHolder;
import com.idear.move.constants.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class MyUserListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_HEADER = 3;

    private List<UserList> mValues = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;

    private FooterHolder mFooterHolder;
    private HeaderHolder mHeaderHolder;
    private int mHeaderCount = 1;
    private int mFooterCount = 1;

    public MyUserListRecyclerViewAdapter(OnListFragmentInteractionListener listener,
                                         ArrayList<UserList> mList) {
        this.mValues = mList;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_NORMAL:
                return new ChatContentHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_user_list, parent, false));
            case TYPE_FOOTER:
                mFooterHolder = new FooterHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.footer_holder, parent, false));
                return mFooterHolder;
            case TYPE_HEADER:
                mHeaderHolder = new HeaderHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.header_holder, parent, false));
                return mHeaderHolder;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_NORMAL:
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
                break;
            case TYPE_FOOTER:
                //默认为加载状态
                ((FooterHolder)holder).setState(AppConstant.FooterState.NORMAL);
                break;
            case TYPE_HEADER:
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position==mValues.size() ? TYPE_FOOTER : (position == 0 ? TYPE_HEADER : TYPE_NORMAL);//配对方法1
    }

    @Override
    public int getItemCount() {
        return mValues==null ? 0 : (mValues.size()+mFooterCount);//配对方法1
    }

    public FooterHolder getFooterHolder() {
        return this.mFooterHolder;
    }
    public HeaderHolder getHeaderHolder() {
        return this.mHeaderHolder;
    }

    public void addHeaderView() {

    }

    public void addFooterView() {

    }

}
