package com.idear.move.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idear.move.R;
import com.idear.move.util.CameraUtil;
import com.idear.move.util.FileSaveUtil;
import com.idear.move.util.ImageCheckoutUtil;
import com.idear.move.util.ProgressDialogUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DynamicPublishingActivity extends BaseActivity {

    private Button back,publish;
    private CheckBox cb_location,cb_lock;
    private ImageView camera;
    private TextView permissionInfo,locationText,countText;
    private EditText userInput;
    private ImageView picShow;

    //拍照相关功能
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private final String TAG = "info";
    private final int SELECT_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private final int CROP_PHOTO = 2;
    private final int SDK_PERMISSION_REQUEST = 127;
    private static final int SHOW_IMAGE = 102;
    private String camPicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_dynamic_publishing);

        getPermissions();
        initView();
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // 需要处理
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DynamicPublishingActivity.this);
                    dialog.setInverseBackgroundForced(true);
                    dialog.setTitle("退出发布");
                    dialog.setMessage("是否确认退出发布动态？");
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
                    //可以如此，也可以直接 用dialog 来执行show()
                    AlertDialog apk = dialog.create();
                    apk.show();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressDialogUtil.showLoadingDialog(DynamicPublishingActivity.this,"正在加载中...",false);
                ProgressDialogUtil.showLoadDialog(DynamicPublishingActivity.this);
            }
        });
        cb_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cb_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cb_lock.isChecked()){
                    permissionInfo.setText("公开");
                } else {
                    permissionInfo.setText("私密");
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(DynamicPublishingActivity.this,v,R.layout.pic_op_popup_window);
            }
        });
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().trim().length();
                if(length <= 256) {
                    countText.setText("还可输入" + (256-length) + "字");
                }
            }
        });
    }

    private void initView() {
        back = (Button) findViewById(R.id.ic_arrow_back);
        publish = (Button) findViewById(R.id.publish_dynamic);
        permissionInfo = (TextView) findViewById(R.id.permission_info);
        locationText = (TextView) findViewById(R.id.location_text);

        cb_location = (CheckBox) findViewById(R.id.cb_publishLocation);
        cb_lock = (CheckBox) findViewById(R.id.cb_whoCanRead);
        camera = (ImageView) findViewById(R.id.camera);
        picShow = (ImageView) findViewById(R.id.pic_show);
        userInput = (EditText) findViewById(R.id.editText);
        countText = (TextView) findViewById(R.id.textCount);
    }

    /**
     * 权限相关处理
     */
    @TargetApi(23)
    protected void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("info","Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n") ;
            }
            if (permissions.size() > 0) {
                //请求权限
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        /**
         * 权限请求结果回调
         */
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                //初始化操作
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                //批量进行权限申请
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果拒绝权限授予则提示
                    CAN_WRITE_EXTERNAL_STORAGE = false;
                    ToastUtil.getInstance().showToast(this,"禁用图片权限将导致发送图片功能无法使用！");
                } else {
                    CAN_WRITE_EXTERNAL_STORAGE = true;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.ACTION_DOWN) {
            // 需要处理
            //return true;//屏蔽back键(不响应back键)
            //show()方法决定是否display
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            //dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setInverseBackgroundForced(true);
            dialog.setTitle("退出发布");
            dialog.setMessage("是否确认退出发布动态？");
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
            //可以如此，也可以直接 用dialog 来执行show()
            AlertDialog apk = dialog.create();
            apk.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 对应文件选取操作
     * @param context
     * @param parent
     * @param layoutId
     */
    private void showPopupWindow(final Context context, View parent, int layoutId) {
        View contentView = LayoutInflater.from(context).inflate(layoutId,null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //拍照功能监听
        contentView.findViewById(R.id.ll_layer_take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加拍照功能
                //添加拍照功能
                if (CAN_WRITE_EXTERNAL_STORAGE) {
                    //当sd卡可用才执行相关操作state
                    if (FileSaveUtil.isSDExist()) {
                        String str[] = CameraUtil.getSavePicPath();
                        camPicPath = str[0] + str[1];//path + filename (执行拍照操作一次生成一个文件路径名)
                        startActivityForResult(CameraUtil.openMyCamera(camPicPath),TAKE_PICTURE);
                    } else {
                        ToastUtil.getInstance().showToast(DynamicPublishingActivity.this,"请检查内存卡");
                    }
                } else {
                    ToastUtil.getInstance().showToast(DynamicPublishingActivity.this,"权限未开通\n请到设置中开通相册权限");
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
                    //当sd卡可用才执行相关操作state
                    String str[] = CameraUtil.getSavePicPath();//只创建了目录
                    camPicPath = str[0] + str[1];//path + filename (执行拍照操作一次生成一个文件路径名)
                    startGallery();
                } else {
                    ToastUtil.getInstance().showToast(DynamicPublishingActivity.this,"权限未开通\n请到设置中开通相册权限");
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
        Uri uri = Uri.fromFile(new File(camPicPath));
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //从照相机返回
                    startCrop(uri);
                    break;
                case SELECT_PICTURE:
                    //从相册返回
                    galleryBack(data.getData());
                    break;
                case CROP_PHOTO:
                    //剪切处理返回
                    cropBack(uri);
                    break;
                default:
                    break;
            }
        }
    }

    //启动画廊
    private void startGallery() {
        Intent intent = CameraUtil.openGallery();
        startActivityForResult(intent, SELECT_PICTURE);
    }

    //剪切图片(选取完图片和拍照的下一步)
    private void startCrop(Uri inUri) {
        //原图被剪切后的图片覆盖
        Intent intent = CameraUtil.cropPicture(inUri, Uri.fromFile(new File(camPicPath)));
        startActivityForResult(intent, CROP_PHOTO);
    }

    //相册返回
    private void galleryBack(Uri inUri) {
        //从相册返回的URI是手机系统媒体库中的URI，一般是以content:///开头，不是我们所希望的
        // 直接从文件获取的URI（文件的URI一般是file:///开头），所以需要根据content的uri获取的实际的文件路径
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(inUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                startCrop(Uri.fromFile(new File(filePath)));
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * @param inUri
     */
    private void cropBack(Uri inUri) {
        if (inUri == null) {
            return;
        }
        try {
            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(inUri));
            //将剪切后图片保存并显示
            String str[] = CameraUtil.getSavePicPath();//压缩图片另存为
            String GalPicPath = str[0] + str[1];//获取文件保存路径
            boolean isSave = FileSaveUtil.saveBitmap(bitmap,str[0],str[1]);
            File file = new File(GalPicPath);
            if (file.exists() && isSave) {
                //图片的相关显示操作
                picShow.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      picShow.setImageBitmap(bitmap);
                    }
                },1000);
            } else {
                ToastUtil.getInstance().showToast(this,"文件保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
