package com.idear.move.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.idear.move.Adapter.ExpressionAdapter;
import com.idear.move.Adapter.ExpressionPagerAdapter;
import com.idear.move.Adapter.MsgAdapter;
import com.idear.move.POJO.Msg;
import com.idear.move.R;
import com.idear.move.myWidget.ExpandGridView;
import com.idear.move.myWidget.SmileUtils;
import com.idear.move.util.KeyBoardUtils;
import com.yqq.swipebackhelper.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UserChatActivity extends BaseActivity {
    //聊天列表
    private ListView msgListView;
    //输入框
    private EditText inputText;
    //发送按钮
    private Button send;
    //消息适配器
    private MsgAdapter adapter;
    //聊天记录数据源
    private List<Msg> msgList = new ArrayList<Msg>();//数据源
    private ImageView iv_back;//返回按钮
    //表情viewPager
    private ViewPager expressionViewpager;
    private ImageView iv_emoji;
    private LinearLayout emoji_group;
    private List<String> resList;//存储图标名字的list列表
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        initMsg();
        initView();
        initEvent();
        initToolBar();
    }

    private void initToolBar() {
        iv_back = (ImageView) findViewById(R.id.ic_arrow_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);

    }



    private void initView() {
        adapter = new MsgAdapter(UserChatActivity.this, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        emoji_group = (LinearLayout) findViewById(R.id.emoji_group);
    }

    private void initEvent() {
        inputText.setOnKeyListener(onKeyListener);
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    if (emoji_group.getVisibility()==View.VISIBLE) {
                        emoji_group.setVisibility(View.GONE);
                    }
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendMessage();
            }

        });
        iv_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText.clearFocus();
                if (emoji_group.getVisibility()==View.GONE) {
                    iv_emoji.setImageResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                    emoji_group.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(UserChatActivity.this,v);
                } else {
                    iv_emoji.setImageResource(R.mipmap.emoji);
                    emoji_group.setVisibility(View.GONE);
                }

            }
        });

        initData();


    }

    /**
     * 初始化表情界面
     */
    private void initData() {
        // 表情list
        resList = getExpressionRes(60);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
    }

    /**
     * 初始化文件名数组
     * @param getSum
     * @return
     */
    private List<String> getExpressionRes(int getSum) {
        List<String> res = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "f" + x;
            res.add(filename);
        }
        return res;

    }

    /**
     * 获取表情的gridView的子view
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.layout_expression_gridview, null);
        ExpandGridView expandGridView = (ExpandGridView) view.findViewById(R.id.gridView);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> listOne = resList.subList(0, 20);
            list.addAll(listOne);
        } else if (i == 2) {
            list.addAll(resList.subList(20, 40));
        } else if(i == 3) {
            list.addAll(resList.subList(40, resList.size()));
        }
        //第21个图标默认为删除键
        list.add("delete_expression");

        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        expandGridView.setAdapter(expressionAdapter);
        expandGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //文件名与position相对应
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (!filename.equals("delete_expression")) {
                        // 不是删除键，则显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        @SuppressWarnings("rawtypes")
                        Class clz = Class.forName("com.idear.move.myWidget.SmileUtils");
                        Field field = clz.getField(filename);
                        String inputContent = inputText.getText().toString();
                        int index = Math.max(inputText.getSelectionStart(), 0);//获取开始光标位置
                        StringBuilder sBuilder = new StringBuilder(inputContent);
                        Spannable insertEmotion = SmileUtils.getSmiledText(
                                UserChatActivity.this, (String) field.get(null));
                        sBuilder.insert(index, insertEmotion);

                        SpannableString txt = new SpannableString(sBuilder.toString());
                        boolean flag = SmileUtils.addSmiles(UserChatActivity.this,txt);
                        if(flag) {
                            inputText.setText(txt);
                        } else  {
                            inputText.setText(sBuilder.toString());
                        }
                        inputText.setSelection(index + insertEmotion.length());//设置光标新位置
                    } else {
                        // 删除文字或者表情
                        if (!TextUtils.isEmpty(inputText.getText())) {
                            int selectionStart = inputText.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = inputText.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                // 获取最后一个表情的位置，如果不存在则返回-1
                                int i = tempStr.lastIndexOf("[");
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString())) {
                                        //如果存在该键值，则将其成对删除
                                        inputText.getEditableText().delete(i, selectionStart);
                                    } else {
                                        //清除一个字符
                                        inputText.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                } else {
                                    //清楚一个字符
                                    inputText.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }
                } catch (Exception e) {

                }

            }
        });
        return view;
    }


    private void initMsg() {
        //模拟初始化数据源
        Msg msg1 = new Msg("I miss you!",Msg.RECEIVED);
        msgList.add(msg1);

        Msg msg2 = new Msg("I miss you,too!",Msg.SENT);
        msgList.add(msg2);

        Msg msg3 = new Msg("I will come back soon!",Msg.RECEIVED);
        msgList.add(msg3);
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                sendMessage();
                return true;
            } else if(keyCode == KeyEvent.ACTION_DOWN) {
                if (emoji_group.getVisibility()==View.VISIBLE) {
                    emoji_group.setVisibility(View.GONE);
                }
                return true;
            }
            return false;
        }
    };

    /**
     * 发送消息具体操作
     */
    private void sendMessage() {
        String content = inputText.getText().toString();
        if(!TextUtils.isEmpty(content)){
            Msg msg = new Msg(content, Msg.SENT);
            msgList.add(msg);
            adapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
            msgListView.setSelection(msgListView.getCount()-1);//将ListView定位到最后一行or msgList.size()
            inputText.setText("");//清空输入框的内容
        }
    }
}
