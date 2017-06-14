package com.idear.move.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.R;
import com.idear.move.util.ProgressDialogUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;


public class DynamicPublishingActivity extends BaseActivity {

    private Button back,publish;
    private CheckBox cb_location,cb_lock;
    private ImageView camera;
    private TextView permissionInfo,locationText;

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
                ProgressDialogUtil.showLoadDialog(DynamicPublishingActivity.this);
            }
        });
        cb_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cb_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cb_lock.isChecked()){
                    permissionInfo.setText("公开");
                } else {
                    permissionInfo.setText("私密");
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        back = (Button) findViewById(R.id.ic_arrow_back);
        publish = (Button) findViewById(R.id.publish_dynamic);
        permissionInfo = (TextView) findViewById(R.id.permission_info);
        locationText = (TextView) findViewById(R.id.location_text);

        cb_location = (CheckBox) findViewById(R.id.cb_publishLocation);
        cb_lock = (CheckBox) findViewById(R.id.cb_whoCanRead);
        camera = (ImageView) findViewById(R.id.camera);
    }


}
