package com.idear.move.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.idear.move.R;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

public class FriendAddActivity extends BaseActivity implements View.OnClickListener{

    private EditText editText;
    private FrameLayout fl;
    private View view;
    private ImageButton searchClose;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.title_bar_blue));
        setContentView(R.layout.activity_friend_add);

        initView();
        initEvent();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edit_text);

        fl = (FrameLayout) findViewById(R.id.fl);
        view = findViewById(R.id.divider);

        searchClose = (ImageButton) findViewById(R.id.ib_close);
        back = (Button) findViewById(R.id.ic_arrow_back);
    }

    private void initEvent() {
        editText.setOnClickListener(this);
        searchClose.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_text:
                if(view.getVisibility() == View.GONE&&fl.getVisibility() == View.GONE) {
                    fl.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ib_close:
                if(view.getVisibility() == View.VISIBLE&&fl.getVisibility() == View.VISIBLE) {
                    fl.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    editText.setText("");
                    editText.setHint("  搜索添加或关注、好友、商户");
                }
                break;
            case R.id.ic_arrow_back:
                finish();
                break;
        }
    }
}
