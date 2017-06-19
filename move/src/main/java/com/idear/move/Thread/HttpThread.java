package com.idear.move.Thread;

import android.util.Log;

import com.google.gson.Gson;
import com.idear.move.network.loginData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpThread extends Thread{

	String url;
	
	String username;
	
	String password;
	
    public HttpThread(String url,String username,String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
    //数据量请求小的，中文要求要求转码
    private void doGet(){
    	try {
			url = url+"?name="+URLEncoder.encode(username,"utf-8")+"&password="+password;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			URL httpurl = new URL(url);
			
			//根据协议选择http 还是https
			//HttpsURLConnection coons = (HttpsURLConnection) httpUrl.openConnection();
			HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
			//设置连接超时
			conn.setReadTimeout(5000);
			//设置请求方法
			conn.setRequestMethod("GET");
			//客户端中读取数据的字符串缓冲
			final StringBuffer sb = new StringBuffer();
			//读取网页回传的页面信息
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String str;
			
			while((str=reader.readLine())!= null){
				sb.append(str);
			}
			
			System.out.println("result" + sb.toString());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
    
    private void doPost(){
		try {
			URL httpurl = new URL(url);
			//根据协议选择http 还是https
			//HttpsURLConnection coons = (HttpsURLConnection) httpUrl.openConnection();
			HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
			//设置连接超时
			conn.setReadTimeout(5000);
			//设置请求方法
			conn.setRequestMethod("POST");
			//获得输出流
			OutputStream out = conn.getOutputStream();
			//语句内容
			String content = "name=" + username +"&password=" + password;
			//通过输出流传送给服务器
			out.write(content.getBytes());
			//客户端中读取数据的字符串缓冲
			final StringBuffer sb = new StringBuffer();
			//读取网页回传的页面信息
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String str;
            int res = conn.getResponseCode();
            Log.d("info","result: " + res + "");
            while((str=reader.readLine())!= null){
				sb.append(str);
			}
			Log.d("info","result: " + sb.toString());
			//System.out.println(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

    public void sendJson2Server(){

        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", username);
            jsonObject.put("password", password);
            String jsonString = jsonObject.toString();
            Log.w("TAG", jsonString);
            //使用工具将其封装成一个类的对象
            Gson gson = new Gson();
            final loginData loginData = gson.fromJson(jsonString, loginData.class);

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
            OutputStream  out = conn.getOutputStream();
            // 写入请求的字符串，转换成字节流传输
            out.write(data);
            out.flush();
            out.close();


            //输入返回状态码
            final int code = conn.getResponseCode();
            Log.w("TAG", code+"");
            if(code == 200){

                //1.在这里接收服务端回传的数据
                //将从网络获取的数据 放到缓存读取器中
                //网络返回的输入流
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                final StringBuffer sb = new StringBuffer();
                String str;

                while((str=reader.readLine())!=null){
                    sb.append(str);
                }
                Log.d("info",sb.toString());
            } else{

            }
            //关闭连接
            conn.disconnect();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
		@Override
		public void run() {
			super.run();
			sendJson2Server();
		}

}
