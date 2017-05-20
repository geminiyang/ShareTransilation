package com.yqq.horizonalscrollviewtest;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by user on 2017/4/26.
 */

public class HSVLinearLayout extends LinearLayout {

    private HSVAdapter adapter;
    private Context context;

    public HSVLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    /**
     * 为当前布局设置数据源
     * @param adapter
     */
    public void setAdapter(HSVAdapter adapter) {

        this.adapter = adapter;
        for (int i = 0; i < adapter.getCount(); i++) {
            final Map<String, Object> map = (Map<String, Object>) adapter.getItem(i);
            View view = adapter.getView(i, null, null);
            view.setPadding(10, 5, 10, 0);
            // 为视图设定点击监听器
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "您选择了" + map.get("index"),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(AppConstant.UPDATE_IMAGE_ACTION);
                    intent.putExtra("index", (Integer)map.get("index"));
                    context.sendBroadcast(intent);

                }
            });
            setOrientation(HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(240,300);
            addView(view, new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        }
    }
}
