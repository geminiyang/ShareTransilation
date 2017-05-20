package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yqq.touchtest.MyWidget.MyScrollView;
import com.yqq.touchtest.R;
import com.yqq.touchtest.Util.TranslucentStatusSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用GirdView实现
 */
public class ScrollActivity extends Activity implements MyScrollView.OnScrollListener {
    private EditText search_edit;//中间的输入框
    private MyScrollView myScrollView;
    private int searchLayoutTop;//顶部位置的高度

    LinearLayout search01,search02;//search02是一开始嵌套了search_edit的LinearLayout布局
    RelativeLayout rllayout;//含有图片的那个相对布局，顶部控件

    private GridView gridView;//网格视图
    private List<Map<String, Object>> data_list;//数据源
    private SimpleAdapter simple_adapter;//适配器
    private int[] icon = { R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    private String[] iconName={ "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
            "设置", "语音", "天气", "浏览器", "视频" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TranslucentStatusSetting.setTranslucentStatusSetting(ScrollActivity.this);
        TranslucentStatusSetting.setIfHaveTitle(true,ScrollActivity.this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#292929"));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscrolllayout);

        init();
        initGirdView();
    }

    private void initGirdView() {
        gridView = (GridView) findViewById(R.id.gridview);
        data_list = new ArrayList<Map<String,Object>>();;
        data_list = get_data();//获取数据
        //新建适配器
        String [] from ={"image","text"};
        final int [] to = {R.id.image,R.id.text};
        simple_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
        //配置适配器
        gridView.setAdapter(simple_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);
                Toast toast = Toast.makeText(ScrollActivity.this,(String)item.get("text"),Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    private List<Map<String, Object>> get_data() {
        //icon和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;

    }

    /**
     * 初始化控件
     */
    private void init() {
        search_edit = (EditText)findViewById(R.id.search_edit);
        myScrollView = (MyScrollView)findViewById(R.id.myScrollView);
        search01 = (LinearLayout)findViewById(R.id.search01);
        search02 = (LinearLayout)findViewById(R.id.search02);
        rllayout = (RelativeLayout)findViewById(R.id.rllayout);
        //设置监听器
        myScrollView.setOnScrollListener(this);

        //三句代码使界面打开时候自定义ScrollView下面的EditText获取焦点的事件不再发生
        myScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        myScrollView.setFocusable(true);
        myScrollView.setFocusableInTouchMode(true);
        //此方法用来设置接触scrollView时候的监听
//        myScrollView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.requestFocusFromTouch();
//                return false;
//            }
//        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            searchLayoutTop = rllayout.getBottom();//获取searchLayout的顶部位置
        }
    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        if(scrollY >= searchLayoutTop){
            //当其滑到指定长度，将其父容器置换
            if (search_edit.getParent()!=search01) {
                search02.removeView(search_edit);
                search01.addView(search_edit);
            }
        }else{
            //当其滑到指定长度，将其父容器置换
            if (search_edit.getParent()!=search02) {
                search01.removeView(search_edit);
                search02.addView(search_edit);
            }
        }
    }

    
}