package com.idear.move.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Adapter.MyUserListRecyclerViewAdapter;
import com.idear.move.Dummy.UserListContent;
import com.idear.move.MyListener.DataStateChangeCheck;
import com.idear.move.R;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.Thread.MyHomeLoadingAsyncTask;
import com.idear.move.constants.AppConstant;
import com.idear.move.myWidget.CustomRecyclerView;
import com.idear.move.util.NetWorkUtil;
import com.idear.move.util.ToastUtil;

import java.util.ArrayList;

/**
 * 表示Item列表的Fragment
 * Activity必须实现 {@link OnListFragmentInteractionListener} 接口
 */
public class UserListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    //默认列数为1
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;//与Activity关联的监听器

    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 15;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 8;

    private CustomRecyclerView myRecyclerView;
    //数据源
    private ArrayList<UserList> mValues = new ArrayList<>();

    private MyUserListRecyclerViewAdapter mAdapter;

    private View rootView;
    /**
     * 空构造函数，屏幕方向改变时可调用
     */
    public UserListFragment() {
    }

    //初始化item每一行的列数
    public static UserListFragment newInstance(int columnCount) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_user_list, container,false);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView(View rootView) {
        //设置adapter
        Context context = getActivity();
        myRecyclerView = (CustomRecyclerView) rootView.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            myRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, mColumnCount);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = myRecyclerView.getAdapter().getItemViewType(position);

                    if(viewType == CustomRecyclerView.TYPE_NORMAL) {
                        return gridLayoutManager.getSpanCount();//spanCount是初始化时候设置的参数
                    } else {
                        return 1;
                    }
                }
            });
            myRecyclerView.setLayoutManager(gridLayoutManager);
        }

        mAdapter = new MyUserListRecyclerViewAdapter(getActivity(),mListener,mValues);
        //添加初始数据源
        myRecyclerView.setAdapter(mAdapter);
        //数据状态监听器
        DataStateChangeCheck mDataEndListener = new DataStateChangeCheck(myRecyclerView);
        //实现了数据操作监听
        mDataEndListener.setLoadDataListener(myRecyclerView);
        myRecyclerView.addDataChangeListener(mDataEndListener);
        myRecyclerView.addHeaderView(context);
        myRecyclerView.addFooterView(context);
        myRecyclerView.setTotalCount(TOTAL_COUNT);
        myRecyclerView.setRequestCount(REQUEST_COUNT);
        myRecyclerView.setDataOperation(new CustomRecyclerView.DataOperation() {
            @Override
            public void onLoadMore() {
                for (int i = 0; i < REQUEST_COUNT; i++) {
                    if (mValues.size() >= TOTAL_COUNT) {
                        break;
                    }
                    mValues.add(UserListContent.createUserItem(mValues.size()));
                }
            }

            @Override
            public void onRefresh() {

            }
        });
        myRecyclerView.Initialize();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //绑定监听器
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //解绑监听器
        mListener = null;
    }

    /**
     * 此接口必须由包含该fragment的Activity实现，以允许该fragment的交互传达到该Activity中
     * 以及该Activity中潜在包含的其他Fragment
     * 具体内容查看文档 <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        //刷新数据
        void onListFragmentInteraction(UserList item);
    }


}
