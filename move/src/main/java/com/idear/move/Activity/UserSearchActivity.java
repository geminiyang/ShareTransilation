package com.idear.move.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.idear.move.Adapter.SearchItemAdapter;
import com.idear.move.Fragment.AllActivitySearchFragment;
import com.idear.move.Fragment.FeedbackSearchFragment;
import com.idear.move.Fragment.SpreadSearchFragment;
import com.idear.move.Helper.RecordSQLiteOpenHelper;
import com.idear.move.R;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class UserSearchActivity extends BaseActivity implements TabLayout.OnTabSelectedListener ,
        FeedbackSearchFragment.OnFragmentInteractionListener,AllActivitySearchFragment.OnFragmentInteractionListener,
        SpreadSearchFragment.OnFragmentInteractionListener {


    private TabLayout mTabLayout;
    private Toolbar mToolBar;
    private ImageView iv_back,iv_clear;
    private TextView tvOp,clearTextView,tvTip;
    private LinearLayout llBefore,llAfter;
    private EditText inputEdit;

    private ViewPager mViewPager;
    private AllActivitySearchFragment f1;
    private FeedbackSearchFragment f2;
    private SpreadSearchFragment f3;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    /*列表及其适配器*/
    private ListView listView;
    private BaseAdapter adapter;
    private SearchItemAdapter searchItemAdapter;
    private List<String> mList = new ArrayList<>();

    /*数据库变量*/
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        initData();
        init();

        //实例化数据库SQLiteOpenHelper子类对象
        helper = new RecordSQLiteOpenHelper(this);
        // 第一次进入时查询所有的历史记录
        queryData("");
    }

    private void init() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager_all_activity);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_all_activity);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        tvOp = (TextView) findViewById(R.id.tv_op);
        llAfter = (LinearLayout) findViewById(R.id.after_search_ll);
        llBefore = (LinearLayout) findViewById(R.id.before_search_ll);
        inputEdit = (EditText) findViewById(R.id.et_search);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        tvTip = (TextView) findViewById(R.id.tip_tv);
        clearTextView = (TextView) findViewById(R.id.clear_tv);
        listView = (ListView) findViewById(R.id.lv);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
        //TabLayout.MODE_SCROLLABLE和TabLayout.MODE_FIXED分别表示当tab的内容超过屏幕宽度是否支持横向水平滑动
        //第一种支持滑动，第二种不支持，默认不支持水平滑动。
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addOnTabSelectedListener(this);

        mTabLayout.getTabAt(0).setText("活动");//自有方法添加icon
        mTabLayout.getTabAt(1).setText("反馈");
        mTabLayout.getTabAt(2).setText("推广");

        //设置分割线
        LinearLayout linearLayout = (LinearLayout) mTabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.template_divider_vertical));

        initToolBar(mToolBar);

        tvOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAction();
            }
        });

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEdit.setText("");
                iv_clear.setVisibility(View.GONE);
                layoutChange();
            }
        });

        clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空数据库
                deleteData();
                queryData("");
            }
        });

        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    //若搜索框为空,则模糊搜索空字符,即显示所有的搜索历史
                    queryData("");
                } else {
                    //每次输入后都查询数据库并显示
                    //根据输入的值去模糊查询数据库中有没有数据
                    String tempName = inputEdit.getText().toString().trim();
                    queryData(tempName);
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }
        });

        inputEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    searchAction();
                }
                return false;
            }
        });

        //列表监听
        //即当用户点击搜索历史里的字段后,会直接将结果当作搜索字段进行搜索
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取到用户点击列表里的文字,并自动填充到搜索框内
                TextView textView = (TextView) view.findViewById(R.id.search_str);
                String name = textView.getText().toString();
                inputEdit.setText(name);

                searchAction();
            }
        });
    }

    private void initData() {
        if (f1 == null) {
            f1 = AllActivitySearchFragment.newInstance("活动");
        }
        if (f2 == null) {
            f2 = FeedbackSearchFragment.newInstance("反馈");
        }
        if (f3 == null) {
            f3 = SpreadSearchFragment.newInstance("推广");
        }

        mTabs.add(f1);
        mTabs.add(f2);
        mTabs.add(f3);

        //要关注如何更新其中的数据
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
    }

    private void initToolBar(Toolbar toolbar) {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                mViewPager.setCurrentItem(0, false);
                break;
            case 1:
                mViewPager.setCurrentItem(1, false);
                break;
            case 2:
                mViewPager.setCurrentItem(2, false);
                break;
            case 3:
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 点击了搜索按钮后的行为
     */
    private void searchAction() {
        if(inputEdit.getText().toString().trim().length()!=0){
            boolean hasData = hasData(inputEdit.getText().toString().trim());
            if (!hasData) {
                insertData(inputEdit.getText().toString().trim());

                queryData("");
            }
            tvOp.post(new Runnable() {
                @Override
                public void run() {
                    layoutChange();
                }
            });
        }
    }

    /**
     * 布局可视化的变化
     */
    private void layoutChange(){
        if(llBefore.getVisibility()==View.VISIBLE
                &&llAfter.getVisibility()==View.GONE) {//点击了搜索按钮
            tvOp.setText("取消");
            llBefore.setVisibility(View.GONE);
            llAfter.setVisibility(View.VISIBLE);
            //添加具体方法实现搜索操作
        } else if(llBefore.getVisibility()==View.GONE
                &&llAfter.getVisibility()==View.VISIBLE) {
            tvOp.setText("搜索");
            llBefore.setVisibility(View.VISIBLE);
            llAfter.setVisibility(View.GONE);
            //添加具体方法从搜索操作中退出
        } else {

        }
    }



    /*插入数据*/
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /*模糊查询数据 并显示在ListView列表上*/
    private void queryData(String tempName) {
        if(adapter!=null) {
            adapter = null;
        }
        if (mList!=null) {
            mList.clear();
        }
        //模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
//        // 创建adapter适配器对象,装入模糊搜索的结果（第一种方法是使用baseAdapter）
//        adapter = new SimpleCursorAdapter(UserSearchActivity.this,
//                R.layout.item_search, cursor, new String[] { "name" },
//                new int[] { R.id.search_str }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mList = cursor2list(cursor);
        searchItemAdapter = new SearchItemAdapter(mList,UserSearchActivity.this);
        searchItemAdapter.setButtonClickListener(new SearchItemAdapter.OnFillIconClickListener() {
            @Override
            public void onClickFillIcon(View view, String str) {
                inputEdit.setText(str);
                inputEdit.clearFocus();
            }
        });

        // 设置适配器
        listView.setAdapter(searchItemAdapter);
        searchItemAdapter.notifyDataSetChanged();
        if(mList.size()>0) {
            tvTip.setVisibility(View.VISIBLE);
            clearTextView.setVisibility(View.VISIBLE);
        } else {
            tvTip.setVisibility(View.GONE);
            clearTextView.setVisibility(View.GONE);
        }
    }

    //也可将Cursor中的数据转为 ArrayList<Map<String, String>> 类型数据
    private ArrayList<String> cursor2list(Cursor cursor) {
        ArrayList<String> list = new ArrayList<>();

        //遍历Cursor
        while(cursor.moveToNext()){
            list.add(cursor.getString(1));//对应name字段
        }
        return list;
    }

    /*检查数据库中是否已经有该条记录*/
    private boolean hasData(String tempName) {
        //从Record这个表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /*清空数据*/
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    @Override
    public void onSpreadSearchFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFeedbackSearchFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAllActivitySearchFragmentInteraction(Uri uri) {

    }
}
