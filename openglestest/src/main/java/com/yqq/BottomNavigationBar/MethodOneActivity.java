package com.yqq.BottomNavigationBar;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yqq.R;

/**
 * (ViewPager)+LinearLayout+TextView
 */
public class MethodOneActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView heart,time,locate,comment;

    private MyFragment f1,f2,f3,f4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_one);

        initView();

    }

    //UI组件初始化与事件绑定
    private void initView() {
        heart = (TextView)findViewById(R.id.heart);
        time = (TextView)findViewById(R.id.time);
        locate = (TextView)findViewById(R.id.locate);
        comment = (TextView)findViewById(R.id.comment);

        heart.setOnClickListener(this);
        time.setOnClickListener(this);
        locate.setOnClickListener(this);
        comment.setOnClickListener(this);
        //模拟点击
        //time.performClick();
        time.setSelected(true);
        setDefaultFragment();
        
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        f1 = MyFragment.newInstance("时间");
        transaction.add(R.id.fragment_container, f1);
        transaction.commit();
    }

    //重置所有文本的选中状态
    public void selected(){
        heart.setSelected(false);
        time.setSelected(false);
        locate.setSelected(false);
        comment.setSelected(false);
    }


    @Override
    public void onClick(View v) {
        //启动Fragment的事务管理器
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(v.getId()){
            case R.id.time:
                selected();
                time.setSelected(true);
                if(f1==null) {
                    f1 = MyFragment.newInstance("时间");
                }
                transaction.replace(R.id.fragment_container,f1);
                break;

            case R.id.comment:
                selected();
                comment.setSelected(true);
                if(f2==null){
                    f2 = MyFragment.newInstance("评论");
                }
                transaction.replace(R.id.fragment_container,f2);
                break;

            case R.id.heart:
                selected();
                heart.setSelected(true);
                if(f3==null){
                    f3 = MyFragment.newInstance("心情");
                }
                transaction.replace(R.id.fragment_container,f3);
                break;

            case R.id.locate:
                selected();
                locate.setSelected(true);
                if(f4==null){
                    f4 = MyFragment.newInstance("定位");
                }
                transaction.replace(R.id.fragment_container,f4);
                break;
        }

        transaction.commit();
    }
}
