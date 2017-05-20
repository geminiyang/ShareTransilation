package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.RelativeLayout;

import com.yqq.touchtest.R;

public class SwipeRefreshActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout parent;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1==0){
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);
        initView();
        initEvent();
    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        parent = (RelativeLayout) findViewById(R.id.activity_swipe_refresh);
    }

    @Override
    public void onRefresh() {
        Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout");
        // This method performs the actual data-refresh operation.
        // The method calls setRefreshing(false) when it's finished.
        myUpdateOperation();

    }

    private void myUpdateOperation() {
        Snackbar.make(parent,"refreshing.......",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
        Message msg = new Message();
        msg.arg1 = 0;
        mHandler.sendMessageDelayed(msg,2000);
    }
}
