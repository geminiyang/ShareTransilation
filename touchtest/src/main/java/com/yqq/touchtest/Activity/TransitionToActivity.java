package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.yqq.touchtest.R;

public class TransitionToActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_to);
    }
}
