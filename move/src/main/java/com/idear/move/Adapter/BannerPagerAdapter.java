package com.idear.move.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.idear.move.Activity.ActivityDetailActivity;
import com.idear.move.Fragment.UserHomeFragment;
import com.idear.move.POJO.HomeInitialItem;
import com.idear.move.R;
import com.idear.move.network.HttpPath;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置一个比较大的值，可以确定ViewPager有一个比较大的滑动范围
 * 动态的修改ViewPager的位置来实现无限循环的效果
 * Created by user on 2017/1/26.
 */

public class BannerPagerAdapter extends PagerAdapter{
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图像列表
     */
    private List<HomeInitialItem> pictureList = new ArrayList<>();

    /**
     * 默认轮播个数，设置一个较大值保证滑动滑动范围
     */
    public static final int BANNER_MAX_SIZE = 1000;
    public BannerPagerAdapter(Context mContext,List<HomeInitialItem> pictureList){
        this.mContext = mContext;
        this.pictureList = pictureList;
    }

    @Override
    public int getCount() {
        //return pictureList.size();
        return  BANNER_MAX_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 通过处理位置，避免出现数据获取错误的情况
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item,container,false);
        final ImageView iv = (ImageView) view.findViewById(R.id.imageView_banner_item);
        //进行取余操作,这样就可以获取到符合数据源取值范围的位置,从而得到想要获取展示的数据.
        position = position % pictureList.size();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(mContext, ActivityDetailActivity.class);
            }
        });
        //iv.setImageResource(R.drawable.family);
        Glide.with(mContext).load(HttpPath.getPath() + pictureList.get(position).getPic_dir()).into(iv);
        container.addView(view);
        return view;
    }

    @Override
    public void finishUpdate(ViewGroup container) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
