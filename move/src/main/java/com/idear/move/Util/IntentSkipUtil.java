package com.idear.move.Util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
}
