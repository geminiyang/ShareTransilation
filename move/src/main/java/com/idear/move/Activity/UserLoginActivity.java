package com.idear.move.Activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.idear.move.R;
import com.yqq.swipebackhelper.BaseActivity;

public class UserLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);//设置为无标题栏
        setContentView(R.layout.activity_user_login);
    }
}
