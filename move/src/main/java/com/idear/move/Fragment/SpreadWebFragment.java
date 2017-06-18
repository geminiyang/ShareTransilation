package com.idear.move.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.idear.move.R;
import com.idear.move.jsJavaInteraction.JsCallJava;

public class SpreadWebFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public SpreadWebFragment() {
        // Required empty public constructor
    }

    public static SpreadWebFragment newInstance(String param1) {
        SpreadWebFragment fragment = new SpreadWebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;
    private LinearLayout ll;
    private WebView wv;
    private ProgressBar pb;
    String url = "http://www.coca-cola.com.cn/homepage";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_spread_web, container, false);
            init(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
}

    private void init(View rootView) {
        ll = (LinearLayout) rootView.findViewById(R.id.ll_loading);
        //webView使用的最佳实践
        wv = (WebView) rootView.findViewById(R.id.wv);
        WebSettings webSettings = wv.getSettings();
        //webSettings.setSupportZoom(true);//启用缩放功能，不正常关闭会引起窗体泄露，这行代码影响的
        //wv.invokeZoomPicker();//显示缩放小工具不正常关闭会引起窗体泄露,这行代码影响的
        webSettings.setBuiltInZoomControls(true);//启用缩放工具,
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//启用自动开启多窗口
        webSettings.setSupportMultipleWindows(true); //启用多窗口功能
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

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
                if(newProgress==100) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                }
            }
        });

        //创建网页开启进度接口
        wv.setInitialScale(0);//设置缩放比例
        wv.loadUrl(url);

        pb = (ProgressBar) rootView.findViewById(R.id.pb);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
