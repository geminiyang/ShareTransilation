package com.idear.move.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.swipebackhelper.BaseActivity;
import com.yqq.swipebackhelper.SwipeBackHelper;

public class UserLoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_password,et_id;
    private TextView tv_register,tv_forgetPassword;
    private ImageView qq,weChat;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);//设置为无标题栏
        setContentView(R.layout.activity_user_login);

        initView();
        initEvent();

        //设置当前Activity不能够滑动返回
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);
    }

    private void initView() {
        et_id = (EditText) findViewById(R.id.et_id);
        et_password = (EditText) findViewById(R.id.et_password);

        tv_forgetPassword = (TextView) findViewById(R.id.end);
        tv_register = (TextView) findViewById(R.id.start);

        qq = (ImageView) findViewById(R.id.qq);
        weChat = (ImageView) findViewById(R.id.wechat);
        login = (Button) findViewById(R.id.bt_login);
    }

    private void initEvent() {
        qq.setOnClickListener(this);
        weChat.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_login:
                login();
                break;
            case R.id.start:
                IntentSkipUtil.skipToNextActivity(this,UserRegisterActivity.class);
                break;
            case R.id.end:
                IntentSkipUtil.skipToNextActivity(this,ForgetPasswordActivity.class);
                break;
            case R.id.wechat:
                Toast.makeText(this,"第三方接入登陆wechat",Toast.LENGTH_SHORT).show();
                break;
            case R.id.qq:
                Toast.makeText(this,"第三方接入登陆qq",Toast.LENGTH_SHORT).show();
                IntentSkipUtil.skipToNextActivity(this,FirstMainUIActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 启用线程进行登陆操作
     */
    private void login() {

    }
}
