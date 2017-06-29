package com.idear.move.Service;

import android.app.Application;

/**
 * 作者:geminiyang on 2017/6/28.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class MyApplication extends Application {
    //在app开始运行时，创建数据库，并创建对应的数据表：
    //private CommDB comDBHelper;
    @Override
    public void onCreate() {
        super.onCreate();
//        comDBHelper = new CommDB(this);
//        comDBHelper.open();
    }


}
