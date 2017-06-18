package com.idear.move.myWidget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.idear.move.Adapter.HSVAdapter;
import com.idear.move.constants.AppConstant;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.Activity.ActivityDetailActivity;
import com.idear.move.Activity.FeedBackDetailActivity;
import com.idear.move.Activity.SpreadDetailActivity;
import com.idear.move.util.ScreenUtil;

import java.util.Map;

/**
 * Created by user on 2017/4/26.
 */

public class HSVLinearLayout extends LinearLayout {

    private HSVAdapter adapter;
    private Context context;
    private int itemWidth,itemHeight;

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;

    public HSVLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    /**
     * 设置数据源
     * @param adapter   数据源适配器
     * @param type  区分监听器的类型
     * @param width item 宽度
     * @param height item 高度
     */
    public void setAdapter(HSVAdapter adapter,int type,int width,int height) {
        this.itemWidth = width;
        this.itemHeight = height;
        this.adapter = adapter;
        final int myType = type;
        for (int i = 0; i < adapter.getCount(); i++) {
            final Map<String, Object> map = (Map<String, Object>) adapter.getItem(i);
            View view = adapter.getView(i, null, null);
            view.setPadding(0, 0, 0, 0);
            // 为视图设定点击监听器
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "您选择了" + map.get("index"),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(AppConstant.UPDATE_IMAGE_ACTION);
                    intent.putExtra("index", (Integer)map.get("index"));
                    context.sendBroadcast(intent);
            switch (myType) {
                case TYPE_ONE:
                    IntentSkipUtil.skipToNextActivity(context, ActivityDetailActivity.class);
                    break;
                case TYPE_TWO:
                    IntentSkipUtil.skipToNextActivity(context, FeedBackDetailActivity.class);
                    break;
                case TYPE_THREE:
                    IntentSkipUtil.skipToNextActivity(context, SpreadDetailActivity.class);
                    break;
            }



                }
            });
            setOrientation(HORIZONTAL);
            LayoutParams params = new LayoutParams(ScreenUtil.dip2px(context,itemWidth),
                    ScreenUtil.dip2px(context,itemHeight));
            addView(view, params);
            //addView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        }
    }
}
