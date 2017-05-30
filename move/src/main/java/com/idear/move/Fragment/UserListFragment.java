package com.idear.move.Fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.idear.move.MyListener.DataEndlessListener;
import com.idear.move.R;
import com.idear.move.Dummy.UserListContent.UserList;
import com.idear.move.constants.AppConstant;
import com.idear.move.util.ToastUtil;

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
    private static final int TOTAL_COUNTER = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 10;
    // 已经获取到多少条数据了
    private int mCurrentCounter = 7;

    private MyUserListRecyclerViewAdapter mAdapter;

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
        if (mAdapter != null && mAdapter.mFooterHolder != null) {
            mAdapter.mFooterHolder.setState(mState);
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
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
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
        mAdapter = new MyUserListRecyclerViewAdapter(UserListContent.ITEMS, mListener);
        //添加初始数据源
        recyclerView.setAdapter(mAdapter);

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

    private DataEndlessListener mDataEndListener = new DataEndlessListener() {
        /**
         * 到底部就会触发
         * @param view
         */
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            if (mState == AppConstant.FooterState.LOADING) {
                Log.d("info", "the state is Loading, just wait..");
                return;
            }
            //加载数据(根据界面显示个数和服务器返回的数据总数实现动态加载)
            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                Log.d("TAG", "请求数据");
            } else {
                //the end
                setState(AppConstant.FooterState.END);
                ToastUtil.getInstance().showToast(getActivity(),"已经到了底部");
            }
        }
    };
}
