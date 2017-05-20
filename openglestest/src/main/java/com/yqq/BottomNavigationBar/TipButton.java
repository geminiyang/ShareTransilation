package com.yqq.BottomNavigationBar;

/**
 * Created by user on 2017/4/21.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.yqq.R;

/**
 * 此类日后还可拓展
 */
public class TipButton extends android.support.v7.widget.AppCompatRadioButton {

    private boolean mTipOn = false;//是否开启提示的标志

    private Dot mDot;//提示的红点

    private class Dot {
        //原点的属性
        int color;
        int radius;
        int marginTop;
        int marginRight;

        Dot() {
            float density = getContext().getResources().getDisplayMetrics().density;
            radius = (int) (5 * density);//将px转换成dp
            marginTop = (int) (3 * density);
            marginRight = (int) (3 * density);

            color = getContext().getResources().getColor(R.color.indianred);
        }

    }

    public TipButton(Context context) {
        super(context);
        init();
    }

    public TipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TipButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mDot = new Dot();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTipOn) {
            //计算出半径
            float cx = getWidth() - mDot.marginRight - mDot.radius;
            float cy = mDot.marginTop + mDot.radius;

            //如果添加了drawableTop需要将x坐标重新计算
            Drawable drawableTop = getCompoundDrawables()[1];
            if (drawableTop != null) {
                int drawableTopWidth = drawableTop.getIntrinsicWidth();
                if (drawableTopWidth > 0) {
                    int dotLeft = getWidth() / 2 + drawableTopWidth / 2;
                    cx = dotLeft + mDot.radius;
                }
            }


            Paint paint = getPaint();
            //save color
            int tempColor = paint.getColor();

            paint.setColor(mDot.color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, mDot.radius, paint);

            //restore color
            paint.setColor(tempColor);
        }
    }

    public void setTipOn(boolean tip) {
        this.mTipOn = tip;

        invalidate();
    }

    public boolean isTipOn() {
        return mTipOn;
    }
}
