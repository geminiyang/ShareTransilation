package com.idear.move.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.idear.move.R;
import com.idear.move.constants.AppConstant;

/**
 * 作者:geminiyang on 2017/5/31.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

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

    //设置全部布局不可见
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
