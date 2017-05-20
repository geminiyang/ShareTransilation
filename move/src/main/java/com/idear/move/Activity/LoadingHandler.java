package com.idear.move.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

/**
 * Created by user on 2016/12/23.
 */

public class LoadingHandler extends Handler{

    private  Activity context;
    private Button button;

    public LoadingHandler(Activity activity, Button bt){
        this.context = activity;
        this.button = bt;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (msg.arg1 == 0){
                        button.setText("重新获取");
                        button.setClickable(true);
                    }else {
                        button.setText("(" + (msg.arg1) + ")秒");
                        button.setClickable(false);
                    }
                    break;
                case 1:
                    if (msg.arg1 == 0){
                        TurnToNext();
                    }else {
                        button.setText("跳过"+"(" + (msg.arg1) + ")秒");
                        button.setClickable(true);
                    }
                    break;
                case 2:
                    //这个方法是用来避免两次通过Intent 去到同一个Activity时，然后Activity On pause出现重复跳转
                    if (msg.arg1 == 0){
                        MyLoadingActivity.INDEX_IF_COMETONEXT = 1;
                    }else {
                        button.setText("跳过"+"(" + (msg.arg1) + ")秒");
                        button.setClickable(true);
                    }
                    break;
            }

    }

    private void TurnToNext() {
        Intent it = new Intent(context,SelectActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(it);
        context.finish();
    }
}
