package com.idear.move.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.idear.move.util.AlertDialogUtil;
import com.idear.move.util.Logger;
import com.idear.move.util.ToastUtil;

/**
 * 只有当网络改变的时候才会 经过广播。
 * 作者:geminiyang on 2017/6/21.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */
public class NetBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Context mContext = context;
        //判断wifi是打开还是关闭
        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){ //此处无实际作用，只是看开关是否开启
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLING:
                    Logger.d("WIFI正在开启");
                    //ToastUtil.getInstance().showToast(mContext,"WIFI正在开启");
                case WifiManager.WIFI_STATE_ENABLED:
                    Logger.d("WIFI已开启");
                    //ToastUtil.getInstance().showToast(mContext,"WIFI已开启");
                case WifiManager.WIFI_STATE_DISABLED:
                    //Logger.d("WIFI已关闭");
                    ToastUtil.getInstance().showToast(mContext,"WIFI已关闭");
                case WifiManager.WIFI_STATE_DISABLING:
                    Logger.d("WIFI正在关闭");
                    //ToastUtil.getInstance().showToast(mContext,"WIFI正在关闭");
            }
        }
        //此处是主要代码，
        //如果是在开启wifi连接和有网络状态下
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(NetworkInfo.State.CONNECTED==info.getState()){
                //连接状态
                Logger.d("网络已连接");
                ToastUtil.getInstance().showToast(mContext,"网络已连接");
                //执行后续代码
                //由于boradCastReciver触发器组件，它和Service服务一样，都是在主线程的，
                //所以，如果你的后续操作是耗时的操作，请new Thread获得AsyncTask等，进行异步操作
            }else{
                AlertDialogUtil.TitleDialog(context, "开启网络", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Build.VERSION.SDK_INT >= 11){
                            //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                            mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        } else {
                            mContext.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }
                });
            }
        }
    }

}
