package com.idear.move.POJO;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.Dummy.GroupListContent;
import com.idear.move.R;

/**
 * 作者:geminiyang on 2017/5/30.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class GroupListHolder extends RecyclerView.ViewHolder{
    public final View mView;
    public final ImageView mGroupImg;
    public final ImageView mTipImg;
    public final TextView mTitle;
    public final TextView mContent;
    public final TextView mTime;
    public GroupListContent.GroupList mItem;

    public GroupListHolder(View view) {
        super(view);
        mView = view;
        mGroupImg = (ImageView) view.findViewById(R.id.GroupImg);
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
