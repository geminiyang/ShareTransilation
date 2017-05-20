package com.yqq.touchtest.Util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yqq.touchtest.MyWidget.LayerBottom;
import com.yqq.touchtest.R;
import com.yqq.touchtest.MyWidget.TipRelativeLayout;

/**
 * 一个弹出框的工具类,简化Activity中的代码
 * Created by user on 2017/1/20.
 */
public class DisplayUtils implements TipRelativeLayout.AnimationEndCallback {

    //用来显示图片获取方式的布局（从底部弹出）
    private LayerBottom layerBottomImg = null;
    private PopupWindow pwindow;

    public DisplayUtils(Activity activity){
        if(layerBottomImg!=null) {
            return;
        }
        init(activity);
    }

    private void init(Activity activity) {
        layerBottomImg = (LayerBottom) activity.findViewById(R.id.layer_bottom_img);
        layerBottomImg.setContent(R.layout.layout_bottom);
        layerBottomImg.setClick();
    }

    /**
     * 封装弹出框的具体操作，具有代理者的效果,需要布置xml文件
     */
    public void show(){
        layerBottomImg.showLayer();
    }

    public void hide(){
        layerBottomImg.hideLayer();
    }

    /**
     * 通用的弹出框,可以在对应activity中声明xml布局
     * @param activity
     */
    public void showTips(Activity activity){
        //滑动布局的高度，此高度由布局文件的高度决定
        int tipsHeight = dip2px(activity,52);
        //activity的根布局
        View parent = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        View popView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_tips, null);
        int statusBar=getStatusBarHeight(activity);
        pwindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT,tipsHeight*2);
        pwindow.showAtLocation(parent, Gravity.TOP, 0, 0);
        TipRelativeLayout tvTips=(TipRelativeLayout) popView.findViewById(R.id.rl_tips);
        tvTips.setTitleHeight(statusBar*2);//移动控件的高度
        tvTips.setAnimationEndListener(this);//设置动画结束监听函数
        tvTips.showTips();//显示提示RelativeLayout,移动动画.
    }

    public int getStatusBarHeight(Context context){
        //状态栏的高度
        int result = 0;
        int resourceId =  context.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId!=0){
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 单位转换
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);  //+0.5是为了向上取整
    }

    /**
     * 单位转换
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);//+0.5是为了向上取整
    }

    @Override
    public void onAnimationEndListener() {
        pwindow.dismiss();//动画结束，隐藏popupwindow
    }
}
