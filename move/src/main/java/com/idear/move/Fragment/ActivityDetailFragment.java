package com.idear.move.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Adapter.ActivityDetailAdapter;
import com.idear.move.Adapter.FeedbackItemAdapter;
import com.idear.move.POJO.ActivityDetailContentDataModel;
import com.idear.move.POJO.FeedbackItemDataModel;
import com.idear.move.R;
import com.idear.move.util.DateUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.LinkedList;

public class ActivityDetailFragment extends Fragment  {
    private static final String ARG = "param1";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<ActivityDetailContentDataModel> dataModels = new LinkedList<>();
    private long TimeStamp = 1497626094;

    public ActivityDetailFragment() {
        //要求要有一个空的构造函数
    }

    /**
     * 使用提供的参数和工厂方法去创建一个fragment实例
     * Activity->Fragment
     */
    public static ActivityDetailFragment newInstance(String values) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG, values);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragement_activity_detail, container, false);
            init(rootView);
            initRecyclerView(rootView);
            initData();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initData() {
        ActivityDetailContentDataModel dataModel = new ActivityDetailContentDataModel();
        //对应网络访问操作
        dataModel.setActivityName("打篮球");
        dataModel.setActivityState("[筹资完成]");
        dataModel.setActivityContent(getString(R.string.testString));
        dataModel.setPublishUsername("123、快跑");
        dataModel.setActivityTime("打篮球活动");
        dataModel.setActivityLocation("广东警官学院篮球场");
        dataModel.setActivityMeaning("锻炼身体");
        dataModel.setActivityPersonNum("5人");
        dataModel.setActivityMoney("1000元");
        dataModel.setActivityRExpireTime(DateUtil.timeStampToStr(System.currentTimeMillis()));
        dataModel.setActivityFExpireTime(DateUtil.timeStampToStr(System.currentTimeMillis()));
        dataModels.add(dataModel);
    }

    private void initRecyclerView(View rootView) {
        //获取RecyclerV
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new ActivityDetailAdapter(getActivity(), dataModels);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);

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

    /**
     * 初始化界面
     * @param rootView
     */
    private void init(View rootView) {
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
    }


    /**
     * 此方法可以调用外部Activity中提供的相关运行逻辑(例如信息传递)
     * 当触发此方法时Activity收到相关数据
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onActivityDetailFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * 此接口必须由包含此Fragment的Activity来实现，以允许在此Fragment与包含其的Activity和潜在的其他Fragment交互。
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onActivityDetailFragmentInteraction(Uri uri);
    }
}
