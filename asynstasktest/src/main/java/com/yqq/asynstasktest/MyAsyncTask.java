package com.yqq.asynstasktest;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by user on 2017/4/27.
 */

public class MyAsyncTask extends AsyncTask<Void,Integer,Void> {

    public static final String TAG = "yqq";

    @Override
    protected void onPreExecute() {
        //1
        super.onPreExecute();
        Log.v(TAG,"onPreExecute");
    }

    @Override
    protected Void doInBackground(Void... params) {
        //2
        Log.v(TAG,"doInBackground");
        for(int i=0;i<100;i++){
            publishProgress(i);
        }
        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //在这里进行更新数据操作
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //3
        Log.v(TAG,"onPostExecute");
    }
}
