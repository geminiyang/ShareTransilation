package com.yqq.asynstasktest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar,hpg;
    private TextView info;
    private String url = "http://img0.imgtn.bdimg.com/it/u=1926840883,609302051&fm=23&gp=0.jpg";

    private ImageDownloadAsyncTask AsyncTask;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initEvent() {
        AsyncTask = new ImageDownloadAsyncTask();
        AsyncTask.execute(url);

        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        calculate();
    }

    private void calculate() {
        StringBuilder stringBuilder = new StringBuilder();

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int memClass = activityManager.getMemoryClass();//以m为单位
        int largeClass = activityManager.getLargeMemoryClass();

        float totalMemory = Runtime.getRuntime().totalMemory()*1.0f*1024*1024;
        float freeMemory = Runtime.getRuntime().freeMemory()*1.0f*1024*1024;
        float maxMemory = Runtime.getRuntime().maxMemory()*1.0f*1024*1024;

        stringBuilder.append("MemoryClass: " + memClass + "\n");
        stringBuilder.append("LargeMemoryClass: " + largeClass + "\n");
        stringBuilder.append("TotalMemory: " + totalMemory + "\n");
        stringBuilder.append("FreeMemory: " + freeMemory + "\n");
        stringBuilder.append("MaxMemory: " + maxMemory + "\n");
        info.setText(stringBuilder);
        Log.d("APP",stringBuilder.toString());
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.image);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        info = (TextView) findViewById(R.id.info);
        hpg = (ProgressBar) findViewById(R.id.pg);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.next:
                Intent it = new Intent(MainActivity.this,SecondActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(it);
                break;
        }
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d("APP",level+"");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myAsyncTask!=null&&myAsyncTask.getStatus() == android.os.AsyncTask.Status.RUNNING) {
            //此方法只是标记该异步任务为取消执行，不可能立即停止一个执行的线程（异步任务）
            myAsyncTask.cancel(true);
        }
    }

    class ImageDownloadAsyncTask  extends AsyncTask<String,Void,Bitmap> {

        /**
         * 处理图片
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(bitmap);
        }

        /**
         * 任务预处理
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * 进行耗时操作
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            String url = params[0];
            URLConnection urlConnection = null;
            InputStream is;
            try {
                URL httpUrl = new URL(url);
                urlConnection = httpUrl.openConnection();
                is = urlConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                Thread.sleep(3000);
                bitmap = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,Void> {

        private static final String TAG = "yqq";

        @Override
        protected void onPreExecute() {
            //1
            super.onPreExecute();
            Log.v(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //2
            Log.v(TAG, "doInBackground");
            for (int i = 0; i <= 100; i++) {
                if(isCancelled()) {
                    break;
                }
                publishProgress(i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(!isCancelled()) {
                //在这里进行更新数据操作
                hpg.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //3
            Log.v(TAG, "onPostExecute");
        }

    }

}

