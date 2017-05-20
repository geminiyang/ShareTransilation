package com.yqq.asynstasktest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ListView mListView;
    private String urlString = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mListView = (ListView) findViewById(R.id.listview);

        //启动异步任务（UI初始化）
        new NewsAsyncTask().execute(urlString);
    }

    /**
     * 将URL对应的json格式转换成我们的NewsBean
     * @param param
     * @return
     */
    private List<NewsBean> myGetJsonData(String param) {
        List<NewsBean> newsBeanList = new ArrayList<>();
        //通过InputStream去和获取Json数据
        InputStream is = null;
        try {
            //is = new URL(param).openConnection().getInputStream();
            is = new URL(param).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject;
            NewsBean newsbean;
            try {
                //将整一个json字符串转换成json对象
                jsonObject = new JSONObject(jsonString);
                //根据数组名将json对象变成json数组
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i=0; i<jsonArray.length();i++) {
                    //将json数组继续拆分为更小的json对象
                    jsonObject = jsonArray.getJSONObject(i);
                    newsbean = new NewsBean();
                    newsbean.setNewsIconUrl(jsonObject.getString("picSmall"));
                    newsbean.setNewsTitle(jsonObject.getString("name"));
                    newsbean.setNewsContent(jsonObject.getString("description"));
                    newsBeanList.add(newsbean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsBeanList;
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
    //NewsBean -> List -> Adapter -> ListView
    private class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>> {

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return myGetJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBean) {
            super.onPostExecute(newsBean);
            NewsAdapter newsAdapter = new NewsAdapter(SecondActivity.this,newsBean,mListView);
            mListView.setAdapter(newsAdapter);
        }
    }
}
