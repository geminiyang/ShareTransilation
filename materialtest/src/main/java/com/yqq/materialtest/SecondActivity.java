package com.yqq.materialtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends Activity {

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset ={"YANGQIQI","Baidu","123"};

    private String[] names ={"杨琦琦","杨琦琦","杨琦琦","杨琦琦","杨琦琦"};
    private String[] pics ={"a","b","c","d","e"};
    private List users = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这句话会影响导航栏的颜色
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setEnterTransition(new Fade().setDuration(2000));
        getWindow().setReturnTransition(new Fade().setDuration(1000));
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_second);

        users.add(new User("yangqiqi","a"));
        getActionBar().setTitle("我不是微信");

        //获取RecyclerV
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //使用此设置，以提高性能，如果内容不改变RecyclerView的布局尺寸，也称为设置固定大小
        myRecyclerView.setHasFixedSize(true);

        //设置ITEM 动画管理者
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //使用垂直布局管理器
        layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);


        //初始化自定义适配器
        adapter = new MyAdapter(this,users);
        // specify（指定） an adapter (see also next example)
        myRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            // 当点击actionbar上的添加按钮时，向adapter中添加一个新数据并通知刷新
            case R.id.add_item:
                if (adapter.getItemCount() != names.length) {
                    users.add(new User(names[adapter.getItemCount()], pics[adapter.getItemCount()]));
                    //更新视图
                    myRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    adapter.notifyDataSetChanged();
                }
                return true;
            // 当点击actionbar上的删除按钮时，向adapter中移除最后一个数据并通知刷新
            case R.id.remove_item:
                if (adapter.getItemCount() != 0) {
                    users.remove(adapter.getItemCount()-1);
                    myRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    adapter.notifyDataSetChanged();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
