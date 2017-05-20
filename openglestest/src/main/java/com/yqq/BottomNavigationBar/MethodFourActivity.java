package com.yqq.BottomNavigationBar;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yqq.R;

/**
 * 仿新浪微博
 */
public class MethodFourActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout ly_one,ly_two,ly_three,ly_four;
    private TextView mTextView1,mTextView2,mTextView3,mTextView4;
    private TextView mTextNum1,mTextNum2,mTextNum3;
    private ImageView mImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_four);

        bindView();
        ly_one.performClick();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MessageTipFragment fg1 = new MessageTipFragment();
        transaction.add(R.id.fragment_container,fg1);
        transaction.commit();
    }

    private void bindView() {

        ly_one = (LinearLayout)findViewById(R.id.ly_tab_menu_time);
        ly_two = (LinearLayout)findViewById(R.id.ly_tab_menu_comment);
        ly_three = (LinearLayout)findViewById(R.id.ly_tab_menu_heart);
        ly_four = (LinearLayout)findViewById(R.id.ly_tab_menu_locate);

        mTextView1 = (TextView)findViewById(R.id.tab_menu_time);
        mTextView2 = (TextView)findViewById(R.id.tab_menu_comment);
        mTextView3 = (TextView)findViewById(R.id.tab_menu_heart);
        mTextView4 = (TextView)findViewById(R.id.tab_menu_locate);

        mTextNum1 = (TextView)findViewById(R.id.tab_menu_time_num);
        mTextNum2 = (TextView)findViewById(R.id.tab_menu_comment_num);
        mTextNum3 = (TextView)findViewById(R.id.tab_menu_heart_num);

        mImageView = (ImageView)findViewById(R.id.tab_menu_locate_partner);

        ly_one.setOnClickListener(this);
        ly_two.setOnClickListener(this);
        ly_three.setOnClickListener(this);
        ly_four.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected() {
        mTextView1.setSelected(false);
        mTextView2.setSelected(false);
        mTextView3.setSelected(false);
        mTextView4.setSelected(false);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ly_tab_menu_time:
                setSelected();
                mTextView1.setSelected(true);
                mTextNum1.setVisibility(View.INVISIBLE);
                break;
            case R.id.ly_tab_menu_comment:
                setSelected();
                mTextView2.setSelected(true);
                mTextNum2.setVisibility(View.INVISIBLE);
                break;
            case R.id.ly_tab_menu_heart:
                setSelected();
                mTextView3.setSelected(true);
                mTextNum3.setVisibility(View.INVISIBLE);
                break;
            case R.id.ly_tab_menu_locate:
                setSelected();
                mTextView4.setSelected(true);
                mImageView.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
