package com.idear.move.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idear.move.R;
import com.idear.move.myWidget.RoundImageViewByXfermode;
import com.idear.move.util.ImageUtil;
import com.idear.move.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/4/26.
 */

public class HSVAdapter extends BaseAdapter {

    private List<Map<String,Object>> list;
    private Context context;
    private LayoutInflater inflater;
    private int picWidth,picHeight;

    public HSVAdapter(Context context){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.list=new ArrayList<>();
        picWidth = ScreenUtil.dip2px(context,160);
        picHeight = ScreenUtil.dip2px(context,800);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_item,null);
            viewHolder.mImg = (RoundImageViewByXfermode) convertView.findViewById(R.id.imageView);
            viewHolder.mText = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map<String,Object> map= (Map<String, Object>) getItem(position); //获取当前的Item

        Glide.with(context).load((int)map.get("image")).override(picWidth,picHeight).
                diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(false).into(viewHolder.mImg);
        viewHolder.mText.setText("我是活动标题名");
        return convertView;
    }

    public void addObjectItem(Map<String,Object> map){
        list.add(map);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        RoundImageViewByXfermode mImg;
        TextView mText;
    }
}
