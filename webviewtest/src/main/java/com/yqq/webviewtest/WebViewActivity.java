package com.yqq.webviewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends Activity {

    private WebView wv;
    private ProgressBar pb;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //必须在setContentView()前面
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);
        //webView使用的最佳实践
        wv = (WebView) findViewById(R.id.wv);
        //wv.getSettings().setSupportZoom(true);//启用缩放功能，不正常关闭会引起窗体泄露，这行代码影响的
        //wv.invokeZoomPicker();//显示缩放小工具不正常关闭会引起窗体泄露,这行代码影响的
        wv.getSettings().setBuiltInZoomControls(true);//启用缩放工具,
        wv.getSettings().setJavaScriptEnabled(true); //启用javaScipt功能
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//启用自动开启多窗口
        wv.getSettings().setSupportMultipleWindows(true); //启用多窗口功能
        //自适应屏幕
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);
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
        String url = "http://120.25.231.96:8081";
        //创建网页开启进度接口
        wv.setInitialScale(0);//设置缩放比例
        wv.loadUrl(url);

//      wv.setWebViewClient(new WebViewClient(){
//          @Override
//          public boolean shouldOverrideUrlLoading(WebView view, String url) {
//              view.LoadUrl();
//              return super.shouldOverrideUrlLoading(view, url);
//          }
//      });

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
