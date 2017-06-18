package com.idear.move.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.idear.move.Activity.PublishRFActivity;
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

    public static void MutiDialogChoice(Context context) {
        new  AlertDialog.Builder(context)
                .setTitle("性别选择" )
                .setMultiChoiceItems(new  String[] {"男", "女" },  null ,  null )
                .setPositiveButton("确定" , null)
                .setNegativeButton("取消" ,  null )
                .show();
    }

    public static void SingleChoiceDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("请选择" )
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(new String[] {"男", "女"},  0 ,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                ).setNegativeButton("取消" ,  null )
                .show();
    }

    public static void classificationDialog(Context context, final View ValuesChangeView) {
        final String [] str = new String[] {"分类1", "分类2","分类3","分类4"};
        new AlertDialog.Builder(context)
                .setTitle("请选择您的分类" )
                .setSingleChoiceItems(str,0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //选择了任意一个按钮需要更新输入文本的值
                                ((TextView)ValuesChangeView).setText(str[which]);
                            }
                        }
                ).setPositiveButton("关闭",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


}
