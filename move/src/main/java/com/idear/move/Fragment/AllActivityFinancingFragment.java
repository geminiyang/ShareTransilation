package com.idear.move.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.idear.move.Adapter.AllActivityRvAdapter;
import com.idear.move.POJO.AllActivityDataModel;
import com.idear.move.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 2017/4/21.
 */

public class AllActivityFinancingFragment extends Fragment {

    private static final String ARG = "arg";

    public static AllActivityFinancingFragment newInstance(String arg){
        AllActivityFinancingFragment fragment = new AllActivityFinancingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;

    private List<AllActivityDataModel> dataModels = new ArrayList<AllActivityDataModel>();
    private String[] usernames ={"yangqiqi","liguancan","liaoyalin"};
    private String[] titles ={"活动1","活动2","活动3"};
    private String[] pics ={"a","b","c","d","e"};
    private String[] visitNum ={"浏览 88 次","浏览 88 次","浏览 88 次"};
    private String[] descriptions ={"abcdefghijklmnopqrstuvwsyz","abcdefghijklmnopqrstuvwsyz","abcdefghijklmnopqrstuvwsyz"};
    private String[] personNum ={"招募人数:10人","招募人数:10人","招募人数:10人"};
    private String[] moneys ={"1000 元","2000 元","3000 元"};

    private Button add,remove;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.recruit_fragment,container,false);
            initView(rootView);
            initRecyclerView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void initView(View view) {
        add = (Button) view.findViewById(R.id.add);
        remove = (Button) view.findViewById(R.id.remove);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() !=usernames.length) {
                    dataModels.add(new AllActivityDataModel(titles[adapter.getItemCount()],
                            descriptions[adapter.getItemCount()], personNum[adapter.getItemCount()],
                            moneys[adapter.getItemCount()], "",
                            usernames[adapter.getItemCount()], pics[adapter.getItemCount()],
                            visitNum[adapter.getItemCount()], AllActivityRvAdapter.TYPE_TWO));
                    //更新视图(添加数据项)
                    myRecyclerView.scrollToPosition(0);
                    adapter.notifyDataSetChanged();
                }
                // 当点击actionbar上的删除按钮时，向adapter中移除最后一个数据并通知刷新
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() != 0) {
                    dataModels.remove(adapter.getItemCount() - 1);
                    myRecyclerView.scrollToPosition(0);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initRecyclerView(View view) {
        dataModels.add(new AllActivityDataModel(titles[0],descriptions[0],personNum[0],moneys[0],
                "",usernames[0],pics[0],visitNum[0],AllActivityRvAdapter.TYPE_TWO));
        dataModels.add(new AllActivityDataModel(titles[1],descriptions[0],personNum[0],moneys[0],
                "",usernames[1],pics[1],visitNum[1],AllActivityRvAdapter.TYPE_TWO));
        //获取RecyclerV
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //使用垂直布局管理器(单次使用)默认垂直
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL,false);
        myRecyclerView.setLayoutManager(layoutManager);
        //初始化自定义适配器
        adapter = new AllActivityRvAdapter(view.getContext(), dataModels);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);
    }

}
