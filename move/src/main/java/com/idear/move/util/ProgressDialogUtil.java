package com.idear.move.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.idear.move.R;

/**
 * 系统默认效果的ProgressDialog
 *
 * 作者:geminiyang on 2017/5/24.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class ProgressDialogUtil {

    private static ProgressDialog progressDialog;

    /**
     * 显示正在加载框
     * @param context 上下文
     * @param message 显示信息
     * @param isCancelable 是否可取消
     */
    public static void showLoadingDialog(Context context, String message, boolean isCancelable) {
        if(progressDialog==null) {
            progressDialog = new ProgressDialog(context, R.style.dialog);
            //设置点击加载框外面的区域不会退出progressDialog
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(isCancelable);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
    }

    /**
     * 关闭正在加载框
     */
    public static void closeLoadingDialog() {
        if(progressDialog!=null) {
            if(progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            progressDialog = null;
        }
    }

}
