package com.yqq.touchtest.Activity;

import android.os.Bundle;
import android.app.Activity;


public class SampleActivityBase extends Activity {

    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        initializeLogging();
    }

    public void initializeLogging() {
    }
}
