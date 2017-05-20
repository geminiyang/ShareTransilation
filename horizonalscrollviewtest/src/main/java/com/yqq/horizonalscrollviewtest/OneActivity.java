package com.yqq.horizonalscrollviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_one_avtivity);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.next:
                Intent it = new Intent(OneActivity.this,SecondActivity.class);
                startActivity(it);
                break;
        }
    }
}
