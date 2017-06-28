package com.idear.move.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.Thread.GetVerifyCodeThread;
import com.idear.move.Thread.PasswordUpdateWithCodeThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.CheckValidUtil;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.lang.ref.WeakReference;


public class UserUpdatePasswordActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView iv_back;
    private Button submit,getCode;
    private EditText email,newPassword,oldPassword,reSurePassword,code;

    //静态Handler处理子线程和UI线程的通信
    private static class MyHandler extends Handler {
        WeakReference mActivity;
        MyHandler(UserUpdatePasswordActivity activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            UserUpdatePasswordActivity theActivity = (UserUpdatePasswordActivity) mActivity.get();
            switch (msg.what) {
                case 0:
                    theActivity.getCode.setText("未获取");
                    break;
                case 1:
                    theActivity.getCode.setText("已获取");
                    break;
                case 2:
                    theActivity.getCode.setText("未获取");
                    break;
                case 3:
                    theActivity.getCode.setText("重新获取");
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_user_update_password);

        initView();
        initEvent();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        submit = (Button) findViewById(R.id.submit);
        getCode = (Button) findViewById(R.id.getCode);
        email = (EditText) findViewById(R.id.email);
        code = (EditText) findViewById(R.id.identifyCode);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        reSurePassword = (EditText) findViewById(R.id.password_reconfirm);
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行修改密码操作,如果修改成功则跳转到登录界面
                modifyPassword();
            }
        });
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode();
            }
        });
    }

    /**
     * 修改密码
     */
    private void modifyPassword() {
        String emailStr = email.getText().toString().trim();
        String codeStr= code.getText().toString().trim();
        String oldPasswordStr = oldPassword.getText().toString().trim();
        String newPasswordStr = newPassword.getText().toString().trim();
        String reSurePasswordStr = reSurePassword.getText().toString().trim();
        //验证两次密码输入是否一致,不一致则提示并退出
        if(!CheckValidUtil.isEqual(newPasswordStr,reSurePasswordStr)) {
            ToastUtil.getInstance().showToast(UserUpdatePasswordActivity.this,"新密码前后输入不一致!");
            return;
        }

        if(CheckValidUtil.isAllNotEmpty(emailStr,codeStr,oldPasswordStr,newPasswordStr,reSurePasswordStr)) {
            PasswordUpdateWithCodeThread passwordUpdateWithCodeThread = new PasswordUpdateWithCodeThread(
                    UserUpdatePasswordActivity.this,HttpPath.getPassWordUpdatePath(),emailStr,codeStr,
                    oldPasswordStr,newPasswordStr);
            passwordUpdateWithCodeThread.setDataGetListener(new DataGetInterface() {
                @Override
                public void finishWork(Object obj) {
                    if(obj instanceof ResultType) {
                        ResultType result = (ResultType) obj;
                        int statusCode = Integer.parseInt(result.getStatus());
                        if(statusCode == 1) {
                            IntentSkipUtil.skipToNextActivity(UserUpdatePasswordActivity.this,
                                    UserLoginActivity.class);
                        } else {
                            ToastUtil.getInstance().showToastInThread(UserUpdatePasswordActivity.this,result.getMessage());
                        }
                    } else {
                        ToastUtil.getInstance().showToastInThread(UserUpdatePasswordActivity.this,obj.toString());
                    }
                }

                @Override
                public void interrupt(Exception e) {
                    //添加网络错误处理
                    ToastUtil.getInstance().showToastInThread(UserUpdatePasswordActivity.this,
                            ErrorHandleUtil.ExceptionToStr(e,UserUpdatePasswordActivity.this));
                }
            });
            passwordUpdateWithCodeThread.start();
        } else {
            ToastUtil.getInstance().showToast(UserUpdatePasswordActivity.this,"请填写完整信息!");
        }
    }

    /**
     * 获取邮箱验证码
     */
    private void getVerifyCode() {
        String emailStr = email.getText().toString().trim();
        if(!TextUtils.isEmpty(emailStr)) {
            getCode.setText("获取中");
            GetVerifyCodeThread getVerifyCodeThread = new GetVerifyCodeThread(UserUpdatePasswordActivity.this,
                    HttpPath.getModifyPWDVerifyPath(),emailStr);
            getVerifyCodeThread.setDataGetListener(new DataGetInterface() {
                @Override
                public void finishWork(Object obj) {
                    ResultType result = (ResultType) obj;
                    int statusCode = Integer.parseInt(result.getStatus());
                    handler.sendEmptyMessage(statusCode);
                }

                @Override
                public void interrupt(Exception e) {
                    //添加网络错误处理
                    ToastUtil.getInstance().showToastInThread(UserUpdatePasswordActivity.this,
                            ErrorHandleUtil.ExceptionToStr(e,UserUpdatePasswordActivity.this));
                    handler.sendEmptyMessage(3);
                }
            });
            getVerifyCodeThread.start();
        } else {
            ToastUtil.getInstance().showToast(this,"请填写邮箱");
            getCode.setText("重新获取");
        }
    }
}

