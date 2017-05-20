package com.yqq.activitytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class D extends Activity  implements View.OnClickListener{

    private Button bt;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d);

        bt = (Button) findViewById(R.id.d);
        bt.setOnClickListener(this);
        et = (EditText) findViewById(R.id.et);
    }

    @Override
    public void onClick(View view) {
        Intent it = new Intent(D.this,B.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//设置可以关掉它所跳到的界面之外已经开启的activity在这里是把C关闭
        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
        //这个方法是将，即将要跳转的页面，提到最上面，顺序从ABCDB->ACDB
        //it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        D.this.startActivity(it);
    }
}
