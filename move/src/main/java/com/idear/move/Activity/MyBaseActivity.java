package com.idear.move.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.idear.move.Service.NetBroadCastReceiver;
import com.idear.move.util.ToastUtil;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者:geminiyang on 2017/6/24.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class MyBaseActivity extends BaseActivity {

    //网络变化检测广播接收器
    private BroadcastReceiver receiver;

    //权限相关操作
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private static final int SDK_PERMISSION_REQUEST = 127;
    private String camPicPath;//照片保存路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过广播设置网络监听
        receiver = new NetBroadCastReceiver();
        getPermissions();
    }

    @Override
    public void onResume() {
        //注册广播接收器
        IntentFilter filter=new IntentFilter();
        //一条信息触发一次广播接收器
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //注销广播接收器
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @TargetApi(23)
    protected void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("info","Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n") ;
            }
            if (permissions.size() > 0) {
                //请求权限
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /**
         * 权限请求结果回调
         */
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                //初始化操作
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                //批量进行权限申请
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    CAN_WRITE_EXTERNAL_STORAGE = false;
                    ToastUtil.getInstance().showToast(this,"禁用图片权限将导致发送图片功能无法使用！");
                } else {
                    CAN_WRITE_EXTERNAL_STORAGE = true;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean getIfCanWrite() {
        return  CAN_WRITE_EXTERNAL_STORAGE;
    }

}
