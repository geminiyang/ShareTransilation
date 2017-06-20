package com.idear.move.util;

import android.content.Context;

import com.idear.move.R;

import org.apache.http.HttpException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * 作者:geminiyang on 2017/6/20.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class ErrorHandleUtil {

    public static String ExceptionToStr(Exception e, Context mContext) {
        if(e instanceof HttpException) {
            Logger.d(mContext.getString(R.string.networkFailure));
            return mContext.getString(R.string.networkFailure);// 网络异常
        } else if (e instanceof SocketTimeoutException) {
            Logger.d(mContext.getString(R.string.responseTimeout));
            return mContext.getString(R.string.responseTimeout);//响应超时
        } else if(e instanceof MalformedURLException) {
            Logger.d(mContext.getString(R.string.malformedURL));
            return mContext.getString(R.string.malformedURL);//json格式转换异常
        } else if (e instanceof ConnectTimeoutException) {
            Logger.d(mContext.getString(R.string.requestTimeout));
            return mContext.getString(R.string.requestTimeout);//请求超时
        } else if (e instanceof IOException) {
            Logger.d(mContext.getString(R.string.networkError));
            return mContext.getString(R.string.networkError);//网络异常
        } else if (e instanceof  JSONException) {
            Logger.d(mContext.getString(R.string.json_error));
            return mContext.getString(R.string.json_error);//json格式转换异常
        }  else {
            Logger.d(mContext.getString(R.string.canNotGetConnected));
            return mContext.getString(R.string.canNotGetConnected);// 无法连接网络
        }
    }
}
