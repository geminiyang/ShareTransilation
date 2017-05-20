package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class SearchActivity extends BaseActivity {

    private Toolbar toolbar;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        mSearchView = (SearchView) findViewById(R.id.searchView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        initSearchView(mSearchView);
        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);//使Toolbar能取代原本的 actionbar

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置系统默认退出按钮
        ActionBar actionBar;
        if ((actionBar=getSupportActionBar())!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置标题栏具有返回按钮
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 初始化搜索框
     * @param searchView
     */
    private void initSearchView(SearchView searchView) {
        //设置我们的SearchView
        //输入框内icon不显示
        searchView.setIconified(false);
        //获取输入焦点
        searchView.requestFocus();
        //设置初始提示文本
        searchView.setQueryHint("搜索活动、反馈或产品");
        //添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        //searchView.setSubmitButtonEnabled(true);

        //设置字体颜色
        TextView textView = (TextView)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setHintTextColor(getResources().getColor(R.color._grey)); //hint文字颜色
        textView.setTextColor(getResources().getColor(R.color.blue_light));  //输入的文字颜色

//        //设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
//        searchView.setIconifiedByDefault(true);
//        // 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
//        searchView.onActionViewExpanded();

//        //将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
//        searchView.setFocusable(true);
//        //模拟焦点点击事件
//        searchView.requestFocusFromTouch();
//        //禁止弹出输入法，在某些情况下有需要
//        searchView.setFocusable(false);
//        //禁止弹出输入法，在某些情况下有需要
//        searchView.clearFocus();

        //search的事件监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
