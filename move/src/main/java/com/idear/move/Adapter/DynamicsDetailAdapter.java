package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idear.move.POJO.AbstractDataModel;
import com.idear.move.POJO.DynamicDetailItemOneDataModel;
import com.idear.move.POJO.DynamicDetailItemThreeDataModel;
import com.idear.move.POJO.DynamicDetailItemTwoDataModel;
import com.idear.move.POJO.DynamicsPraiseDataModel;
import com.idear.move.R;
import com.idear.move.myWidget.MySpannableTextView;
import com.idear.move.myWidget.RoundImageViewByXfermode;
import com.idear.move.myWidget.UpdateTimeTextView;
import com.idear.move.util.ScreenUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.List;

public class DynamicsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ONE = 101;
    public static final int TWO = 102;
    public static final int THREE = 103;
    public static final int ATTENTION_BTN = 201;
    public static final int THUMB_UP_ICON_ONE = 202;
    public static final int FAVORITE_ICON= 203;
    public static final int THUMB_UP_ICON_THREE = 204;
    public static final int COMMENT_ICON = 205;


    private Context mContext;
    private List<AbstractDataModel> modelList;
    private LayoutInflater mInflater;
    private int userIconSize,dynamicPicHeight,dynamicPicWidth,discussantIconSize;
    public OnViewClickListener onViewClickListener;//item子view点击事件
    public OnItemClickListener onItemClickListener;//item点击事件
    public OnItemLongClickListener onItemLongClickListener;//item长按事件

    public DynamicsDetailAdapter(Context context , List<AbstractDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        mInflater = LayoutInflater.from(this.mContext);
        userIconSize = ScreenUtil.dip2px(mContext,40);
        dynamicPicWidth = ScreenUtil.dip2px(mContext,300);
        dynamicPicHeight = ScreenUtil.dip2px(mContext,120);
        discussantIconSize = ScreenUtil.dip2px(mContext,40);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 给ViewHolder设置布局文件
        View v = null;
        switch (viewType) {
            case ONE:
                v = mInflater.inflate(R.layout.item_one_dynamics_detail, parent, false);
                return  new ViewHolderOne(v);
            case TWO:
                v = mInflater.inflate(R.layout.item_two_dynamics_detail, parent, false);
                return  new ViewHolderTwo(v);
            case THREE:
                v = mInflater.inflate(R.layout.item_three_dynamics_detail, parent, false);
                return  new ViewHolderThree(v);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int tempPosition = position;
        //不同类型不同的UI
        if(holder instanceof ViewHolderOne) {
            // 给ViewHolder设置元素,先获取强转后的数据源
            DynamicDetailItemOneDataModel u = (DynamicDetailItemOneDataModel) modelList.get(position);
            //设置用户名
            ((ViewHolderOne) holder).mUsername.setText(u.getUsername());
            //设置发布时间
            ((ViewHolderOne) holder).mPublishTime.setText(u.getPublishTime());
            //设置动态内容
            ((ViewHolderOne) holder).mDynamicContent.setText(u.getDynamicContent());
            //设置动态发布用户头像
            Glide.with(mContext).load(u.getUserIconUrl()).skipMemoryCache(false).
                    diskCacheStrategy(DiskCacheStrategy.RESULT).override(userIconSize, userIconSize).
                    into(((ViewHolderOne) holder).mUserIcon);
            //设置动态图片
            Glide.with(mContext).load(u.getDynamicPicUrl()).skipMemoryCache(false).diskCacheStrategy(
                    DiskCacheStrategy.RESULT).override(dynamicPicWidth, dynamicPicHeight).
                    into(((ViewHolderOne) holder).mDynamicPic);
            if(onViewClickListener!=null) {
                //设置关注按钮点击事件
                ((ViewHolderOne) holder).mAttention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onViewClickListener.onViewClick(tempPosition,ATTENTION_BTN);
                    }
                });
                ((ViewHolderOne) holder).mThumbUpIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onViewClickListener.onViewClick(tempPosition,THUMB_UP_ICON_ONE);
                    }
                });
                ((ViewHolderOne) holder).mFavoriteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onViewClickListener.onViewClick(tempPosition,FAVORITE_ICON);
                    }
                });
            }
        } else if(holder instanceof ViewHolderTwo) {
            // 给ViewHolder设置元素,先获取强转后的数据源
            DynamicDetailItemTwoDataModel u = (DynamicDetailItemTwoDataModel) modelList.get(position);
            ((ViewHolderTwo) holder).mCommentCount.setText(u.getCommentCount());
        } else if(holder instanceof ViewHolderThree) {
            // 给ViewHolder设置元素,先获取强转后的数据源
            DynamicDetailItemThreeDataModel u = (DynamicDetailItemThreeDataModel) modelList.get(position);
            //设置评论者昵称
            ((ViewHolderThree) holder).mDiscussantName.setText(u.getDiscussantName());
            //设置评论内容
            ((ViewHolderThree) holder).mCommentContent.setText(u.getCommentContent());
            //设置评论时间
            ((ViewHolderThree) holder).mTime.setText(u.getTime());
            //设置点赞数目
            ((ViewHolderThree) holder).mThumbUpCount.setText(u.getThumbUpCount());
            //设置评论数目
            ((ViewHolderThree) holder).mCommentCount.setText(u.getCommentCount());
            //设置评论者头像
            Glide.with(mContext).load(u.getDiscussantIconUrl()).skipMemoryCache(false).
                    diskCacheStrategy(DiskCacheStrategy.RESULT).override(discussantIconSize,
                    discussantIconSize).into(((ViewHolderThree) holder).mDiscussantIcon);
            if(onViewClickListener!=null) {
                //设置关注按钮点击事件
                ((ViewHolderThree) holder).mCommentIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onViewClickListener.onViewClick(tempPosition,COMMENT_ICON);
                    }
                });
                ((ViewHolderThree) holder).mThumbUpIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onViewClickListener.onViewClick(tempPosition,THUMB_UP_ICON_THREE);
                    }
                });
            }
        }
        //不管是哪种类型都统一设置点击事件
        //整个item绘制对应的点击事件
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
        int Type;
            switch (position) {
                case 0:
                    Type = ONE;
                    break;
                case 1:
                    Type = TWO;
                    break;
                case 2:
                default:
                    Type = THREE;
                    break;
            }
        return Type;
    }

    //重写的自定义ViewHolder
    private class ViewHolderOne extends RecyclerView.ViewHolder {
        private ImageView mUserIcon,mDynamicPic,mThumbUpIcon,mFavoriteIcon;
        private TextView mUsername,mPublishTime;
        private Button mAttention;
        private MySpannableTextView mDynamicContent;

        private ViewHolderOne(View v) {
            super(v);
            mUserIcon = (ImageView) v.findViewById(R.id.userIcon);
            mUsername = (TextView) v.findViewById(R.id.username);
            mPublishTime = (TextView) v.findViewById(R.id.publishTime);
            mAttention = (Button) v.findViewById(R.id.attention);
            mDynamicContent = (MySpannableTextView) v.findViewById(R.id.dynamicContent);
            mDynamicPic = (ImageView) v.findViewById(R.id.dynamicPic);
            mThumbUpIcon = (ImageView) v.findViewById(R.id.thumbUpIcon);
            mFavoriteIcon = (ImageView) v.findViewById(R.id.favoriteIcon);
        }
    }

    private class ViewHolderTwo extends RecyclerView.ViewHolder {
        private TextView mCommentCount;

        private ViewHolderTwo(View v) {
            super(v);
            mCommentCount = (TextView) v.findViewById(R.id.commentCountText);
        }
    }

    private class ViewHolderThree extends RecyclerView.ViewHolder {
        private TextView mDiscussantName,mCommentContent,mTime,mThumbUpCount,mCommentCount;
        private ImageView mThumbUpIcon,mCommentIcon;
        private RoundImageViewByXfermode mDiscussantIcon;

        private ViewHolderThree(View v) {
            super(v);
            mDiscussantName = (TextView) v.findViewById(R.id.discussantName);
            mCommentContent = (TextView) v.findViewById(R.id.commentContent);
            mTime = (TextView) v.findViewById(R.id.time);
            mThumbUpCount = (TextView) v.findViewById(R.id.thumbUpCount);
            mCommentCount = (TextView) v.findViewById(R.id.commentCount);
            mThumbUpIcon = (ImageView) v.findViewById(R.id.thumbUpIcon);
            mCommentIcon = (ImageView) v.findViewById(R.id.commentIcon);
            mDiscussantIcon = (RoundImageViewByXfermode) v.findViewById(R.id.discussantIcon);
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

    /**
     * 设置子View的点击事件
     * @param onViewClickListener
     */
    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }
}