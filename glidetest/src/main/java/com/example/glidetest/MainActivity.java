package com.example.glidetest;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tv;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
        tv = (TextView) findViewById(R.id.tv);

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
//        imageView.setImageBitmap(
//                ImageUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.rina, 250, 250));

        loadBitmap(R.drawable.rina,imageView);
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
}
