package com.idear.move.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.idear.move.R;
import com.idear.move.util.DisplayUtil;
import com.idear.move.util.FileSaveUtil;
import com.idear.move.util.ImageCheckoutUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.KeyBoardUtils;
import com.idear.move.util.PictureUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.swipebackhelper.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PublishRFActivity extends BaseActivity implements NumberPicker.OnValueChangeListener {

    private RelativeLayout rlRoot;
    private CheckBox cb_goods,cb_money;
    private CheckedTextView ctv;
    private Button publish;
    private TextView urlTextView;

    // 更新显示当前值的TextView
    private EditText personNum,moneyAmount;
    //图片选择控件
    private static final int TAKE_PICTURE = 100;
    private static final int SELECT_PICTURE = 101;
    private static final int IMAGE_SIZE = 100 * 1024;// 100kb,受载入4096×4096图片限制，与工具类的缩放比例对应
    private static final int SHOW_IMAGE = 102;
    private File mCurrentPhotoFile;
    private ImageView pic;
    private FrameLayout fl_bg_pan;
    public ImageView imageShow;
    public ImageHandler imageHandler;

    private ImageView iv_back;

    private EditText editText;
    //用来拼接日期和时间，最终用来显示的
    private StringBuilder str = new StringBuilder("");
    private final int MIN = 1;
    private final int MAX = 100;
    private int currentNum = 2;

    //权限相关操作
    private String permissionInfo;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private static final int SDK_PERMISSION_REQUEST = 127;
    private String camPicPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_rf);

        initView();
        initEvent();
    }

    /**
     * 绑定ID
     */
    private void initView() {

        cb_goods = (CheckBox) findViewById(R.id.cb_goods);
        cb_money = (CheckBox) findViewById(R.id.cb_money);
        ctv = (CheckedTextView) findViewById(R.id.check_tv_title);

        publish = (Button) findViewById(R.id.publish);
        urlTextView = (TextView) findViewById(R.id.tv_url);

        moneyAmount = (EditText) findViewById(R.id.money_amount);
        personNum = (EditText) findViewById(R.id.edit_personNum);

        editText = (EditText) findViewById(R.id.time_select);
        pic = (ImageView) findViewById(R.id.pic);
        fl_bg_pan = (FrameLayout) findViewById(R.id.pan_bg);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        imageShow = (ImageView) findViewById(R.id.pic_show);

        imageHandler = new ImageHandler(this);
    }

    /**
     * 绑定相关监听器
     */
    private void initEvent() {
        //货物复选框监听
        cb_goods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        //资金复选框监听
        cb_money.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    moneyAmount.setVisibility(View.VISIBLE);
                } else {
                    moneyAmount.setVisibility(View.GONE);
                }
            }
        });
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

                } else {
                    //非打勾状态进行提示
                    ToastUtil.getInstance().showToast(PublishRFActivity.this,"请阅读协议并打勾!");
                }
            }
        });

        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件，跳转到一个WEBVIEW
                IntentSkipUtil.skipToNextActivity(PublishRFActivity.this,MoveProtocolWebViewActivity.class);
            }
        });

        //时间编辑框监听
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTimeDialog();
            }
        });
        //招募人数编辑框监听
        personNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog();
            }
        });
        //图片选择器监听
        fl_bg_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先获取权限
                getPermissions();
                showPopupWindow(PublishRFActivity.this,rlRoot,R.layout.pic_op_popup_window);
            }
        });
        //返回按钮监听
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        View contentView = LayoutInflater.from(context).inflate(layoutId,null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //拍照功能监听
        contentView.findViewById(R.id.ll_layer_take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加拍照功能
                if (!CAN_WRITE_EXTERNAL_STORAGE) {
                    ToastUtil.getInstance().showToast(PublishRFActivity.this,"权限未开通\n请到设置中开通相册权限");
                } else {
                    final String state = Environment.getExternalStorageState();
                    //当sd卡可用才执行相关操作
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        String str[] = getSavePicPath();
                        camPicPath = str[0] + str[1];//path + filename
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri uri = Uri.fromFile(new File(camPicPath));
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//所拍的照保存在指定路径
                        startActivityForResult(openCameraIntent,TAKE_PICTURE);
                    } else {
                        ToastUtil.getInstance().showToast(PublishRFActivity.this,"请检查内存卡");
                    }
                }
            }
        });
        //照片选取监听
        contentView.findViewById(R.id.ll_layer_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加选取相册图片功能
                if (!CAN_WRITE_EXTERNAL_STORAGE) {
                    ToastUtil.getInstance().showToast(PublishRFActivity.this,"权限未开通\n请到设置中开通相册权限");
                } else {
                    String status = Environment.getExternalStorageState();
                    // 判断是否有SD卡
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        Intent intent = new Intent();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                        } else {
                            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.putExtra("crop", "true");
                            intent.putExtra("scale", "true");
                            intent.putExtra("scaleUpIfNeeded", true);
                        }
                        intent.setType("image/*");
                        startActivityForResult(intent,SELECT_PICTURE);
                    } else {
                        ToastUtil.getInstance().showToast(PublishRFActivity.this,"没有SD卡");
                    }
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

    /**
     * 获取图片存储路径
     * @return
     */
    private String[] getSavePicPath() {
        final String dir = FileSaveUtil.SD_CARD_PATH + "image_data/";
        try {
            FileSaveUtil.createSDCardDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = String.valueOf(System.currentTimeMillis() + ".jpg");
        final String[] str = {dir,fileName};
        return str;
    }

    protected void showDialog_First(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(R.layout.number_picker_dialog,null);
        AlertDialog.Builder  dialog = new AlertDialog.Builder(context);
        dialog.setView(dialog_layout);
        final AlertDialog display = dialog.create();
        //点击空白区域会退出dialog,true时候会退出
        display.setCanceledOnTouchOutside(true);

        display.show();

        //dialog中的修改按钮
        Button cancel = (Button) dialog_layout.findViewById(R.id.bt_cancel);
        Button sure = (Button) dialog_layout.findViewById(R.id.bt_sure);

        cancel.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                display.cancel();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personNum.setText("");
                personNum.setText(currentNum+"");
                display.cancel();
            }
        });
    }

    private void showNumberPickerDialog() {
        final Dialog dialog = new Dialog(PublishRFActivity.this);
        dialog.setTitle("NumberPicker");

        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.number_picker_dialog);
        Button cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        Button sure = (Button) dialog.findViewById(R.id.bt_sure);
        final NumberPicker mNumberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        mNumberPicker.setMaxValue(MAX); // max value 100
        mNumberPicker.setMinValue(MIN);   // min value 1
        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setOnValueChangedListener(this);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                personNum.setText("");
                personNum.setText(currentNum+"");
                dialog.dismiss(); // dismiss the dialog
            }
        });
        dialog.show();
    }

    private void productTimeDialog() {
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        Dialog dateDialog = new
                DatePickerDialog(PublishRFActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                str.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                Calendar time = Calendar.getInstance();

                Dialog timeDialog =
                        new TimePickerDialog(PublishRFActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog, //在这里指定样式
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                        str.append(hourOfDay + ":" + minute);
                                        EditText show = (EditText) findViewById(R.id.time_select);
                                        show.setText("");
                                        show.setText(str);
                                    }
                                }
                                // 设置初始时间
                                ,time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true);
                timeDialog.setTitle("请选择日期");
                timeDialog.show();
            }
        }
                // 设置初始日期
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH));
        dateDialog.setTitle("请选择日期");
        dateDialog.show();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        currentNum = newVal;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //操作成功后处理
            switch (requestCode) {
                case TAKE_PICTURE:
                    //ToastUtil.getInstance().showToast(PublishRFActivity.this,"TAKE_PICTURE");
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
                                imageShow.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                Message msg = new Message();
                                msg.obj = camPicPath;
                                msg.what = SHOW_IMAGE;
                                imageHandler.sendMessage(msg);
                            }
                        } else {
                            ToastUtil.getInstance().showToast(PublishRFActivity.this,"该文件不存在");
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
                    //ToastUtil.getInstance().showToast(PublishRFActivity.this,"SELECT_PICTURE");
                    Uri uri = data.getData();
                    String path = FileSaveUtil.getPath(getApplicationContext(), uri);//将系统文档应用的图片保存在指定路径
                    mCurrentPhotoFile = new File(path); // 图片文件路径
                    if (mCurrentPhotoFile.exists()) {
                        int size = ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLocalBitmap(path));
                        if (size > IMAGE_SIZE) {
                            imageShow.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            //添加压缩处理
                            compressPicture(path);
                        } else {
                            //不需要压缩直接处理
                            imageShow.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            Message msg = new Message();
                            msg.obj = path;
                            msg.what = SHOW_IMAGE;
                            imageHandler.sendMessage(msg);
                        }
                    } else {
                        ToastUtil.getInstance().showToast(PublishRFActivity.this,"该文件不存在");
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
                    String str[] = getSavePicPath();
                    String GalPicPath = str[0] + str[1];//获取文件保存路径
                    Bitmap bitmap = PictureUtil.compressSizeImage(path,
                            imageShow.getWidth(), imageShow.getHeight()
                    );//图片压缩处理具体逻辑
                    //判断图像是否有90度旋转
                    boolean isSave = FileSaveUtil.saveBitmap(
                            PictureUtil.reviewPicRotate(bitmap, str[0]+str[1]),
                            str[0],str[1]);
                    File file = new File(GalPicPath);
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
     * 权限相关处理
     */
    @TargetApi(23)
    protected void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (permissions.size() > 0) {
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

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                //初始化操作
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果拒绝权限授予则提示
                    CAN_WRITE_EXTERNAL_STORAGE = false;
                    Toast.makeText(this, "禁用图片权限将导致发送图片功能无法使用！", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


     static class ImageHandler extends Handler {
         WeakReference<PublishRFActivity> mActivity;

         ImageHandler(PublishRFActivity activity) {
             mActivity = new WeakReference<PublishRFActivity>(activity);
         }
        @Override
        public void handleMessage(Message msg) {
            PublishRFActivity mCurrentActivity = mActivity.get();
            if(mCurrentActivity!=null) {
                switch (msg.what) {
                    case SHOW_IMAGE:
                        mCurrentActivity.imageShow.setImageBitmap(ImageCheckoutUtil.getLocalBitmap(((msg.obj).toString())));
                        break;
                    default:
                        break;
                }
            }
        }

    }
}
