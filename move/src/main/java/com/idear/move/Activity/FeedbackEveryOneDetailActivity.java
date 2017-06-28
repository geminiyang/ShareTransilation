package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.idear.move.R;

public class FeedbackEveryOneDetailActivity extends MyBaseActivity {

    //UI相关
    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_every_one_detail);
        initView();
        initEvent();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
