package com.idear.move.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.idear.move.R;

import java.util.Calendar;

public class PublishRFActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private CheckBox cb_goods,cb_money;
    // 更新显示当前值的TextView
    private EditText personNum,moneyAmount;

    private EditText editText;
    //用来拼接日期和时间，最终用来显示的
    private StringBuilder str = new StringBuilder("");
    private final int MIN = 1;
    private final int MAX = 100;
    private int currentNum = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_rf);

        initView();
        initEvent();
    }

    private void initView() {
        cb_goods = (CheckBox) findViewById(R.id.cb_goods);
        cb_money = (CheckBox) findViewById(R.id.cb_money);

        moneyAmount = (EditText) findViewById(R.id.money_amount);
        personNum = (EditText) findViewById(R.id.edit_personNum);

        editText = (EditText) findViewById(R.id.time_select);
    }

    private void initEvent() {
        cb_goods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        cb_money.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    moneyAmount.setVisibility(View.VISIBLE);
                } else {
                    moneyAmount.setVisibility(View.GONE);
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTimeDialog();
            }
        });
        
        personNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog();
            }
        });
    }

    protected void showDialog_First(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(R.layout.number_picker_dialog,null);
        AlertDialog.Builder  dialog = new AlertDialog.Builder(context);
        dialog.setView(dialog_layout);
        final AlertDialog display = dialog.create();
        //点击空白区域会退出dialog,true时候会退出
        display.setCanceledOnTouchOutside(true);

        display.show();

        //dialog中的修改按钮
        Button cancel = (Button) dialog_layout.findViewById(R.id.bt_cancel);
        Button sure = (Button) dialog_layout.findViewById(R.id.bt_sure);

        cancel.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                display.cancel();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personNum.setText("");
                personNum.setText(currentNum+"");
                display.cancel();
            }
        });
    }

    private void showNumberPickerDialog() {
        final Dialog dialog = new Dialog(PublishRFActivity.this);
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

    private void productTimeDialog() {
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        Dialog dateDialog = new
                DatePickerDialog(PublishRFActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                str.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                Calendar time = Calendar.getInstance();

                Dialog timeDialog =
                        new TimePickerDialog(PublishRFActivity.this,
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
