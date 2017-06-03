package com.idear.move.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idear.move.POJO.Msg;
import com.idear.move.R;

import java.util.List;

/**
 * 作者:geminiyang on 2017/5/28.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class MsgAdapter extends BaseAdapter {
    private List<Msg> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public MsgAdapter(Context context, List<Msg> objects) {
        this.mList = objects;
        this.mContext = context;
        this.mInflater =LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //使用ArrayAdapter<Msg>时，需要在构造函数的super里面提供默认布局
        Msg msg = (Msg)getItem(position);
        ViewHolder viewHolder;
        switch (getItemViewType(position)) {
            case Msg.RECEIVED:
                if(convertView==null) {
                    convertView = mInflater.inflate(R.layout.chat_item_left_layout, null);
                    viewHolder = new MsgAdapter.ViewHolder();
                    viewHolder.mLayout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);
                    viewHolder.Msg = (TextView) convertView.findViewById(R.id.tv_msg);
                    viewHolder.Img = (ImageView) convertView.findViewById(R.id.iv_img);
                    convertView.setTag(viewHolder);//通过tag标记 避免加载错位
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.Msg.setText(msg.getContent());
                viewHolder.Img.setBackgroundResource(R.drawable.rina);
                break;
            case Msg.SENT:
                if(convertView==null) {
                    convertView = mInflater.inflate(R.layout.chat_item_right_layout, null);
                    viewHolder = new MsgAdapter.ViewHolder();
                    viewHolder.mLayout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);
                    viewHolder.Msg = (TextView) convertView.findViewById(R.id.tv_msg);
                    viewHolder.Img = (ImageView) convertView.findViewById(R.id.iv_img);
                    convertView.setTag(viewHolder);//通过tag标记 避免加载错位
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.Msg.setText(msg.getContent());
                viewHolder.Img.setBackgroundResource(R.mipmap.e);
                break;
            default:
                break;
        }
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        return (mList.get(position).getType()==Msg.RECEIVED)?Msg.RECEIVED:Msg.SENT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        RelativeLayout mLayout;
        TextView Msg;
        ImageView Img;
    }
}
