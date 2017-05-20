package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yqq.touchtest.R;
import com.yqq.touchtest.Util.DisplayUtils;

/**
 * Created by user on 2017/1/19.
 */

public class MyEditText extends LinearLayout {

    private ImageView imageView=null;
    private EditText editText=null;

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.myedittext, this);//设置布局文件
        imageView=(ImageView)findViewById(R.id.imageView);
        editText=(EditText)findViewById(R.id.editText);
    }

    public void setHint(String s){
        editText.setHint(s);
    }
    public void setBackground(int resId) {
        imageView.setImageResource(resId);
    }
    public Editable getText(){
        return editText.getText();
    }
    public void setInputType(int a){
        editText.setInputType(a);
        editText.requestFocusFromTouch();
        editText.requestFocus();
    }
    public void setText(String s){
        editText.setText(s);
    }

    /**
     * 设置et是否可以编辑
     */
    public void setEditTextEnabled(boolean isEnabled){
        editText.setEnabled(isEnabled);
    }

    public void setEditTextTextColor(int color){
        editText.setTextColor(color);
    }

    /**
     * 设置EditText的hint文本的字体颜色
     * @param color
     */
    public void setEditTextHintColor(int color){
        editText.setHintTextColor(color);
    }

    /**设置EditText的hint文本和大小
     * @param text
     * @param hintSize 单位dp
     */
    public void setEditTextHint(String text, int hintSize){
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(text);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(ss));
    }

    /**
     * 设置点击监听
     * @param click
     */
    public void setEditTextClickListener(OnClickListener click){
        editText.setOnClickListener(click);
    }

    /**
     * 设置触摸监听(要使用匿名内部类的方法)
     * @param touch
     */
    public void setEditTextTouchListener(OnTouchListener touch){
        editText.setOnTouchListener(touch);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置margin
     * @param left
     * @param right
     * @param top
     * @param bottom
     */
    public void changeEditMargin(int left, int right, int top, int bottom){
        LayoutParams lp = (LayoutParams) editText.getLayoutParams();
        lp.leftMargin = left;
        lp.rightMargin = right;
        lp.topMargin = top;
        lp.bottomMargin = bottom;
        editText.setLayoutParams(lp);
    }

    /**
     *
     * @param weight
     */
    public void setEditWeight(int weight){
        LayoutParams lp = (LayoutParams) editText.getLayoutParams();
        lp.weight = weight;
        editText.setLayoutParams(lp);
    }

    /**
     *
     * @param width
     * @param height
     */
    public void setEditWeight(int width, int height){
        LayoutParams lp = (LayoutParams) editText.getLayoutParams();
        lp.width = width;
        lp.height = height;
        editText.setLayoutParams(lp);
    }

    /**
     * 设置是否可以聚焦
     * @param focus
     */
    public void setEditFocus(boolean focus){
        editText.setFocusable(focus);
    }


    /**
     * 初始化操作(参考用)
     */
    private void initSetting(Context context){
        int dip = DisplayUtils.dip2px(getContext(), 2);
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(Color.parseColor("#ff0000"));


        editText = new EditText(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(null);
        }else{
            setBackground(null);
        }
        editText.setSingleLine(true);
        editText.setBackgroundResource(R.color.colorPrimaryDark);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
//      et_right.setBackgroundResource(R.drawable.frame_edit_gray);

        LayoutParams editLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        editLp.gravity = Gravity.CENTER;
        editLp.leftMargin = DisplayUtils.dip2px(getContext(), 5);
        editLp.rightMargin = dip;
        editLp.topMargin = dip;
        editLp.bottomMargin = dip;
        editText.setLayoutParams(editLp);
        //在布局添加视图
        addView(editText);
    }
}
