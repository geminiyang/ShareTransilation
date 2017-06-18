package com.idear.move.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idear.move.R;

public class ActivityDetailFragment extends Fragment  {
    private static final String ARG = "param1";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private View rootView;

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
