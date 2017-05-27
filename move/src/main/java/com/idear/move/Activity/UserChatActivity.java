package com.idear.move.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.idear.move.R;
import com.yqq.swipebackhelper.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class UserChatActivity extends BaseActivity {

    private ListView msgListView;

    private EditText inputText;

    private Button send;

    private MsgAdapter adapter;

    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        initMsg();
        init();

    }

    private void init() {
        adapter = new MsgAdapter(UserChatActivity.this, R.layout.chat_item_layout, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content, Msg.SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                    msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                    inputText.setText("");//清空输入框的内容
                }
            }

        });
    }

    private void initMsg() {
        Msg msg1 = new Msg("I miss you!",Msg.RECEIVED);
        msgList.add(msg1);

        Msg msg2 = new Msg("I miss you,too!",Msg.SENT);
        msgList.add(msg2);

        Msg msg3 = new Msg("I will come back soon!",Msg.RECEIVED);
        msgList.add(msg3);

    }


    public class Msg{

        public static final int RECEIVED = 0;//收到一条消息

        public static final int SENT = 1;//发出一条消息

        private String  content;//消息的内容

        private int type;//消息的类型

        public  Msg(String content,int type){
            this.content = content;
            this.type = type;
        }

        public String getContent(){
            return content;
        }

        public int getType(){
            return type;
        }
    }

    public class MsgAdapter extends ArrayAdapter<Msg> {
        private int resourceId;

        public MsgAdapter(Context context, int textViewresourceId, List<Msg> objects) {
            super(context, textViewresourceId, objects);
            resourceId = textViewresourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Msg msg = getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);
                viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.right_Layout);
                viewHolder.leftMsg = (TextView)view.findViewById(R.id.left_msg);
                viewHolder.rightMsg = (TextView)view.findViewById(R.id.right_msg);
                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            if(msg.getType()==Msg.RECEIVED){
                //如果是收到的消息，则显示左边消息布局，将右边消息布局隐藏
                viewHolder.leftLayout.setVisibility(View.VISIBLE);
                viewHolder.rightLayout.setVisibility(View.GONE);
                viewHolder.leftMsg.setText(msg.getContent());
            }else if(msg.getType()==Msg.SENT){
                //如果是发出去的消息，显示右边布局的消息布局，将左边的消息布局隐藏
                viewHolder.rightLayout.setVisibility(View.VISIBLE);
                viewHolder.leftLayout.setVisibility(View.GONE);
                viewHolder.rightMsg.setText(msg.getContent());
            }
            return view;
        }

        class ViewHolder{
            LinearLayout leftLayout;
            LinearLayout rightLayout;
            TextView leftMsg;
            TextView rightMsg;
        }
    }

}
