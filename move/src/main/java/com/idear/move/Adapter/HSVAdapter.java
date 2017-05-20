package com.idear.move.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.idear.move.R;

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

    public HSVAdapter(Context context){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.list=new ArrayList<Map<String,Object>>();
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
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.imageView);
            viewHolder.mText = (TextView) convertView
                    .findViewById(R.id.textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map<String,Object> map= (Map<String, Object>) getItem(position); //获取当前的Item
        viewHolder.mImg.setBackgroundResource((Integer) map.get("image"));
        viewHolder.mText.setText("第"+(position+1)+"张");
        return convertView;
    }

    public void addObjectItem(Map<String,Object> map){
        list.add(map);
        notifyDataSetChanged();
    }

    /**
     * Unconditional layout inflation from view adapter:
     * Should use View Holder pattern (use recycled view passed into this method as the second parameter) for smoother scrolling less... (Ctrl+F1)
     *When implementing a view Adapter, you should avoid unconditionally inflating a new layout;
     *if an available item is passed in for reuse, you should try to use that one instead.
     *This helps make for example ListView scrolling much smoother.
     */
    private class ViewHolder
    {
        ImageView mImg;
        TextView mText;
    }
}
