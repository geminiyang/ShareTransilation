package com.yqq.sharetransilation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
        final TextView textView = (TextView) findViewById(R.id.tv_first);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                // 这里指定共享的视图元素
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(FirstActivity.this,
                        textView, "shareAnim");
                ActivityCompat.startActivity(FirstActivity.this,intent, options.toBundle());
                overridePendingTransition(0, 0);
            }
        });

    }

    private void runEnterAnimation() {
        findViewById(R.id.ll).animate()
                .setDuration(1000)
                .translationX(1000)
                .translationY(0)
                .start();
    }
}
