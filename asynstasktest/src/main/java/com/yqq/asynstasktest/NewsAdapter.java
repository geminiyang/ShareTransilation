package com.yqq.asynstasktest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by user on 2017/4/28.
 */

public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<NewsBean> mList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private Context mContext;

    private int mStart,mEnd;
    private static String [] URLS;//当前所获取图像的所有URL地址
    private boolean isFirstInitial;//表示是否是第一次启动

    public NewsAdapter(Context context, List<NewsBean> data, ListView listView) {
        this.mList = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        isFirstInitial = true;
        //确保只有一个Lru 的cache
        this.mImageLoader = new ImageLoader(listView);
        URLS = new String[data.size()];
        for(int i=0; i < data.size(); i++) {
            URLS[i] = data.get(i).getNewsIconUrl();
        }
        //设置监听事件
        listView.setOnScrollListener(this);
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

        ViewHolder _ViewHolder = null;
        if(convertView == null) {
            _ViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout,null);
            _ViewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            _ViewHolder.content = (TextView) convertView.findViewById(R.id.tv_content);
            _ViewHolder.picSmall = (ImageView) convertView.findViewById(R.id.iv_item);
            convertView.setTag(_ViewHolder);
        } else {
            _ViewHolder = (ViewHolder) convertView.getTag();
        }
        _ViewHolder.title.setText(mList.get(position).getNewsTitle());
        _ViewHolder.content.setText(mList.get(position).getNewsContent());
        //占位图
        _ViewHolder.picSmall.setImageResource(R.mipmap.ic_launcher);
        //通过tag绑定特定控件
        String url = mList.get(position).getNewsIconUrl();
        //特定控件的tag只设置一次
        _ViewHolder.picSmall.setTag(url);
        //启用异步任务,将显示图片的控制权交给ListView的滑动状态
        mImageLoader.showImgByAsyncTask(_ViewHolder.picSmall,url);//设置缓存中url对应的图片
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE) {
            //滑动停止状态(加载可见项)
            mImageLoader.LoadImage(mStart,mEnd);
            if(view.getLastVisiblePosition() == (view.getCount()-1)) {
                Toast.makeText(mContext,"已经到底部了",Toast.LENGTH_SHORT).show();
            }
        } else {
            //取消加载任务
            mImageLoader.cancelAllTask();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //始终是会调用的
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if((isFirstInitial)&&visibleItemCount>0) {
            mImageLoader.LoadImage(mStart,mEnd);
            isFirstInitial = false;
        }
    }

    public static String getURL(int index) {
        return URLS[index];
    }

    private class ViewHolder {
        TextView title , content;
        ImageView picSmall;
    }
}
