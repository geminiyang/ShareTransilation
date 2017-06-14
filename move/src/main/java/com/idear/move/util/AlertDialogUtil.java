package com.idear.move.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.idear.move.R;
import com.idear.move.myWidget.LoadingProgressDialog;

/**
 * Created by user on 2017/5/10.
 */

public class AlertDialogUtil {

    public static void NewDialog(Context context){
        //生成一个默认弹出框
        AlertDialog.Builder dialog_test = new AlertDialog.Builder(context)
                .setPositiveButton("确认", null).setMessage("消息").setTitle("");
        AlertDialog display_test = dialog_test.create();
        display_test.show();
    }

    public static void NewDialogInBroadCast(Context context) {
        //生成一个默认弹出框
        AlertDialog.Builder dialog_test = new AlertDialog.Builder(context)
                .setPositiveButton("确认", null).setMessage("消息").setTitle("");
        AlertDialog display_test = dialog_test.create();
        display_test.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        display_test.show();
    }

    public static void NewCustomDialog(Context context, int layoutId) {
        //生成一个自定义弹出框
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(layoutId,null);
        //可以通过findViewById设置监听事件
        AlertDialog.Builder  dialog = new AlertDialog.Builder(context);
        dialog.setView(dialog_layout);
        final AlertDialog display = dialog.create();
        //点击空白区域会退出dialog,true时候会退出
        display.setCanceledOnTouchOutside(false);
        display.show();
    }

}
