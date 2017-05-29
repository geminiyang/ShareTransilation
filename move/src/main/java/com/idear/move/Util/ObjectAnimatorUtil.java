package com.idear.move.Util;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 为View视图添加属性动画的工具类
 * Created by user on 2017/4/23.
 */

public class ObjectAnimatorUtil {

    public static  void startAnimation(View view) {
        //执行属性动画的数组
        float[] distanceY = new float[]{150,0};
        ObjectAnimator anim_bt1 = ObjectAnimator.ofFloat(view, "translationY", distanceY);
        anim_bt1.setDuration(2000);
        anim_bt1.start();

        float[] distanceX = new float[]{0,0};
        ObjectAnimator anim_bt2 = ObjectAnimator.ofFloat(view, "translationX", distanceX);
        anim_bt2.setDuration(2000);
        anim_bt2.start();
    }

    public static void closeAnimation(View view) {
        //执行属性动画的数组
        float[] distance = new float[]{0,150};
        ObjectAnimator anim_bt1 = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim_bt1.setDuration(2000);
        anim_bt1.start();
    }

    public static void rotateAnimation(View view,float startAngle,float endAngle) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view ,"rotation", startAngle, endAngle);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }
}
