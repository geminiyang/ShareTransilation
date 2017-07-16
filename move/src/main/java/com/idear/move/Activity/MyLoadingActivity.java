package com.idear.move.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;

import java.lang.ref.WeakReference;

public class MyLoadingActivity extends MyBaseActivity implements View.OnClickListener{

    private Button bt;
    private TextView text;
    static int INDEX_IF_COMETONEXT = 1;//这个标志位与handler中的相应what对应

    static class InnerHandler extends Handler {
        WeakReference mActivity;
        public InnerHandler(Activity activity){
            this.mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyLoadingActivity theActivity = (MyLoadingActivity) mActivity.get();
            switch (msg.what){
                case 0:
                    if (msg.arg1 == 0){
                        theActivity.bt.setText("重新获取");
                        theActivity.bt.setClickable(true);
                    }else {
                        theActivity.bt.setText("(" + (msg.arg1) + ")秒");
                        theActivity.bt.setClickable(false);
                    }
                    break;
                case 1:
                    if (msg.arg1 == 0){
                        TurnToNext(theActivity);
                    } else {
                        theActivity.bt.setText("跳过"+"(" + (msg.arg1) + ")秒");
                        theActivity.bt.setClickable(true);
                    }
                    break;
                case 2:
                    //这个方法是用来避免两次通过Intent 去到同一个Activity时，然后Activity On pause出现重复跳转
                    if (msg.arg1 == 0){
                        MyLoadingActivity.INDEX_IF_COMETONEXT = 1;
                    } else {
                        theActivity.bt.setText("跳过"+"(" + (msg.arg1) + ")秒");
                        theActivity.bt.setClickable(true);
                    }
                    break;
            }

        }
        private void TurnToNext(Activity activity) {
            Intent it = new Intent(activity,SelectActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(it);
            activity.finish();
        }
    }

    private InnerHandler innerHandler = new InnerHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_myloading);


        bt = (Button) findViewById(R.id.surface_bt);
        bt.setOnClickListener(this);
        text = (TextView) findViewById(R.id.versionName);

        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo("com.idear.move", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo!=null) {
            String versionName = packageInfo.versionName;//清单文件的版本号
            text.setText("版本号:"+ versionName);
        }
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
        Message msg = innerHandler.obtainMessage();
        msg.arg1 = 0;
        msg.what = INDEX_IF_COMETONEXT;
        innerHandler.sendMessage(msg);
        INDEX_IF_COMETONEXT = 2;
    }

    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 5; i >=0; i--) {
                    Message msg = innerHandler.obtainMessage();
                    msg.arg1 = i;
                    msg.what = INDEX_IF_COMETONEXT;
                    innerHandler.sendMessage(msg);
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
