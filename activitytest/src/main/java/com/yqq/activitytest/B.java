package com.yqq.activitytest;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.Visibility;
import com.transitionseverywhere.extra.Scale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import constant.Constans;

public class B extends BaseActivity implements View.OnClickListener{

    private Button bt,call,sdcard,test,changeMethod;
    private EditText et;
    private TextView tv,tv2;
    private ImageView iv;
    private boolean visible,isRotated,isFirstText,isReturnAnimation;
    private ProgressBar mProgressBar;
    private Button pathbt;
    private LinearLayout transitionsContainerTransitionName;
    private LayoutInflater inflater;
    ArrayList<String> titles;
    private int index = 1;
    static int METHODFlAG=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b);

        bt = (Button) findViewById(R.id.b);
        bt.setOnClickListener(this);
        call = (Button) findViewById(R.id.call);
        sdcard = (Button) findViewById(R.id.sdcard);
        et = (EditText) findViewById(R.id.et);
        call.setOnClickListener(this);
        sdcard.setOnClickListener(this);
        pathbt = (Button) findViewById(R.id.btn_path_motion);
        pathbt.setOnClickListener(this);

        changeMethod = (Button) findViewById(R.id.change);
        changeMethod.setOnClickListener(this);

        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);
        test = (Button) findViewById(R.id.transition);
        test.setOnClickListener(this);

        iv = (ImageView) findViewById(R.id.ball);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //需要配合方法6使用
        initItemView();
    }

    /**
     * 初始化打乱视图，配合方法六
     */
    private void initItemView() {
        transitionsContainerTransitionName =
                (LinearLayout) findViewById(R.id.ll_container_transition_name);
        inflater = LayoutInflater.from(this);

        //生成四个标签
        titles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            titles.add(String.format(Locale.CHINA, "Item %d", i));
        }
        createViews(inflater, transitionsContainerTransitionName, titles);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b:
                int i = 0;
                //Toast.makeText(B.this,"test",Toast.LENGTH_SHORT).show();
                ToastUtil.getInstance().showToast(B.this,"test");
