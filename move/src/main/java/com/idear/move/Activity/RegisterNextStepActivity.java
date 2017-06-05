package com.idear.move.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.idear.move.R;
import com.yqq.swipebackhelper.BaseActivity;

/**
 * 检验是否注册成功，验证码是否匹配等操作
 */
public class RegisterNextStepActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_next_step);

        initToolbar();
    }

    private void initToolbar() {
        findViewById(R.id.ic_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
