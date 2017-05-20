package com.idear.move.Thread;

import android.os.Handler;

import java.util.TimerTask;

/**
 * 用来传递控制无限循环广告栏的handler
 * Created by user on 2017/1/26.
 */

public class BannerTimerTask extends TimerTask{

    private Handler handler;

    public BannerTimerTask(Handler handler){
        this.handler = handler;
    }
    @Override
    public void run() {
        handler.sendEmptyMessage(MyHomeLoadingAsyncTask.AUTO_BANNER_CODE);
    }
}
