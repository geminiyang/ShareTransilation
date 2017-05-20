package com.yqq.touchtest.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.yqq.touchtest.MyWidget.MyScrollView;
import com.yqq.touchtest.R;
import com.yqq.touchtest.Util.MySimpleAdapter;
import com.yqq.touchtest.Util.TranslucentStatusSetting;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableActivity extends Activity implements
        MyScrollView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener{

    private MyScrollView scrollView;
    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;
    private ListView mListView;
    private List<Map<String,String>> data_list;

    private TableLayout mytable;
    private LinearLayout lllayout;
    private int LLLayoutTop;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1==0){
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(TableActivity.this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#292929"));
        }
        ActionBar actionBar = getActionBar();
        setTitle("");
        //使ActionBar的左侧出现一个向左的箭头
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_table);
        initView();
        initEvent();
        setOverflowShowingAlways();
    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);
        mListView.setAdapter(new MySimpleAdapter(this,getdata(),R.layout.list_item,
                new String[]{"text"},new int[]{R.id.text_list}));
        mListView.setTextFilterEnabled(true);
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        scrollView = (MyScrollView) findViewById(R.id.scroll_root);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setOnScrollListener(this);

        lllayout = (LinearLayout) findViewById(R.id.ll_layout);
        mytable = (TableLayout) findViewById(R.id.mytable);

        mListView = (ListView) findViewById(R.id.listView);

    }

    private List<? extends Map<String,?>> getdata() {
        data_list = new ArrayList<Map<String, String>>();
        for(int i=0;i<mStrs.length;i++){
            Map<String, String> listem = new HashMap<String, String>();
            listem.put("text",mStrs[i]);
            data_list.add(listem);
        }
        return data_list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //ActionView在展开和合并时候显示不同的界面
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //展开时候的监听器
                Log.d("TAG", "on expand");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //合并时候的监听器
                Log.d("TAG", "on collapse");
                return true;
            }
        });
        mSearchView= (SearchView) searchItem.getActionView();
        // 配置SearchView的属性
        mSearchView.setIconifiedByDefault(false);//默认时图标不显示在搜索框内(true)
        mSearchView.setQueryHint("查找");
        mSearchView.setBackground(getResources().getDrawable(R.drawable.button_selector));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    //mListView.setFilterText(newText);//此方法会有黑色提示框
                    Object[] obj=searchItem(newText);//实现对应s字符串的搜索，并将结果放置到obj中。
                    updateListView(obj);//通知listView进行更新。
                    return true;
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

          //此部分用来获取默认可使用的分享项
