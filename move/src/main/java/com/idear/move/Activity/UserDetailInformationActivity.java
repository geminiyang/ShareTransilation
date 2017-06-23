package com.idear.move.Activity;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.idear.move.POJO.UserInfoViewModel;
import com.idear.move.R;
import com.idear.move.Service.NetBroadCastReceiver;
import com.idear.move.Thread.UpdateUserInfoThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.ErrorHandleUtil;
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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserDetailInformationActivity extends BaseActivity {
    //参数
    public static String NICKNAME="username";
    public static String SCHOOL="school";
    public static String TEL ="tel";
    //标题烂
    private Toolbar mToolBar;
    //界面相关
    private TextView nickName,Sex,birthDay,school,email,password,phoneNumber;
    private RelativeLayout LoadingFace;
    private ProgressBar progressBar;
    private TextView loadingText;
    private ImageView iv_back,updatePassword,updateSex,updateNickname,updateSchool,updateTel;
    //异步任务相关
    private List<UserInfoAsyncTask> list= new ArrayList<>();
    //网络去处理相关
    private NetBroadCastReceiver receiver;//广播接收器


    private static class MyHandler extends Handler {
        WeakReference mActivity;
        MyHandler(UserDetailInformationActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            UserDetailInformationActivity theActivity = (UserDetailInformationActivity) mActivity.get();
            switch (msg.what) {
                case 0:
                    theActivity.Sex.setText("女");
                    break;
                case 1:
                    theActivity.Sex.setText("男");
                case 2:
                    //启动异步任务（UI初始化）
                    //检查网络是否连接
                    if (NetWorkUtil.isNetworkConnected(theActivity)) {
                        theActivity.list.get((theActivity.list.size()-1)).execute(HttpPath.getUserInfoPath());
                    } else {
                        //提示无网络连接
                        theActivity.LoadingFace.setBackgroundColor(Color.WHITE);
                        theActivity.loadingText.setText("网络无连接");
                        theActivity.loadingText.setTextColor(Color.RED);
                        theActivity.progressBar.setVisibility(View.INVISIBLE);
                    }
                    theActivity.addNewAsyncTask();
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_user_detail_information);


        initView();
        initEvent();

        //通过广播设置网络监听
        receiver = new NetBroadCastReceiver();//全局检测网络变化
        startFirstAsyncTask();//开启第一个异步任务

    }
    /**
     * 开启第一个异步任务
     */
    private void startFirstAsyncTask() {
        addNewAsyncTask();
        handler.sendEmptyMessage(2);
    }

    /**
     * 添加新的异步任务
     */
    private void addNewAsyncTask() {
        list.clear();
        list.add(new UserInfoAsyncTask());
    }

    /**
     * 绑定视图
     */
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
        updateSex = (ImageView) findViewById(R.id.iv_next_sex);
        updateNickname = (ImageView) findViewById(R.id.iv_next_nickname);
        updateSchool = (ImageView) findViewById(R.id.iv_next_school);
        updateTel = (ImageView) findViewById(R.id.iv_next_cellphoneNumber);

        LoadingFace = (RelativeLayout) findViewById(R.id.loading_face);
        loadingText = (TextView) findViewById(R.id.tv_progressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**
     * 绑定事件
     */
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
                updateSwitcher(6);
            }
        });
        updateSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSwitcher(2);
            }
        });
        updateNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSwitcher(1);
            }
        });
        updateSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSwitcher(4);
            }
        });
        updateTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSwitcher(7);
            }
        });
    }

    /**
     * 更新信息的分支
     * @param position 在视图上的位置
     */
    private void updateSwitcher(int position) {
        switch (position) {
            case 1:
                updateInfo(NICKNAME,"修改昵称");
                break;
            case 2:
                updateSex();
                break;
            case 3:

                break;
            case 4:
                updateInfo(SCHOOL,"修改学校");
                break;
            case 5:
                //邮箱不能修改
                break;
            case 6:
                IntentSkipUtil.skipToNextActivity(UserDetailInformationActivity.this,
                        UserUpdatePasswordActivity.class);
                break;
            case 7:
                updateInfo(TEL,"修改电话号码");
                break;
        }
    }


    /**
     * 修改昵称,学校,电话号码
     * @param key 键值
     * @param title 标题名
     */
    private void updateInfo(String key,String title) {
        final EditText input = new EditText(UserDetailInformationActivity.this);
        final String mKey = key;
        //获取按钮的LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //在LayoutParams中设置margin
        params.setMarginStart(25);
        params.setMarginEnd(25);
        //把这个LayoutParams设置给按钮
        input.setLayoutParams(params);
        input.setMaxLines(1);
        input.setSingleLine(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailInformationActivity.this);
        builder.setCancelable(false);
        builder.setTitle(title).setIcon(null).setView(input)
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdateUserInfoThread updateUserInfoThread = new UpdateUserInfoThread(UserDetailInformationActivity.this,
                        HttpPath.getUpdateUserInfoPath(),mKey, input.getText().toString());
                updateUserInfoThread.setDataGetListener(new DataGetInterface() {
                    @Override
                    public void finishWork(Object obj) {
                        if(obj instanceof ResultType) {
                            ResultType result = (ResultType) obj;
                            if(Integer.parseInt(result.getStatus()) == 1) {
                                handler.sendEmptyMessage(2);
                            }
                            ToastUtil.getInstance().showToastInThread(UserDetailInformationActivity.this,
                                    result.getMessage());

                        } else {
                            ToastUtil.getInstance().showToastInThread(UserDetailInformationActivity.this,
                                    obj.toString());
                        }
                    }

                    @Override
                    public void interrupt(Exception e) {
                        //添加网络错误处理
                        ToastUtil.getInstance().showToastInThread(UserDetailInformationActivity.this,
                                ErrorHandleUtil.ExceptionToStr(e,UserDetailInformationActivity.this));
                    }
                });
                updateUserInfoThread.start();
            }
        });
        builder.show();
    }

    //修改性别
    private void updateSex(){
        final String[] str = new String[]{"女", "男"};
        final int[] position = {0};
        new AlertDialog.Builder(this)
                .setTitle("修改性别" )
                .setIcon(null)
                .setSingleChoiceItems(str, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                position[0] = which;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdateUserInfoThread updateUserInfoThread = new UpdateUserInfoThread(UserDetailInformationActivity.this,
                        HttpPath.getUpdateUserInfoPath(),"sex", position[0]+"");
                updateUserInfoThread.setDataGetListener(new DataGetInterface() {
                    @Override
                    public void finishWork(Object obj) {
                        if(obj instanceof ResultType) {
                            ResultType result = (ResultType) obj;
                            if(Integer.parseInt(result.getStatus()) == 1) {
                                handler.sendEmptyMessage(position[0]);
                            }
                            ToastUtil.getInstance().showToastInThread(UserDetailInformationActivity.this,
                                    result.getMessage());

                        } else {
                            ToastUtil.getInstance().showToastInThread(UserDetailInformationActivity.this,
                                    obj.toString());
                        }
                    }

                    @Override
                    public void interrupt(Exception e) {
                        //添加网络错误处理
                        ToastUtil.getInstance().showToastInThread(UserDetailInformationActivity.this,
                                ErrorHandleUtil.ExceptionToStr(e,UserDetailInformationActivity.this));
                    }
                });
                updateUserInfoThread.start();
            }
        }).show();
    }


    @Override
    public void onResume() {
        IntentFilter filter=new IntentFilter();
        //一条信息触发一次广播接收器
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
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
                user.setSex(receiveJson.getInt("sex"));
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
                if(user.getSex()==0) {
                    Sex.setText("女");
                } else {
                    Sex.setText("男");
                }

                birthDay.setText(user.getCreate_time());
                school.setText(user.getSchool());
                email.setText(user.getEmail());
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

        /**
         * 异步任务的取消
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
