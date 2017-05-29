package com.idear.move.MyWidget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.idear.move.Util.DateUtil;

import java.util.Calendar;

/**
 * Created by user on 2017/5/14.
 */

public class UpdateTimeTextView  extends android.support.v7.widget.AppCompatTextView{

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UpdateTimeTextView.this.setText((String)msg.obj);
        }
    };
    private String DEFAULT_TIME_FORMAT = "MM月dd月HH:mm";
    private long FirstTime;

    public UpdateTimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        FirstTime = Calendar.getInstance().getTimeInMillis();

                new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    //方法一
//                    SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_TIME_FORMAT);
//                    String str=sdf.format(new Date());
                    //方法二
//                    SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
//                    String time = dateFormatter.format(Calendar.getInstance().getTime());
                    //方法三
                    String time = DateUtil.convertTimeToFormat(FirstTime);
                    handler.sendMessage(handler.obtainMessage(100,time));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setCurrentTimeMills(long value) {
        this.FirstTime = value;
    }

    public long getCurrentTimeMills() {
        return this.FirstTime;
    }

}
