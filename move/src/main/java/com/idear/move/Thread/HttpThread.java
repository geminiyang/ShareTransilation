package com.idear.move.Thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpThread extends Thread{

	String url;
	
	String name;
	
	String password;
	
    public HttpThread(String url,String name,String password) {
		this.url = url;
		this.name = name;
		this.password = password;
	}
	
    //数据量请求小的，中文要求要求转码
    private void doGet(){
    	try {
			url = url+"?name="+URLEncoder.encode(name,"utf-8")+"&password="+password;
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
			String content = "name=" + name +"&password=" + password;
			//通过输出流传送给服务器
			out.write(content.getBytes());
			//客户端中读取数据的字符串缓冲
			final StringBuffer sb = new StringBuffer();
			//读取网页回传的页面信息
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String str;
			
			while((str=reader.readLine())!= null){
				sb.append(str);
			}
			
			System.out.println("result" + sb.toString());
			
			//System.out.println(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
    
		@Override
		public void run() {
			super.run();
			doGet();
		}

}