//        MenuItem shareItem = menu.findItem(R.id.action_share);
//        ShareActionProvider saProvider = (ShareActionProvider) shareItem.getActionProvider();
//        saProvider.setShareIntent(getDefaultIntent());


        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 获取分享用的Intent，电话，短信，等隐式Intent
     * @return 分享用Intent
     */
    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 点击事件,回退按钮的逻辑处理
     * @param item 点击的item项
     * @return 是否点击
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    //不在同一个Task中，则需要借助TaskStackBuilder来创建一个新的Task。
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    //如果父Activity和当前Activity是在同一个Task中的，则直接调用navigateUpTo()方法进行跳转
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清除回退栈中，当前Activity到目的Activity之间已经有的Activiy
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.action_refresh:
                swipeRefreshLayout.setRefreshing(true);
                myUpdateOperation();
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 修改overflow打开menu时候的默认行为
     * @param featureId id
     * @param menu menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //通过反射的方法使OverFlowMenu的每个Item的图标都显示出来
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 设置overflow总是显示
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            //根据这个值判断，手机有没有物理Menu键
            //这是一个内部变量，我们通过反射的方法使它的值永远为false
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 寻找数据中是否有匹配数据项
     * @param name
     * @return
     */
    public Object[] searchItem(String name) {
        ArrayList<String> mSearchList = new ArrayList<String>();
        for (int i = 0; i < mStrs.length; i++) {
            int index = mStrs[i].indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                mSearchList.add(mStrs[i]);
            }
        }
        return mSearchList.toArray();
    }

    /**
     * 更新ListView内容
     * @param obj
     */
    public void updateListView(Object[] obj) {
        mListView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),
                R.layout.list_item, obj));
    }

    /**
     * 自定义控件Item的点击事件
     * @param view 每个自定义控件的Item项
     */
    public void tableClick(View view) {
        AlertDialog.Builder Builder= new AlertDialog.Builder(this);
        Builder.setTitle("你想干什么");
        final EditText editText= new EditText(this);
        editText.setText("请输入你的企图");
        Builder.setView(editText);
        Builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });//为对话框添加确定按钮
        Builder.create().show();
    }

    /**
     * android4.0 SearchView去掉（修改）搜索框的背景 修改光标
     * @param searchView 搜索视图
     */
    public void setSearchViewBackground(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            // 指定某个私有属性
            Field ownField = argClass.getDeclaredField("mSearchPlate"); // 注意mSearchPlate的背景是stateListDrawable(不同状态不同的图片)
            // 所以不能用BitmapDrawable
            // setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackground(getResources().getDrawable(R.drawable.button_selector));

            // 指定某个私有属性
            Field mQueryTextView = argClass.getDeclaredField("mQueryTextView");
            mQueryTextView.setAccessible(true);

            // mCursorDrawableRes光标图片Id的属性
            // 这个属性是TextView的属性，所以要用mQueryTextView（SearchAutoComplete）的父类（AutoCompleteTextView）的父
            // 类( EditText）的父类(TextView)
            Class<?> mTextViewClass = mQueryTextView.get(searchView).getClass()
                    .getSuperclass().getSuperclass().getSuperclass();
            Field mCursorDrawableRes = mTextViewClass.getDeclaredField("mCursorDrawableRes");

            // setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(mQueryTextView.get(searchView), R.drawable.button_selector);
            // 注意第一个参数持有这个属性(mQueryTextView)的对象(mSearchView)
            // 光标必须是一张图片不能是颜色，因为光标有两张图片，一张是第一次获得焦点的时候的闪烁的图片，
            // 一张是后边有内容时候的图片，如果用颜色填充的话，就会失去闪烁的那张图片，
            // 颜色填充的会缩短文字和光标的距离（某些字母会背光标覆盖一部分）。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            LLLayoutTop = lllayout.getTop();//获取LLLayout的顶部位置
        }
    }

        @Override
        public void onScroll(int scrollY) {
            //根据滑动的距离改变透明度
            //需要获取一个参考物的高度来决定ActionBar是显示还是隐藏

//            //此方法是当大于自定义控件的高度的时候，显示ActionBar
//                if (scrollY >= mytable.getHeight()) {
//                    if(!getActionBar().isShowing()){
//                        getActionBar().show();
//                    }
//                }else{
//                    if (getActionBar().isShowing()) {
//                        getActionBar().hide();
//                    }
//                }

            //此方法是当大于自定义控件的高度的时候，隐藏ActionBar，80取决于actionbar的高度，我这里是actionBar高度50
            //故两倍actionbar高度可以防止重复绘制的现象
            if (scrollY < mytable.getHeight()) {
                if(!getActionBar().isShowing()){
                    getActionBar().show();
                }
            }else if(scrollY>mytable.getHeight()+101){
                if (getActionBar().isShowing()) {
                    getActionBar().hide();
                }
            }

        }


    /**
     * 该方法是触发刷新时候的方法
     */
    @Override
    public void onRefresh() {
        Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout");
        // This method performs the actual data-refresh operation.
        // The method calls setRefreshing(false) when it's finished.
        myUpdateOperation();
    }

    private void myUpdateOperation() {
        Snackbar.make(swipeRefreshLayout,"refreshing.......",Snackbar.LENGTH_SHORT).setAction("8888",null).show();
        Message msg = new Message();
        msg.arg1 = 0;
        mHandler.sendMessageDelayed(msg,2000);
    }
}