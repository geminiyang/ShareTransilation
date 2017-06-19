package com.idear.move.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.idear.move.Activity.DynamicPublishingActivity;
import com.idear.move.Activity.FriendAddActivity;
import com.idear.move.Adapter.MyDynamicsRvAdapter;
import com.idear.move.POJO.MyDynamicsDataModel;
import com.idear.move.R;
import com.idear.move.Thread.LoadMoreThread;
import com.idear.move.Thread.RefreshThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.util.DateUtil;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.idear.CustomRecyclerView;
import com.yqq.idear.DataStateChangeCheck;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 2017/5/3.
 */

public class DynamicsFragment extends Fragment implements CustomRecyclerView.DataOperation{

    private static final String ARG = "arg";

    public static DynamicsFragment newInstance(String arg){
        DynamicsFragment fragment = new DynamicsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ImageView addFriend,publishDynamics;
    private View rootView;
    private CustomRecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<MyDynamicsDataModel> dataModels = new LinkedList<>();
    private String[] states ={"[审核中]","[进行中]","[筹资中]","[已结束]"};
    private int[] pics ={R.mipmap.family,R.mipmap.family,R.mipmap.family,R.mipmap.family};
    private int[] userIcons ={R.mipmap.paintbox,R.mipmap.paintbox,
            R.mipmap.paintbox,R.mipmap.paintbox};
    private String[] userNames = {"大丸子","二丸子","三丸子","四丸子"};
    private String[] times = {"2017.6.11","2017.6.12","2017.6.13","2017.6.14"};
    private String[] commentOne = {"大丸子","一级棒"};
    private List<String[]> lists = new ArrayList<>();

    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_dynamics,container,false);
            init(rootView);
            initRecyclerView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void init(View view) {
        lists.add(commentOne);
        addFriend = (ImageView) view.findViewById(R.id.iv_add_friend);
        publishDynamics = (ImageView) view.findViewById(R.id.iv_publish);
        myRecyclerView = (CustomRecyclerView) view.findViewById(R.id.my_recycler_view);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(v.getContext(),FriendAddActivity.class);
            }
        });
        publishDynamics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(v.getContext(),DynamicPublishingActivity.class);
            }
        });

    }

    private void initRecyclerView(View view) {
        //获取RecyclerV
        myRecyclerView = (CustomRecyclerView) view.findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new MyDynamicsRvAdapter(getActivity(), dataModels);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);
        //数据状态监听器
        DataStateChangeCheck mDataEndListener = new DataStateChangeCheck(myRecyclerView);
        //实现了数据操作监听
        mDataEndListener.setLoadDataListener(myRecyclerView);
        myRecyclerView.addDataChangeListener(mDataEndListener);
        myRecyclerView.addHeaderView(getActivity());
        myRecyclerView.addFooterView(getActivity());
        myRecyclerView.setTotalCount(TOTAL_COUNT);
        myRecyclerView.setRequestCount(REQUEST_COUNT);
        myRecyclerView.setDataOperation(this);
        myRecyclerView.Initialize();
        //网格布局管理器
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        //网格布局管理器
        myRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onLoadMore() {
        new LoadMoreThread(new DataGetInterface() {
            @Override
            public void finishWork(Object obj) {
            }

            @Override
            public void interrupt(Exception e) {

            }
        }, dataModels).start();
    }

    @Override
    public void onRefresh() {
        new RefreshThread(new DataGetInterface() {
            @Override
            public void finishWork(Object obj) {
                myRecyclerView.finishComplete();
            }

            @Override
            public void interrupt(Exception e) {

            }
        },dataModels).start();
    }
}
