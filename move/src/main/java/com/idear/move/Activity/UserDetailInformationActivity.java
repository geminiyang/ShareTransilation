package com.idear.move.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.idear.move.POJO.UserInfoViewModel;
import com.idear.move.R;
import com.idear.move.network.HttpPath;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.Logger;
import com.idear.move.util.NetWorkUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserDetailInformationActivity extends BaseActivity {

    private Toolbar mToolBar;

    private TextView nickName,Sex,birthDay,school,email,password,phoneNumber;
    private RelativeLayout LoadingFace;
    private ProgressBar progressBar;
    private TextView loadingText;
    private ImageView iv_back,updatePassword;

    private BroadcastReceiver connectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_user_detail_information);



        initView();
        initEvent();
        //启动异步任务（UI初始化）
        NetWorkUtil.isConnected(this);
        if (NetWorkUtil.isNetworkConnected(this)) {
            new UserInfoAsyncTask().execute(HttpPath.getUserInfoPath());
        } else {
            //提示无网络连接
            LoadingFace.setBackgroundColor(Color.WHITE);
            loadingText.setText("网络无连接");
            loadingText.setTextColor(Color.RED);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(UserDetailInformationActivity.this,
                        UserUpdatePasswordActivity.class);
            }
        });

        connectionReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                    ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"网络连接失败");
                } else {
                    ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"网络连接成功");

                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        nickName = (TextView) findViewById(R.id.tv_nickname);
        Sex = (TextView) findViewById(R.id.tv_sex);
        birthDay = (TextView) findViewById(R.id.tv_birthday);
        school = (TextView) findViewById(R.id.tv_school);
        email = (TextView) findViewById(R.id.tv_email);
        password = (TextView) findViewById(R.id.tv_password);
        phoneNumber = (TextView) findViewById(R.id.tv_cellphoneNumber);

        //ImageView
        updatePassword = (ImageView) findViewById(R.id.iv_next_password);

        LoadingFace = (RelativeLayout) findViewById(R.id.loading_face);
        loadingText = (TextView) findViewById(R.id.tv_progressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
    }

    /**
     * 将URL对应的json格式转换成我们的Bean
     * @param param
     * @return
     */
    private List<UserInfoViewModel> myGetJsonData(String param) {
        List<UserInfoViewModel> users = new ArrayList<>();
        //通过InputStream去和获取Json数据
        InputStream is = null;
        //可以写成URLConnection urlConnection = null;
        HttpURLConnection urlConnection = null;
        try {
            JSONObject sendJson = new JSONObject();
            sendJson.put("user_id", CookiesSaveUtil.getUserId(UserDetailInformationActivity.this));
            String sendJsonString = sendJson.toString();
            Logger.d(sendJsonString);
            //使用工具将其封装成一个类的对象
            Gson gson = new Gson();

            urlConnection = (HttpURLConnection) new URL(param).openConnection();
            //设置相应请求头
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            // 设置允许输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            //设置维持长链接
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            //设置文件字符集
            urlConnection.setRequestProperty("Charset", "UTF-8");
            // 设置contentType
            urlConnection.setRequestProperty("Content-Type","application/json");
            //转换为字节数组
            byte[] data = (sendJson.toString()).getBytes();
            //设置文件长度
            urlConnection.setRequestProperty("Content-length", String.valueOf(data.length));
            // 开始连接请求
            urlConnection.connect();
            OutputStream out = urlConnection.getOutputStream();
            // 写入请求的字符串，转换成字节流传输
            out.write(data);
            out.flush();
            out.close();


            //如果请求码不是200则退出
            if(HttpURLConnection.HTTP_OK != urlConnection.getResponseCode()) {
                return null;
            }
            is = urlConnection.getInputStream();
            //is = new URL(param).openStream();
            String receiverJsonString = readStream(is);
            JSONObject receiveJson;
            UserInfoViewModel user;
            try {
                //将整一个json字符串转换成json对象
                receiveJson = new JSONObject(receiverJsonString);
                user = new UserInfoViewModel();
                user.setUser_id(receiveJson.getString("user_id"));
                user.setUsername(receiveJson.getString("username"));
                user.setSchool(receiveJson.getString("school"));
                user.setEmail(receiveJson.getString("email"));
                user.setTel(receiveJson.getString("tel"));
                user.setSex(receiveJson.getString("sex"));
                user.setStatus(receiveJson.getInt("status"));
                users.add(user);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * 通过InputStream解析网页返回的数据
     * @param is
     * @return
     */
    private String readStream(InputStream is) {
        InputStreamReader isr = null;
        String result = "";

        try {
            String line = "";
            isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine())!= null) {
                result += line;
            }
            br.close();
            isr.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 实现网络的异步访问
     */
    //UserInfoViewModel-> List -> Adapter -> ListView
    private class UserInfoAsyncTask extends AsyncTask<String,Void,List<UserInfoViewModel>> {

        @Override
        protected List<UserInfoViewModel> doInBackground(String... params) {
                List<UserInfoViewModel> list = null;
                list = myGetJsonData(params[0]);
                return list;
        }

        @Override
        protected void onPostExecute(List<UserInfoViewModel> users) {
            super.onPostExecute(users);
            if(users != null) {
                LoadingFace.setVisibility(View.GONE);

                UserInfoViewModel user = users.get(0);
                //显示一个ProgressBar
                //获取成功后移除，然后更新数据
                nickName.setText(user.getUsername());
                //Sex.setText();
                birthDay.setText(user.getCreate_time());
                school.setText(user.getSchool());
                email.setText(user.getEmail());
                phoneNumber.setText(user.getTel());
            } else {
                //提示服务器出现错误
                //LoadingFace.setBackgroundColor(Color.WHITE);
                //loadingText.setText("服务器出现错误");
                //loadingText.setTextColor(Color.RED);
                //progressBar.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 任务预处理
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingFace.setVisibility(View.VISIBLE);
        }
    }
}
