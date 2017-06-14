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
import com.idear.move.POJO.MyDynamicsDataModel;
import com.idear.move.POJO.MyInitiateDataModel;
import com.idear.move.R;
import com.idear.move.myWidget.CommentTextView;
import com.idear.move.util.DisplayUtil;
import com.idear.move.util.ScreenUtil;
import com.idear.move.util.ToastUtil;

import java.util.List;


/**
 *
 */
public class MyDynamicsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MyDynamicsDataModel> modelList;
    private LayoutInflater mInflater;
    private int iconSize,screenSize,picHeight;
    public MyDynamicsRvAdapter(Context context , List<MyDynamicsDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
        iconSize = ScreenUtil.dip2px(mContext,32);
        screenSize = ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dip2px(mContext,10);
        picHeight = ScreenUtil.dip2px(mContext,220);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 给ViewHolder设置布局文件
        View v = mInflater.inflate(R.layout.item_mydynamics, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        MyDynamicsDataModel u = modelList.get(position);
        MyViewHolder mHolder = (MyViewHolder) holder;
        Glide.with(mContext).load(u.userIcon).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .override(iconSize,iconSize).centerCrop().into(mHolder.mUserIcon);
        mHolder.mUsername.setText(u.username);
        mHolder.mTime.setText(u.time);
        //设置图像
        Glide.with(mContext).load(u.pic).override(screenSize,picHeight).centerCrop().into(mHolder.mPic);
        mHolder.mState.setText(u.state);
        mHolder.mThumbUpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.getInstance().showToastTest(mContext);
            }
        });
        mHolder.mCommentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.getInstance().showToastTest(mContext);
            }
        });
        mHolder.mFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.getInstance().showToastTest(mContext);
            }
        });
        mHolder.commentTextView.setNameLabelText(u.commentList[0]);
        mHolder.commentTextView.setCommentContentText(u.commentList[1]);
        mHolder.mMoreContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.getInstance().showToastTest(mContext);
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
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsername,mTime,mState,mMoreContent;
        private ImageView mUserIcon,mPic,mFavoriteIcon,mCommentIcon,mThumbUpIcon;
        private CommentTextView commentTextView;

        private MyViewHolder(View v) {
            super(v);
            mUserIcon = (ImageView) v.findViewById(R.id.iv_user_icon);
            mUsername = (TextView)v.findViewById(R.id.tv_username);
            mTime = (TextView)v.findViewById(R.id.tv_time);
            mPic = (ImageView)v.findViewById(R.id.pic);
            mState= (TextView)v.findViewById(R.id.state);
            mThumbUpIcon = (ImageView) v.findViewById(R.id.iv_thumbUpIcon);
            mCommentIcon = (ImageView) v.findViewById(R.id.iv_commentIcon);
            mFavoriteIcon = (ImageView) v.findViewById(R.id.iv_favoriteIcon);
            commentTextView = (CommentTextView) v.findViewById(R.id.commentTextView);
            mMoreContent = (TextView) v.findViewById(R.id.tv_more_content);
        }
    }

}