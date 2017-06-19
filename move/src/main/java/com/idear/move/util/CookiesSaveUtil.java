package com.idear.move.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者:geminiyang on 2017/6/19.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CookiesSaveUtil {

    public static void saveCookies(String cookies, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        preferences.edit().putString("cookies",cookies).apply();
    }

    public static String getCookies(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        return sharedPreferences.getString("cookies","");
    }
}
