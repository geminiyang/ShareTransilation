package com.idear.move.util;

import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者:geminiyang on 2017/6/14.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class TextUtils {

    public void setAllViewTag(TextView... views) {
        for (TextView view :views) {
            view.setTag(view.getId());
        }
    }

    /**
     * 通过获取map的特定Key获取文本
     * @param views
     * @return
     */
    public Map<Object, String> getAllTextFromView(TextView... views) {
        Map<Object,String> map = new HashMap<>();
        for (TextView view :views) {
            map.put(view.getTag(),view.getText().toString());
        }
        return map;
    }
}
