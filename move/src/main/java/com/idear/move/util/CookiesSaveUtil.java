package com.idear.move.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yqq.idear.*;
import com.yqq.idear.Logger;

/**
 * 作者:geminiyang on 2017/6/19.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CookiesSaveUtil {

    public static void saveCookies(String cookies, Context context) {
        //根据业务需求截取到cookie
        String[] s1 = cookies.split(";");
        String[] s2= s1[0].split("=");
        String myCookie = s2[1];
        SharedPreferences preferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        preferences.edit().putString("cookies",myCookie).apply();
    }

    public static String getCookies(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        return sharedPreferences.getString("cookies","");
    }
}
