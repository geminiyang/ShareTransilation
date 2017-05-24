package com.idear.move.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.idear.move.R;
import com.idear.move.myWidget.MyProgressDialog;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class DynamicPublishingActivity extends BaseActivity {

    private Button back,publish;
    private MyProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_dynamic_publishing);

        initView();
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOne(v);
            }
        });
    }

    private void initView() {
        back = (Button) findViewById(R.id.ic_arrow_back);
        publish = (Button) findViewById(R.id.publish_dynamic);
    }

    private void showDialogOne(View view) {
        dialog =new MyProgressDialog(this, "正在加载中",R.anim.progress_loading);
        dialog.show();
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);
    }
}
