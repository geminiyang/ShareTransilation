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
import com.idear.move.Activity.FeedBackDetailActivity;
import com.idear.move.Adapter.CardLayoutFourAdapter;
import com.idear.move.Adapter.FeedBackRvAdapter;
import com.idear.move.POJO.CardLayoutFourDataModel;
import com.idear.move.POJO.FeedBackDataModel;
import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.idear.CustomRecyclerView;
import com.yqq.idear.DataStateChangeCheck;

import java.util.LinkedList;


public class FeedbackSearchFragment extends Fragment implements
        FeedBackRvAdapter.OnItemClickListener,CustomRecyclerView.DataOperation {

    private OnFragmentInteractionListener mListener;
    private static final String ARG = "arg";

    public static FeedbackSearchFragment newInstance(String arg){
        FeedbackSearchFragment fragment = new FeedbackSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FeedbackSearchFragment() {
        //要求要有一个空的构造函数
    }

    //RecyclerView相关参数
    private CustomRecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 2;

    private LinkedList<FeedBackDataModel> dataModels = new LinkedList<>();
    private String[] titles ={"aa活动","bb活动","cc活动",
            "d活动"};
    private int[] picUrl ={R.drawable.rina,R.drawable.rina,R.drawable.family,R.drawable.family};
    private String[] Num ={"88","12","188","788"};

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
        adapter = new FeedBackRvAdapter(view.getContext(), dataModels);
        ((FeedBackRvAdapter)adapter).setOnItemClickListener(this);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);
        //数据状态监听器
        DataStateChangeCheck mDataEndListener = new DataStateChangeCheck(myRecyclerView);
        //实现了数据操作监听
        mDataEndListener.setLoadDataListener(myRecyclerView);
        myRecyclerView.addDataChangeListener(mDataEndListener);
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
            int index = dataModels.size()%4;
            Log.d("info","refreshMode------" + "LIST SIZE : " + dataModels.size()+1);
            dataModels.addFirst(new FeedBackDataModel(titles[index],"",picUrl[index],Num[index],
                    CustomRecyclerView.TYPE_NORMAL));
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onItemClick(int position) {
        IntentSkipUtil.skipToNextActivity(getActivity(), FeedBackDetailActivity.class);
    }

    /**
     * 此方法可以调用外部Activity中提供的相关运行逻辑(例如信息传递)
     * 当触发此方法时Activity收到相关数据
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFeedbackSearchFragmentInteraction(uri);
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
        void onFeedbackSearchFragmentInteraction(Uri uri);
    }
}
