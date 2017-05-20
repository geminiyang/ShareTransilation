package com.yqq.BottomNavigationBar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.yqq.R;

/**
 * BottomNavigationBar
 */
public class MethodFiveActivity extends AppCompatActivity {

    private String TAG = MethodFiveActivity.class.getSimpleName();
    private MyFragment f1,f2,f3,f4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_five);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.tab_menu_time, "时间"))
                .addItem(new BottomNavigationItem(R.drawable.tab_menu_comment, "评论"))
                .addItem(new BottomNavigationItem(R.drawable.tab_menu_heart, "心情"))
                .addItem(new BottomNavigationItem(R.drawable.tab_menu_locate, "定位"))
                .initialise();

        setDefaultFragment();


        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
                FragmentManager fm = getSupportFragmentManager();
                //开启事务
                FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0:
                        if (f1 == null) {
                            f1 = MyFragment.newInstance("时间");
                        }
                        transaction.replace(R.id.fragment_container, f1);
                        break;
                    case 1:
                        if (f2 == null) {
                            f2 = MyFragment.newInstance("评论");
                        }
                        transaction.replace(R.id.fragment_container, f2);
                        break;
                    case 2:
                        if (f3 == null) {
                            f3 = MyFragment.newInstance("心情");
                        }
                        transaction.replace(R.id.fragment_container, f3);
                        break;
                    case 3:
                        if (f4 == null) {
                            f4 = MyFragment.newInstance("定位");
                        }
                        transaction.replace(R.id.fragment_container, f4);
                        break;
                    default:
                        break;
                }
                // 事务提交
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {
                Log.d(TAG, "onTabUnselected() called with: " + "position = [" + position + "]");

            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        f1 = MyFragment.newInstance("时间");
        transaction.add(R.id.fragment_container, f1);
        transaction.commit();
    }

}