//        Intent it = new Intent(B.this,C.class);
//        B.this.startActivity(it);
//        //ActivityManagerApplication.getInstance().addActivity(B.this);
//        ActivityManagerApplication aa = (ActivityManagerApplication) B.this.getApplication();
//        aa.addActivity(B.this);
                break;
            case R.id.call:
                callPhone();
                break;
            case R.id.sdcard:
                sdDownLoad();
                break;
            case R.id.transition:
                switch (METHODFlAG){
                    case 1:
                        MethodOne();
                        break;
                    case 2:
                        MethodTwo();
                        break;
                    case 3:
                        MethodThree();
                        break;
                    case 4:
                        MethodFour();
                        break;
                    case 5:
                        MethodFive();
                        break;
                    case 6:
                        MethodSix();
                        break;
                    case 7:
                        MethodSeven();
                        break;
                    case 8:
                        ToastUtil.getInstance().showToast(B.this,"please Click PATH MOTION");
                        break;
                    case 9:
                        MethodNight();//此方法会被颜色改变效果覆盖，因为颜色改变的那个方法没有添加目标
                        break;
                    case 10:
                        if(true){
                            index = index%4;
                            MethodTen(index*33);
                            index ++;
                        }
                        break;

                }

                break;
            case R.id.btn_path_motion:
                MethodEight();
                break;
            case R.id.change:
                if(METHODFlAG>=10){
                    METHODFlAG=0;
                } else {
                    ++METHODFlAG;
                }
                if (METHODFlAG==0){
                    changeMethod.setText("SELECT_Method");
                } else{
                    changeMethod.setText(METHODFlAG+"");
                }

                break;

        }

    }

    private void MethodTen(int value) {
        final RelativeLayout mTransitionsContainer = (RelativeLayout) findViewById(R.id.rl);
            TransitionManager.beginDelayedTransition(mTransitionsContainer, new ProgressTransition());
            value = Math.max(0, Math.min(100, value));
            mProgressBar.setProgress(value);
    }

    //transition动画的目标有两个
    private void MethodNight() {
        final RelativeLayout transitionsContainerTarget = (RelativeLayout) findViewById(R.id.rl);
        Slide slide = new Slide(Gravity.RIGHT);
        slide.setDuration(2000);
        slide.excludeTarget(tv, true);//排除不想做动画的目标

        Fade fade = new Fade();
        fade.setDuration(2000);
        fade.excludeTarget(tv2, true);

        TransitionSet transitionSet = new TransitionSet()
                .addTransition(slide)
                .addTransition(fade);

        TransitionManager.beginDelayedTransition(transitionsContainerTarget, transitionSet);

        if (tv.getVisibility() == View.VISIBLE) {
            tv2.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        } else {
            tv2.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
        }
    }

    //changeImageTransformPath (Curved) motion 路径过渡动画
    //所有的过渡动画都需要两个值：起始值和结束值
    //比如：通过 ChangeBounds 来改变 view 的位置，通过 setPathMotion 来提供路径
    private void MethodEight() {
        final FrameLayout transitionsContainerPathMotion = (FrameLayout) findViewById(
                R.id.fl_container_path_motion);


            TransitionManager.beginDelayedTransition(transitionsContainerPathMotion,
                    new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) pathbt.getLayoutParams();
            //下面这个参数指的是移动到哪里，isReturnAnimation在这里初始值是false
            params.gravity = isReturnAnimation ? (Gravity.LEFT | Gravity.TOP) :
                    (Gravity.BOTTOM | Gravity.RIGHT);
            pathbt.setLayoutParams(params);
            isReturnAnimation = !isReturnAnimation;
    }

    /**
     * Explode and Propagation 爆炸和传播，具体是现在ExplodeActivity
     */
    private void MethodSeven() {
        //通过Itent跳转
        Intent it = new Intent(B.this,ExplodeActivity.class);
        startActivity(it);
    }


    /**
     * 打乱动画
     */
    private void MethodSix() {
        TransitionManager.beginDelayedTransition(transitionsContainerTransitionName,
                new ChangeBounds());
        Collections.shuffle(titles);//打乱数组列表的数据
        createViews(inflater, transitionsContainerTransitionName, titles);
    }

    /**
     * 生成需要打乱的视图
     * @param inflater
     * @param layout
     * @param titles
     */
    private void createViews(LayoutInflater inflater, LinearLayout layout, ArrayList<String> titles) {
        layout.removeAllViews();//删除上一次的视图
        for (String title : titles) {
            TextView textView = (TextView) inflater.inflate(R.layout.text_view, layout, false);
            textView.setText(title);
            TransitionManager.setTransitionName(textView, title);
            layout.addView(textView);
        }
    }

    /**
     * 改变字体
     */
    private void MethodFive() {
        final RelativeLayout transitionContainer = (RelativeLayout) findViewById(R.id.rl);
        String secText = " Second Text";
        String firstText = "First Text";
        if(tv.getVisibility()==View.GONE){
            tv.setVisibility(View.VISIBLE);
        }
        TransitionManager.beginDelayedTransition(transitionContainer,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN).setDuration(1000));
        //初始值为false
        tv.setText(isFirstText ? secText : firstText);
        isFirstText = !isFirstText;
    }

    /**
     * 旋转
     */
    private void MethodFour() {
        final RelativeLayout transitionContainer = (RelativeLayout) findViewById(R.id.rl);
        //第二种实现方法
        isRotated = !isRotated;
        //一开始isRotated的值是true
        //ToastUtil.getInstance().showToast(B.this,isRotated+"");
        TransitionManager.beginDelayedTransition(transitionContainer,new Rotate());
        iv.setRotation(isRotated?135:0);

    }

    /**
     * 改变颜色
     */
    private void MethodThree() {
        // bt.setBackgroundColor(visible ? green : white);
        // 无动画效果 通过 setBackgroundColor 背景色时没有动画效果
        // 可以使用 setBackground, setBackgroundDrawable
        final RelativeLayout transitionContainer = (RelativeLayout) findViewById(R.id.rl);
        //第二种实现方法
        visible = !visible;
        int green = getResources().getColor(R.color.green);
        int white = getResources().getColor(R.color.white);
        int grey = getResources().getColor(R.color.grey);

        int curcolor = getWindow().getStatusBarColor();
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),curcolor,green);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getWindow().setStatusBarColor((Integer) animation.getAnimatedValue());
            }
        });
        colorAnimator.setDuration(300).setStartDelay(0);
        colorAnimator.start();

        Recolor recolor = new Recolor();
        recolor.setDuration(2000);
        TransitionManager.beginDelayedTransition(transitionContainer,recolor);
        //test.setBackgroundColor(visible ? green:white);//无动画效果
        test.setTextColor(visible ? green:white);
        test.setBackground(new ColorDrawable(visible ? white:green));
    }

    /**
     * 动画集
     */
    private void MethodTwo() {
        final RelativeLayout transitionContainer = (RelativeLayout) findViewById(R.id.rl);
        boolean visible;
        //第一种实现方法
        if(tv.getVisibility()==View.VISIBLE){
            visible = true;
        }else {
            visible = false;
        }

        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new Fade());
        transitionSet.addTransition(new Scale(0.6f));
        transitionSet.addTransition(new Slide(Gravity.LEFT));
        transitionSet.setInterpolator(visible ? new LinearOutSlowInInterpolator():
                new FastOutSlowInInterpolator());
        TransitionManager.beginDelayedTransition(transitionContainer,transitionSet);
        if(tv.getVisibility()==View.VISIBLE){
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置单个动画
     */
    private void MethodOne() {
        final RelativeLayout transitionContainer = (RelativeLayout) findViewById(R.id.rl);
        //淡入淡出
        Fade fade = new Fade();
        fade.setMode(Visibility.MODE_OUT);
        fade.setDuration(1000);
        //缩放
        final Scale scale = new Scale(0.7f);
        scale.setDuration(1000);

        //滑动
        Slide slide = new Slide(Gravity.RIGHT);
        slide.setDuration(1000);
        slide.setStartDelay(1000);
        slide.setInterpolator(new FastOutLinearInInterpolator());

        if(test.isPressed()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        test.post(new Runnable() {
                            @Override
                            public void run() {
                                test.setTranslationX(15.0f);
                                test.setText("right");
                                TransitionManager.beginDelayedTransition(transitionContainer, scale);
                                if (tv.getVisibility()==View.VISIBLE){
                                    tv.setVisibility(View.GONE);
                                } else{
                                    tv.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        Thread.sleep(1000);
                        test.post(new Runnable() {
                            @Override
                            public void run() {
                                test.setTranslationX(-15.0f);
                                test.setText("left");
                                TransitionManager.beginDelayedTransition(transitionContainer, scale);
                                if (tv.getVisibility()==View.VISIBLE){
                                    tv.setVisibility(View.GONE);
                                } else{
                                    tv.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        TransitionManager.beginDelayedTransition(transitionContainer);


    }

    private void callPhone(){
        if(hasPermission(Manifest.permission.CALL_PHONE)){
            doCallPhone();
        } else{
            requestPermissions(Constans.CALL_PHONE_CODE,Manifest.permission.CALL_PHONE);
        }
    }

    private void sdDownLoad(){
        if(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            doSDCardPermission();
        } else{
            requestPermissions(Constans.WRITE_EXTRENAL_CODE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }


    /**
     * 具体实现父类中的打电话操作
     */
    @Override
    public void doCallPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "15118871363");
        intent.setData(data);
        B.this.startActivity(intent);

    }

    /**
     * 具体实现父类中读写sdcard的操作
     */
    @Override
    public void doSDCardPermission() {
        ToastUtil.getInstance().showToast(B.this,"downloading!");
    }
}
