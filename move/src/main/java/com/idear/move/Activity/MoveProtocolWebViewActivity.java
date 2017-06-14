package com.idear.move.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.idear.move.jsJavaInteraction.JsCallJava;
import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class MoveProtocolWebViewActivity extends BaseActivity {

    private WebView wv;
    private ProgressBar pb;
    String url = "http://www.baidu.com";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_web_view);
        //webView使用的最佳实践
        wv = (WebView) findViewById(R.id.wv);
        WebSettings webSettings = wv.getSettings();
        //webSettings.setSupportZoom(true);//启用缩放功能，不正常关闭会引起窗体泄露，这行代码影响的
        //wv.invokeZoomPicker();//显示缩放小工具不正常关闭会引起窗体泄露,这行代码影响的
        webSettings.setBuiltInZoomControls(true);//启用缩放工具,
        webSettings.setJavaScriptEnabled(true); //启用javaScipt功能
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//启用自动开启多窗口
        webSettings.setSupportMultipleWindows(true); //启用多窗口功能
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        //这是4.2之后java和js交互的方法
        wv.addJavascriptInterface(new JsCallJava(this), "JsCallJava");
        wv.setWebViewClient(new WebViewClient(){

        });
        //用于处理开启超链接操作
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //第一个参数是正在载入的webView，第二个参数是进度值
                //加入自己的进度显示处理程序
                pb.setProgress(newProgress);
                pb.setVisibility(newProgress <100? View.VISIBLE:View.GONE);
            }
        });

        //创建网页开启进度接口
        wv.setInitialScale(0);//设置缩放比例
        wv.loadUrl(url);

        pb = (ProgressBar) findViewById(R.id.pb);
    }

    @Override
    public void onBackPressed() {
        if(wv.canGoBack()){
            wv.goBack();
            return;
        }
        //系统默认直接退出当前webView
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
