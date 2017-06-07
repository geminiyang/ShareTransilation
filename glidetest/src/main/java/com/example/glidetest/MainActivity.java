package com.example.glidetest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tv,bottomTv;
    private LruCache<String, Bitmap> mMemoryCache;

    //权限相关操作
    private String permissionInfo;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private static final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
        tv = (TextView) findViewById(R.id.tv);
        bottomTv = (TextView) findViewById(R.id.bottom_txt);


        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };

        getPermissions();


//        TextViewUtil.limitTextViewString(tv.getText().toString(), 140,tv, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    /**
     * 将图片存储到缓存空间
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从缓存空间中取出图片
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 载入图片
     * @param resId
     * @param imageView
     */
    public void loadBitmap(int resId, ImageView imageView) {
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher_round);
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(resId);
        }
    }


    public void loadImage(View view) {
//        String url = "http://img4.imgtn.bdimg.com/it/u=1495511926,3386591440&fm=23&gp=0.jpg";
//        Glide.with(this).load(url).asBitmap().
//                placeholder(R.mipmap.ic_launcher_round).
//                diskCacheStrategy(DiskCacheStrategy.ALL).
//                error(R.mipmap.ic_launcher_round).
//                into(imageView);
        //显示压缩后的图片
        //获取屏幕的高度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240 / 320 / 480）
//        imageView.setImageBitmap(
//                ImageUtil.decodeSampledBitmapFromResource(getResources(), R.mipmap.rina, width, height));
        imageView.setImageBitmap(
                ImageUtil.decodeSampledBitmapFromResource(getResources(), R.mipmap.rina, 100, 100));

        //loadBitmap(R.drawable.rina,imageView);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(
                Environment.getExternalStorageDirectory().getAbsolutePath()  + "/DCIM/imagetest.jpg"
                ,opts);

        int b = 1;
        switch (bitmap.getConfig()) {
            case ALPHA_8:
                b = 1;
                break;
            case ARGB_4444:
                b = 2;
                break;
            case ARGB_8888:
                b = 4;
                break;
        }
        int bytes1 = bitmap.getWidth() * bitmap.getHeight() * b;

        //bytes = 原始图片宽*(options.inTargetDensity/options.inDensity)
        // *原始图片长*(options.inTargetDensity/options.inDensity)*每个像素点位数
//        bottomTv.setText("压缩前图片的大小" + (bitmap.getByteCount() / 1024 / 1024 )
//                + "M,宽度为 " + bitmap.getWidth() + "高度为 " + bitmap.getHeight()
//                + "图片格式 " + bitmap.getConfig() + "像素密度 " + bitmap.getDensity()
//                + "two:" + bytes1/1024/1024 + "inDensity:" + opts.inDensity +"inTargetDensity:"
//                + opts.inTargetDensity);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmapOne = BitmapFactory.decodeResource(getResources(), R.mipmap.rina, options);

        Bitmap bitmap1 = null;
        try {
            bitmap1 = BitmapFactory.decodeStream(getAssets().open("rina.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bottomTv.setText("压缩前图片的大小" + ((float)bitmapOne.getByteCount() / 1024.0f / 1024.0f )
                + "M,宽度为 " + bitmapOne.getWidth() + "高度为 " + bitmapOne.getHeight()
                + "图片格式 " + bitmapOne.getConfig() + "options: " + "inDensity: " + options.inDensity + ","
                + "inTargetDensity: " + options.inTargetDensity  + "屏幕宽度 "+ width + "屏幕高度 " + height
                + "bitmap1" + (float)bitmap1.getByteCount()/1024.0f/1024.0f + "M");
    }

    private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        // 在后台加载图片。
        @Override
        protected Bitmap doInBackground(Integer... params) {
            final Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(
                    getResources(), params[0], 250, 250);
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }
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
}
