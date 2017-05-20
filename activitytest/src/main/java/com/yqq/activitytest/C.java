package com.yqq.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class C extends Activity implements View.OnClickListener{

    private Button bt;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c);

        bt = (Button) findViewById(R.id.c);
        bt.setOnClickListener(this);
        et = (EditText) findViewById(R.id.et);
    }

    @Override
    public void onClick(View view) {
//        Intent it = new Intent(C.this,D.class);
//        C.this.startActivity(it);
        ActivityManagerApplication.getInstance().exit();
    }
}
