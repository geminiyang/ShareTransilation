package com.idear.move.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import com.idear.move.Activity.MyActivityActivity;
import com.idear.move.Activity.MyDynamicsActivity;
import com.idear.move.Activity.MyFavoritesActivity;
import com.idear.move.Activity.UserDetailInformationActivity;
import com.idear.move.Activity.UserSettingActivity;
import com.idear.move.R;
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

    //ToolBar
    private ImageView iv_setting;

    private ScrollView myScrollView;
    private RelativeLayout rllayout;//含有图片的那个相对布局，顶部控件

    private ImageView ivToUserInfo,ivToDynamics,ivToMyFavorites;

    private GridView gridView;//网格视图
    private List<Map<String, Object>> data_list;//数据源
    private SimpleAdapter simple_adapter;//适配器
    private int[] icon = { R.mipmap.takepart, R.mipmap.trends,R.mipmap.takepart, R.mipmap.trends,
            R.mipmap.takepart};

    private String[] iconName={ "未通过", "审核中","进行中","待反馈","已结束"};

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

        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);

        rllayout = (RelativeLayout)view.findViewById(R.id.rllayout);

        myScrollView = (ScrollView)view.findViewById(R.id.myScrollView);

        ivToUserInfo = (ImageView) view.findViewById(R.id.to_user_info);
        ivToDynamics = (ImageView) view.findViewById(R.id.to_dynamics);
        ivToMyFavorites = (ImageView) view.findViewById(R.id.to_my_favorites);

        //三句代码使界面打开时候自定义ScrollView下面的EditText获取焦点的事件不再发生
        myScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        myScrollView.setFocusable(true);
        myScrollView.setFocusableInTouchMode(true);

        initEvent();
    }

    //初始化监听器
    private void initEvent() {
        ivToUserInfo.setOnClickListener(this);
        ivToDynamics.setOnClickListener(this);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(getActivity(),UserSettingActivity.class);
            }
        });
        ivToMyFavorites.setOnClickListener(this);
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
                IntentSkipUtil.skipToNextActivityWithBundle(getActivity(),
                        MyActivityActivity.class,"select_tab",(String)item.get("text"));
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
            case R.id.to_user_info:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserDetailInformationActivity.class);
                break;
            case R.id.to_dynamics:
                IntentSkipUtil.skipToNextActivity(getActivity(),MyDynamicsActivity.class);
                break;
            case R.id.to_my_favorites:
                IntentSkipUtil.skipToNextActivity(getActivity(),MyFavoritesActivity.class);
                break;
            default:
                break;
        }
    }
}
