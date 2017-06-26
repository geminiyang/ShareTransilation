package com.idear.move.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.idear.move.Service.ActivityManager;

/**
 * Created by user on 2017/3/31.
 */

public class IntentSkipUtil {
    /**
     * 基础跳转到下一个界面
     * @param context
     * @param cls
     */
    public static void skipToNextActivity(Context context, Class<?> cls){
        Intent it = new Intent(context,cls);
        //防止同一时间调用同一方法导致实例化多一个相同的Activity
        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityManager.getInstance().addActivity((Activity)context);
        context.startActivity(it);
    }

    /**
     * 基础跳转到下一个界面
     * @param context
     * @param cls
     */
    public static void skipToNext(Context context, Class<?> cls){
        Intent it = new Intent(context,cls);
        //防止同一时间调用同一方法导致实例化多一个相同的Activity
        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(it);
    }

    /**
     * 基础跳转到下一个界面,带上数据邮包
     * @param context
     * @param cls
     */
    public static void skipToNextActivityWithBundle(Context context, Class<?> cls,String key,String value){
        Intent it = new Intent(context,cls);
        Bundle bundle = new Bundle();
        bundle.putString(key,value);
        it.putExtras(bundle);
        //防止同一时间调用同一方法导致实例化多一个相同的Activity
        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityManager.getInstance().addActivity((Activity)context);
        context.startActivity(it);
    }
}
