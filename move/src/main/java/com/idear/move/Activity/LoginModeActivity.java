package com.idear.move.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idear.move.R;
import com.idear.move.SponsorActivity.SponsorLoginActivity;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.myutillibrary.BitmapToRoundUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;
import com.yqq.swipebackhelper.SwipeBackHelper;

public class LoginModeActivity extends BaseActivity {

    private ImageButton modeOne,modeTwo;
    private View viewOne,viewTwo;
    private RelativeLayout rl;
    private Drawable drawable;
    private TextView text;

//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.arg1==1){
//                rl.setBackground(drawable);
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.title_bar_blue));
        setContentView(R.layout.activity_login_mode);
        initView();
        initEvent();



        //设置当前Activity不能够滑动返回
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);

    }

    private void initEvent() {
        viewOne .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(LoginModeActivity.this,SponsorLoginActivity.class);
            }
        });
        viewTwo .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(LoginModeActivity.this,UserLoginActivity.class);
            }
        });


        //ProductRoundImage();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rina);
//                drawable =new BitmapDrawable(getResources(),ImageBlur.doBlur(bmp,25,false));
//                Message msg = new Message();
//                msg.arg1 = 1;
//                handler.sendMessage(msg);
//            }
//        };
//        Thread thread = new Thread(run);
//
//        if(!thread.isAlive()){
//            thread.start();
//        }

        //setContent();
    }

    private void setContent() {
        SpannableString txt = new SpannableString("选择更适合职业");
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan04 = new RelativeSizeSpan(1.8f);
        RelativeSizeSpan sizeSpan05 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan06 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan07 = new RelativeSizeSpan(1.2f);

        txt.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txt.setSpan(sizeSpan02, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txt.setSpan(sizeSpan03, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txt.setSpan(sizeSpan04, 3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txt.setSpan(sizeSpan05, 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txt.setSpan(sizeSpan06, 5, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txt.setSpan(sizeSpan07, 6, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        text.setText(txt);
    }

    private void ProductRoundImage() {

        Bitmap srcBitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);//默认为黑色
        Bitmap pictureBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bg);
        srcBitmap.eraseColor(Color.parseColor("#00aaff"));//填充颜色
        Bitmap roundBitmap;
        //方法1:生成圆形bitmap
        if(Build.VERSION.SDK_INT>=23){
            roundBitmap = BitmapToRoundUtil.toRoundBitmap(pictureBitmap,
                    getResources().getColor(R.color.blue_light,getTheme()));
        } else{
            roundBitmap = BitmapToRoundUtil.toRoundBitmap(pictureBitmap,
                    getResources().getColor(R.color.blue_light));
        }

        Drawable round = new BitmapDrawable(getResources(),roundBitmap);
        modeOne.setBackground(round);
        //方法2:生成圆形bitmap
        RoundedBitmapDrawable roundTwo = BitmapToRoundUtil.makeRoundBitmap(getResources(),pictureBitmap);
        modeTwo.setBackground(roundTwo);
    }

    private void initView() {
        modeOne = (ImageButton) findViewById(R.id.modeone);
        modeTwo = (ImageButton) findViewById(R.id.modetwo);
        rl = (RelativeLayout) findViewById(R.id.rl_layout);

        viewOne = findViewById(R.id.view_one);
        viewTwo = findViewById(R.id.view_two);

        text = (TextView) findViewById(R.id.text);
    }

    /**
     第一个登陆模式是普通用户
     第二个登陆模式是赞助商
     */
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.modeone:
                //API 23
                IntentSkipUtil.skipToNextActivity(LoginModeActivity.this,SponsorLoginActivity.class);
                break;
            case R.id.modetwo:
                IntentSkipUtil.skipToNextActivity(LoginModeActivity.this,UserLoginActivity.class);
                break;
        }
    }
}
