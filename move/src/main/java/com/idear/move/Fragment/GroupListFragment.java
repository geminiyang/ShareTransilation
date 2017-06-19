package com.idear.move.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Activity.UserChatActivity;
import com.idear.move.Adapter.MyGroupListRecyclerViewAdapter;
import com.idear.move.Dummy.GroupListContent;
import com.idear.move.Dummy.GroupListContent.GroupList;
import com.idear.move.R;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.idear.CustomRecyclerView;
import com.yqq.idear.DataStateChangeCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示Item列表的Fragment
 * Activity必须实现 {@link OnListFragmentInteractionListener} 接口
 */
public class GroupListFragment extends Fragment implements MyGroupListRecyclerViewAdapter.OnItemClickListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    //默认列数为1
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;//与Activity关联的监听器

    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 5;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 1;

    private CustomRecyclerView myRecyclerView;
    //数据源
    private List<GroupList> mValues = new ArrayList<>();

    private MyGroupListRecyclerViewAdapter mAdapter;

    private View rootView;
    /**
     * 空构造函数，屏幕方向改变时可调用
     */
    public GroupListFragment() {
    }

    //初始化item每一行的列数
    public static GroupListFragment newInstance(int columnCount) {
        GroupListFragment fragment = new GroupListFragment();
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

        mAdapter = new MyGroupListRecyclerViewAdapter(getActivity(),mValues);
        mAdapter.setOnItemClickListener(this);
        //添加初始数据源
        myRecyclerView.setAdapter(mAdapter);
        //数据状态监听器
        DataStateChangeCheck mDataEndListener = new DataStateChangeCheck(myRecyclerView);
        //实现了数据操作监听
        mDataEndListener.setLoadDataListener(myRecyclerView);
        myRecyclerView.addDataChangeListener(mDataEndListener);
        myRecyclerView.addFooterView(context);
        myRecyclerView.setTotalCount(TOTAL_COUNT);
        myRecyclerView.setRequestCount(REQUEST_COUNT);
        myRecyclerView.setDataOperation(new CustomRecyclerView.DataOperation() {
            @Override
            public void onLoadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < REQUEST_COUNT; i++) {
                            if (mValues.size() >= TOTAL_COUNT) {
                                break;
                            }
                            mValues.add(GroupListContent.createGroupItem(mValues.size()));
                        }
                    }
                }).start();

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
     * 此方法可以调用外部Activity中提供的相关运行逻辑(例如信息传递)
     * 当触发此方法时Activity收到相关数据
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGroupListFragmentInteraction(uri);
        }
    }

    @Override
    public void onItemClick(int position) {
        //对应的点击事件
        IntentSkipUtil.skipToNextActivity(this.getActivity(),UserChatActivity.class);
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
        void onGroupListFragmentInteraction(Uri uri);
    }


}
