package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 自己绘制一个特定的V
 * Created by user on 2017/1/24.
 */
public class CircleView extends View{

    private Paint mPaint;//画笔类
    private RectF mRectF;//绘制区域
    private int paintWidth;//画笔宽度
    private int drawFlag = 0;
    private int oneRadian;
    private int twoRadian;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    //初始化绘制工具等
    private void initPaint() {
        mPaint = new Paint();
        paintWidth = dip2px(getContext(),24);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mRectF = new RectF(dip2px(getContext(), 12), dip2px(getContext(), 12),
                dip2px(getContext(), 218), dip2px(getContext(), 218));
    }

    /**
     * 自定义绘画操作
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //根据角度绘制不同颜色
        if(drawFlag>=0 && drawFlag<oneRadian){
            mPaint.setColor(Color.parseColor("#ff0000"));// 设置颜色
            canvas.drawArc(mRectF, 270, drawFlag, false, mPaint);
        }else if(drawFlag>=oneRadian && drawFlag <twoRadian){
            mPaint.setColor(Color.parseColor("#ff0000"));
            canvas.drawArc(mRectF, 270, oneRadian, false, mPaint);
            mPaint.setColor(Color.parseColor("#00ff00"));
            //此处的 270+oneRadian 是临时写的，这是不准确的，需要考虑 这个值大于360的情况
            canvas.drawArc(mRectF, 270+oneRadian, drawFlag-oneRadian, false, mPaint);
        }else if(drawFlag>=twoRadian && drawFlag <=360){
            mPaint.setColor(Color.parseColor("#ff0000"));
            canvas.drawArc(mRectF, 270, oneRadian, false, mPaint);

            mPaint.setColor(Color.parseColor("#00ff00"));
            canvas.drawArc(mRectF, 270+oneRadian, twoRadian-oneRadian, false, mPaint);

            mPaint.setColor(Color.parseColor("#ffff00"));
            canvas.drawArc(mRectF,270+twoRadian-360, drawFlag-twoRadian, false, mPaint);
        }
    }

    /**
     * 重绘过程中的偏移角度
     * @param i
     */
    public void startDraw(int i) {
        drawFlag = i;
    }

    /**
     * 设置绘制起点角度
     * @param radianOne
     * @param radianTwo
     */
    public void setDemarcation(int radianOne,int radianTwo){
        oneRadian = radianOne;
        twoRadian = radianTwo;
    }

    /**
     * 单位转换
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float ret = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue, context.getResources().getDisplayMetrics());
        return (int) (ret + 0.5f);

    }
}
