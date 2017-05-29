package com.idear.move.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.idear.move.R;
import com.idear.move.myWidget.LoadingProgressDialog;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class DynamicPublishingActivity extends BaseActivity {

    private Button back,publish;
    private LoadingProgressDialog dialog;
    private CheckBox cb_location,cb_lock;

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
                //ProgressDialogUtil.showLoadingDialog(DynamicPublishingActivity.this,"正在加载中...",false);
                showDialogOne(v);
            }
        });
        cb_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cb_location.setChecked(!cb_location.isChecked());
            }
        });
        cb_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        back = (Button) findViewById(R.id.ic_arrow_back);
        publish = (Button) findViewById(R.id.publish_dynamic);

        cb_location = (CheckBox) findViewById(R.id.cb_publishLocation);
        cb_lock = (CheckBox) findViewById(R.id.cb_whoCanRead);
    }

    private void showDialogOne(View view) {
        dialog =new LoadingProgressDialog(this,R.drawable.progress_loading);
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
