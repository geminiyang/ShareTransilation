package com.idear.move.Activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class MyLoadingActivity extends BaseActivity implements View.OnClickListener{

    private static Button bt;
    Thread thread;

    static int INDEX_IF_COMETONEXT = 1;//这个标志位与handler中的相应what对应

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_myloading);


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
        final LoadingHandler handler = new LoadingHandler(MyLoadingActivity.this,bt);
        Message msg = handler.obtainMessage();
        msg.arg1 = 0;
        msg.what = INDEX_IF_COMETONEXT;
        handler.sendMessage(msg);
        INDEX_IF_COMETONEXT = 2;
    }

    private void sendMessage(){
        final LoadingHandler handler = new LoadingHandler(MyLoadingActivity.this,bt);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 5; i >=0; i--) {
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
