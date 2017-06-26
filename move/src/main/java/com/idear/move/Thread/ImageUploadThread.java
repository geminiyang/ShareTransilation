package com.idear.move.Thread;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.idear.move.Activity.DynamicPublishingActivity;
import com.idear.move.R;
import com.idear.move.myWidget.LoadingProgressDialog;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.ResultType;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.Logger;
import com.idear.move.util.ProgressDialogUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

public class ImageUploadThread extends Thread {
    private static final int TIME_OUT = 20 * 1000; // 超时时间
	private String url;
    private Context mContext;
    private Map<String, String> params;
    private Map<String, File> files;
    private DataGetInterface mListener;
    public ImageUploadThread(Context context, String url, Map<String, String> params, Map<String, File> files) {
		this.url = url;
        this.params = params;
        this.files = files;
        this.mContext = context;
	}

    public void setDataGetListener(DataGetInterface mListener) {
        this.mListener = mListener;
    }

    @Override
    public void run() {
        super.run();
        uploadImg();
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     */
    private void uploadImg() {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINE_END = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";//文件传输类型
        String CHARSET = "UTF-8";//字符类型
        //使用工具将其封装成一个类的对象
        Gson gson = new Gson();
        try {

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
            //请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            outStream.write(end_data);
            outStream.flush();

            //得到响应码
            int code = conn.getResponseCode();
            Logger.d("响应码:" + code + "");

            InputStream in = conn.getInputStream();
            StringBuilder sb2 = new StringBuilder();
            if (code == 200) {
                int ch;
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }

                Logger.d(sb2.toString());
                ResultType result = gson.fromJson(sb2.toString(), ResultType.class);//通过Gson解析json
                Logger.d(result.getMessage());

                if (mListener != null) {
                    mListener.finishWork(result);
                }

            } else {
                if (mListener != null) {
                    mListener.finishWork(code+"");
                }
            }
            // 移除进度框

            //关闭连接关闭流
            outStream.close();
            conn.disconnect();
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
}
