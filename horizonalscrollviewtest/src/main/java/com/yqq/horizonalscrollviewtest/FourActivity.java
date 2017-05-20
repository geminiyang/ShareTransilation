package com.yqq.horizonalscrollviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义HorizontalScrollView思想：
 *1、首先根据屏幕的大小和Item的大小，计算可以一个屏幕最多可以加载多少个Item，然后加载该数量Item。
 *2、当用户右滑（从右向左），滑动到一定距离时，加载下一张，删除第一张
 *3、当用户左滑（从左向右），滑动到一定距离时，加载上一张，删除最后一张
 */
public class FourActivity extends AppCompatActivity {

    private HorizontalScrollViewAdapter hsvAdapter;
    private MyHorizontalScrollView myHorizontalScrollView;

    private ImageView mImg;

    private Integer[] images = {
            R.mipmap.rina1,R.mipmap.rina2,R.mipmap.rina3,
            R.mipmap.rina4,R.mipmap.rina5,R.mipmap.rina6,
            R.mipmap.rina7,R.mipmap.rina8
    };
    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
            R.mipmap.rina1,R.mipmap.rina2,R.mipmap.rina3,
            R.mipmap.rina4,R.mipmap.rina5,R.mipmap.rina6,
            R.mipmap.rina7,R.mipmap.rina8)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        init();
    }

    private void init() {
        myHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.my_hsv);
        mImg = (ImageView) findViewById(R.id.id_content);
        initAdapterData();
    }

    private void initAdapterData() {
        hsvAdapter = new HorizontalScrollViewAdapter(this,mDatas);
        myHorizontalScrollView.setCurrentImageChangeListener(new MyHorizontalScrollView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position, View viewIndicator) {
                //mImg.setImageResource(images[position]);
                //设置界面的图片
                mImg.setImageResource(mDatas.get(position));
                viewIndicator.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });

        //添加点击回调
        myHorizontalScrollView.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mImg.setImageResource(images[position]);
                view.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });

        myHorizontalScrollView.initDatas(hsvAdapter);
    }
}
