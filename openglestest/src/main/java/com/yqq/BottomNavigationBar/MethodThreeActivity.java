package com.yqq.BottomNavigationBar;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yqq.R;

/**
 * RadioGroup+RadioButton
 */
public class MethodThreeActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener {

    private RadioButton heart,time,locate,comment;
    private MyFragment f1,f2,f3,f4;

    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_three);

        initView();
    }

    //UI组件初始化与事件绑定
    private void initView() {
        heart = (RadioButton) findViewById(R.id.heart);
        time = (RadioButton) findViewById(R.id.time);
        locate = (RadioButton) findViewById(R.id.locate);
        comment = (RadioButton) findViewById(R.id.comment);

        radioGroup = (RadioGroup) findViewById(R.id.rd_group);
        radioGroup.setOnCheckedChangeListener(this);

        setDefaultFragment();

    }

    private void setDefaultFragment() {
        time.setChecked(true);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        f1 = MyFragment.newInstance("时间");
        transaction.add(R.id.fragment_container, f1);
        transaction.commit();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        //启动Fragment的事务管理器
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(checkedId){
            case R.id.time:
                if(f1==null) {
                    f1 = MyFragment.newInstance("时间");
                }
                transaction.replace(R.id.fragment_container,f1);
                break;

            case R.id.comment:
                if(f2==null){
                    f2 = MyFragment.newInstance("评论");
                }
                transaction.replace(R.id.fragment_container,f2);
                break;

            case R.id.heart:
                if(f3==null){
                    f3 = MyFragment.newInstance("心情");
                }
                transaction.replace(R.id.fragment_container,f3);
                break;

            case R.id.locate:
                if(f4==null){
                    f4 = MyFragment.newInstance("定位");
                }
                transaction.replace(R.id.fragment_container,f4);
                break;
        }

        transaction.commit();
    }

}

