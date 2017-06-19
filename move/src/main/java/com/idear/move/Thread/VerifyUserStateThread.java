package com.idear.move.Thread;

import android.content.Context;

import com.google.gson.Gson;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.ResultTypeTwo;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerifyUserStateThread extends Thread{
	private String url;
    private Context mContext;
    private DataGetInterface mListener;
    public VerifyUserStateThread(Context context, String url) {
		this.url = url;
        this.mContext = context;
	}

    public void setDataGetListener(DataGetInterface mListener) {
        this.mListener = mListener;
    }

    private void verifyUserState() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("cookie", CookiesSaveUtil.getCookies(mContext));
            String jsonString = jsonObject.toString();
            Logger.d(jsonString);
            //使用工具将其封装成一个类的对象
            Gson gson = new Gson();

            URL httpurl;
            httpurl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            //设置维持长链接
            conn.setRequestProperty("connection", "Keep-Alive");
            //设置文件字符集
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置User-Agent: Fiddler
            conn.setRequestProperty("ser-Agent", "Fiddler");
            // 设置contentType
            conn.setRequestProperty("Content-Type","application/json");
            //转换为字节数组
            byte[] data = (jsonObject.toString()).getBytes();
            //设置文件长度
            conn.setRequestProperty("Content-length", String.valueOf(data.length));

            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串，转换成字节流传输
            out.write(data);
            out.flush();
            out.close();

            //输入返回状态码
            final int code = conn.getResponseCode();
            Logger.d(code+"");
            if(code == 200){
                //网络返回数据,放入的输入流
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                final StringBuffer sb = new StringBuffer();
                String str;
                while((str=reader.readLine())!=null){
                    sb.append(str);
                }
                Logger.d(sb.toString());
                ResultTypeTwo result = gson.fromJson(sb.toString(), ResultTypeTwo.class);
                Logger.d(result.getMessage());

                if (mListener != null) {
                    mListener.finishWork(result.getMessage());
                }
            } else{
                if (mListener != null) {
                    mListener.finishWork(code+"");
                }
            }
            //关闭连接
            conn.disconnect();
        } catch (JSONException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.interrupt(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.interrupt(e);
            }
        }
    }

    @Override
    public void run() {
        super.run();
        verifyUserState();
    }

}
