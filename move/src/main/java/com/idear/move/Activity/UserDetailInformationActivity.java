package com.idear.move.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.idear.move.Fragment.UserInformationFragment;
import com.idear.move.Helper.ImgSQLiteOpenHelper;
import com.idear.move.POJO.UserInfoViewModel;
import com.idear.move.R;
import com.idear.move.Service.ActivityManager;
import com.idear.move.Service.NetBroadCastReceiver;
import com.idear.move.Thread.ImageUploadThread;
import com.idear.move.Thread.UpdateUserInfoThread;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.CameraUtil;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.FileSaveUtil;
import com.idear.move.util.ImageCheckoutUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.Logger;
import com.idear.move.util.NetWorkUtil;
import com.idear.move.util.PictureUtil;
import com.idear.move.util.ToastUtil;
import com.idear.move.util.UploadUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailInformationActivity extends MyBaseActivity {
    //参数
    public static String NICKNAME="username";
    public static String SCHOOL="school";
    public static String TEL ="tel";

    private RelativeLayout rootLayout;
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
    //图片选择控件
    private static final int TAKE_PICTURE = 100;
    private static final int SELECT_PICTURE = 101;
    private static final int IMAGE_SIZE = 100 * 1024;// 100kb,受载入4096×4096图片限制，与工具类的缩放比例对应
    private static final int SHOW_IMAGE = 102;
    private File uploadFile;
    private File mCurrentPhotoFile;
    private String camPicPath;//照片保存路径
    private ImageView imageShow;
    /**
     * 进行异步显示图片到控件和信息的更新
     */
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
                case SHOW_IMAGE:
                    theActivity.imageShow.setImageBitmap(ImageCheckoutUtil.getLocalBitmap(((msg.obj).toString())));
                    //开启异步上传任务
                    theActivity.submitUploadFile();
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    /*数据库变量*/
    private ImgSQLiteOpenHelper helper ;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_user_detail_information);
        initView();
        initEvent();
        startFirstAsyncTask();//开启第一个异步任务

        //实例化数据库SQLiteOpenHelper子类对象
        helper = new ImgSQLiteOpenHelper(this);
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
        imageShow = (ImageView) findViewById(R.id.avatar);

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

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
        imageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先获取权限
                getPermissions();
                showPopupWindow(UserDetailInformationActivity.this,rootLayout,R.layout.pic_op_popup_window);
            }
        });
    }

    /**
     * 对应文件选取操作
     * @param context
     * @param parent
     * @param layoutId
     */
    private void showPopupWindow(final Context context, View parent, int layoutId) {
        final boolean CAN_WRITE_EXTERNAL_STORAGE = getIfCanWrite();//获取是否具有读写权限
        View contentView = LayoutInflater.from(context).inflate(layoutId,null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //拍照功能监听
        contentView.findViewById(R.id.ll_layer_take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加拍照功能
                if (CAN_WRITE_EXTERNAL_STORAGE) {
                    //当sd卡可用才执行相关操作state
                    if (FileSaveUtil.isSDExist()) {
                        String str[] = CameraUtil.getSavePicPath();
                        camPicPath = str[0] + str[1];//path + filename (执行拍照操作一次生成一个文件路径名)
                        startActivityForResult(CameraUtil.openMyCamera(camPicPath),TAKE_PICTURE);
                    } else {
                        ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"请检查内存卡");
                    }
                } else {
                    ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"权限未开通\n请到设置中开通相册权限");
                    getPermissions();
                }
            }
        });

        //照片选取监听
        contentView.findViewById(R.id.ll_layer_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加选取相册图片功能
                if (CAN_WRITE_EXTERNAL_STORAGE) {
                    // 判断是否有SD卡
                    if (FileSaveUtil.isSDExist()) {
                        startActivityForResult(CameraUtil.openRecentPhotoList(),SELECT_PICTURE);
                    } else {
                        ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"请检查SD卡");
                    }
                } else {
                    ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"权限未开通\n请到设置中开通相册权限");
                    getPermissions();
                }
            }
        });
        contentView.findViewById(R.id.ll_layer_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //设置入场动画
        popupWindow.setAnimationStyle(R.style.PopupSlideFromBottomAnimation);
        // 设置焦点在弹窗上
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        //设置显示位置
        if(!popupWindow.isShowing()) {
            //相对屏幕，显示在指定位置
            popupWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //操作成功后处理(压缩操作)
            switch (requestCode) {
                case TAKE_PICTURE:
                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(camPicPath);
                        File camFile = new File(camPicPath); // 图片文件路径
                        if (camFile.exists()) {
                            //检查图片大小
                            int size = ImageCheckoutUtil.getImageSize(
                                    ImageCheckoutUtil.getLocalBitmap(camPicPath));
                            if (size > IMAGE_SIZE) {
                                //压缩处理
                                imageShow.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                compressPicture(camPicPath);
                            } else {
                                uploadFile = camFile;//上传文件
                                imageShow.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                Message msg = new Message();
                                msg.obj = camPicPath;
                                msg.what = SHOW_IMAGE;
                                handler.sendMessage(msg);
                            }
                        } else {
                            ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"该文件不存在");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        // 关闭流
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case SELECT_PICTURE:
                    Uri uri = data.getData();
                    String path = FileSaveUtil.getPath(getApplicationContext(), uri);//转换路径
                    if(path!=null) {
                        mCurrentPhotoFile = new File(path); // 图片文件路径
                        if (mCurrentPhotoFile.exists()) {
                            int size = ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLocalBitmap(path));
                            if (size > IMAGE_SIZE) {
                                imageShow.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                //添加压缩处理
                                compressPicture(path);
                            } else {
                                uploadFile = mCurrentPhotoFile;//上传文件
                                //不需要压缩直接处理
                                imageShow.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                Message msg = new Message();
                                msg.obj = camPicPath;
                                msg.what = SHOW_IMAGE;
                                handler.sendMessage(msg);
                            }
                        } else {
                            ToastUtil.getInstance().showToast(UserDetailInformationActivity.this,"该文件不存在");
                        }
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            //操作取消
        }
    }


    //图片的后续操作(压缩处理)
    private void compressPicture(final String path) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String str[] = CameraUtil.getSavePicPath();//压缩图片另存为
                    String GalPicPath = str[0] + str[1];//获取文件保存路径
                    //生成对应bitmap
                    Bitmap bitmap = PictureUtil.compressSizeImage(path,
                            imageShow.getWidth(), imageShow.getHeight()
                    );//图片压缩处理具体逻辑
                    //判断图像是否有90度旋转
                    boolean isSave = FileSaveUtil.saveBitmap(
                            PictureUtil.reviewPicRotate(bitmap, str[0]+str[1]),
                            str[0],str[1]);
                    File file = new File(GalPicPath);
                    uploadFile = file;//上传文件
                    if (file.exists() && isSave) {
                        //图片的操作()
                        Looper.prepare();
                        Message msg = new Message();
                        msg.obj = path;
                        msg.what = SHOW_IMAGE;
                        handler.sendMessage(msg);
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 上传图片操作
     */
    private void submitUploadFile() {
        if (uploadFile == null || (!uploadFile.exists())) {
            return;
        }
        final String requestURL = HttpPath.getUpdateUserInfoPath();
        Logger.d("请求的---URL=" + requestURL);
        Logger.d("请求的---fileName=" + uploadFile.getName());
        Logger.d("请求的---fileSize=" + uploadFile.length());
        final Map<String, String> params = new HashMap<>();
        params.put("user_id", CookiesSaveUtil.getUserId(UserDetailInformationActivity.this));
        final Map<String, File> files = new HashMap<>();
        files.put("img", uploadFile);
        ImageUploadThread imageUploadThread = new ImageUploadThread(UserDetailInformationActivity.this,
                requestURL,params,files);
        imageUploadThread.setDataGetListener(new DataGetInterface() {
            @Override
            public void finishWork(Object obj) {
                if(obj instanceof ResultType) {
                    ResultType result = (ResultType) obj;
                    if(Integer.parseInt(result.getStatus()) == 1) {
                        ToastUtil.getInstance().showToastInThread(UserDetailInformationActivity.this,"上传完成!");
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
        imageUploadThread.start();
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
                int userId = Integer.parseInt(CookiesSaveUtil.getUserId(
                        UserDetailInformationActivity.this));//用户Id
                String url = queryData(userId);
                Glide.with(UserDetailInformationActivity.this).load(HttpPath.getPath() + url).
                        error(R.mipmap.paintbox).into(imageShow);
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

    /*查询id对应的URL*/
    private String queryData(int id) {
        Logger.d("query---" + id);
        //模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id ,picUrl from imgRecords where id = " + id, null);
        ArrayList<String> list = new ArrayList<>();

        //遍历Cursor
        while(cursor.moveToNext()){
            list.add(cursor.getString(1));//对应picUrl字段
        }
        return list.get(0);
    }
}
