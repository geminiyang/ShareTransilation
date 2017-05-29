package com.idear.move.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.Adapter.MyUserListRecyclerViewAdapter;
import com.idear.move.Dummy.UserListContent;
import com.idear.move.R;
import com.idear.move.Dummy.UserListContent.UserList;

/**
 * 表示Item列表的Fragment
 * Activity必须实现 {@link OnListFragmentInteractionListener} 接口
 */
public class UserListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    //默认列数为1
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;//与Activity关联的监听器

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

        //设置adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //添加初始数据源
            recyclerView.setAdapter(new MyUserListRecyclerViewAdapter(UserListContent.ITEMS, mListener));
        }
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
}
