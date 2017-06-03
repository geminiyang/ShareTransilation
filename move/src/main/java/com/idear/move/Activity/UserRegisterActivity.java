package com.idear.move.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ScrimUtil;
import com.yqq.swipebackhelper.BaseActivity;

public class UserRegisterActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button bt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.title_bar_blue));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bt_next = (Button) findViewById(R.id.bt_next_step);
        findViewById(R.id.ic_arrow_back).setOnClickListener(new View.OnClickListener() {
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
