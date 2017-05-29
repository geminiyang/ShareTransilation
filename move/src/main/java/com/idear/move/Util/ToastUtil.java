package com.idear.move.Util;

import android.content.Context;
import android.widget.Toast;

/**
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

}
