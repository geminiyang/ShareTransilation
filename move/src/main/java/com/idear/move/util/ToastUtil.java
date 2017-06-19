package com.idear.move.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Looper;
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

    public void showToast(Fragment fragment, String string){
        showToast(fragment.getActivity(),string);
    }

    public void showToast(Activity activity, String string){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(activity, string,Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToastTest(Context context){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(context, "click",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToastInThread(Context context,String msg){
        Looper.prepare();
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        toast.show();
        Looper.loop();
    }

    public void showToastTest(Fragment fragment){
        showToastTest(fragment.getActivity());
    }

    public void showToastTest(Activity activity){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(activity, "click",Toast.LENGTH_SHORT);
        toast.show();
    }
}
