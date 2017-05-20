package com.yqq.activitytest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SurfaceActivity extends Activity implements View.OnClickListener{

    private static Button bt;
    Thread thread;

    static int INDEX_IF_COMETONEXT = 1;//这个标志位与handler中的相应what对应

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //通过设置全屏达到一体化效果，但是看不到状态信息
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置导航栏透明
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        //通过设置状态栏透明实现一体化效果，但是会出现渐变效果
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        }

        //实现全透明状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //设置状态栏的模式
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }


        setContentView(R.layout.surface);

        bt = (Button) findViewById(R.id.surface_bt);
        bt.setOnClickListener(this);
        sendMessage();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.surface_bt:
                TurnToNext();
                break;
        }
    }

    private void TurnToNext() {
        final com.yqq.activitytest.MyHandler handler = new com.yqq.activitytest.MyHandler(SurfaceActivity.this,bt);
        Message msg = handler.obtainMessage();
        msg.arg1 = 0;
        msg.what = INDEX_IF_COMETONEXT;
        handler.sendMessage(msg);
        INDEX_IF_COMETONEXT = 2;
    }

    private void sendMessage(){
        final com.yqq.activitytest.MyHandler handler = new com.yqq.activitytest.MyHandler(SurfaceActivity.this,bt);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 3; i >=0; i--) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = i;
                    msg.what = INDEX_IF_COMETONEXT;
                    handler.sendMessage(msg);
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
