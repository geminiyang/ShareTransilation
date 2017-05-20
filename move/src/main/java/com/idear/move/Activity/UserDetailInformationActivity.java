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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idear.move.POJO.UserInformation;
import com.idear.move.R;
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
    private BroadcastReceiver connectionReceiver;

    private String jsonUrl = "http://idear.party/info/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_user_detail_information);

        initView();
        //initToolBar(mToolBar);
        //启动异步任务（UI初始化）
        NetWorkUtil.isConnected(this);
        if (NetWorkUtil.isNetworkConnected(this)) {
            new UserInfoAsyncTask().execute(jsonUrl);
        } else {
            //提示无网络连接
            LoadingFace.setBackgroundColor(Color.WHITE);
            loadingText.setText("网络无连接");
            loadingText.setTextColor(Color.RED);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        nickName = (TextView) findViewById(R.id.tv_nickname);
        Sex = (TextView) findViewById(R.id.tv_sex);
        birthDay = (TextView) findViewById(R.id.tv_birthday);
        school = (TextView) findViewById(R.id.tv_school);
        email = (TextView) findViewById(R.id.tv_email);
        password = (TextView) findViewById(R.id.tv_password);
        phoneNumber = (TextView) findViewById(R.id.tv_cellphoneNumber);

        LoadingFace = (RelativeLayout) findViewById(R.id.loading_face);
        loadingText = (TextView) findViewById(R.id.tv_progressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.ic_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
    }

    private void initToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);//使Toolbar能取代原本的 actionbar

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置系统默认退出按钮
        ActionBar actionBar;
        if ((actionBar=getSupportActionBar())!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置标题栏具有返回按钮
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 将URL对应的json格式转换成我们的NewsBean
     * @param param
     * @return
     */
    private List<UserInformation> myGetJsonData(String param) {
        List<UserInformation> users = new ArrayList<>();
        //通过InputStream去和获取Json数据
        InputStream is = null;
        //可以写成URLConnection urlConnection = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(param).openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if(HttpURLConnection.HTTP_OK!=urlConnection.getResponseCode()) {
                return null;
            }
            is = urlConnection.getInputStream();

//            is = new URL(param).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject;
            UserInformation user;
            try {
                //将整一个json字符串转换成json对象
                jsonObject = new JSONObject(jsonString);
                user = new UserInformation();
                user.setUsername(jsonObject.getString("username"));
                user.setPassword(jsonObject.getString("password"));
                user.setSchool(jsonObject.getString("school"));
                user.setEmail(jsonObject.getString("email"));
                user.setTel(jsonObject.getString("tel"));
                users.add(user);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
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
            Thread.sleep(500);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 实现网络的异步访问
     */
    //UserInformation-> List -> Adapter -> ListView
    private class UserInfoAsyncTask extends AsyncTask<String,Void,List<UserInformation>> {

        @Override
        protected List<UserInformation> doInBackground(String... params) {
                List<UserInformation> list = null;
                list = myGetJsonData(params[0]);

                return list;
        }

        @Override
        protected void onPostExecute(List<UserInformation> users) {
            super.onPostExecute(users);
            if(users != null) {
                LoadingFace.setVisibility(View.GONE);

                UserInformation user = users.get(0);
                //显示一个ProgressBar
                //获取成功后移除，然后更新数据
                nickName.setText(user.getUsername());
                //Sex.setText();
                birthDay.setText(user.getCreate_time());
                school.setText(user.getSchool());
                email.setText(user.getEmail());
                password.setText(user.getPassword());
                phoneNumber.setText(user.getTel());
            } else {
                //提示服务器出现错误
                LoadingFace.setBackgroundColor(Color.WHITE);
                loadingText.setText("服务器出现错误");
                loadingText.setTextColor(Color.RED);
                progressBar.setVisibility(View.INVISIBLE);
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
