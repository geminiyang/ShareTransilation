package com.idear.move.Activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.Service.NetBroadCastReceiver;
import com.idear.move.Thread.GetVerifyCodeThread;
import com.idear.move.Thread.PasswordForgetWithCodeThread;
import com.idear.move.Thread.PasswordUpdateWithCodeThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.CheckValidUtil;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.swipebackhelper.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * 赞助商和用户公用一个忘记密码页面，通过Intent传递过来的值判断忘记密码哦操作 所对应的邮箱
 */
public class ForgetPasswordActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView iv_back;
    private Button submit,getCode;
    private EditText email,newPassword,reSurePassword,code;

    private BroadcastReceiver receiver;

    //静态Handler处理子线程和UI线程的通信
    private static class MyHandler extends Handler {
        WeakReference mActivity;
        MyHandler(ForgetPasswordActivity activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            ForgetPasswordActivity theActivity = (ForgetPasswordActivity) mActivity.get();
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);
        //通过广播设置网络监听
        receiver = new NetBroadCastReceiver();
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

    @Override
    public void onResume() {
        IntentFilter filter=new IntentFilter();
        //一条信息触发一次广播接收器
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 修改密码
     */
    private void modifyPassword() {
        String emailStr = email.getText().toString().trim();
        String codeStr= code.getText().toString().trim();
        String newPasswordStr = newPassword.getText().toString().trim();
        String reSurePasswordStr = reSurePassword.getText().toString().trim();
        //验证两次密码输入是否一致,不一致则提示并退出
        if(!CheckValidUtil.isEqual(newPasswordStr,reSurePasswordStr)) {
            ToastUtil.getInstance().showToast(ForgetPasswordActivity.this,"新密码前后输入不一致!");
            return;
        }

        if(CheckValidUtil.isAllNotEmpty(emailStr,codeStr,newPasswordStr,reSurePasswordStr)) {
            PasswordForgetWithCodeThread passwordForgetWithCodeThread = new PasswordForgetWithCodeThread(
                    ForgetPasswordActivity.this,HttpPath.getPassWordForgetPath(),emailStr,codeStr,newPasswordStr);
            passwordForgetWithCodeThread.setDataGetListener(new DataGetInterface() {
                @Override
                public void finishWork(Object obj) {
                    if(obj instanceof ResultType) {
                        ResultType result = (ResultType) obj;
                        int statusCode = Integer.parseInt(result.getStatus());
                        if(statusCode == 1) {
                            IntentSkipUtil.skipToNextActivity(ForgetPasswordActivity.this,
                                    UserLoginActivity.class);
                        } else {
                            ToastUtil.getInstance().showToastInThread(ForgetPasswordActivity.this,result.getMessage());
                        }
                    } else {
                        ToastUtil.getInstance().showToastInThread(ForgetPasswordActivity.this,obj.toString());
                    }
                }

                @Override
                public void interrupt(Exception e) {
                    //添加网络错误处理
                    ToastUtil.getInstance().showToastInThread(ForgetPasswordActivity.this,
                            ErrorHandleUtil.ExceptionToStr(e,ForgetPasswordActivity.this));
                }
            });
            passwordForgetWithCodeThread.start();
        } else {
            ToastUtil.getInstance().showToast(ForgetPasswordActivity.this,"请填写完整信息!");
        }
    }

    /**
     * 获取邮箱验证码
     */
    private void getVerifyCode() {
        String emailStr = email.getText().toString().trim();
        if(!TextUtils.isEmpty(emailStr)) {
            getCode.setText("获取中");
            GetVerifyCodeThread getVerifyCodeThread = new GetVerifyCodeThread(ForgetPasswordActivity.this,
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
                    ToastUtil.getInstance().showToastInThread(ForgetPasswordActivity.this,
                            ErrorHandleUtil.ExceptionToStr(e,ForgetPasswordActivity.this));
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
