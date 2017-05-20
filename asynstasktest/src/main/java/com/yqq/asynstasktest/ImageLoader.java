package com.yqq.asynstasktest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * 异步加载图片的方法
 * Created by user on 2017/4/28.
 */

public class ImageLoader {

    private ImageView mImageView;
    private String mUrl;
    //创建cache
    private LruCache<String,Bitmap> mCaches;
    private ListView mListView;
    private Set<NewsAsyncTask> mTasks;

    public ImageLoader(ListView listView) {
        this.mListView = listView;
        this.mTasks = new HashSet<>();
        //获取最大缓存内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        this.mCaches = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    /**
     * 将Bitmap增加到缓存
     * @param url
     * @param bitmap
     */
    private void addBitmapToCache(String url,Bitmap bitmap) {
        if(getBitmapFromCache(url) == null) {
            mCaches.put(url,bitmap);
        }
    }

    /**
     * 从缓存中获取数据
     * @param url
     * @return
     */
    private Bitmap getBitmapFromCache(String url) {
        return mCaches.get(url);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //不加上这个判断的话正确的Item不会显示正确的图片
            if(mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    /**
     * 多线程加载图片的方法(没有添加缓存)
     * @param imageView
     * @param imgUrl
     */
    public void showImageByThread(ImageView imageView, final String imgUrl) {

        this.mImageView = imageView;
        this.mUrl = imgUrl;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(imgUrl);
                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();
    }

    private Bitmap getBitmapFromURL(String imgUrl) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(httpURLConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            httpURLConnection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 通过AsyncTask方法异步加载图片
     * @param imageView
     * @param imgUrl
     */
    public void showImgByAsyncTask(ImageView imageView,String imgUrl) {
        //从缓存中获取对应的图片
        Bitmap bitmap = getBitmapFromCache(imgUrl);
        if(bitmap==null) {
            //当bitmap为空则设置占位图
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 加载图片，根据位置起始加载图片
     */
    public void LoadImage(int start ,int end) {
        for (int i = start;i < end;i++) {
            String url = NewsAdapter.getURL(i);
            //从缓存中获取对应的图片
            Bitmap bitmap = getBitmapFromCache(url);
            if(bitmap==null) {
                //如果缓存中没有对应的图片就从网络加载
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTasks.add(task);
            } else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void cancelAllTask() {
        if (mTasks != null) {
            for (NewsAsyncTask task:mTasks) {
                //取消并执行下去
                task.cancel(false);
            }
        }
    }

    private class NewsAsyncTask extends AsyncTask<String,Void,Bitmap> {

        private ImageView mImageView;//可抛弃
        private String mUrl;//解决异步加载导致的显示错误的Bug

        public NewsAsyncTask(String imgUrl) {
            this.mUrl = imgUrl;
        }

        public NewsAsyncTask(ImageView imageView,String imgUrl) {
            this.mImageView = imageView;
            this.mUrl = imgUrl;
        }

        /**
         * 完成异步加载Bitmap的任务
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            //从网络获取图片
            Bitmap bitmap = getBitmapFromURL(url);
            if(bitmap!=null) {
                //将图片获取图片
                addBitmapToCache(url,bitmap);
            }
            return bitmap;
        }

        /**
         * 设置bitmap
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if(imageView!=null&& bitmap!=null) {
                imageView.setImageBitmap(bitmap);
            }
            mTasks.remove(this);
        }
    }
}
