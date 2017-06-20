package com.idear.move.SponsorActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;

public class SponsorRegisterActivity extends AppCompatActivity {

    private Button bt_next,getCode;
    private ImageView icBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(SponsorRegisterActivity.this,
                        SponsorRegisterNextStepActivity.class);
            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在线程中进行网络操作验证邮箱
            }
        });
    }

}
