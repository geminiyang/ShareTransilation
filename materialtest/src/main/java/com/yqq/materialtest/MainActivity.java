package com.yqq.materialtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button one,two,three,four,five,six;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enable transitions
        //设置允许过度动画，一定要在setContentView之前调用
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //getWindow().setExitTransition(new Fade().setDuration(1000));
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSomeButtonClicked(view);
            }
        });

        //获取outLine，我们需要使用ViewoutLineProvider
        ViewOutlineProvider viewOutlineProvider=new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //修改outLine的形状，这里是设置分别设置左上右下，以及Radius
                outline.setRoundRect(0,0,view.getWidth(),view.getHeight(),8.0f);
            }

        };
        //将需要控件重写设置形状
        textView.setOutlineProvider(viewOutlineProvider);

        //心形图片点击
        final ImageView heart = (ImageView) findViewById(R.id.heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getVisibility() == View.INVISIBLE){
                    // get the center for the clipping circle
                    //获取裁剪圆的中心点
                    int cx = (heart.getLeft() + heart.getRight()) / 2;
                    int cy = (heart.getTop() + heart.getBottom()) / 3;

                    // get the final radius for the clipping circle
                    //获取裁剪圆的最后弧度
                    int finalRadius = Math.max(heart.getWidth(), heart.getHeight());

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(heart, cx, cy, 0, finalRadius);

                    // make the view visible and start the animation
                    heart.setVisibility(View.VISIBLE);
                    anim.start();
                } else{
                    // get the center for the clipping circle
                    int cx = (heart.getLeft() + heart.getRight()) / 2;
                    int cy = (heart.getTop() + heart.getBottom()) / 2;

                    // get the initial radius for the clipping circle
                    int initialRadius = heart.getWidth();

                    // create the animation (the final radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(heart, cx, cy, initialRadius, 0);

                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                    anim.setDuration(1500);

                    // make the view invisible when the animation is done
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            heart.setVisibility(View.INVISIBLE);
                        }
                    });

                    // start the animation
                    anim.start();

                }
            }
        });

        final ImageView imageView = (ImageView) findViewById(R.id.iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = view.getBackground();
                if(drawable instanceof Animatable){
                    ((Animatable) drawable).start();
                }

                //显示隐藏视图
                if(heart.getVisibility() ==View.INVISIBLE){
                    // get the center for the clipping circle
                    //获取裁剪圆的中心点
                    int cx = (heart.getLeft() + heart.getRight()) / 2;
                    int cy = (heart.getTop() + heart.getBottom()) / 2;

                    // get the final radius for the clipping circle
                    //获取裁剪圆的最后弧度
                    int finalRadius = Math.max(heart.getWidth(), heart.getHeight());

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(heart, cx, cy, 0, finalRadius);

                    // make the view visible and start the animation
                    heart.setVisibility(View.VISIBLE);
                    anim.start();
                }

            }
        });

        initButton();
    }

    private void initButton() {
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);


    }

    public void onSomeButtonClicked(View view) {
        getWindow().setExitTransition(new Explode());
        Intent intent = new Intent(this, SecondActivity.class);
        //启用普通元素的transition
        startActivity(intent,
                ActivityOptions
                        .makeSceneTransitionAnimation(this).toBundle());
    }


    //测试transition 的方法
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.one:
                getWindow().setExitTransition(new Explode());
                intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.two:
                getWindow().setExitTransition(new ChangeTransform());
                intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.three:
                getWindow().setExitTransition(new ChangeClipBounds());
                intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.four:
                ChangeBounds changeBounds = new ChangeBounds();
                getWindow().setExitTransition(changeBounds);
                intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.five:
                getWindow().setExitTransition(new Fade().setDuration(1200).setStartDelay(500));
                intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.six:
                Slide slide = new Slide();
                slide.setDuration(1000); //设置返回到该界面过度的时间
                slide.setInterpolator(new DecelerateInterpolator()); //设置返回到该界面的的加速模式
                slide.setStartDelay(500);
                getWindow().setExitTransition(slide);


                intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            default:
        }
    }
}
