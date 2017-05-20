package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.yqq.touchtest.R;
import com.yqq.touchtest.Util.TranslucentStatusSetting;


public class SecondActivity extends Activity implements View.OnClickListener {

    private Button bt,SLIDE,start,stop,totable,toswipe;
    private ImageView imageView;
    private ViewGroup viewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(SecondActivity.this);
        setContentView(R.layout.activity_second);

        viewGroup = (ViewGroup) findViewById(R.id.layout_root_view);
        viewGroup.setOnClickListener(this);

        bt= (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SecondActivity.this,ThreeActivity.class);
                startActivity(it);
            }
        });

        SLIDE = (Button) findViewById(R.id.slide);
        SLIDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SecondActivity.this,TransitionFromActivity.class);
                startActivity(it);
            }
        });


        imageView = (ImageView) findViewById(R.id.pig_img);

//        final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//        animationDrawable.setOneShot(false);
//        animationDrawable.setDither(true);
        final AnimationDrawable animationDrawable = initAnimationDrawable();


        start = (Button) findViewById(R.id.frameAnimation_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationDrawable.start();
            }
        });

        stop = (Button) findViewById(R.id.frameAnimation_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationDrawable.stop();
            }
        });

        AnimationSet animationset = (AnimationSet) AnimationUtils.loadAnimation
                (SecondActivity.this,R.anim.animationset1);
        imageView.startAnimation(animationset);

        totable = (Button) findViewById(R.id.to_table);
        totable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SecondActivity.this,TableActivity.class);
                startActivity(it);
            }
        });
        toswipe = (Button) findViewById(R.id.to_swiperefresh);
        toswipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SecondActivity.this,SwipeRefreshActivity.class);
                startActivity(it);
            }
        });

        setupWindowAnimations();

    }

    /**
     * 初始化帧动画
     * @return
     */
    private AnimationDrawable initAnimationDrawable() {
        //使用代码方式,如果使用xml方式就要设置background为animframe.xml
        AnimationDrawable animDrawable = new AnimationDrawable();
        for(int i=1;i<8;i++){
            int id = getResources().getIdentifier("pigrun" + i , "drawable" , getPackageName());
            Drawable drawable =  getResources().getDrawable(id);
            animDrawable.addFrame(drawable,120);
        }
        imageView.setImageDrawable(animDrawable);
        animDrawable.setOneShot(false);
        return animDrawable;
    }

    private void setupWindowAnimations() {
        //代码方式
//        Slide slide = new Slide();
//        slide.setDuration(1000);
//        getWindow().setExitTransition(slide);
        //xml方式
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setReenterTransition(fade);
    }

    @Override
    public void onClick(View v) {
        //transition框架的两个主要优点。
        //第一、Transitions抽象和封装了属性动画，Animator的概念对开发者来说是透明的，因此它极大的精简了代码量。
        // 开发者所做的所有事情只是改变一下view前后的状态数据，Transition就会自动的根据状态的区别去生成动画效果。
        // 第二、不同场景之间变换的动画效果可以简单的通过使用不同的Transition类来改变
        Explode explode = new Explode();
        explode.setDuration(1000);
        TransitionManager.beginDelayedTransition(viewGroup,
                TransitionInflater.from(this).inflateTransition(R.transition.activity_slide)
                        .setDuration(1000));
        toggleVisible(SLIDE);
    }

    private void toggleVisible(View... views) {
        for (View view:views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
