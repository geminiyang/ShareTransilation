package com.idear.move.Activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class SpreadDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this,getResources().getColor(R.color.title_bar_blue));
        setContentView(R.layout.activity_spread_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
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

        final CollapsingToolbarLayout collapsing_toolbar_layout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        collapsing_toolbar_layout.setTitle("");
        collapsing_toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsing_toolbar_layout.setExpandedTitleColor(getResources().getColor(R.color.blue_light));

        AppBarLayout app_bar_layout = (AppBarLayout)findViewById(R.id.app_bar);

        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e("APP","appBarLayoutHeight:"+appBarLayout.getHeight()+" getTotalScrollRange:"+appBarLayout.getTotalScrollRange()+" offSet:"+verticalOffset);
                if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //当向下滑动超过一定距离时(达到collapsed状态）
                    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                    collapsing_toolbar_layout.setTitle("具体推广(collapsed)");
                }else{
                    collapsing_toolbar_layout.setTitle("具体推广(expanded)");
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
