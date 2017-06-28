package com.idear.move.SponsorFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.idear.move.Activity.UserDetailInformationActivity;
import com.idear.move.Activity.UserSettingActivity;
import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;


/**
 * Created by user on 2017/4/21.
 */

public class SponsorInformationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG = "arg";

    private ScrollView myScrollView;
    private RelativeLayout rllayout;//含有图片的那个相对布局，顶部控件

    private ImageView ivComeUserInfo,ivComeUserSetting;

    public static SponsorInformationFragment newInstance(String arg){
        SponsorInformationFragment fragment = new SponsorInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_sponsorinformation,container,false);
            init(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    /**
     * 初始化控件
     */
    private void init(View view) {
        rllayout = (RelativeLayout)view.findViewById(R.id.rllayout);

        myScrollView = (ScrollView)view.findViewById(R.id.myScrollView);

        ivComeUserInfo = (ImageView) view.findViewById(R.id.image_come_userInfo);
        ivComeUserSetting = (ImageView) view.findViewById(R.id.image_come_setting);

        //三句代码使界面打开时候自定义ScrollView下面的EditText获取焦点的事件不再发生
        myScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        myScrollView.setFocusable(true);
        myScrollView.setFocusableInTouchMode(true);

        initEvent();
    }

    //初始化监听器
    private void initEvent() {
        ivComeUserInfo.setOnClickListener(this);
        ivComeUserSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.image_come_userInfo:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserDetailInformationActivity.class);
                break;
            case R.id.image_come_setting:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserSettingActivity.class);
                break;
            default:
                break;
        }
    }
}
