package com.yqq.BottomNavigationBar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yqq.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

   public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.one:
                IntentSkipUtil.skipToNextActivity(MainMenuActivity.this,MethodOneActivity.class);
                break;
            case R.id.two:
                IntentSkipUtil.skipToNextActivity(MainMenuActivity.this,MethodTwoActivity.class);
                break;
            case R.id.three:
                IntentSkipUtil.skipToNextActivity(MainMenuActivity.this,MethodThreeActivity.class);
                break;
            case R.id.four:
                IntentSkipUtil.skipToNextActivity(MainMenuActivity.this,MethodFourActivity.class);
                break;
            case R.id.five:
                IntentSkipUtil.skipToNextActivity(MainMenuActivity.this,MethodFiveActivity.class);
                break;
            case R.id.six:
                IntentSkipUtil.skipToNextActivity(MainMenuActivity.this,MethodSixActivity.class);
                break;
        }
    }
}
