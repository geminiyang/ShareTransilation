package com.yqq.horizonalscrollviewtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class ThreeActivity extends AppCompatActivity {

    private HSVAdapter hsvAdapter;
    private HSVLinearLayout movieLayout = null;
    private IntentFilter intentFilter = null;
    private BroadcastReceiver receiver = null;
    private int nCount = 0;
    // pic in the drawable
    private Integer[] images = {
            R.mipmap.rina1,R.mipmap.rina2,R.mipmap.rina3,
            R.mipmap.rina4,R.mipmap.rina5,R.mipmap.rina6,
            R.mipmap.rina7,R.mipmap.rina8,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        init();
    }

    private void init() {
        movieLayout = (HSVLinearLayout) findViewById(R.id.my_gallery);
        initAdapterData();

    }

    private void initAdapterData() {
        hsvAdapter = new HSVAdapter(this);
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            // map.put("image", getResources().getDrawable(images[i]));
            map.put("index", (i+1));//代表第张图片，而数组从零开始计数
            hsvAdapter.addObjectItem(map);
        }
        movieLayout.setAdapter(hsvAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver == null) {
            receiver = new UpdateImageReceiver();
        }
        //注册广播接收器只接收特定消息
        registerReceiver(receiver, getIntentFilter());
    }

    /**
     * 创建一个消息过滤器
     * 只接收特定标签的广播信息
     * @return
     */
    private IntentFilter getIntentFilter() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(AppConstant.UPDATE_IMAGE_ACTION);
        }
        return intentFilter;
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.next:
                Intent it = new Intent(ThreeActivity.this,FourActivity.class);
                startActivity(it);
                break;
        }
    }

}

class UpdateImageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(AppConstant.UPDATE_IMAGE_ACTION)) {

            int index = intent.getIntExtra("index", Integer.MAX_VALUE);
            System.out.println("UpdateImageReceiver----" + index);
        }
    }

}
