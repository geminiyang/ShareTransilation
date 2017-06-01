package com.idear.move.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.idear.move.R;

/**
 * 作者:geminiyang on 2017/5/31.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class HeaderHolder extends RecyclerView.ViewHolder{

    public ProgressBar progressBar;
    //定义参数

    public HeaderHolder(View itemView) {
        super(itemView);
        //绑定视图id
        progressBar = (ProgressBar) itemView.findViewById(R.id.pb_count_down_top);
    }
}
