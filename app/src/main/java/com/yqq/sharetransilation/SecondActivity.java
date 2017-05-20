package com.yqq.sharetransilation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by user on 2016/12/11.
 */
public class SecondActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          //设置沉浸式状态栏,如果添加了这句话就会导致出现渐变效果，5.0及以后默认具有沉浸状态栏
          //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
          //这是设置沉浸式的NavigationView
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_second);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
    }

    /**
        *设置过场动画的·方法
        */
    private void runExitAnimation() {
                 findViewById(R.id.ll_root).animate()
                .setDuration(1000)
                .translationX(-1000)
                .translationY(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }).start();
    }

    @Override
    public void onBackPressed() {
        runExitAnimation();
    }
}
