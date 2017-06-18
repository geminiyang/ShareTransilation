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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.idear.move.R;
import com.idear.move.util.AlertDialogUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.KeyBoardUtils;
import com.idear.move.util.ToastUtil;

import java.util.Calendar;

public class PublishRActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private ImageView iv_back;//返回按钮
    private RelativeLayout rlRoot;
    private EditText activityTimeInput,expireTimeInput;
    //用来拼接日期和时间，最终用来显示的
    private StringBuilder str = new StringBuilder("");

    private CheckedTextView ctv;
    private Button publish;
    private TextView urlTextView;

    private final int MIN = 1;
    private final int MAX = 100;
    private int currentNum = 2;
    // 更新显示当前值的TextView
    private EditText personNum,classification;


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
                AlertDialogUtil.classificationDialog(PublishRActivity.this,classification);
            }
        });
        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件，跳转到一个WEBVIEW
                IntentSkipUtil.skipToNextActivity(PublishRActivity.this,MoveProtocolWebViewActivity.class);
            }
        });
        //招募人数编辑框监听
        personNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNumberPickerDialog();
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
        rlRoot = (RelativeLayout) findViewById(R.id.root_rl);
        classification = (EditText) findViewById(R.id.classification);
        activityTimeInput = (EditText) findViewById(R.id.time_select);

        ctv = (CheckedTextView) findViewById(R.id.check_tv_title);

        personNum = (EditText) findViewById(R.id.edit_personNum);
        classification = (EditText) findViewById(R.id.classification);
        publish = (Button) findViewById(R.id.publish);
        urlTextView = (TextView) findViewById(R.id.tv_url);
        expireTimeInput = (EditText) findViewById(R.id.expire_time_select);
    }

    /**
     * 自定义布局的NumberPicker
     */
    private void showNumberPickerDialog() {
        final Dialog dialog = new Dialog(PublishRActivity.this);
        dialog.setTitle("NumberPicker");

        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.number_picker_dialog);
        Button cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        Button sure = (Button) dialog.findViewById(R.id.bt_sure);
        final NumberPicker mNumberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        mNumberPicker.setMaxValue(MAX); // max value 100
        mNumberPicker.setMinValue(MIN);   // min value 1
        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setOnValueChangedListener(this);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                personNum.setText("");
                personNum.setText(currentNum+"");
                dialog.dismiss(); // dismiss the dialog
            }
        });
        dialog.show();
    }
    /**
     * 系统默认风格的NumberPicker
     */
    private void productNumberPickerDialog() {
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


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        currentNum = newVal;
    }
}

