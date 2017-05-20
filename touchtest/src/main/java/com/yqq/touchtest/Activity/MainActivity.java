package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yqq.touchtest.MyWidget.MyEditText;
import com.yqq.touchtest.MyWidget.MyTextView;
import com.yqq.touchtest.MyWidget.ProgressView;
import com.yqq.touchtest.R;
import com.yqq.touchtest.Util.DisplayUtils;
import com.yqq.touchtest.Util.TranslucentStatusSetting;

public class MainActivity extends Activity implements View.OnClickListener,View.OnTouchListener{

    private MyTextView myTextView;
    private static  final String TAG = "MainActivity";
    private MyEditText username;
    private MyEditText password;

    private Button bt,btone,bttwo,show,btthree,btfour,btfive,btsix;
    private ProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(MainActivity.this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#292929"));
        }
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView() {
        myTextView = (MyTextView) findViewById(R.id.mytextview);
        bt = (Button) findViewById(R.id.bt);

        username=(MyEditText)findViewById(R.id.username);
        password=(MyEditText)findViewById(R.id.password);

        btone = (Button) findViewById(R.id.bt_one);
        bttwo = (Button) findViewById(R.id.bt_two);
        btthree = (Button) findViewById(R.id.bt_three);
        btfour = (Button) findViewById(R.id.bt_four);
        btfive = (Button) findViewById(R.id.bt_five);
        btsix = (Button) findViewById(R.id.bt_six);
        show = (Button) findViewById(R.id.show);

        progressView = (ProgressView) findViewById(R.id.progressView);
    }

    private void initEvent() {
        username.setBackground(R.mipmap.ic_launcher);
        password.setBackground(R.mipmap.ic_launcher);
        username.setEditTextHint("请输入账号",12);
        username.setEditTextHintColor(Color.parseColor("#ff0000"));
        username.setEditTextTextColor(Color.parseColor("#ff0000"));
        password.setHint("请输入密码");
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        myTextView.setOnClickListener(this);
        myTextView.setOnTouchListener(this);

        btone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.startRun();
            }
        });
        bttwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayUtils util = new DisplayUtils(MainActivity.this);
                util.showTips(MainActivity.this);
            }
        });
        btthree.setOnClickListener(this);
        btfour.setOnClickListener(this);
        btfive.setOnClickListener(this);
        btsix.setOnClickListener(this);
        show.setOnClickListener(this);
        bt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DisplayUtils util = new DisplayUtils(MainActivity.this);
                util.show();
                return false;
            }
        });

        progressView.setContent(R.layout.progressview);//设置视图
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"dispatchTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG,"dispatchTouchEvent ACTION_CANCEL");
                break;
            default:
                break;

        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"onTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG,"onTouchEvent ACTION_CANCEL");
                break;
            default:
                break;

        }
        return super.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mytextview:
                progressView.stopRun();
                Log.e(TAG,"MyTextView onClick");
                break;
            case R.id.show:
                showMyDialog();
                break;
            case R.id.bt_three:
                Intent it = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(it);
                break;
            case R.id.bt_four:
                Intent it2 = new Intent(MainActivity.this,ThreeActivity.class);
                startActivity(it2);
                break;
            case R.id.bt_five:
                Intent it3 = new Intent(MainActivity.this,ScrollActivity.class);
                startActivity(it3);
                break;
            case R.id.bt_six:
                Intent it4 = new Intent(MainActivity.this,RoundImgActivity.class);
                startActivity(it4);
                break;
            default:
                break;
        }

    }



    private void showMyDialog() {
        //普通提示框
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        LayoutInflater factory = LayoutInflater.from(this);
//        final View dialog = factory.inflate(R.layout.dialog,null);
//        builder.setView(dialog);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.create().show();

        //进度提示框
        ProgressDialog myProgressDialog = new ProgressDialog(MainActivity.this);
        myProgressDialog.setTitle("ProgressDialog");
        myProgressDialog.setMessage("Loading……");
        myProgressDialog.setCancelable(true);//设置点击对话框外部区域不关闭对话框
        myProgressDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.mytextview:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG,"MyTextView onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG,"MyTextView onTouch ACTION_UP");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG,"MyTextView onTouch ACTION_MOVE");
                        break;
                    default:
                        break;

                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DisplayUtils utils = new DisplayUtils(MainActivity.this);
        utils.hide();
    }
}
