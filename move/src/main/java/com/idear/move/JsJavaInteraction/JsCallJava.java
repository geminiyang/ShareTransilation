package com.idear.move.JsJavaInteraction;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.idear.move.Util.IntentSkipUtil;
import com.idear.move.Activity.LoginActivity;

/**
 * Created by user on 2017/4/8.
 */
public class JsCallJava {
    private static final String TAG = "JsCallJava";
    private Context mContext;

    public JsCallJava(Context context){
        this.mContext = context;
    }

    @JavascriptInterface
    public void skipToLoginActivity(){
        IntentSkipUtil.skipToNextActivity(mContext, LoginActivity.class);
    }
}
