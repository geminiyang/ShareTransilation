package com.idear.move.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.idear.move.Activity.FansAndAttentionActivity;
import com.idear.move.Activity.MyActivityActivity;
import com.idear.move.Activity.MyDynamicsActivity;
import com.idear.move.Activity.MyFavoritesActivity;
import com.idear.move.Activity.UserDetailInformationActivity;
import com.idear.move.Activity.UserSearchActivity;
import com.idear.move.Activity.UserSettingActivity;
import com.idear.move.Adapter.SearchItemAdapter;
import com.idear.move.Helper.ImgSQLiteOpenHelper;
import com.idear.move.Helper.RecordSQLiteOpenHelper;
import com.idear.move.POJO.FansViewModel;
import com.idear.move.R;
import com.idear.move.myWidget.RoundImageViewByXfermode;
import com.idear.move.network.HttpPath;
import com.idear.move.network.ViewModelResult;
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
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by user on 2017/4/21.
 */

public class UserInformationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG = "arg";
    //关注和粉丝接口相关
    private RoundImageViewByXfermode userImg;
    private TextView nameText,attentionQuantity,fansNum;
    //正在加载异步任务相关
    private RelativeLayout LoadingFace;
    private ProgressBar progressBar;
    private TextView loadingText;
    private List<UserInfoAsyncTask> list= new ArrayList<>();

    //ToolBar
    private ImageView iv_setting;

    private ScrollView myScrollView;
    private RelativeLayout rllayout;//含有图片的那个相对布局，顶部控件

    private ImageView ivToUserInfo,ivToDynamics,ivToMyFavorites;

    private GridView gridView;//网格视图
    private List<Map<String, Object>> data_list;//数据源
    private SimpleAdapter simple_adapter;//适配器
    private int[] icon = { R.mipmap.takepart, R.mipmap.trends,R.mipmap.takepart, R.mipmap.trends,
            R.mipmap.takepart};

    private String[] iconName={ "未通过", "审核中","进行中","待反馈","已结束"};

    public static UserInformationFragment newInstance(String arg){
        UserInformationFragment fragment = new UserInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private View rootView;

    /**
     * 进行异步显示图片到控件和信息的更新
     */
    private static class MyHandler extends Handler {
        WeakReference mFragment;
        MyHandler(UserInformationFragment fragment) {
            mFragment = new WeakReference(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            UserInformationFragment theFragment= (UserInformationFragment) mFragment.get();
            switch (msg.what) {
                case 0:
                    //启动异步任务（UI初始化）
                    //检查网络是否连接
                    if (NetWorkUtil.isNetworkConnected(theFragment.getActivity())) {
                        theFragment.list.get((theFragment.list.size()-1)).execute(HttpPath.getFansInfoPath());
                    } else {
                        //提示无网络连接
                        theFragment.LoadingFace.setBackgroundColor(Color.WHITE);
                        theFragment.loadingText.setText("网络无连接");
                        theFragment.loadingText.setTextColor(Color.RED);
                        theFragment.progressBar.setVisibility(View.INVISIBLE);
                    }
                    theFragment.addNewAsyncTask();
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    /*数据库变量*/
    private ImgSQLiteOpenHelper helper ;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null) {
            rootView = inflater.inflate(R.layout.fragment_userinformation,container,false);
            init(rootView);
            initGirdView(rootView);
            startFirstAsyncTask();
            //实例化数据库SQLiteOpenHelper子类对象
            helper = new ImgSQLiteOpenHelper(this.getContext());
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    /*插入数据*/
    private void insertData(int userId,String picUrl) {
        Logger.d("insert---" + userId + ":" + picUrl);
        db = helper.getWritableDatabase();
        db.execSQL("insert into imgRecords values(" + userId + ",'" + picUrl + "')");
        db.close();
    }

    /*更新数据*/
    private void updateData(int userId,String picUrl) {
        Logger.d("update---" + userId + ":" + picUrl);
        db = helper.getWritableDatabase();
        db.execSQL("update imgRecords set picUrl ='" + picUrl + "' where id =" + userId);
        db.close();
    }

    /*查询id对应的URL*/
    private String queryData(int id) {
        Logger.d("query---" + id);
        //模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id ,picUrl from imgRecords where id = " + id, null);
        ArrayList<String> list = new ArrayList<>();

        //遍历Cursor
        while(cursor.moveToNext()){
            list.add(cursor.getString(1));//对应picUrl字段
        }
        return list.get(0);
    }

    /*检查数据库中是否已经有该条记录*/
    private boolean hasData(int tempId) {
        Logger.d("hasData---");
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id from imgRecords where id =" + tempId, null);
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /*清空数据*/
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from imgRecords");
        db.close();
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
        list.add(new UserInfoAsyncTask());
    }

    /**
     * 初始化控件
     */
    private void init(View view) {
        nameText = (TextView) view.findViewById(R.id.name_text);
        attentionQuantity = (TextView) view.findViewById(R.id.attentionQuantity);
        fansNum = (TextView) view.findViewById(R.id.fansNum);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        userImg = (RoundImageViewByXfermode) view.findViewById(R.id.user_img);
        //异步处理任务相关
        LoadingFace = (RelativeLayout) view.findViewById(R.id.loading_face);
        loadingText = (TextView) view.findViewById(R.id.tv_progressBar);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        rllayout = (RelativeLayout)view.findViewById(R.id.rllayout);

        myScrollView = (ScrollView)view.findViewById(R.id.myScrollView);

        ivToUserInfo = (ImageView) view.findViewById(R.id.to_user_info);
        ivToDynamics = (ImageView) view.findViewById(R.id.to_dynamics);
        ivToMyFavorites = (ImageView) view.findViewById(R.id.to_my_favorites);

        //三句代码使界面打开时候自定义ScrollView下面的EditText获取焦点的事件不再发生
        myScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        myScrollView.setFocusable(true);
        myScrollView.setFocusableInTouchMode(true);

        initEvent();
    }

    //初始化监听器
    private void initEvent() {
        ivToUserInfo.setOnClickListener(this);
        ivToDynamics.setOnClickListener(this);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSkipUtil.skipToNextActivity(getActivity(),UserSettingActivity.class);
            }
        });
        ivToMyFavorites.setOnClickListener(this);
        attentionQuantity.setOnClickListener(this);
        fansNum.setOnClickListener(this);

    }


    private void initGirdView(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_view);
        data_list = new ArrayList<Map<String,Object>>();
        data_list = get_data();//获取数据
        //新建适配器
        String [] from ={"image","text"};
        final int [] to = {R.id.image,R.id.text};
        simple_adapter = new SimpleAdapter(view.getContext(), data_list, R.layout.user_info_setting_item, from, to);
        //配置适配器
        gridView.setAdapter(simple_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item= (HashMap<String, Object>) parent.getItemAtPosition(position);
                IntentSkipUtil.skipToNextActivityWithBundle(getActivity(),
                        MyActivityActivity.class,"select_tab",(String)item.get("text"));
            }
        });
    }



    private List<Map<String, Object>> get_data() {
        //icon和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.to_user_info:
                IntentSkipUtil.skipToNextActivity(getActivity(),UserDetailInformationActivity.class);
                break;
            case R.id.to_dynamics:
                IntentSkipUtil.skipToNextActivity(getActivity(),MyDynamicsActivity.class);
                break;
            case R.id.to_my_favorites:
                IntentSkipUtil.skipToNextActivity(getActivity(),MyFavoritesActivity.class);
                break;
            case R.id.fansNum:
            case R.id.attentionQuantity:
                IntentSkipUtil.skipToNextActivity(getActivity(),FansAndAttentionActivity.class);
            default:
                break;
        }
    }

    /**
     * 将URL对应的json格式转换成我们的Bean
     * @param param
     * @return
     */
    private List<FansViewModel> myGetJsonData(String param) {
        List<FansViewModel> viewModels = new ArrayList<>();
        //通过InputStream去和获取Json数据
        InputStream is = null;
        //可以写成URLConnection urlConnection = null;
        HttpURLConnection urlConnection = null;
        try {
            JSONObject sendJson = new JSONObject();
            sendJson.put("user_id", CookiesSaveUtil.getUserId(UserInformationFragment.this.getActivity()));
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
            //先将下面这个返回字符串转换成对象
            String receiverJsonString = readStream(is);
            Logger.d(receiverJsonString);
            ViewModelResult result = gson.fromJson(receiverJsonString,ViewModelResult.class);
            JsonObject receiveJson = result.getData();
            Logger.d(receiveJson.toString());
            //JsonObject 进一步拆分
            FansViewModel viewModel;
            //将整一个json字符串转换成json对象
            viewModel = new FansViewModel();
            viewModel.setPic_dir(receiveJson.get("pic_dir").getAsString());
            viewModel.setFensi(receiveJson.get("fensi").getAsInt());
            viewModel.setGuanzhu(receiveJson.get("guanzhu").getAsInt());
            viewModel.setNickname(receiveJson.get("username").getAsString());
            viewModels.add(viewModel);

        }  catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewModels;
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
    //UserInfoViewModel-> List -> Adapter -> ListView
    private class UserInfoAsyncTask extends AsyncTask<String,Void,List<FansViewModel>> {

        @Override
        protected List<FansViewModel> doInBackground(String... params) {
            List<FansViewModel> list = null;
            list = myGetJsonData(params[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<FansViewModel> viewModels){
            super.onPostExecute(viewModels);
            if(viewModels != null) {
                LoadingFace.setVisibility(View.GONE);

                FansViewModel viewModel = viewModels.get(0);
                //显示一个ProgressBar
                attentionQuantity.setText(viewModel.getGuanzhu() + "");
                fansNum.setText(viewModel.getFensi() + "");
                nameText.setText(viewModel.getNickname());
                String urlStr = viewModel.getPic_dir();//图像路径
                int userId = Integer.parseInt(CookiesSaveUtil.getUserId(
                        UserInformationFragment.this.getContext()));//用户Id

                if(!urlStr.contentEquals("0")) {
                    //保存到数据库的操作
                    if(!hasData(userId)) {
                        insertData(userId,urlStr);
                    } else {
                        updateData(userId,urlStr);
                    }
                    Glide.with(UserInformationFragment.this).load("http://idear.party/" + urlStr).
                            error(R.mipmap.paintbox).into(userImg);
                } else {
                    Glide.with(UserInformationFragment.this).load("http://idear.party/" + urlStr).
                            error(R.mipmap.paintbox).into(userImg);
                }
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
