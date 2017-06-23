package com.idear.move.util;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

/**
 * 为View视图添加属性动画的工具类
 * Created by user on 2017/4/23.
 */

public class ObjectAnimatorUtil {

    public static  void startAnimation(View ... views) {
        int length = views.length;
        double angle = Math.PI  / (length+1);
        int radius = 200;
        for(int i = 0;i < length; i++) {
            //计算按钮之间的夹角
            float translationX = (float) (Math.cos((i + 1) * angle) * radius);
            //执行属性动画的数组
            //float[] distanceX = new float[]{0,-200};
            ObjectAnimator animOne = ObjectAnimator.ofFloat(views[i], "translationX", 0,translationX);
            animOne.setInterpolator(new BounceInterpolator());
            animOne.setDuration(1000);
            animOne.start();

            float translationY = (float) (Math.sin((i+1)*angle) * - radius);
            ObjectAnimator animTwo = ObjectAnimator.ofFloat(views[i], "translationY", 0,translationY);
            animTwo.setInterpolator(new BounceInterpolator());
            animTwo.setDuration(1000);
            animTwo.start();

            ObjectAnimator animThree = ObjectAnimator.ofFloat(views[i],"scaleX",0.8f,1.0f);
            ObjectAnimator animFour = ObjectAnimator.ofFloat(views[i],"scaleY",0.8f,1.0f);
            animThree.setInterpolator(new BounceInterpolator());
            animThree.setDuration(1000);
            animThree.start();
            animFour.setInterpolator(new BounceInterpolator());
            animFour.setDuration(1000);
            animFour.start();

        }
    }

    public static void closeAnimation(View ... views) {
        int length = views.length;
        int radius = 200;
        double angle = Math.PI  / (length + 1);
        for(int i = 0;i < length;i++) {
            float translationX = (float) (Math.cos((i + 1) * angle) * radius);
            //执行属性动画的数组
            ObjectAnimator animOne = ObjectAnimator.ofFloat(views[i], "translationX", translationX, 0);
            animOne.setInterpolator(new BounceInterpolator());
            animOne.setDuration(1000);
            animOne.start();

            float translationY = (float) (Math.sin((i + 1)*angle) * -radius);
            ObjectAnimator animTwo = ObjectAnimator.ofFloat(views[i], "translationY", translationY, 0);
            animTwo.setInterpolator(new BounceInterpolator());
            animTwo.setDuration(1000);
            animTwo.start();
        }
    }

    public static void rotateAnimation(View view,float startAngle,float endAngle) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view ,"rotation", startAngle, endAngle);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }
}
