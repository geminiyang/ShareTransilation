package com.idear.move.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.idear.move.R;
import com.idear.move.util.AlertDialogUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.KeyBoardUtils;
import com.idear.move.util.ToastUtil;

import java.util.Calendar;

public class PublishFActivity extends AppCompatActivity {

    private ImageView iv_back;//返回按钮
    // 更新显示当前值的TextView
    private EditText moneyAmount,classification;
    private CheckedTextView ctv;
    private Button publish;
    private TextView urlTextView;

    private EditText activityTimeInput,expireTimeInput;
    //用来拼接日期和时间，最终用来显示的
    private StringBuilder str = new StringBuilder("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubulish_f);

        initView();
        initEvent();
    }

    private void initEvent() {
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctv.toggle();
            }
        });
        //时间编辑框监听
        activityTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTimeDialog();
            }
        });
        expireTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTimeDialog();
            }
        });
        //分类功能
        classification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtil.classificationDialog(PublishFActivity.this,classification);
            }
        });
        //发布按钮监听器
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctv.isChecked()) {
                    //打勾状态才能提交发布，相关的网络访问操作

                } else {
                    //非打勾状态进行提示
                    ToastUtil.getInstance().showToast(PublishFActivity.this,"请阅读协议并打勾!");
                }
            }
        });

        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件，跳转到一个WEBVIEW
                IntentSkipUtil.skipToNextActivity(PublishFActivity.this,MoveProtocolWebViewActivity.class);
            }
        });
        //返回按钮监听
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        activityTimeInput = (EditText) findViewById(R.id.time_select);
        expireTimeInput = (EditText) findViewById(R.id.expire_time_select);
        ctv = (CheckedTextView) findViewById(R.id.check_tv_title);
        publish = (Button) findViewById(R.id.publish);
        urlTextView = (TextView) findViewById(R.id.tv_url);
        moneyAmount = (EditText) findViewById(R.id.money_amount);
        classification = (EditText) findViewById(R.id.classification);
    }

    private void productTimeDialog() {
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        Dialog dateDialog = new
                DatePickerDialog(PublishFActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                str.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                Calendar time = Calendar.getInstance();

                Dialog timeDialog =
                        new TimePickerDialog(PublishFActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog, //在这里指定样式
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                        str.append(hourOfDay + ":" + minute);
                                        EditText show = (EditText) findViewById(R.id.time_select);
                                        show.setText("");
                                        show.setText(str);
                                    }
                                }
                                // 设置初始时间
                                ,time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true);
                timeDialog.setTitle("请选择日期");
                timeDialog.show();
            }
        }
                // 设置初始日期
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH));
        dateDialog.setTitle("请选择日期");
        dateDialog.show();
    }
}
