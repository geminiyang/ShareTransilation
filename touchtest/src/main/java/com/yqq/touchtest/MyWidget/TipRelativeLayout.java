package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

/**
 * tip 的自定义布局
 */
public class TipRelativeLayout extends RelativeLayout{
    private static final int START_TIME=400;//动画出现滑动时间
    private static final int END_TIME=400;//动画移出滑动时间
    private static final int SHOW_TIME=2000;//动画停止显示时间

    private AnimationEndCallback animationEnd;
    private int titleHeight=100;//标题栏默认的高度设置成100

    public TipRelativeLayout(Context context) {
        super(context);
    }

    public TipRelativeLayout(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    public TipRelativeLayout(Context context, AttributeSet paramAttributeSet,int paramInt) {
        super(context, paramAttributeSet, paramInt);
    }

    public void showTips(){
        setVisibility(View.VISIBLE);

        //向下移动动画
        TranslateAnimation downTranslateAnimation=new TranslateAnimation(0,0,0,titleHeight);
        downTranslateAnimation.setDuration(START_TIME);
        downTranslateAnimation.setFillAfter(true);

        startAnimation(downTranslateAnimation);

        //动画监听
        downTranslateAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation){//向下移动动画结束
                topTranslateAnimation();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void topTranslateAnimation(){
        new Handler().postDelayed(new Runnable() {//延时1秒之后再向上移动
            @Override
            public void run(){

                //向上移动动画
                TranslateAnimation topTranslateAnimation=new TranslateAnimation(0,0,titleHeight,0);
                topTranslateAnimation.setDuration(END_TIME);
                topTranslateAnimation.setFillAfter(true);

                //改变透明度
                AlphaAnimation alphaAnimation=new AlphaAnimation(1,0);
                alphaAnimation.setDuration(END_TIME);

                //两个动画添加到动画集合中
                AnimationSet animationSet=new AnimationSet(true);
                animationSet.addAnimation(topTranslateAnimation);
                animationSet.addAnimation(alphaAnimation);

                startAnimation(animationSet);//开启动画

                animationSet.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation){
                        //动画结束隐藏提示的自定义控件
                        setVisibility(View.GONE);
                        if(animationEnd!=null){
                            animationEnd.onAnimationEndListener();
                        }
                    }
                });
            }
        },SHOW_TIME);
    }

    /**
     * 设置标题栏高度,也相当于滑动的距离
     * @param titleHeight
     */
    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public void setAnimationEndListener(AnimationEndCallback animationEnd) {
        this.animationEnd = animationEnd;
    }

    /**
     * 动画结束监听函数
     */
    public interface AnimationEndCallback{
        void onAnimationEndListener();
    }
}
