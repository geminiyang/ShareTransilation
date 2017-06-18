package com.idear.move.util;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 作者:geminiyang on 2017/6/18.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */
public class UploadUtil {
    private static final String TAG = "info";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String SUCCESS="1";
    private static final String FAILURE="0";
    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url Service net address
     * @param params text content
     * @param files pictures
     * @return String result of Service response
     * @throws IOException
     */
    public static String uploadImg(String url, Map<String, String> params, Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINE_END = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";//文件传输类型
        String CHARSET = "UTF-8";//字符类型

        //开启进度框
        //startProgressDialog();
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(TIME_OUT);//缓存的最长时间
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoInput(true);//允许输入
        conn.setDoOutput(true);//允许输出
        conn.setUseCaches(false);//不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
            sb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
            sb.append(LINE_END);
            sb.append(entry.getValue());
            sb.append(LINE_END);
        }
        //文件输出流
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());

        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINE_END);
                sb1.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + file.getValue().getName() + "\"" + LINE_END);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb1.append(LINE_END);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINE_END.getBytes());
            }
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        int res = conn.getResponseCode();
        Log.d(TAG,"响应码" + res + "");
        InputStream in = conn.getInputStream();
        StringBuilder sb2 = new StringBuilder();
        if (res == 200) {
            int ch;
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
        }
        // 移除进度框
        //removeProgressDialog();
        outStream.close();
        conn.disconnect();
        return sb2.toString();
    }
}
