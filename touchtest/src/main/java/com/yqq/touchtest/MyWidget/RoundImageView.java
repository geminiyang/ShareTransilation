package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yqq.touchtest.R;

/**
 * Created by user on 2017/2/7.
 */

public class RoundImageView extends View{

    /**
     * TYPE_CIRCLE / TYPE_ROUND
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    /**
     * 图片
     */
    private Bitmap mSrc;
    /**
     * 圆角的大小
     */
    private int mRadius;

    /**
     * 控件的宽度
     */
    private int mWidth;
    /**
     * 控件的高度
     */
    private int mHeight;

    public RoundImageView(Context context) {
        this(context,null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //从attr.xml文件中获取自定义属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RoundImageView, defStyleAttr, 0);

        //得到自定义属性总数
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            //自定义属性对应的id
            int attr = typedArray.getIndex(i);
            switch (attr)
            {
                case R.styleable.RoundImageView_round_img_src:
                    mSrc = BitmapFactory.decodeResource(getResources(),
                            typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.RoundImageView_type:
                    type = typedArray.getInt(attr, 0);//获取绘制类型,默认为Circle
                    break;
                case R.styleable.RoundImageView_borderRadius:
                    mRadius = typedArray.getDimensionPixelSize(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,
                                    getResources().getDisplayMetrics()));// 默认为10DP
                    break;
            }
        }
        typedArray.recycle();
    }

    /**
     * 设置控件宽度和高度
     * @param widthMeasureSpec  宽度测量模式
     * @param heightMeasureSpec 高度测量模式
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置控件宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate（精确模式）
        {
            mWidth = specSize;
        } else {
            // 由图片决定的宽，xml中赋予的数值
            int desireWidthByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
            if (specMode == MeasureSpec.AT_MOST)// wrap_content或者指定了大小
            {
                //其大小不能超过屏幕大小
                mWidth = Math.min(desireWidthByImg, specSize);
            } else {
                mWidth = desireWidthByImg;
            }
        }
        /***
         * 设置控件高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else
        {
            //因为内边距才属于控件本身
            int desireHeightByImg = getPaddingTop() + getPaddingBottom() + mSrc.getHeight();
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desireHeightByImg, specSize);
            } else {
                mHeight = desireHeightByImg;
            }
        }
        //设置测试好的宽高
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //根据绘制类型决定调用不同的方法
        switch (type)
        {
            // 如果是TYPE_CIRCLE绘制圆形
            case TYPE_CIRCLE:
                //测出涉及到ScaleType,如果自己需求设计
                int min = Math.min(mWidth, mHeight);
                //有些尺寸的压缩出来的Bitmap和原Bitmap是不一样的，这时候旧的Bitmap就可以回收
                Bitmap temp = mSrc;
                /**
                 * 长度如果不一致，按小的值进行压缩
                 */
                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
                if (!temp.sameAs(mSrc)) {
                    temp.recycle();
                    temp = null;
                }
                canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
                break;
            case TYPE_ROUND:
                int minTypeRound = Math.min(mWidth, mHeight);
                //有些尺寸的压缩出来的Bitmap和原Bitmap是不一样的，这时候旧的Bitmap就可以回收
                Bitmap tempTypeRound = mSrc;
                /**
                 * 长度如果不一致，按小的值进行压缩
                 */
                mSrc = Bitmap.createScaledBitmap(mSrc, minTypeRound, minTypeRound, false);
                if (!tempTypeRound.sameAs(mSrc)) {
                    tempTypeRound.recycle();
                    tempTypeRound = null;
                }
                canvas.drawBitmap(createRoundCornerImage(mSrc), 0, 0, null);
                break;

        }
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min   onDraw中所指定宽高的较小值
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);//减少锯齿
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 根据原图添加圆角
     *
     * @param source
     * @return
     */
    private Bitmap createRoundCornerImage(Bitmap source)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);

        /**
         * 首先绘制圆角矩形
         * source.getWidth(),source.getHeight(),这是图片原来的宽高
         */
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        /**
         * 设置绘制模式
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
