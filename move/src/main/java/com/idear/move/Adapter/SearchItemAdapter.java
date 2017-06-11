package com.idear.move.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idear.move.R;

import java.util.List;
import java.util.Map;

/**
 * 作者:geminiyang on 2017/6/10.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class SearchItemAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnFillIconClickListener fillIconClickListener;//声明自定义监听器
    private View.OnClickListener iconClickListener = new View.OnClickListener() {//创建自定义点击监听事件
        public void onClick(View v) {
            if (fillIconClickListener != null) {
                //如果自定义监听器不为空,则调用
                fillIconClickListener.onClickFillIcon(v,(String)v.getTag());
            }
        }
    };

    public SearchItemAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        this.mInflater =LayoutInflater.from(mContext);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchItemViewHolder viewHolder = new SearchItemViewHolder();

        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.item_search, null);
            viewHolder.searchString = (TextView) convertView.findViewById(R.id.search_str);
            viewHolder.fillIcon = (ImageView) convertView.findViewById(R.id.fill_icon);
            convertView.setTag(viewHolder);//通过tag标记 避免加载错位
        } else {
            viewHolder = (SearchItemViewHolder) convertView.getTag();
        }
        viewHolder.searchString.setText(mList.get(position));
        viewHolder.fillIcon.setTag(mList.get(position));
        viewHolder.fillIcon.setOnClickListener(iconClickListener);
        return convertView;
    }

    public interface OnFillIconClickListener  {
        void onClickFillIcon(View view,String str);
    }

    public void setButtonClickListener(OnFillIconClickListener fillIconClickListener) {
        //提供Set方法让Activity调用
        this.fillIconClickListener = fillIconClickListener;
    }

    class SearchItemViewHolder {
        RelativeLayout mLayout;
        TextView searchString;
        ImageView searchIcon,fillIcon;
    }
}
