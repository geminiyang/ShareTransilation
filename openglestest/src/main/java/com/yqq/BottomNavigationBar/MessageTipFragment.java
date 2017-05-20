package com.yqq.BottomNavigationBar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yqq.R;

/**
 * Created by user on 2017/4/21.
 */

public class MessageTipFragment extends Fragment implements View.OnClickListener{
    private Context mContext;
    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private Button btn_four;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_tip_fragment,container,false);
        btn_one = (Button)view.findViewById(R.id.btn_one);
        btn_two = (Button)view.findViewById(R.id.btn_two);
        btn_three = (Button)view.findViewById(R.id.btn_three);
        btn_four = (Button)view.findViewById(R.id.btn_four);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_one:
                TextView mTextViewTime = (TextView)getActivity().findViewById(R.id.tab_menu_time_num);
                mTextViewTime.setText("11");
                mTextViewTime.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_two:
                TextView mTextViewComment = (TextView)getActivity().findViewById(R.id.tab_menu_comment_num);
                mTextViewComment.setText("99");
                mTextViewComment.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_three:
                TextView mTextViewHeart = (TextView)getActivity().findViewById(R.id.tab_menu_heart_num);
                mTextViewHeart.setText("999+");
                mTextViewHeart.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_four:
                ImageView mImageView = (ImageView) getActivity ().findViewById(R.id.tab_menu_locate_partner);
                mImageView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
