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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.Activity.AllActivityActivity;
import com.idear.move.Activity.FeedbackActivity;
import com.idear.move.Activity.SpreadActivity;
import com.idear.move.Activity.UserSearchActivity;
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

public class UserHomeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG = "arg";

    public static UserHomeFragment newInstance(String arg){
        UserHomeFragment fragment = new UserHomeFragment();
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

    private TextView moreActivity_tv,moreFeedback_tv,moreSpread_tv;
    private EditText searchEditText;
    private ImageView searchImageView;

    private IntentFilter intentFilter = null;
    private BroadcastReceiver receiver = null;
    private int nCount = 0;
    // pic in the drawable
    private int[] images = {
            R.mipmap.family,R.mipmap.family,R.mipmap.family
    };

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_user_myhome, container,false);
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

        moreActivity_tv = (TextView) view.findViewById(R.id.more_activity);
        moreFeedback_tv = (TextView) view.findViewById(R.id.more_feedback);
        moreSpread_tv = (TextView) view.findViewById(R.id.more_spread);

        searchEditText = (EditText) view.findViewById(R.id.et_search);
        searchImageView = (ImageView) view.findViewById(R.id.iv_search);

        final View v= view;

        initAdapterData();

        moreActivity_tv.setOnClickListener(this);
        moreFeedback_tv.setOnClickListener(this);
        moreSpread_tv.setOnClickListener(this);
        searchEditText.setOnClickListener(this);
        searchImageView.setOnClickListener(this);

    }

    private void initAdapterData() {
        hsvAdapter = new HSVAdapter(UserHomeFragment.this.getContext());
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            // map.put("image", getResources().getDrawable(images[i]));
            map.put("index", (i+1));//代表第张图片，而数组从零开始计数
            hsvAdapter.addObjectItem(map);
        }
        mGalleryLayoutOne.setAdapter(hsvAdapter,TYPE_ONE,180,120);//第三个参数和第四个参数分别为宽和高
        mGalleryLayoutTwo.setAdapter(hsvAdapter,TYPE_TWO,180,120);
        mGalleryLayoutThree.setAdapter(hsvAdapter,TYPE_THREE,180,120);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.more_activity:
                IntentSkipUtil.skipToNextActivity(UserHomeFragment.this.getActivity(),AllActivityActivity.class);
                break;
            case R.id.more_feedback:
                IntentSkipUtil.skipToNextActivity(UserHomeFragment.this.getActivity(),FeedbackActivity.class);
                break;
            case R.id.more_spread:
                IntentSkipUtil.skipToNextActivity(UserHomeFragment.this.getActivity(),SpreadActivity.class);
                break;
            case R.id.et_search:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserSearchActivity.class);
                break;
            case R.id.iv_search:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserSearchActivity.class);
                break;
            default:
                break;
        }
    }
    /**
     * 通过广播来实现轮播的自动刷新
     */
    class UpdateImageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppConstant.UPDATE_IMAGE_ACTION)) {

                int index = intent.getIntExtra("index", Integer.MAX_VALUE);
                System.out.println("UpdateImageReceiver----" + index);
            }
        }

    }
}

