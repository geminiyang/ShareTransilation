package com.idear.move.SponsorActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idear.move.Activity.MoveProtocolWebViewActivity;
import com.idear.move.Activity.MyBaseActivity;
import com.idear.move.R;
import com.idear.move.Thread.ImageUploadThread;
import com.idear.move.myWidget.LoadingProgressDialog;
import com.idear.move.network.DataGetInterface;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ResultType;
import com.idear.move.util.CameraUtil;
import com.idear.move.util.CheckValidUtil;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.ErrorHandleUtil;
import com.idear.move.util.FileSaveUtil;
import com.idear.move.util.ImageCheckoutUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.Logger;
import com.idear.move.util.PictureUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.swipebackhelper.SwipeBackHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class PublishSActivity extends MyBaseActivity {

    private RelativeLayout rlRoot;//根布局
    private CheckedTextView ctv;
    private Button publish;
    private TextView urlTextView;
    //loading图片
    private LoadingProgressDialog dialog;
    //用户输入相关
    private EditText spreadNameInput,spreadContentInput,spreadUrlInput;
    //图片选择控件
    private static final int TAKE_PICTURE = 100;
    private static final int SELECT_PICTURE = 101;
    private static final int IMAGE_SIZE = 100 * 1024;// 100kb,受载入4096×4096图片限制，与工具类的缩放比例对应
    private static final int SHOW_IMAGE = 102;
    private File uploadFile;//上传图片
    private File mCurrentPhotoFile;//当前图片
    private String camPicPath;//照片保存路径
    private FrameLayout fl_bg_pan;
    public ImageView imageShow;

    private ImageView iv_back;//返回按钮

    //用来拼接日期和时间，最终用来显示的
    private StringBuilder str = new StringBuilder("");
    /**
     * 进行异步显示图片到控件
     */
    private static class ImageHandler extends Handler {
        WeakReference<PublishSActivity> mActivity;

        ImageHandler(PublishSActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            PublishSActivity mCurrentActivity = mActivity.get();
            if(mCurrentActivity!=null) {
                switch (msg.what) {
                    case SHOW_IMAGE:
                        mCurrentActivity.imageShow.setImageBitmap(ImageCheckoutUtil.getLocalBitmap(((msg.obj).toString())));
                        break;
                    case 101:
                        mCurrentActivity.dialog.dismiss();
                        mCurrentActivity.finish();
                        break;
                    default:
                        break;
                }
            }
        }

    }
    public ImageHandler imageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_s);

        initView();
        initEvent();
        //设置当前Activity不能够滑动返回
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);
    }

    /**
     * 绑定ID
     */
    private void initView() {
        ctv = (CheckedTextView) findViewById(R.id.check_tv_title);
        publish = (Button) findViewById(R.id.publish);
        urlTextView = (TextView) findViewById(R.id.tv_url);
        fl_bg_pan = (FrameLayout) findViewById(R.id.pan_bg);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);

        spreadNameInput = (EditText) findViewById(R.id.spreadNameInput);
        spreadContentInput = (EditText) findViewById(R.id.spreadContentInput);
        spreadUrlInput = (EditText) findViewById(R.id.spreadUrlInput);

        imageShow = (ImageView) findViewById(R.id.pic_show);
        imageHandler = new ImageHandler(this);
    }

    /**
     * 绑定相关监听器
     */
    private void initEvent() {
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctv.toggle();
            }
        });
        //发布按钮监听器
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctv.isChecked()) {
                    //打勾状态才能提交发布，相关的网络访问操作
                    submitUploadFile();
                } else {
                    //非打勾状态进行提示
                    ToastUtil.getInstance().showToast(PublishSActivity.this,"请阅读协议并打勾!");
                }
            }
        });

        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件，跳转到一个WEBVIEW
                IntentSkipUtil.skipToNextActivity(PublishSActivity.this,MoveProtocolWebViewActivity.class);
            }
        });

        //图片选择器监听
        fl_bg_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先获取权限
                getPermissions();
                showPopupWindow(PublishSActivity.this,rlRoot,R.layout.pic_op_popup_window);
            }
        });
        //返回按钮监听
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PublishSActivity.this);
                dialog.setIcon(null);
                dialog.setInverseBackgroundForced(true);
                dialog.setTitle("退出");
                dialog.setMessage("你确定要退出推广发布？");
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                dialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog apk = dialog.create();
                apk.show();
            }
        });
    }

    /**
     * 对应文件选取操作
     * @param context
     * @param parent
     * @param layoutId
     */
    private void showPopupWindow(final Context context, View parent,int layoutId) {
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
                        ToastUtil.getInstance().showToast(PublishSActivity.this,"请检查内存卡");
                    }
                } else {
                    ToastUtil.getInstance().showToast(PublishSActivity.this,"权限未开通\n请到设置中开通相册权限");
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
                        ToastUtil.getInstance().showToast(PublishSActivity.this,"请检查SD卡");
                    }
                } else {
                    ToastUtil.getInstance().showToast(PublishSActivity.this,"权限未开通\n请到设置中开通相册权限");
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
                                imageHandler.sendMessage(msg);
                            }
                        } else {
                            ToastUtil.getInstance().showToast(PublishSActivity.this,"该文件不存在");
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
                                msg.obj = path;
                                msg.what = SHOW_IMAGE;
                                imageHandler.sendMessage(msg);
                            }
                        } else {
                            ToastUtil.getInstance().showToast(PublishSActivity.this,"该文件不存在");
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.ACTION_DOWN) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setIcon(null);
            dialog.setInverseBackgroundForced(true);
            dialog.setTitle("退出");
            dialog.setMessage("你确定要退出推广发布？");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            dialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog apk = dialog.create();
            apk.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    /**
     * 图片的后续操作(压缩处理)
     */
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
                        imageHandler.sendMessage(msg);
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
        String spreadNameInputStr = spreadNameInput.getText().toString().trim();
        String spreadContentInputStr = spreadContentInput.getText().toString().trim();
        String spreadUrlInputStr = spreadUrlInput.getText().toString().trim();
        if(!CheckValidUtil.isAllNotEmpty(spreadNameInputStr,spreadContentInputStr,
                spreadUrlInputStr)) {
            ToastUtil.getInstance().showToast(this,"请确保表单输入完整");
            return;
        }
        //检查所填数据是否完整
        if (uploadFile == null || (!uploadFile.exists())) {
            return;
        }
        final String requestURL = HttpPath.getSPath();
        Logger.d("请求的---URL=" + requestURL);
        Logger.d("请求的---fileName=" + uploadFile.getName());
        Logger.d("请求的---fileSize=" + uploadFile.length());
        //提交到服务器的文本
        final Map<String, String> params = new HashMap<>();
        params.put("bis_id", CookiesSaveUtil.getUserId(PublishSActivity.this));
        params.put("content", spreadContentInputStr);
        params.put("title", spreadNameInputStr);
        params.put("url", spreadUrlInputStr);
        //提交到服务器的图片
        final Map<String, File> files = new HashMap<>();
        files.put("img", uploadFile);
        ImageUploadThread imageUploadThread = new ImageUploadThread(PublishSActivity.this,
                requestURL,params,files);
        imageUploadThread.setDataGetListener(new DataGetInterface() {
            @Override
            public void finishWork(Object obj) {
                if(obj instanceof ResultType) {
                    ResultType result = (ResultType) obj;
                    if(Integer.parseInt(result.getStatus()) == 1) {
                        imageHandler.sendEmptyMessage(101);
                    }
                    ToastUtil.getInstance().showToastInThread(PublishSActivity.this,
                            result.getMessage());
                } else {
                    ToastUtil.getInstance().showToastInThread(PublishSActivity.this,
                            obj.toString());
                }
            }

            @Override
            public void interrupt(Exception e) {
                //添加网络错误处理
                ToastUtil.getInstance().showToastInThread(PublishSActivity.this,
                        ErrorHandleUtil.ExceptionToStr(e,PublishSActivity.this));
            }
        });
        imageUploadThread.start();
        dialog =new LoadingProgressDialog(this, R.drawable.progress_loading);
        dialog.show();
    }
}
