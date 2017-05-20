package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.yqq.touchtest.MyWidget.RoundImageViewBitmapShader;
import com.yqq.touchtest.R;

public class RoundImgActivity extends Activity{

    private RoundImageViewBitmapShader RIVBS1;
    private int one,two,three,four;
    private int numMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_img);

        RIVBS1 = (RoundImageViewBitmapShader) findViewById(R.id.ffff);
        RIVBS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RIVBS1.setBorderRadius(20);
            }
        });

    }
}
