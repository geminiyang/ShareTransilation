package com.idear.move.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;
import com.yqq.swipebackhelper.SwipeBackHelper;

public class SelectActivity extends BaseActivity {

    private Button register,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this,getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_select);
        initView();
    }

    /**
     * 初始化视图操作
     */
    private void initView() {
        //设置当前Activity不能够滑动返回
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);
        register = (Button) findViewById(R.id.register_button);
        login = (Button) findViewById(R.id.login_button);
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.register_button:
                //API 23
                IntentSkipUtil.skipToNextActivity(SelectActivity.this,UserRegisterActivity.class);
                break;
            case R.id.login_button:
                IntentSkipUtil.skipToNextActivity(SelectActivity.this,LoginModeActivity.class);
                break;
        }
    }
}
