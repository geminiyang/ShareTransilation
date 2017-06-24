package com.idear.move.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.idear.move.R;
import com.idear.move.Service.ActivityManager;
import com.idear.move.Thread.LogoutThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class UserSettingActivity extends BaseActivity {

    private ImageView iv_back;
    private Button loginOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_user_setting);

        initView();
        initEvent();
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOutOP();
            }
        });
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        loginOutBtn = (Button) findViewById(R.id.log_out_bt);
    }

    private void loginOutOP() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(android.R.drawable.ic_dialog_info);
        dialog.setInverseBackgroundForced(true);
        dialog.setTitle("注销");
        dialog.setMessage("你确定要退出当前程序？");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogoutThread logoutThread = new LogoutThread(UserSettingActivity.this,
                                HttpPath.getUserLogOutPath());
                        logoutThread.setDataGetListener(new DataGetInterface() {
                            @Override
                            public void finishWork(Object obj) {
                                if(obj instanceof ResultType) {
                                    ResultType result = (ResultType) obj;
                                    if(Integer.parseInt(result.getStatus()) == 1) {
                                        ActivityManager.getInstance().finishAllActivities();
                                        finish();
                                    }
                                    ToastUtil.getInstance().showToastInThread(UserSettingActivity.this,
                                            result.getMessage());
                                } else {
                                    ToastUtil.getInstance().showToastInThread(UserSettingActivity.this,
                                            obj.toString());
                                }
                            }

                            @Override
                            public void interrupt(Exception e) {
                                //添加网络错误处理
                                ToastUtil.getInstance().showToastInThread(UserSettingActivity.this,
                                        ErrorHandleUtil.ExceptionToStr(e,UserSettingActivity.this));
                            }
                        });
                        logoutThread.start();
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //可以如此，也可以直接 用dialog 来执行show()
        AlertDialog apk = dialog.create();
        apk.show();
    }
}
