package com.yqq.activitytest;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * 运用单例模式结束所有ACTIVITY
 * Created by user on 2016/12/22.
 */

public class ActivityManagerApplication extends Application{
    //运用list来保存们每一个activity
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static ActivityManagerApplication instance;
    //构造方法
    public ActivityManagerApplication(){}
    //实例化一次
    public synchronized static ActivityManagerApplication getInstance(){
        if (null == instance) {
            instance = new ActivityManagerApplication();
        }
        return instance;
    }
    // 添加 Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


}
