package com.idear.move.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idear.move.R;
import com.idear.move.Thread.LoginThread;
import com.idear.move.Thread.VerifyUserStateThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.LoginResult;
import com.idear.move.network.ResultType;
import com.idear.move.util.CheckValidUtil;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.swipebackhelper.BaseActivity;
import com.yqq.swipebackhelper.SwipeBackHelper;

import java.lang.ref.WeakReference;

public class UserLoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_password,et_email;
    private TextView tv_register,tv_forgetPassword;
    private ImageView qq,weChat;
    private Button login;

    private static class MyHandler extends Handler {
        WeakReference mActivity;
        MyHandler(UserLoginActivity activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            UserLoginActivity theActivity = (UserLoginActivity) mActivity.get();
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

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
        et_email = (EditText) findViewById(R.id.et_email);
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
                break;
            default:
                break;
        }
    }

    private void verifyUserState() {
        VerifyUserStateThread verifyUserStateThread = new VerifyUserStateThread(
                this, HttpPath.getVerifyUserStatePath());
        verifyUserStateThread.setDataGetListener(new DataGetInterface() {
            @Override
            public void finishWork(Object obj) {
                if(obj instanceof ResultType) {
                    ResultType result = (ResultType) obj;
                    if(Integer.parseInt(result.getStatus()) == 1) {
                        //当验证的到的登录状态为1时
                    }
                    ToastUtil.getInstance().showToastInThread(UserLoginActivity.this, result.getMessage());
                } else {
                    ToastUtil.getInstance().showToastInThread(UserLoginActivity.this, obj.toString());
                }
            }

            @Override
            public void interrupt(Exception e) {
                //添加网络错误处理
                ToastUtil.getInstance().showToastInThread(UserLoginActivity.this,
                        ErrorHandleUtil.ExceptionToStr(e,UserLoginActivity.this));
            }
        });
        verifyUserStateThread.start();
    }

    /**
     * 启用线程进行登陆操作,并且附带检验格式是否正确的操作
     */
    private void login() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if(CheckValidUtil.isAllNotEmpty(email,password)) {
            LoginThread loginThread = new LoginThread(this,HttpPath.getUserLoginPath(), email, password);
            loginThread.setDataGetListener(new DataGetInterface() {
                @Override
                public void finishWork(Object obj) {
                    if(obj instanceof LoginResult) {
                        LoginResult result = (LoginResult) obj;
                        if(Integer.parseInt(result.getStatus()) == 1) {
                            IntentSkipUtil.skipToNextActivity(UserLoginActivity.this,UserMainUIActivity.class);
                            Looper.prepare();
                            verifyUserState();
                            Looper.loop();
                        }
                        ToastUtil.getInstance().showToastInThread(UserLoginActivity.this,
                                result.getMessage());
                    } else {
                        ToastUtil.getInstance().showToastInThread(UserLoginActivity.this,
                                obj.toString());
                    }
                }

                @Override
                public void interrupt(Exception e) {
                    //添加网络错误处理
                    ToastUtil.getInstance().showToastInThread(UserLoginActivity.this,
                            ErrorHandleUtil.ExceptionToStr(e,UserLoginActivity.this));
                }
            });
            loginThread.start();
        } else {
            ToastUtil.getInstance().showToast(this,"用户名或密码不能为空");
        }
    }
}
