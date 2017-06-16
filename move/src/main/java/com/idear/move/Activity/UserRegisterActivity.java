package com.idear.move.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ScrimUtil;
import com.yqq.swipebackhelper.BaseActivity;

public class UserRegisterActivity extends BaseActivity {
    private Button bt_next,getCode;
    private ImageView icBack;

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
    }

    private void initEvent() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //use();
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(UserRegisterActivity.this,
                        RegisterNextStepActivity.class);
            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在线程中进行网络操作验证邮箱
            }
        });
    }

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
