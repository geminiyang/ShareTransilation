package com.idear.move.SponsorActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.idear.move.Activity.RegisterNextStepActivity;
import com.idear.move.Activity.UserRegisterActivity;
import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;

public class SponsorRegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button bt_next;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_register);

        initView();
        initEvent();
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(SponsorRegisterActivity.this,
                        RegisterNextStepActivity.class);
            }
        });
    }

    private void initView() {
        bt_next = (Button) findViewById(R.id.bt_next_step);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
    }
}
