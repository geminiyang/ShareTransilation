package com.yqq.idear;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * 网络管理工具类
 * Created by user on 2017/5/10.
 */

public class NetWorkUtil {
    private static final int CMNET = 1;
    private static final int WIFI = 2;
    private static final int CMWAP = 3;


    //判断网络是否可用
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null&&mNetworkInfo.isConnected()) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断WiFi是否打开
    public static boolean isWiFiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI
                &&mWiFiNetworkInfo.isConnected()) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    //判断移动数据是否打开
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (mMobileNetworkInfo != null && mMobileNetworkInfo.isConnected() &&
                    mMobileNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //获取连接类型
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    //判断网络连接是否可用
    public static boolean isNetworkAvailable(Context context) {
        if(context!=null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
            } else {
                NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
                if (networkInfo != null && networkInfo.length > 0) {
                    for (int i = 0; i < networkInfo.length; i++) {
                        if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void isConnected(Context context) {
        ConnectivityManager manger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manger.getActiveNetworkInfo();
        //判断是否有网络连接
        if(info!=null&&info.isConnected()) {
            ToastUtil.getInstance().showToast(context,"网络已连接");
        } else {
            ToastUtil.getInstance().showToast(context,"网络连接失败");
            if(Build.VERSION.SDK_INT >= 11){
                //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            } else {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }

        }
    }

    public static int getAPNType(Context context){

        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null){
            return netType;
        }

        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            Log.e("ExtraInfo", "ExtraInfo is "+networkInfo.getExtraInfo());
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                netType = CMNET;
            } else{
                netType = CMWAP;
            }
        } else if(nType==ConnectivityManager.TYPE_WIFI){
            netType = WIFI;
        }

        return netType;

    }

    public static void setNetwork(Context context) {
        final  Context mContext = context;

        Toast.makeText(mContext, "wifi is closed!", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("网络提示信息");
        builder.setMessage("网络不可用，如果继续，请先设置网络！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 */
                if (Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                mContext.startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }


}
