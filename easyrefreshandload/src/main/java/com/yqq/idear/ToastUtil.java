package com.yqq.idear;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by user on 2016/12/22.
 */

public class ToastUtil {

    private static Toast toast = null;
    private static ToastUtil toastUtil = null;

    public ToastUtil(){}

    public synchronized static ToastUtil getInstance(){
        if(null == toastUtil){
            toastUtil = new ToastUtil();
        }

        return toastUtil;
    }

    public void showToast(Context context, String string){

        if(toast != null){
            toast.cancel();
        }
            toast = Toast.makeText(context, string,Toast.LENGTH_SHORT);
            toast.show();
    }

    public void showToastTest(Context context){

        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(context, "click",Toast.LENGTH_SHORT);
        toast.show();
    }

}
