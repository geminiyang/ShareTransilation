package com.idear.move.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.KeyBoardUtils;
import com.idear.move.util.ToastUtil;

import java.util.Calendar;

public class PublishRActivity extends AppCompatActivity {

    private RelativeLayout rlRoot;
    private EditText editText;
    //用来拼接日期和时间，最终用来显示的
    private StringBuilder str = new StringBuilder("");

    private CheckedTextView ctv;
    private Button publish;
    private TextView urlTextView;

    private NumberPicker numberPicker;
    private final int MIN = 1;
    private final int MAX = 100;
    private int currentNum = 2;
    // 更新显示当前值的TextView
    private EditText personNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_r);
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
        //发布按钮监听器
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctv.isChecked()) {
                    //打勾状态才能提交发布，相关的网络访问操作

                } else {
                    //非打勾状态进行提示
                    ToastUtil.getInstance().showToast(PublishRActivity.this,"请阅读协议并打勾!");
                }
            }
        });

        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件，跳转到一个WEBVIEW
                IntentSkipUtil.skipToNextActivity(PublishRActivity.this,MoveProtocolWebViewActivity.class);
            }
        });
    }

    private void initView() {
        rlRoot = (RelativeLayout) findViewById(R.id.root_rl);
        editText = (EditText) findViewById(R.id.time_select);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTimeDialog();
            }
        });
        ctv = (CheckedTextView) findViewById(R.id.check_tv_title);

        publish = (Button) findViewById(R.id.publish);
        urlTextView = (TextView) findViewById(R.id.tv_url);

        //productNumberPicker();
        productNumberPickerDialog();

    }

    private void productNumberPickerDialog() {
        personNum = (EditText) findViewById(R.id.edit_personNum);
        personNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout linearLayout = new RelativeLayout(PublishRActivity.this);
                final NumberPicker aNumberPicker = new NumberPicker(PublishRActivity.this);
                aNumberPicker.setMaxValue(MAX);
                aNumberPicker.setMinValue(MIN);
                aNumberPicker.setValue(currentNum);

                aNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        currentNum = newVal;
                    }
                });

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                RelativeLayout.LayoutParams numPickerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                numPickerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                linearLayout.setLayoutParams(params);
                linearLayout.addView(aNumberPicker,numPickerParams);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublishRActivity.this);
                alertDialogBuilder.setTitle("选择招募人数");
                alertDialogBuilder.setView(linearLayout);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //更新UI
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                personNum.setText("");
                                                personNum.setText(currentNum+"");
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void productNumberPicker() {
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        // 设置NumberPicker属性
        numberPicker.setMinValue(MIN);
        numberPicker.setMaxValue(MAX);
        numberPicker.setValue(currentNum);
        // 监听数值改变事件
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentNum = newVal;
            }
        });

    }

    private void productTimeDialog() {
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        Dialog dateDialog = new
                DatePickerDialog(PublishRActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                str.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                Calendar time = Calendar.getInstance();

                Dialog timeDialog =
                        new TimePickerDialog(PublishRActivity.this,
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

