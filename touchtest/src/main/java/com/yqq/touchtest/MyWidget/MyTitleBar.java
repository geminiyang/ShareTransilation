package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yqq.touchtest.R;

/**
 * Created by user on 2017/1/24.
 */

public class MyTitleBar extends LinearLayout{

    private RelativeLayout rltitle;//整个布局
    private View btLeft;//左按钮
    private View btRight;//右按钮
    private String title;//标题文本
    private TextView tvtitle;//textView


    public MyTitleBar(Context context) {
        super(context);
    }

    public MyTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    //实际上初始化控件的方法
    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        //获取自定义的属性
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MyTitleBar);
        CharSequence cstitle = typeArray.getText(R.styleable.MyTitleBar_title);
        Drawable bgDrawable = typeArray.getDrawable(R.styleable.MyTitleBar_background);
        int leftImg = typeArray.getResourceId(R.styleable.MyTitleBar_leftImg,-1);
        int rightImg = typeArray.getResourceId(R.styleable.MyTitleBar_rightImg,-1);
        CharSequence rightText = typeArray.getText(R.styleable.MyTitleBar_rightText);//按钮上的文本
        View view;//选择的布局文件

        int type =0;//0:右侧按钮模式，1:右侧文本模式
        //判断资源属性是否存在，然后选择使用哪一个布局
        if(rightText!=null&&!"".equals(rightText)){
            type = 1;
            view = LayoutInflater.from(getContext()).inflate(R.layout.layout_top_right_text,this,true);
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.layout_top,this,true);
        }

        //绑定布局文件id
        rltitle = (RelativeLayout) view.findViewById(R.id.rl_title);//布局文件中的ReleativeLayout
        tvtitle = (TextView) view.findViewById(R.id.tv_title);
        ImageButton ib_left = (ImageButton) view.findViewById(R.id.btn_left);
        btLeft = ib_left;
        //title这个属性是否赋值
        if(cstitle!=null&&!"".equals(cstitle)){
            tvtitle.setText(cstitle);
        }
        //让整个布局可见
        rltitle.setVisibility(VISIBLE);
        //是否给左边按钮赋予图片属性
        if(leftImg!=-1){
            ib_left.setVisibility(VISIBLE);
            ib_left.setImageResource(leftImg);
        }else{
            ib_left.setVisibility(INVISIBLE);
        }

        //根据目前的模式选择下一步的初始化
        if(type==0){
            //右侧按钮模式
            ImageButton ib_right = (ImageButton) view.findViewById(R.id.btn_right);
            ib_right.setVisibility(VISIBLE);
            if(rightImg!=-1){
                ib_right.setVisibility(VISIBLE);
                ib_right.setImageResource(rightImg);
            }else{
                ib_right.setVisibility(INVISIBLE);
            }
            btRight = ib_right;
        }else {//1：右侧文本模式
                Button btn_right = (Button) view.findViewById(R.id.btn_right);
                if (rightText!=null &&!"".equals(rightText)) {
                    btn_right.setText(rightText);
                    btn_right.setVisibility(View.VISIBLE);
                } else {
                    btn_right.setVisibility(View.INVISIBLE);
                }
                btRight = btn_right;
        }

        //设置整个布局的背景
        if (bgDrawable != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                rltitle.setBackground(bgDrawable);
            } else {
                rltitle.setBackgroundDrawable(bgDrawable);
            }

        } else {
            //缺省颜色(默认时候的配色)
            rltitle.setBackgroundColor(Color.parseColor("#365663"));
        }

        typeArray.recycle();
    }
    public View getBtnleft() {
        return btLeft;
    }
    public View getBtnRight() {
        return btRight;
    }

    public String getTitle() {
        return title;
    }
    /**
     * 设置标题文本
     * @param title string文件中的
     */
    public void setTitle(int title) {
        setTitle(getResources().getString(title));
    }

    /**
     * 设置标题文本
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
        if (title == null || "".equals(title)) {
            rltitle.setVisibility(View.GONE);
        } else {
            rltitle.setVisibility(View.VISIBLE);
            tvtitle.setText(title);
        }
        invalidate();
    }
}
