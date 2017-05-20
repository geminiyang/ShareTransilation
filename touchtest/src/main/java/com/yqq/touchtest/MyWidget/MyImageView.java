package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yqq.touchtest.R;

/**
 * Created by user on 2017/2/4.
 */

public class MyImageView extends LinearLayout{

    private ImageView imageView;//图片
    private TextView textView;//图像下面的文本
    private int width,height;//imageView的宽和高
    private View view;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        //获取自定义属性
        TypedArray tarray = context.obtainStyledAttributes(attrs, R.styleable.MyImageView);
        CharSequence cstring = tarray.getText(R.styleable.MyImageView_imgtext);
        int resId = tarray.getResourceId(R.styleable.MyImageView_src,-1);
        width = tarray.getDimensionPixelOffset(R.styleable.MyImageView_width,12);
        height = tarray.getDimensionPixelOffset(R.styleable.MyImageView_height,12);

        LinearLayout.LayoutParams lpContent =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        view = LayoutInflater.from(getContext()).inflate(R.layout.myimageviewlayout,this,true);

        imageView = (ImageView) view.findViewById(R.id.myimg);
        textView = (TextView) view.findViewById(R.id.mytext);

        //设置它的宽和高
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height=height;
        params.width=width;
        imageView.setLayoutParams(params);

        //是否给左边按钮赋予图片属性
        if(resId !=-1){
            imageView.setVisibility(VISIBLE);
            imageView.setImageResource(resId);
        }else{
            imageView.setVisibility(INVISIBLE);
        }
        imageView.setImageResource(resId);
        textView.setText(cstring);
        tarray.recycle();
    }

}
