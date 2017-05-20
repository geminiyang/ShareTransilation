package com.yqq.swipebacklayouttest;

import android.graphics.Color;
import android.os.Bundle;

import com.yqq.swipebackhelper.BaseActivity;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.yqq.myutillibrary.TranslucentStatusSetting.setTranslucentStatusSetting(this, Color.TRANSPARENT);
        setContentView(R.layout.activity_second);
    }
}
