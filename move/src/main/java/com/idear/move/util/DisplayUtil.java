package com.idear.move.util;

import android.content.Context;

/**
 * 单位转换工具类
 * Created by user on 2017/5/12.
 */
public class DisplayUtil {
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
