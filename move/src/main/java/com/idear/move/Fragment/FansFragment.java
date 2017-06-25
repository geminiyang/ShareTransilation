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
import android.widget.Button;

import com.idear.move.Adapter.CardLayoutOneAdapter;
import com.idear.move.Adapter.FansAndAttentionAdapter;
import com.idear.move.POJO.CardLayoutOneDataModel;
import com.idear.move.POJO.FansAndAttentionDataModel;
import com.idear.move.POJO.SearchToAddFriendsDataModel;
import com.idear.move.R;
import com.idear.move.util.ToastUtil;
import com.yqq.idear.CustomRecyclerView;

import java.util.LinkedList;

public class FansFragment extends Fragment implements FansAndAttentionAdapter.OnItemClickListener,
        FansAndAttentionAdapter.OnViewClickListener {
    private static final String ARG = "param1";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinkedList<FansAndAttentionDataModel> dataModels = new LinkedList<>();
    private static final String picUrl = "http://img0.imgtn.bdimg.com/it/u=1928150351,2755968131&fm=26&gp=0.jpg";

    public FansFragment() {
        //要求要有一个空的构造函数
    }

    /**
     * 使用提供的参数和工厂方法去创建一个fragment实例
     * Activity->Fragment
     */
    public static FansFragment newInstance(String values) {
        FansFragment fragment = new FansFragment();
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
            rootView = inflater.inflate(R.layout.fragment_fans, container, false);
            init(rootView);
            initRecyclerView(rootView);
            addData();
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
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
    }

    private void addData() {
        //这里执行提交数据并获取相关数据的异步操作
        for(int i=0;i<2;i++) {
            dataModels.add(new FansAndAttentionDataModel("20143501140331",picUrl,"123、快跑"));
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView(View view) {
        //获取RecyclerV
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);
        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化自定义适配器
        adapter = new FansAndAttentionAdapter(getActivity(), dataModels);
        ((FansAndAttentionAdapter)adapter).setOnItemClickListener(this);
        ((FansAndAttentionAdapter)adapter).setOnViewClickListener(this);
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
     * 此方法可以调用外部Activity中提供的相关运行逻辑(例如信息传递)
     * 当触发此方法时Activity收到相关数据
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFansFragmentInteraction(uri);
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
    public void onItemClick(int position) {
        //点击每一个子项后的响应
    }

    @Override
    public void onViewClick(int position, int viewType,View view) {
        switch (viewType) {
            case 1:
                ToastUtil.getInstance().showToast(getActivity(),"点击了关注按钮");
                ((Button)view).setText("关注");
                //这里执行相关的关注异步操作
                break;
            default:
                break;
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
        void onFansFragmentInteraction(Uri uri);
    }
}
