package com.yqq.touchtest.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yqq.touchtest.R;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/2/7.
 */

public class MySimpleAdapter extends SimpleAdapter{

    private LayoutInflater mInflater;
    private float titleFontSize;
    private float screenWidth;
    private float screenHeight;


    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        // 获取屏幕的长和宽
        DisplayMetrics dm;
        dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        //设置标题字体大小
        titleFontSize = adjustTextSize();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private float adjustTextSize() {
        float textsize = 12;
        //在这实现你自己的字体大小算法，可根据之前计算的屏幕的高和宽来按比例显示
        textsize = screenWidth/320 * 12;
        return textsize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item, null);
        }
        TextView tv_title = (TextView) convertView.findViewById(R.id.text_list);
        tv_title.setTextSize(titleFontSize); //设置字体大小，
        return super.getView(position, convertView, parent);
    }
}
