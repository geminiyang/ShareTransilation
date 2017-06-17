package com.idear.move.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Adapter.CardLayoutOneAdapter;
import com.idear.move.Adapter.CardLayoutThreeAdapter;
import com.idear.move.POJO.CardLayoutThreeDataModel;
import com.idear.move.R;
import com.idear.move.util.ToastUtil;
import com.yqq.idear.CustomRecyclerView;
import com.yqq.idear.DataStateChangeCheck;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFavoritesSpreadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFavoritesSpreadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFavoritesSpreadFragment extends Fragment implements CustomRecyclerView.DataOperation,
        CardLayoutOneAdapter.OnItemClickListener,CardLayoutThreeAdapter.OnItemClickListener{
    private static final String ARG = "param1";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    private CustomRecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<CardLayoutThreeDataModel> dataModels = new LinkedList<>();
    private String activityName = "分享漫画活动";
    private String breviaryContent = "  这个漫画分享活动旨在提高参与者的鉴赏水平和文化底蕴,欢迎参加";
    private static final String picUrl = "http://img0.imgtn.bdimg.com/it/u=1928150351,2755968131&fm=26&gp=0.jpg";

    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 2;

    public MyFavoritesSpreadFragment() {
        //要求要有一个空的构造函数
    }

    /**
     * 使用提供的参数和工厂方法去创建一个fragment实例
     * Activity->Fragment
     */
    public static MyFavoritesSpreadFragment newInstance(String values) {
        MyFavoritesSpreadFragment fragment = new MyFavoritesSpreadFragment();
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
            rootView = inflater.inflate(R.layout.fragment_my_favorites_spread, container, false);
            init(rootView);
            initRecyclerView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * 初始化界面
     * @param rootView
     */
    private void init(View rootView) {
        myRecyclerView = (CustomRecyclerView) rootView.findViewById(R.id.my_recycler_view);
    }

    private void initRecyclerView(View view) {
        //获取RecyclerV
        myRecyclerView = (CustomRecyclerView) view.findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new CardLayoutThreeAdapter(getActivity(), dataModels);
        ((CardLayoutThreeAdapter)adapter).setOnItemClickListener(this);
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

    /**
     * 此方法可以调用外部Activity中提供的相关运行逻辑(例如信息传递)
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFavoritesSpreadFragmentInteraction(uri);
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

    @Override
    public void onLoadMore() {
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            Log.d("info","loadMode------" + "LIST SIZE : " + dataModels.size()+1);
            dataModels.add(new CardLayoutThreeDataModel(activityName,picUrl,breviaryContent));
        }
    }

    @Override
    public void onRefresh() {
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            Log.d("info","refreshMode------" + "LIST SIZE : " + dataModels.size()+1);
            dataModels.addFirst(new CardLayoutThreeDataModel(activityName,picUrl,breviaryContent));
        }
    }

    @Override
    public void onItemClick(int position) {
        //点击每一个子项后的响应
        ToastUtil.getInstance().showToastTest(this.getActivity());
    }

    /**
     * 此接口必须由包含此Fragment的活动来实现，以允许在此Fragment与包含其的活动和潜在的其他Fragment交互。
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFavoritesSpreadFragmentInteraction(Uri uri);
    }
}
