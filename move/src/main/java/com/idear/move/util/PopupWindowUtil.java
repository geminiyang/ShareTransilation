package com.idear.move.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


/**
 * 作者:geminiyang on 2017/6/1.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class PopupWindowUtil {

    private PopupWindow popup;

    public static PopupWindow getPopupWindow(Context context, int layoutId) {
        View contentView = LayoutInflater.from(context).inflate(layoutId,null);
        return new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }
}
