package com.idear.move.Activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.Thread.GetVerifyCodeThread;
import com.idear.move.Thread.VerifyEmailWithCodeThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ScrimUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.swipebackhelper.BaseActivity;

import java.lang.ref.WeakReference;

public class UserRegisterActivity extends BaseActivity {
    private Button bt_next,getCode;
    private EditText email,code;
    private ImageView icBack;

    private static class MyHandler extends Handler {
        WeakReference mActivity;
        MyHandler(UserRegisterActivity activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            UserRegisterActivity theActivity = (UserRegisterActivity) mActivity.get();
            switch (msg.what) {
                case 0:
                    theActivity.getCode.setText("未获取");
                    break;
                case 1:
                    theActivity.getCode.setText("已获取");
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.title_bar_blue));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        initView();
        initEvent();
    }

    private void initView() {
        bt_next = (Button) findViewById(R.id.bt_next_step);
        icBack = (ImageView) findViewById(R.id.ic_arrow_back);
        getCode = (Button) findViewById(R.id.getCode);
        email = (EditText) findViewById(R.id.email);
        code = (EditText) findViewById(R.id.identifyCode);
    }

    private void initEvent() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            verifyEmailWithCode();
            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode.setText("获取中");
                //在线程中进行网络操作验证邮箱
                getVerifyCode();
            }
        });
    }

    private void verifyEmailWithCode() {
        String codeStr = code.getText().toString().trim();
        final String emailStr = email.getText().toString().trim();
        if(!TextUtils.isEmpty(emailStr)&&!TextUtils.isEmpty(codeStr)) {
            VerifyEmailWithCodeThread verifyEmailWithCodeThread = new VerifyEmailWithCodeThread(
                    this,HttpPath.getUserRegisterPath(),emailStr,codeStr);
            verifyEmailWithCodeThread.setDataGetListener(new DataGetInterface() {
                @Override
                public void finishWork(Object obj) {
                    if(obj instanceof ResultType) {
                        ResultType result = (ResultType) obj;
                        int statusCode = Integer.parseInt(result.getStatus());
                        if(statusCode == 1) {
                            IntentSkipUtil.skipToNextActivityWithBundle(UserRegisterActivity.this,
                                    UserRegisterNextStepActivity.class,"email",emailStr);
                        } else {
                            ToastUtil.getInstance().showToastInThread(UserRegisterActivity.this,result.getMessage());
                        }
                    } else {
                        ToastUtil.getInstance().showToastInThread(UserRegisterActivity.this,obj.toString());
                    }
                }

                @Override
                public void interrupt(Exception e) {

                }
            });
            verifyEmailWithCodeThread.start();
        } else {
            ToastUtil.getInstance().showToast(this,"请填写验证码或邮箱");
        }
    }

    private void getVerifyCode() {
        String emailStr = email.getText().toString().trim();
        if(!TextUtils.isEmpty(emailStr)) {
            GetVerifyCodeThread getVerifyCodeThread = new GetVerifyCodeThread(UserRegisterActivity.this,
                    HttpPath.getEmailVerifyPath(),emailStr);
            getVerifyCodeThread.setDataGetListener(new DataGetInterface() {
                @Override
                public void finishWork(Object obj) {
                    ResultType result = (ResultType) obj;
                    int statusCode = Integer.parseInt(result.getStatus());
                    handler.sendEmptyMessage(statusCode);
                }

                @Override
                public void interrupt(Exception e) {

                }
            });
            getVerifyCodeThread.start();
        } else {
            ToastUtil.getInstance().showToast(this,"请填写邮箱");
        }
    }

    /**
     * 渐变测试
     */
    private void use() {
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            View bottom = findViewById(R.id.root_rl);
            bottom.setBackground(
                    ScrimUtil.makeCubicGradientScrimDrawable(
                            getResources().getColor(R.color.blue_light), //颜色
                            16, //渐变层数
                            Gravity.CENTER)); //起始方向
        }
    }
}
