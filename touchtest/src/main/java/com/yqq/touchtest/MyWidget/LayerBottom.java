package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yqq.touchtest.R;

/**
 * Created by user on 2017/1/19.
 */

public class LayerBottom extends RelativeLayout{

    private Animation animationShow,animationHide,animationBackShow,animationBackHide;
    private AnimationSet set;
    private View viewContent;
    private View viewBlackBack;

    private View one,two,three;

    public LayerBottom(Context context, AttributeSet attrs) {
        super(context, attrs);

        //设置这个视图的可见性
       setVisibility(View.INVISIBLE);
        //设置后面那层黑色为整个屏幕，就像是阴影效果
        viewBlackBack = new View(getContext());
        viewBlackBack.setBackgroundColor(Color.parseColor("#66000000"));
        //设置整个自定义参数的布局控件
        RelativeLayout.LayoutParams lpBack =
                new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                360);
        lpBack.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(viewBlackBack,lpBack);
    }

    /**
     * 确认当前自定义控件有没有处于显示状态
     * @return
     */
    public boolean isShow(){
        return (getVisibility()==View.VISIBLE);
    }

    /**
     * 设置内容窗口
     * @param resourceId
     */
    public void setContent(int resourceId){
        //已经有视图的话先清空
        if(getChildCount()>1){
            removeView(getChildAt(1));
        }

        viewContent = LayoutInflater.from(getContext()).inflate(resourceId,null);
        RelativeLayout.LayoutParams lpContent =
                new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        lpContent.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpContent.setMargins(12,0,12,0);
        //这个是相对于整个视图的参数
        addView(viewContent,lpContent);
        //或者这个是对于特定视图的参数
        //viewContent.setLayoutParams(lpContent);
    }

    /**
     * 获取布局内容
     * @return
     */
    private View getContentView(){
        return viewContent;
    }

    public void setClick(){
        one = getContentView().findViewById(R.id.ll_layer_take_picture);
        two = getContentView().findViewById(R.id.ll_layer_select);
        three = getContentView().findViewById(R.id.ll_layer_cancel);

        one.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT).show();
            }
        });

        two.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT).show();
            }
        });

        three.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 代码动态绘制View的圆角边框
     * @param radius 15dp
     * @param borderLength 1dp
     * @param borderColor
     * @return
     */
    public static ShapeDrawable createRoundCornerShapeDrawable(float radius, float borderLength, int borderColor) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = radius + borderLength;
            innerRadii[i] = radius;
        }

        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(borderLength, borderLength,
                borderLength, borderLength), innerRadii));
        sd.getPaint().setColor(borderColor);

        return sd;
    }

    /**
     *打开窗口
     */
    public void showLayer(){
        if(getVisibility()==View.GONE ||getVisibility()==View.INVISIBLE){
            setVisibility(View.VISIBLE);
        }
        showBackBlack();
        //出场动画
        if(animationShow==null){
            //位移动画(指的是相对view的坐标系的移动)
            //animationShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
            //        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            //旋转动画
            //animationShow = new RotateAnimation(-90,0);
            //透明度变化动画

            //缩放动画

            //RELATIVE_TO_SELF，RELATIVE_TO_PARENT,ABSOLUTE 这是参考系的选择

            set = new AnimationSet(true);
            Animation animationShowAfter = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,
                    Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            Animation animationShowFinal = new ScaleAnimation(1.2f,1.0f,1.2f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            //这里指的都是参考系
            animationShow = new ScaleAnimation(0, 1.0f, 0, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            set.addAnimation(animationShow);
            set.addAnimation(animationShowAfter);
            set.addAnimation(animationShowFinal);
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    //通过Animatoin的嵌套实现动画的执行顺序控制
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            hideLayer();
//                        }
//                    },1000);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        animationShow.setDuration(200);
        animationShow.setFillAfter(true);
        viewContent.startAnimation(set);
    }

    //控件从自身位置的最右端开始向左水平滑动了自身的宽度，持续时间为0.2s
    private void showScrollAnim(View view) {
        //入场动画
        view.setVisibility(View.VISIBLE);
        TranslateAnimation mShowAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(200);
        view.startAnimation(mShowAction);
    }

    //控件从自身位置的最左端开始水平向右滑动隐藏动画，持续时间0.2s
    private void hiddenScrollAnim(View view) {
        //退场动画
        view.setVisibility(View.GONE);
        TranslateAnimation mHiddenAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(200);
        view.startAnimation(mHiddenAction);
    }

    /**
     *  隐藏窗口
     */
    public void hideLayer(){
        hideBackBlack();
        if(animationHide==null){
            //控件从自身位置的最上端开始垂直向下滑动隐藏动画
            animationHide = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            //控件从自身位置的最上端开始垂直向下滑动显示动画
//            animationHide = new TranslateAnimation(
//                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        //退场动画
        animationHide.setDuration(500);
        viewContent.startAnimation(animationHide);
        animationHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getVisibility() == View.VISIBLE) {
                    setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showBackBlack(){
        if (animationBackShow == null) {
            animationBackShow = new AlphaAnimation(0.0f, 1.0f);
            animationBackShow.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewBlackBack.setVisibility(View.VISIBLE);
                    viewContent.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                }

            });
        }
        animationBackShow.setDuration(500);
        viewBlackBack.startAnimation(animationBackShow);
    }

    public void hideBackBlack(){
        if (animationBackHide == null) {
            animationBackHide = new AlphaAnimation(1.0f, 0.0f);
            animationBackHide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    viewBlackBack.setVisibility(View.INVISIBLE);
                    viewContent.setVisibility(View.INVISIBLE);
                }
            });

        }
        animationBackHide.setDuration(500);
        viewBlackBack.startAnimation(animationBackHide);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //保证后面UI不点击
        return true;
    }

}
