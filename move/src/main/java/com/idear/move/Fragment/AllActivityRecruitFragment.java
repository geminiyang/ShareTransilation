package com.idear.move.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Activity.ActivityDetailActivity;
import com.idear.move.Adapter.CardLayoutFourAdapter;
import com.idear.move.POJO.CardLayoutFourDataModel;
import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.idear.CustomRecyclerView;
import com.yqq.idear.DataStateChangeCheck;

import java.util.LinkedList;



public class AllActivityRecruitFragment extends Fragment implements
        CardLayoutFourAdapter.OnItemClickListener,CustomRecyclerView.DataOperation {

    private OnFragmentInteractionListener mListener;
    private static final String ARG = "arg";

    public static AllActivityRecruitFragment newInstance(String arg){
        AllActivityRecruitFragment fragment = new AllActivityRecruitFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    public AllActivityRecruitFragment() {
        //要求要有一个空的构造函数
    }

    //RecyclerView相关参数
    private CustomRecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 9;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 3;

    private LinkedList<CardLayoutFourDataModel> dataModels = new LinkedList<>();
    private String[] titles ={"活动1","活动2","活动3"};
    private int[] activityPics ={R.drawable.family,R.drawable.rina,R.drawable.family};
    private String[] visitNums ={"浏览 88 次","浏览 188 次","浏览 881 次"};
    private String[] favoriteNums = {"10","20","60"};
    private String[] personNums ={"招募人数:10人","招募人数:10人","招募人数:10人"};
    private String[] moneyNums ={"招募金额:1000 元","招募金额:2000 元","招募金额:3000 元"};
    private String[] activityStates = {"[进行中]","[招募完成]","[已结束]"};


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_in_all_activity,container,false);
            initView(rootView);
            initEvent(rootView);
            initRecyclerView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void initEvent(View rootView) {

    }

    private void initView(View rootView) {

    }

    private void initRecyclerView(View view) {
        //获取RecyclerV
        myRecyclerView = (CustomRecyclerView) view.findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //使用垂直布局管理器(单次使用)默认垂直
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL,false);
        myRecyclerView.setLayoutManager(layoutManager);
        //初始化自定义适配器
        adapter = new CardLayoutFourAdapter(view.getContext(), dataModels);
        ((CardLayoutFourAdapter)adapter).setOnItemClickListener(this);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);

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
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%3;
            Log.d("info","loadMode------" + "LIST SIZE : " + dataModels.size()+1);
            dataModels.add(new CardLayoutFourDataModel(titles[index],personNums[index],"",
                    visitNums[index],favoriteNums[index],"",activityStates[index],activityPics[index],
                    CustomRecyclerView.TYPE_NORMAL));
        }
    }

    @Override
    public void onRefresh() {
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%3;
            Log.d("info","refreshMode------" + "LIST SIZE : " + dataModels.size()+1);
            dataModels.addFirst(new CardLayoutFourDataModel(titles[index],personNums[index],"",
                    visitNums[index],favoriteNums[index],"",activityStates[index],activityPics[index],
                    CustomRecyclerView.TYPE_NORMAL));
        }
    }

    @Override
    public void onItemClick(int position) {
        IntentSkipUtil.skipToNextActivity(getActivity(), ActivityDetailActivity.class);
    }

    /**
     * 此方法可以调用外部Activity中提供的相关运行逻辑(例如信息传递)
     * 当触发此方法时Activity收到相关数据
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRecruitFragmentInteraction(uri);
        }
    }
    /**
     * 此接口必须由包含此Fragment的Activity来实现，以允许在此Fragment与包含其的Activity和潜在的其他Fragment交互。
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onRecruitFragmentInteraction(Uri uri);
    }
}
