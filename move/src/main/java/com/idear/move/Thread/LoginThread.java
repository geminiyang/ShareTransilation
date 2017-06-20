package com.idear.move.Thread;

import android.content.Context;

import com.google.gson.Gson;
import com.idear.move.R;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.LoginResult;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.Logger;

import org.apache.http.HttpException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class LoginThread extends Thread{
	private String url;
	private String email;
	private String password;
    private Context mContext;
    private DataGetInterface mListener;
    public LoginThread(Context context, String url, String email, String password) {
		this.url = url;
		this.email = email;
		this.password = password;
        this.mContext = context;
	}

    public void setDataGetListener(DataGetInterface mListener) {
        this.mListener = mListener;
    }

    private void login() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
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
            //conn.setRequestProperty("user-Agent", "Fiddler");
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
            //获取Cookie并且本地化
            Map<String, List<String>> cookieMap = conn.getHeaderFields();
            List<String> cookies = cookieMap.get("Set-Cookie");
            StringBuffer cookieStr = new StringBuffer();
            if (null != cookies && 0 < cookies.size()) {
                for (String cookie : cookies) {
                    cookieStr.append(cookie);
                }
            }
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
                LoginResult result = gson.fromJson(sb.toString(), LoginResult.class);
                Logger.d(result.getMessage());

                if(1 == Integer.parseInt(result.getStatus())) {
                    //成功则保存cookie和用户id
                    Logger.d("saving---cookie" + cookieStr.toString());
                    CookiesSaveUtil.saveCookies(cookieStr.toString(),mContext);
                    Logger.d("saving---u_id" + result.getData());
                    CookiesSaveUtil.saveUserId(result.getData(),mContext);
                }
                Logger.d(code+"");
                if (mListener != null) {
                    mListener.finishWork(result);
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
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.interrupt(e);
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.interrupt(e);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.interrupt(e);
            }
        } catch (MalformedURLException e) {
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
        login();
    }
}
