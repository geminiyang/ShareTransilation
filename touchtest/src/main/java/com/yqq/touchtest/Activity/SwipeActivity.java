package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.yqq.touchtest.Fragment.SwipeRefreshLayoutFragment;
import com.yqq.touchtest.R;

public class SwipeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SwipeRefreshLayoutFragment fragment = new SwipeRefreshLayoutFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }
}
