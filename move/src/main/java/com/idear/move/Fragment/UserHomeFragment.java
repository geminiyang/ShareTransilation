package com.idear.move.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.idear.move.Activity.AllActivityActivity;
import com.idear.move.Activity.FeedbackActivity;
import com.idear.move.Activity.SpreadActivity;
import com.idear.move.Activity.UserSearchActivity;
import com.idear.move.Adapter.HSVAdapter;
import com.idear.move.POJO.HomeInitialItem;
import com.idear.move.R;
import com.idear.move.Thread.MyHomeLoadingAsyncTask;
import com.idear.move.constants.AppConstant;
import com.idear.move.myWidget.HSVLinearLayout;
import com.idear.move.network.HomeInitialResult;
import com.idear.move.network.HttpPath;
import com.idear.move.util.CookiesSaveUtil;
import com.idear.move.util.IntentSkipUtil;
import com.idear.move.util.Logger;
import com.idear.move.util.NetWorkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHomeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG = "arg";

    public static UserHomeFragment newInstance(String arg){
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private View rootView;

    //界面初始化异步操作
    private MyHomeLoadingAsyncTask myHomeLoadingAsyncTask;

    private HSVAdapter hsvAdapter;
    private HSVLinearLayout mGalleryLayoutOne = null;
    private HSVLinearLayout mGalleryLayoutTwo = null;
    private HSVLinearLayout mGalleryLayoutThree = null;

    private TextView moreActivity_tv,moreFeedback_tv,moreSpread_tv;
    private EditText searchEditText;
    private ImageView searchImageView;
    //轮播图更新
    private IntentFilter intentFilter = null;
    private BroadcastReceiver receiver = null;
    private int nCount = 0;
    // pic in the drawable
    private int[] images = {
            R.mipmap.family,R.mipmap.family,R.mipmap.family
    };

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;

    /**
     * 通过广播来实现轮播的自动刷新
     */
    class UpdateImageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppConstant.UPDATE_IMAGE_ACTION)) {

                int index = intent.getIntExtra("index", Integer.MAX_VALUE);
                System.out.println("UpdateImageReceiver----" + index);
            }
        }

    }

    //正在加载异步任务相关
    private RelativeLayout LoadingFace;
    private ProgressBar progressBar;
    private TextView loadingText;
    private List<HomeInitialItem> dataLists= new ArrayList<>();
    private List<HomeInitialAsyncTask> list= new ArrayList<>();

    /**
     * 进行异步显示图片到控件和信息的更新
     */
    public static class MyHandler extends Handler {
        WeakReference mFragment;
        MyHandler(UserHomeFragment fragment) {
            mFragment = new WeakReference(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            UserHomeFragment theFragment= (UserHomeFragment) mFragment.get();
            switch (msg.what) {
                case 0:
                    //启动异步任务（UI初始化）
                    //检查网络是否连接
                    if (NetWorkUtil.isNetworkConnected(theFragment.getActivity())) {
                        theFragment.list.get((theFragment.list.size()-1)).execute(HttpPath.getHomeInitialPath());
                    } else {
                        //提示无网络连接
                        theFragment.LoadingFace.setBackgroundColor(Color.WHITE);
                        theFragment.loadingText.setText("网络无连接");
                        theFragment.loadingText.setTextColor(Color.RED);
                        theFragment.progressBar.setVisibility(View.INVISIBLE);
                    }
                    theFragment.addNewAsyncTask();
                    break;
                case 1:
                    Logger.d("datalist Size --- " + theFragment.dataLists.size() + "");
                    theFragment.myHomeLoadingAsyncTask = new
                            MyHomeLoadingAsyncTask(theFragment.getActivity(),theFragment.rootView,
                            theFragment.dataLists);
                    theFragment.myHomeLoadingAsyncTask.execute();
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_user_myhome, container,false);
            initView(rootView);
            startFirstAsyncTask();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * 即时销毁，防止异常
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(myHomeLoadingAsyncTask!=null) {
            myHomeLoadingAsyncTask.quitBannerTask();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (receiver == null) {
            receiver = new UpdateImageReceiver();
        }
        //注册广播接收器只接收特定消息
        getActivity().registerReceiver(receiver, getIntentFilter());
    }

    /**
     * 创建一个消息过滤器
     * 只接收特定标签的广播信息
     * @return
     */
    private IntentFilter getIntentFilter() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(AppConstant.UPDATE_IMAGE_ACTION);
        }
        return intentFilter;
    }


    private void initView(View view) {

        //水平的ScrollView
        mGalleryLayoutOne = (HSVLinearLayout) view.findViewById(R.id.my_gallery_one);
        mGalleryLayoutTwo= (HSVLinearLayout) view.findViewById(R.id.my_gallery_two);
        mGalleryLayoutThree = (HSVLinearLayout) view.findViewById(R.id.my_gallery_three);

        moreActivity_tv = (TextView) view.findViewById(R.id.more_activity);
        moreFeedback_tv = (TextView) view.findViewById(R.id.more_feedback);
        moreSpread_tv = (TextView) view.findViewById(R.id.more_spread);

        searchEditText = (EditText) view.findViewById(R.id.et_search);
        searchImageView = (ImageView) view.findViewById(R.id.iv_search);

        //异步处理任务相关
        LoadingFace = (RelativeLayout) view.findViewById(R.id.loading_face);
        loadingText = (TextView) view.findViewById(R.id.tv_progressBar);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final View v= view;

        initAdapterData();

        moreActivity_tv.setOnClickListener(this);
        moreFeedback_tv.setOnClickListener(this);
        moreSpread_tv.setOnClickListener(this);
        searchEditText.setOnClickListener(this);
        searchImageView.setOnClickListener(this);

    }

    private void initAdapterData() {
        hsvAdapter = new HSVAdapter(UserHomeFragment.this.getContext());
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            // map.put("image", getResources().getDrawable(images[i]));
            map.put("index", (i+1));//代表第张图片，而数组从零开始计数
            hsvAdapter.addObjectItem(map);
        }
        mGalleryLayoutOne.setAdapter(hsvAdapter,TYPE_ONE,170,100);//第三个参数和第四个参数分别为宽和高
        mGalleryLayoutTwo.setAdapter(hsvAdapter,TYPE_TWO,170,100);
        mGalleryLayoutThree.setAdapter(hsvAdapter,TYPE_THREE,170,100);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.more_activity:
                IntentSkipUtil.skipToNextActivity(UserHomeFragment.this.getActivity(),AllActivityActivity.class);
                break;
            case R.id.more_feedback:
                IntentSkipUtil.skipToNextActivity(UserHomeFragment.this.getActivity(),FeedbackActivity.class);
                break;
            case R.id.more_spread:
                IntentSkipUtil.skipToNextActivity(UserHomeFragment.this.getActivity(),SpreadActivity.class);
                break;
            case R.id.et_search:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserSearchActivity.class);
                break;
            case R.id.iv_search:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserSearchActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 开启第一个异步任务
     */
    private void startFirstAsyncTask() {
        addNewAsyncTask();
        handler.sendEmptyMessage(0);
    }
    /**
     * 添加新的异步任务
     */
    private void addNewAsyncTask() {
        list.clear();
        list.add(new HomeInitialAsyncTask());
    }

    /**
     * 将URL对应的json格式转换成我们的Bean
     * @param param
     * @return
     */
    private List<HomeInitialItem> myGetJsonData(String param) {
        //通过InputStream去和获取Json数据
        InputStream is = null;
        //可以写成URLConnection urlConnection = null;
        HttpURLConnection urlConnection = null;
        try {
            JSONObject sendJson = new JSONObject();
            sendJson.put("user_id", CookiesSaveUtil.getUserId(getActivity()));
            String sendJsonString = sendJson.toString();
            Logger.d(sendJsonString);
            //使用工具将其封装成一个类的对象
            Gson gson = new Gson();

            urlConnection = (HttpURLConnection) new URL(param).openConnection();
            //设置相应请求头
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            // 设置允许输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            //设置维持长链接
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            //设置文件字符集
            urlConnection.setRequestProperty("Charset", "UTF-8");
            // 设置contentType
            urlConnection.setRequestProperty("Content-Type","application/json");
            //转换为字节数组
            byte[] data = (sendJson.toString()).getBytes();
            //设置文件长度
            urlConnection.setRequestProperty("Content-length", String.valueOf(data.length));
            // 开始连接请求
            urlConnection.connect();
            OutputStream out = urlConnection.getOutputStream();
            // 写入请求的字符串，转换成字节流传输
            out.write(data);
            out.flush();
            out.close();


            //如果请求码不是200则退出,如果是20则开始接受数据
            if(HttpURLConnection.HTTP_OK != urlConnection.getResponseCode()) {
                return null;
            }
            is = urlConnection.getInputStream();
            //is = new URL(param).openStream();
            //先将下面这个服务器返回的字符串转换成Json数组
            String receiverJsonString = readStream(is);
            Logger.d(receiverJsonString);
            HomeInitialResult result = gson.fromJson(receiverJsonString,HomeInitialResult.class);
            JsonArray receiveJson = result.getData();

            int size = receiveJson.size();
            Logger.d(receiveJson.toString()+"\nJsonArraySize --- " + size);
            //JsonObject 进一步拆分
            HomeInitialItem viewModel = null;
            for(int i=0; i<size; i++) {
                //将整一个json字符串转换成json对象
                viewModel = new HomeInitialItem();
                viewModel.setPic_dir(receiveJson.get(i).getAsJsonObject().get("pic_dir").getAsString());
                viewModel.setAct_id(receiveJson.get(i).getAsJsonObject().get("act_id").getAsInt());
                viewModel.setAct_title(receiveJson.get(i).getAsJsonObject().get("act_title").getAsString());
                dataLists.add(viewModel);
            }

        }  catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataLists;
    }

    /**
     * 通过InputStream解析网页返回的数据
     * @param is
     * @return
     */
    private String readStream(InputStream is) {
        InputStreamReader isr;
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
    //UserInfoViewModel-> List -> Adapter -> ListView
    private class HomeInitialAsyncTask extends AsyncTask<String,Void,List<HomeInitialItem>> {

        @Override
        protected List<HomeInitialItem> doInBackground(String... params) {
            List<HomeInitialItem> list;
            list = myGetJsonData(params[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<HomeInitialItem> viewModels){
            super.onPostExecute(viewModels);
            if(viewModels != null) {
                LoadingFace.setVisibility(View.GONE);

                int size = viewModels.size();
                for(int i =0;i<size;i++) {
                    HomeInitialItem viewModel = viewModels.get(i);
                    //执行相关的初始化到界面的逻辑操作
                    Logger.d("id---" + viewModel.getAct_id() + ", title---" + viewModel.getAct_title() +
                            ", picUrl---" + viewModel.getPic_dir());
                }

                handler.sendEmptyMessage(1);
            } else {
                //提示服务器出现错误
                LoadingFace.setBackgroundColor(Color.WHITE);
                loadingText.setText("服务器出现错误");
                loadingText.setTextColor(Color.RED);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 任务预处理
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingFace.setVisibility(View.VISIBLE);
        }

        /**
         * 异步任务的取消
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}


