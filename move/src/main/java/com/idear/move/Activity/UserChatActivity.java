package com.idear.move.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.idear.move.Adapter.MsgAdapter;
import com.idear.move.POJO.Msg;
import com.idear.move.R;
import com.yqq.swipebackhelper.BaseActivity;

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

    private List<Msg> msgList = new ArrayList<Msg>();//数据源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        initMsg();
        init();

    }

    private void init() {
        adapter = new MsgAdapter(UserChatActivity.this, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!TextUtils.isEmpty(content)){
                    Msg msg = new Msg(content, Msg.SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                    msgListView.setSelection(msgListView.getCount()-1);//将ListView定位到最后一行or msgList.size()
                    inputText.setText("");//清空输入框的内容
                }
            }

        });
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
}
