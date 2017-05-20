package com.yqq.touchtest.MyWidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yqq.touchtest.R;

/**
 * Created by user on 2017/1/20.
 */

public class ProgressView extends RelativeLayout{

    private View viewContent;
    private ImageView iv1,iv2,iv3,iv4;
    private ValueAnimator iv1_anim,iv2_anim,iv3_anim,iv4_anim;
    private boolean isAnimationRun;
    private ValueAnimator NullAnimator1,NullAnimator2,NullAnimator3;
    private AnimatorSet set;

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置这个视图的可见性
        setVisibility(View.INVISIBLE);
    }

    public void setContent(int resourceId) {

        //已经有视图的话先清空
        if(getChildCount()>1){
            removeView(getChildAt(1));
        }

        viewContent = LayoutInflater.from(getContext()).inflate(resourceId,null);
        RelativeLayout.LayoutParams lpContent =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        lpContent.addRule(RelativeLayout.CENTER_IN_PARENT);
        lpContent.setMargins(70,0,70,0);
        //这个是相对于整个视图的参数
        addView(viewContent,lpContent);

        setInterVal();
        setAnim();
    }

    private void setAnim() {
        iv1 = (ImageView) viewContent.findViewById(R.id.iv1);
        iv2 = (ImageView) viewContent.findViewById(R.id.iv2);
        iv3 = (ImageView) viewContent.findViewById(R.id.iv3);
        iv4 = (ImageView) viewContent.findViewById(R.id.iv4);

        iv1_anim = ValueAnimator.ofFloat(1.3f,0.8f,1.0f);
        iv1_anim.setRepeatCount(20);
        iv1_anim.setRepeatMode(ValueAnimator.REVERSE);
        iv1_anim.setInterpolator(new LinearInterpolator());
        iv1_anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv1.setScaleX((float)animation.getAnimatedValue());
                iv1.setScaleY((float) animation.getAnimatedValue());
                iv1.setAlpha(animation.getAnimatedFraction());
            }
        });

        iv2_anim = ValueAnimator.ofFloat(1.3f,0.8f,1.0f);
        iv2_anim.setRepeatCount(20);
        iv2_anim.setRepeatMode(ValueAnimator.REVERSE);
        iv2_anim.setInterpolator(new LinearInterpolator());
        iv2_anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv2.setScaleX((float)animation.getAnimatedValue());
                iv2.setScaleY((float) animation.getAnimatedValue());
                iv2.setAlpha(animation.getAnimatedFraction());
            }
        });

        iv3_anim = ValueAnimator.ofFloat(1.3f,0.8f,1.0f);
        iv3_anim.setRepeatCount(20);
        iv3_anim.setRepeatMode(ValueAnimator.REVERSE);
        iv3_anim.setInterpolator(new LinearInterpolator());
        iv3_anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv3.setScaleX((float)animation.getAnimatedValue());
                iv3.setScaleY((float) animation.getAnimatedValue());
                iv3.setAlpha(animation.getAnimatedFraction());
            }
        });

        iv4_anim = ValueAnimator.ofFloat(1.3f,0.8f,1.0f);
        iv4_anim.setRepeatCount(20);
        iv4_anim.setRepeatMode(ValueAnimator.REVERSE);
        iv4_anim.setInterpolator(new LinearInterpolator());
        iv4_anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv4.setScaleX((float)animation.getAnimatedValue());
                iv4.setScaleY((float) animation.getAnimatedValue());
                iv4.setAlpha(animation.getAnimatedFraction());
            }
        });

        /** 参数说明
         * float fromX 动画起始时 X坐标上的伸缩尺寸
         *float toX 动画结束时 X坐标上的伸缩尺寸
         *float fromY 动画起始时Y坐标上的伸缩尺寸
         *float toY 动画结束时Y坐标上的伸缩尺寸
         *int pivotXType 动画在X轴相对于物件位置类型
         *float pivotXValue 动画相对于物件的X坐标的开始位置
         *int pivotYType 动画在Y轴相对于物件位置类型
         *float pivotYValue 动画相对于物件的Y坐标的开始位置
         */

//        //animatorUpdateListener用于ValueAnimator
//        iv1_anim.addUpdateListener(new AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//
//            }
//        });

        set = new AnimatorSet();
        //iv1_anim 与 NullAnimator1 一起执行
        set.play(iv1_anim).with(NullAnimator1) ;
        //iv2_anim 在 NullAnimator1 之前执行
        set.play(NullAnimator1).before(iv2_anim) ;
        // NullAnimator1 在 NullAnimator2 之前执行
        set.play(NullAnimator1).before(NullAnimator2) ;
        // NullAnimator2 与 iv2_anim一起执行
        set.play(NullAnimator2).with(iv2_anim) ;
        // iv3_anim 在 NullAnimator2 之前执行
        set.play(NullAnimator2).before(iv3_anim) ;
        // NullAnimator2 在 NullAnimator3 之前执行
        set.play(NullAnimator2).before(NullAnimator3) ;
        //iv3_anim 与 NullAnimator3 一起执行
        set.play(NullAnimator3).with(iv3_anim) ;
        // iv4_anim 在 NullAnimator3 之前执行
        set.play(NullAnimator3).before(iv4_anim) ;


        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimationRun = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimationRun = false;
                stopRun();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimationRun = false;
            }
        });
    }

    private void setInterVal(){
        NullAnimator1 = ValueAnimator.ofFloat(1.0f,0);
        NullAnimator1.setDuration(100);

        NullAnimator2 = ValueAnimator.ofFloat(1.0f,0);
        NullAnimator2.setDuration(100);

        NullAnimator3 = ValueAnimator.ofFloat(1.0f,0);
        NullAnimator3.setDuration(100);

    }

    public void startRun(){
        if(getVisibility()==View.GONE ||getVisibility()==View.INVISIBLE){
            setVisibility(View.VISIBLE);
        }
        if (isAnimationRun) {
            set.cancel();
        }
        set.setStartDelay(500);
        set.start();
    }

    public void stopRun(){
        setVisibility(INVISIBLE);
        if (isAnimationRun) {
            set.end();
        }
    }

    public void showView(){

    }

    public void hideView(){

    }

    private void methodOne(){
          //此重复针对一个大物体的组合动画

        ObjectAnimator yqq = ObjectAnimator.ofFloat(viewContent,"yqq",200,300);

//        ObjectAnimator animtion1=ObjectAnimator.ofFloat(mIvLine, "translationX", 8, 20);
//        animtion1.setRepeatCount(1000);
//        animtion1.setRepeatMode(ValueAnimator.INFINITE);
//        ObjectAnimator animtion2=ObjectAnimator.ofFloat(mIvLine, "translationY",20, 30);
//        animtion2.setRepeatCount(1000);
//
//        ObjectAnimator animtion3=ObjectAnimator.ofFloat(mIvLine, "rotation", 30, 60);
//        animtion3.setRepeatCount(1000);
//        ObjectAnimator animtion5=ObjectAnimator.ofFloat(mIvLine, "translationX", 20, 0);
//        animtion5.setRepeatCount(1000);
//        ObjectAnimator animtion6=ObjectAnimator.ofFloat(mIvLine, "translationY", 30, 0);
//        animtion6.setRepeatCount(1000);
//        ObjectAnimator animtion4=ObjectAnimator.ofFloat(mIvLine, "rotation", 60, 30);
//        animtion4.setRepeatCount(1000);
//        AnimatorSet set=new AnimatorSet();
//
//        set.playTogether(animtion1,animtion2,animtion3,animtion4,animtion5,animtion6);
//        set.setDuration(1300).start();
    }
}
