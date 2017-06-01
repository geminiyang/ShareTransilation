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
import com.idear.move.MyListener.DataStateChangeListener;
import com.idear.move.R;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.constants.AppConstant;
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

    protected AppConstant.FooterState mState = AppConstant.FooterState.NORMAL;
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNTER = 15;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 8;

    private RecyclerView recyclerView;
    //数据源
    private ArrayList<UserList> mValues = new ArrayList<>();

    private MyUserListRecyclerViewAdapter mAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            //加载完毕时
                            setState(AppConstant.FooterState.NORMAL);
                        }
                    },1500);
                    break;
                case 2:
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(1);
                        }
                    },100);
                    break;
                default:
                    break;
            }

        }
    };

    private void setState(AppConstant.FooterState mState) {
        this.mState = mState;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeAdapterState();
            }
        });
    }

    //改变底部bottom的样式
    private void changeAdapterState() {
        if (mAdapter != null && mAdapter.getFooterHolder() != null) {
            mAdapter.getFooterHolder().setState(mState);
        }
    }

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
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.addOnScrollListener(mDataEndListener);

        //设置adapter
        Context context = getActivity();
        if (mColumnCount <= 1) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, mColumnCount);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = recyclerView.getAdapter().getItemViewType(position);

                    if(viewType == MyUserListRecyclerViewAdapter.TYPE_NORMAL) {
                        return gridLayoutManager.getSpanCount();//spanCount是初始化时候设置的参数
                    } else {
                        return 1;
                    }
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        //初始化Adapter
        getRemoteData();
        mAdapter = new MyUserListRecyclerViewAdapter(mListener,mValues);
        //添加初始数据源
        recyclerView.setAdapter(mAdapter);
        //确保HeaderView不优先显示出来
        recyclerView.scrollToPosition(1);

        return view;
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

    private DataStateChangeListener mDataEndListener = new DataStateChangeListener() {
        /**
         * 到底部就会触发
         * @param view
         */
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            if (mState == AppConstant.FooterState.LOADING) {
                Log.d("info", "the state is Loading, just wait..");
                ToastUtil.getInstance().showToast(getActivity(),"正在加载中...");
                return;
            }
            //加载数据(根据界面显示个数和服务器返回的数据总数实现动态加载)
            if (mValues.size() < TOTAL_COUNTER) {
                //载入更多数据
                requestData();
                Log.d("TAG", "请求数据");
            } else {
                //the end
                setState(AppConstant.FooterState.END);
                ToastUtil.getInstance().showToast(getActivity(),"已经到了底部");
            }
        }

        @Override
        public void onRefreshPage(final View view) {
            ToastUtil.getInstance().showToast(getActivity(),"到达了顶部");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(getLastCompletelyVisibleItemPosition((RecyclerView) view) + 2);
                    Message msg = new Message();
                    msg.arg1 = 2;
                    handler.handleMessage(msg);
                }
            },3000);

        }

        @Override
        public void onStopRefreshPage(final View view) {
            ToastUtil.getInstance().showToast(getActivity(),"停止刷新");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(getLastCompletelyVisibleItemPosition((RecyclerView) view) + 2);
                    Message msg = new Message();
                    msg.arg1 = 2;
                    handler.handleMessage(msg);
                }
            },250);
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {
        setState(AppConstant.FooterState.LOADING);
        new Thread() {
            @Override
            public void run() {
                super.run();
                //模拟网络连接
                if (NetWorkUtil.isNetworkConnected(getActivity())) {
                    //模拟请求远程数据
                    getRemoteData();
                    Looper.prepare();
                    Message msg = new Message();
                    msg.arg1 = 1;
                    handler.handleMessage(msg);
                    Looper.loop();


                } else {
                    //模拟一下网络请求失败的情况
                    setState(AppConstant.FooterState.NETWORK_ERROR);
                }
            }
        }.start();
    }

     //模拟请求数据(在线程中执行)
     private void getRemoteData() {
         //要减去adapter最后一页
         for (int i = 0; i < REQUEST_COUNT; i++) {
             if (mValues.size() >= TOTAL_COUNTER) {
                 break;
             }
             mValues.add(UserListContent.createUserItem(i-1));
         }
     }

}
