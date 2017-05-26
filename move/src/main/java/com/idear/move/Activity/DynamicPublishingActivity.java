package com.idear.move.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.idear.move.R;
import com.idear.move.myWidget.LoadingProgressDialog;
import com.idear.move.util.ProgressDialogUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class DynamicPublishingActivity extends BaseActivity {

    private Button back,publish;
    private LoadingProgressDialog dialog;
    private RadioButton rb_location,rb_lock;

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
        rb_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        rb_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rb_location.setChecked(!rb_location.isChecked());
            }
        });
        rb_lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        rb_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //rb_lock.setChecked(!rb_lock.isChecked());
            }
        });
    }

    private void initView() {
        back = (Button) findViewById(R.id.ic_arrow_back);
        publish = (Button) findViewById(R.id.publish_dynamic);

        rb_location = (RadioButton) findViewById(R.id.rb_publishLocation);
        rb_lock = (RadioButton) findViewById(R.id.rb_whoCanRead);
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
