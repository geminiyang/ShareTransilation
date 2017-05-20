package com.yqq.BottomNavigationBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yqq.R;

/**
 * Created by user on 2017/4/21.
 */

public class MyFragment extends Fragment {
    private String context;
    private TextView mTextView;

    private static final String ARG = "arg";

    public static MyFragment newInstance(String arg){
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,container,false);
        mTextView = (TextView)view.findViewById(R.id.txt_content);
        context = getArguments().getString(ARG);
        mTextView.setText(context);
        return view;
    }


}
