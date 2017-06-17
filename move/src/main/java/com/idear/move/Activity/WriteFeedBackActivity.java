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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idear.move.R;
import com.idear.move.util.CameraUtil;
import com.idear.move.util.FileSaveUtil;
import com.idear.move.util.ProgressDialogUtil;
import com.idear.move.util.ScreenUtil;
import com.idear.move.util.ToastUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteFeedBackActivity extends BaseActivity {

    //界面元素
    private Button back,publish;
    private EditText userInput;
    private TextView countText;
    private ImageView imgAdd,picShow;
    //拍照相关功能
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private final String TAG = "info";
    private final int SELECT_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private final int CROP_PHOTO = 2;
    private final int SDK_PERMISSION_REQUEST = 127;
    private static final int SHOW_IMAGE = 102;
    private String camPicPath;

    private GridView gridView;//网格视图
    private List<Map<String, Object>> data_list;//数据源
    private SimpleAdapter simple_adapter;//适配器
    private int[] icon = {R.mipmap.best, R.mipmap.nice,R.mipmap.ok, R.mipmap.bad};

    private String[] iconName={ "十分满意", "满意","还可以","不太理想"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.blue_light));
        setContentView(R.layout.activity_write_feed_back);

        getPermissions();
        initView();
        initEvent();
    }

    private List<Map<String, Object>> get_data() {
        data_list = new ArrayList<>();
        //icon和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;

    }

    private void initGirdView() {
        data_list = get_data();//获取数据
        //新建适配器
        String [] from ={"image","text"};
        final int [] to = {R.id.image,R.id.text};
        simple_adapter = new SimpleAdapter(this, data_list, R.layout.item_attitude, from, to);
        //配置适配器
        gridView.setAdapter(simple_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item= (HashMap<String, Object>) parent.getItemAtPosition(position);
                ToastUtil.getInstance().showToast(WriteFeedBackActivity.this,item.get("text").toString());
            }
        });
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 需要处理
                AlertDialog.Builder dialog = new AlertDialog.Builder(WriteFeedBackActivity.this);
                dialog.setInverseBackgroundForced(true);
                dialog.setTitle("结束反馈");
                dialog.setMessage("是否确认退出填写反馈？");
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
                //发布活动相关线程的编写
                ProgressDialogUtil.showLoadDialog(WriteFeedBackActivity.this);
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

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(WriteFeedBackActivity.this,v,R.layout.pic_op_popup_window);
            }
        });
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        initGirdView();

        back = (Button) findViewById(R.id.ic_arrow_back);
        publish = (Button) findViewById(R.id.publish_dynamic);
        userInput = (EditText) findViewById(R.id.editText);
        countText = (TextView) findViewById(R.id.textCount);
        imgAdd = (ImageView) findViewById(R.id.addImg);
        picShow = (ImageView) findViewById(R.id.pic_show);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setInverseBackgroundForced(true);
            dialog.setTitle("退出反馈");
            dialog.setMessage("是否确认退出填写反馈？");
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
                        ToastUtil.getInstance().showToast(WriteFeedBackActivity.this,"请检查内存卡");
                    }
                } else {
                    ToastUtil.getInstance().showToast(WriteFeedBackActivity.this,"权限未开通\n请到设置中开通相册权限");
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
                    ToastUtil.getInstance().showToast(WriteFeedBackActivity.this,"权限未开通\n请到设置中开通相册权限");
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
