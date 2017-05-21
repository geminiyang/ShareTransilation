package com.idear.move.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.idear.move.R;

import java.util.Calendar;

public class PublishFActivity extends AppCompatActivity {

    private CheckBox cb_goods,cb_moneys;
    // 更新显示当前值的TextView
    private EditText moneyAmount;

    private EditText editText;
    //用来拼接日期和时间，最终用来显示的
    private StringBuilder str = new StringBuilder("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubulish_f);

        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.time_select);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTimeDialog();
            }
        });

        moneyAmount = (EditText) findViewById(R.id.money_amount);

        cb_goods = (CheckBox) findViewById(R.id.cb_goods);
        cb_moneys = (CheckBox) findViewById(R.id.cb_money);
        cb_moneys.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    moneyAmount.setVisibility(View.VISIBLE);
                } else {
                    moneyAmount.setVisibility(View.GONE);
                }
            }
        });
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