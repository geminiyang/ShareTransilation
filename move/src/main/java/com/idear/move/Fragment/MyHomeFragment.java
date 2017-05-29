package com.idear.move.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.idear.move.Activity.AllActivityActivity;
import com.idear.move.Activity.FeedbackActivity;
import com.idear.move.Activity.SpreadActivity;
import com.idear.move.Thread.MyHomeLoadingAsyncTask;
import com.idear.move.R;
import com.idear.move.constants.AppConstant;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.Adapter.HSVAdapter;
import com.idear.move.myWidget.HSVLinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/4/26.
 */

public class MyHomeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG = "arg";

    public static MyHomeFragment newInstance(String arg){
        MyHomeFragment fragment = new MyHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private View rootView;

    private MyHomeLoadingAsyncTask myHomeLoadingAsyncTask;

    private HSVAdapter hsvAdapter;
    private HSVLinearLayout mGalleryLayoutOne = null;
    private HSVLinearLayout mGalleryLayoutTwo = null;
    private HSVLinearLayout mGalleryLayoutThree = null;

    private ImageView moreActivity_iv,moreFeedback_iv,moreSpread_iv;

    private IntentFilter intentFilter = null;
    private BroadcastReceiver receiver = null;
    private int nCount = 0;
    // pic in the drawable
    private Integer[] images = {
            R.mipmap.rina8,R.mipmap.rina8,R.mipmap.rina8,R.mipmap.rina8,
            R.mipmap.rina8,R.mipmap.rina8
    };

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_myhome, null);
            initView(rootView);
            myHomeLoadingAsyncTask = new MyHomeLoadingAsyncTask();
            myHomeLoadingAsyncTask.execute(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * 即时销毁，防止异常
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myHomeLoadingAsyncTask.quitBannerTask();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (receiver == null) {
            receiver = new UpdateImageReceiver();
        }
        //注册广播接收器只接收特定消息
        getActivity().registerReceiver(receiver, getIntentFilter());
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


    private void initView(View view) {

        mGalleryLayoutOne = (HSVLinearLayout) view.findViewById(R.id.my_gallery_one);
        mGalleryLayoutTwo= (HSVLinearLayout) view.findViewById(R.id.my_gallery_two);
        mGalleryLayoutThree = (HSVLinearLayout) view.findViewById(R.id.my_gallery_three);

        moreActivity_iv = (ImageView) view.findViewById(R.id.more_activity);
        moreFeedback_iv = (ImageView) view.findViewById(R.id.more_feedback);
        moreSpread_iv = (ImageView) view.findViewById(R.id.more_spread);

        final View v= view;

        new Thread(new Runnable() {
            @Override
            public void run() {
                initAdapterData(v);
            }
        }).start();

        moreActivity_iv.setOnClickListener(this);
        moreFeedback_iv.setOnClickListener(this);
        moreSpread_iv.setOnClickListener(this);
    }

    private void initAdapterData(View view) {
        hsvAdapter = new HSVAdapter(view.getContext());
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            // map.put("image", getResources().getDrawable(images[i]));
            map.put("index", (i+1));//代表第张图片，而数组从零开始计数
            hsvAdapter.addObjectItem(map);
        }
        mGalleryLayoutOne.setAdapter(hsvAdapter,TYPE_ONE);
        mGalleryLayoutTwo.setAdapter(hsvAdapter,TYPE_TWO);
        mGalleryLayoutThree.setAdapter(hsvAdapter,TYPE_THREE);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.more_activity:
                IntentSkipUtil.skipToNextActivity(MyHomeFragment.this.getActivity(),AllActivityActivity.class);
                break;
            case R.id.more_feedback:
                IntentSkipUtil.skipToNextActivity(MyHomeFragment.this.getActivity(),FeedbackActivity.class);
                break;
            case R.id.more_spread:
                IntentSkipUtil.skipToNextActivity(MyHomeFragment.this.getActivity(),SpreadActivity.class);
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
