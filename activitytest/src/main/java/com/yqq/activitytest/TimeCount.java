package com.yqq.activitytest;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 自定义T
 * Created by user on 2016/12/23.
 */

public class TimeCount extends CountDownTimer{

    private Button button;

    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public TimeCount(Button bt,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.button = bt;
    }

    @Override
    public void onTick(long l) {
        String time = "(" + l/1000 + ")秒";
        setButtonInfo(time,false);
    }

    @Override
    public void onFinish() {
        setButtonInfo("重新获取",true);
    }

    private void setButtonInfo(String content,boolean isClick){
        button.setText(content);
        button.setClickable(isClick);
    }
}
