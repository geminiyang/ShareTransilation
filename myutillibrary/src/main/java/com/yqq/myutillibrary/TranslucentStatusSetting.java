package com.yqq.myutillibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 设置沉浸状态栏的工具类
 * @author yqq
 *
 */
public class TranslucentStatusSetting {

	
	public static void setTranslucentStatusSetting(Activity context,int statusBarColor){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
            //setTranslucentStatus(true,context);//相当于后面两句
            //设置沉浸状态栏
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
           //设置导航栏透明
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

				//API LEVEL>5.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS  
            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);               
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                              window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置默认状态栏和导航栏颜色
            window.setStatusBarColor(statusBarColor);
            window.setNavigationBarColor(Color.BLACK);
        }

        //设置状态栏的字体模式 设置为黑字
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           context.getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        
	}


    public static  void setIfHaveTitle(boolean flag, Activity context){
        if(!flag){
//            //第一种方法(不适合Material主题)
//            context.getWindow().requestFeature(Window.FEATURE_NO_TITLE);//设置为无标题栏
            //隐藏ActionBar，第二种方法
        android.app.ActionBar actionBar = context.getActionBar();
        actionBar.hide();
        }
    }

	
	@TargetApi(19)
    private static void setTranslucentStatus(boolean on,Activity context) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
