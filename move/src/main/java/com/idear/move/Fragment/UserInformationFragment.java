package com.idear.move.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.idear.move.Activity.MyInitiateActivity;
import com.idear.move.Activity.UserDetailInformationActivity;
import com.idear.move.Activity.UserSettingActivity;
import com.idear.move.R;
import com.idear.move.Service.ActivityManager;
import com.idear.move.util.IntentSkipUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by user on 2017/4/21.
 */

public class UserInformationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG = "arg";

    private ScrollView myScrollView;
    private RelativeLayout rllayout;//含有图片的那个相对布局，顶部控件

    private ImageView ivComeUserInfo,ivComeUserSetting;
    private Button loginOutBtn;

    private GridView gridView;//网格视图
    private List<Map<String, Object>> data_list;//数据源
    private SimpleAdapter simple_adapter;//适配器
    private int[] icon = { R.mipmap.takepart, R.mipmap.trends,R.mipmap.feedback};

    private String[] iconName={ "我的参与", "我的动态", "我的反馈"};

    public static UserInformationFragment newInstance(String arg){
        UserInformationFragment fragment = new UserInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_userinformation,container,false);
            init(rootView);
            initGirdView(rootView);
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

        loginOutBtn = (Button) view.findViewById(R.id.log_out_bt);

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

        loginOutBtn.setOnClickListener(this);
    }


    private void initGirdView(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_view);
        data_list = new ArrayList<Map<String,Object>>();
        data_list = get_data();//获取数据
        //新建适配器
        String [] from ={"image","text"};
        final int [] to = {R.id.image,R.id.text};
        simple_adapter = new SimpleAdapter(view.getContext(), data_list, R.layout.user_info_setting_item, from, to);
        //配置适配器
        gridView.setAdapter(simple_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item= (HashMap<String, Object>) parent.getItemAtPosition(position);
                Toast toast = Toast.makeText(view.getContext(),(String)item.get("text"),Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                switch (position) {
                    case 0:
                        IntentSkipUtil.skipToNextActivity(getActivity(),MyInitiateActivity.class);
                        break;
                }
            }
        });
    }



    private List<Map<String, Object>> get_data() {
        //icon和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;

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
            case R.id.log_out_bt:
                loginOutOP();
                break;
            default:
                break;
        }
    }

    private void loginOutOP() {
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(getActivity());
        dialog1.setTitle("Tips");
        dialog1.setMessage("Are you sure quit the App?");
        dialog1.setIcon(android.R.drawable.ic_dialog_email);
        dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setIcon(android.R.drawable.ic_dialog_info);
        dialog.setInverseBackgroundForced(true);
        dialog.setTitle("注销");
        dialog.setMessage("你确定要退出当前程序？");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityManager.getInstance().finishAllActivities();
                        getActivity().finish();
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //可以如此，也可以直接 用dialog 来执行show()
        AlertDialog apk = dialog.create();
        apk.show();
    }

}
