package com.yqq.activitytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class A extends Activity implements View.OnClickListener{

    private static Button bt,test,test2,test3;
    private EditText et;
    public static int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);

        bt = (Button) findViewById(R.id.a);
        test = (Button) findViewById(R.id.test);
        test2 = (Button) findViewById(R.id.test_two);
        test3 = (Button) findViewById(R.id.test_three);
        bt.setOnClickListener(this);
        test.setOnClickListener(this);
        test2.setOnClickListener(this);
        test3.setOnClickListener(this);
        et = (EditText) findViewById(R.id.et);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.a:
                getWindow().setStatusBarColor(getResources().getColor(R.color.red));
                Intent it = new Intent(A.this,B.class);
                A.this.startActivity(it);
                ActivityManagerApplication.getInstance().addActivity(A.this);
                break;
            case R.id.test:
                sendMessageClick();
                break;
            case R.id.test_three:
                sendMessage();
                break;
            case R.id.test_two:
                new TimeCount(test2,10000,1000).start();
                break;
            default:
                break;
        }

    }


    private void sendMessage(){
        final com.yqq.activitytest.MyHandler handler = new com.yqq.activitytest.MyHandler(A.this,test3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 10; i >=0; i--) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = i;
                    msg.what = 0;
                    handler.sendMessage(msg);
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }


    private void sendMessageClick(){
        final MyHandler myHandler = new MyHandler(A.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 10; i >=0; i--) {
                    Message msg = myHandler.obtainMessage();
                    msg.arg1 = i;
                    myHandler.sendMessage(msg);
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        final MyHandler handler = new MyHandler(A.this);
        Message msg = handler.obtainMessage();
        msg.arg1 = i++;
        msg.what = 1;
        handler.sendMessage(msg);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将询问是否退出的计数器置为空值
        i= 0;
    }

    /**
     * 避免内存泄漏
     */
    private static class MyHandler extends Handler {
        private  WeakReference<A> weakReference;

        public MyHandler(A a){
            weakReference = new WeakReference<>(a);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            A a = weakReference.get();
            if(a!= null){
                switch (msg.what){
                    case 0:
                        if (msg.arg1 == 0){
                            test.setText("重新获取");
                            //test.setBackgroundColor(Color.parseColor("#666600"));
                            test.setClickable(true);
                        }else {
                            test.setText("(" + (msg.arg1) + ")秒");
                            //test.setBackgroundColor(Color.parseColor("#666666"));
                            test.setClickable(false);
                        }
                        break;
                    case 1:
                        if (msg.arg1 == 0){
                            ToastUtil.getInstance().showToast(a,"再按一次退出程序");
                        }else {
                            a.finish();
                        }
                        break;

                }
            }
        }
    }


}
