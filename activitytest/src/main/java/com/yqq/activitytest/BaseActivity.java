package com.yqq.activitytest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import constant.Constans;

/**
 * Created by user on 2017/1/1.
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /**
     * 为子类添加权限请求方法
     * @param permissions
     * @return
     */
    public boolean hasPermission(String ... permissions){

        for(String permission:permissions){

            if(ContextCompat.checkSelfPermission(this,permission) !=
                    PackageManager.PERMISSION_GRANTED){


                return false;
            }
        }
        return true;
    }

    /**
     * 为子类添加一个权限请求方法
     * @param code
     * @param permissions
     */
    public void requestPermissions(int code,String ... permissions){
        ActivityCompat.requestPermissions(this,permissions,code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case Constans.WRITE_EXTRENAL_CODE:
                if(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    doSDCardPermission();
                } else{
                    ToastUtil.getInstance().showToast(getApplicationContext(),"download fail!");
                }

                break;
            case Constans.CALL_PHONE_CODE:
                if(hasPermission(Manifest.permission.CALL_PHONE)){
                    doCallPhone();
                } else{
                    ToastUtil.getInstance().showToast(getApplicationContext(),"call fail!");
                }

                break;
        }
    }

    /**
     * 默认的打电话权限处理（通过给子类重写）
     */
    public void doCallPhone() {
    }

    /**
     * 默认的写SD卡权限处理
     */
    public void doSDCardPermission() {
    }
}
