package com.idear.move.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.Fragment.UserListFragment.OnListFragmentInteractionListener;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.POJO.ChatContentHolder;
import com.idear.move.R;
import com.idear.move.constants.AppConstant;

import java.util.List;

public class MyUserListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;

    private final List<UserList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FooterHolder mFooterHolder;

    public MyUserListRecyclerViewAdapter(List<UserList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user_list, parent, false);
                return new ChatContentHolder(view);
            case TYPE_FOOTER:
                View viewFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_holder, parent, false);
                mFooterHolder = new FooterHolder(viewFooter);
                return mFooterHolder;
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
                //((FooterHolder)holder).setState(AppConstant.FooterState.NORMAL);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position==mValues.size()? TYPE_FOOTER : TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mValues.size()+1;
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        View mLoadingViewStub;
        View mEndViewStub;
        View mNetworkErrorViewStub;

        public FooterHolder(View itemView) {
            super(itemView);
            //绑定视图id
            mLoadingViewStub = itemView.findViewById(R.id.loading_viewStub);
            mEndViewStub = itemView.findViewById(R.id.end_viewStub);
            mNetworkErrorViewStub = itemView.findViewById(R.id.network_error_viewStub);
        }

        //根据传过来的status控制哪个状态可见
        public void setState(AppConstant.FooterState status) {
            Log.d("TAG", "DemoAdapter" + status + "");
            switch (status) {
                case NORMAL:
                    setAllGone();
                    break;
                case LOADING:
                    setAllGone();
                    mLoadingViewStub.setVisibility(View.VISIBLE);
                    break;
                case END:
                    setAllGone();
                    mEndViewStub.setVisibility(View.VISIBLE);
                    break;
                case NETWORK_ERROR:
                    setAllGone();
                    mNetworkErrorViewStub.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

        }

        //全部不可见
        void setAllGone() {
            if (mLoadingViewStub != null) {
                mLoadingViewStub.setVisibility(View.GONE);
            }
            if (mEndViewStub != null) {
                mEndViewStub.setVisibility(View.GONE);
            }
            if (mNetworkErrorViewStub != null) {
                mNetworkErrorViewStub.setVisibility(View.GONE);
            }
        }

    }
}
