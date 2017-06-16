package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.swipebackhelper.BaseActivity;

/**
 * 赞助商和用户公用一个忘记密码页面，通过Intent传递过来的值判断忘记密码哦操作 所对应的邮箱
 */
public class ForgetPasswordActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView iv_back;
    private Button nextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        initView();
        initEvent();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        nextStep = (Button) findViewById(R.id.bt_next_step);
    }

    private void initEvent() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行验证密码操作
                IntentSkipUtil.skipToNextActivity(ForgetPasswordActivity.this
                        ,ForgetPasswordNextStepActivity.class);
            }
        });
    }
}
