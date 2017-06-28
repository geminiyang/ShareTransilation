package com.idear.move.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.Thread.FullInInfoThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.CheckValidUtil;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.swipebackhelper.BaseActivity;

/**
 * 检验是否注册成功，验证码是否匹配等操作
 */
public class UserRegisterNextStepActivity extends BaseActivity {

    private ImageView iv_back;
    private EditText nickname,password,reSurePassword,school;
    private Button submit;
    private String emailFromBefore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_next_step);

        emailFromBefore = getIntent().getExtras().getString("email");

        initView();
        initEvent();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        nickname = (EditText) findViewById(R.id.nickname);
        password = (EditText) findViewById(R.id.password);
        reSurePassword = (EditText) findViewById(R.id.password_reconfirm);
        school = (EditText) findViewById(R.id.school);
        submit = (Button) findViewById(R.id.submit);
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
                //提交填写简单资料的方法
                fallInInfo();
            }
        });
    }

    private void fallInInfo() {
        String nicknameStr = nickname.getText().toString().trim();
        String schoolStr = school.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String reSurePasswordStr = reSurePassword.getText().toString().trim();

        //检测不能有空的输入框
        if(!CheckValidUtil.isAllNotEmpty(nicknameStr,schoolStr,passwordStr,reSurePasswordStr)) {
            ToastUtil.getInstance().showToast(this,"请将信息填写完整");
            return;
        }
        //检测两次密码输入要相等
        if(!CheckValidUtil.isEqual(passwordStr,reSurePasswordStr)) {
            ToastUtil.getInstance().showToast(this,"请确认两次密码输入一致");
            return;
        }
        if(CheckValidUtil.isAllNotEmpty(emailFromBefore)) {
            FullInInfoThread fullInInfoThread = new FullInInfoThread(HttpPath.getFullInInfoPath(),this,
                    emailFromBefore,nicknameStr,passwordStr,schoolStr);
            fullInInfoThread.setDataGetListener(new DataGetInterface() {
                @Override
                public void finishWork(Object obj) {
                    if(obj instanceof ResultType) {
                        ResultType result = (ResultType) obj;
                        if(Integer.parseInt(result.getStatus()) == 1) {
                            IntentSkipUtil.skipToNextActivity(UserRegisterNextStepActivity.this,
                                    UserLoginActivity.class);
                        }
                        ToastUtil.getInstance().showToastInThread(UserRegisterNextStepActivity.this,
                                result.getMessage());
                    } else {
                        ToastUtil.getInstance().showToastInThread(UserRegisterNextStepActivity.this,
                                obj.toString());
                    }
                }

                @Override
                public void interrupt(Exception e) {
                    //添加网络错误处理
                    ToastUtil.getInstance().showToastInThread(UserRegisterNextStepActivity.this,
                            ErrorHandleUtil.ExceptionToStr(e,UserRegisterNextStepActivity.this));
                }
            });
            fullInInfoThread.start();
        } else {
            //提示获取失败
        }

    }
}
