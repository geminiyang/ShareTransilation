package com.yqq.myutillibrary;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by user on 2017/4/19.
 */

public class BitmapToRoundUtil {

    /**
     * 将bitmap变成圆形
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap,int roundColor) {

        float roundRadius;
        float src_left,src_top,src_right,src_bottom,
                dst_left,dst_top,dst_right,dst_bottom;

        //获取Bitmap的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //将长方形截取为正方形，取出较小值当边长
        if (width <= height) {
            //取较小值
            roundRadius = width / 2;
            //调整图片的位置
            float clip = (height - width) / 2;
            //左上角的点
            src_top = clip;
            src_left = 0;
            //右下角的点
            src_bottom = width-clip;
            src_right = width;
            //将较小值赋值给较大值
            height = width;
            //左上角的点
            dst_left = 0;
            dst_top = 0;
            //右下角的点
            dst_right = width;
            dst_bottom = width;
        } else {
            //取较小值
            roundRadius = height / 2;
            //调整图片的位置
            float clip = (width - height) / 2;
            //左上角的店
            src_left = clip;
            src_top = 0;
            //右下角的点
            src_right = width - clip;
            src_bottom = height;
            //将较小值赋值给较大值
            width = height;
            //左上角的点
            dst_left = 0;
            dst_top = 0;
            //右下角的点
            dst_right = height;
            dst_bottom = height;
        }
        //使用截取处理后的宽和高创建bitmap
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();

        final Rect src = new Rect((int)src_left, (int)src_top, (int)src_right, (int)src_bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);
        canvas.drawARGB(255,255,255,255);//canvas背景色
        paint.setColor(roundColor);

        //先画出的在下层

        canvas.drawBitmap(bitmap, src, dst, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//取交集，显示上层
        canvas.drawBitmap(getBitmap(rectF,width,height,roundRadius),0,0,paint);

        return output;
    }

    public static RoundedBitmapDrawable makeRoundBitmap(Resources res,Bitmap srcBitmap) {

        Bitmap dstBitmap;
        //将长方形图片裁剪成正方形图片
        if (srcBitmap.getWidth() >= srcBitmap.getHeight()) {
            //取小值当边长
            dstBitmap = Bitmap.createBitmap(srcBitmap,
                    srcBitmap.getWidth()-srcBitmap.getHeight(), 0,
                    srcBitmap.getHeight(), srcBitmap.getHeight());
        } else {
            dstBitmap = Bitmap.createBitmap(srcBitmap,
                    0, srcBitmap.getHeight()-srcBitmap.getWidth(),
                    srcBitmap.getWidth(), srcBitmap.getWidth());
        }

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, dstBitmap);
        roundedBitmapDrawable.setCornerRadius(srcBitmap.getHeight()>srcBitmap.getWidth() ?
                2 * srcBitmap.getWidth() : 2 * srcBitmap.getHeight()); //设置圆角半径为正方形边长(较小值)的两倍
        roundedBitmapDrawable.setAntiAlias(true);
        return  roundedBitmapDrawable;
    }

    public static RoundedBitmapDrawable makeRoundCornerBitmap(Resources res,Bitmap srcBitmap,float radius) {

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, srcBitmap);
        roundedBitmapDrawable.setCornerRadius(radius); //设置圆角半径为正方形边长的一半
        roundedBitmapDrawable.setAntiAlias(true);
        return  roundedBitmapDrawable;
    }

    /**
     * 获取圆形的的Bitmap
     * @return 形状的bitmap
     */
    public static  Bitmap getBitmap(RectF rectF,int width,int height,float roundRadius) {
        //根据ImageView的宽高创建bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 构建Paint时直接加上去锯齿属性
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
        return bitmap;
    }

}