package com.idear.move.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.idear.move.R;
import com.idear.move.Service.ActivityManager;
import com.idear.move.util.IntentSkipUtil;
import com.yqq.myutillibrary.BitmapToRoundUtil;
import com.yqq.myutillibrary.TranslucentStatusSetting;
import com.yqq.swipebackhelper.BaseActivity;

import java.lang.reflect.Field;

public class LoginActivity extends BaseActivity {

    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ImageButton wechat,qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusSetting.setTranslucentStatusSetting(this, getResources().getColor(R.color.title_bar_blue));
        setContentView(R.layout.activity_login);

        init();
        initEvent();

    }

    private void initEvent() {
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这儿判断操作，如果输入错误可以给用户提示
                if(s.length()>5||s.length()==0){
                    usernameWrapper.setErrorEnabled(false);
                } else {
                    usernameWrapper.setErrorEnabled(true);
                    usernameWrapper.setError("用户名不能小于6位");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这儿判断操作，如果输入错误可以给用户提示
                if(s.length()>5||s.length()==0) {
                    passwordWrapper.setErrorEnabled(false);
                } else {
                    passwordWrapper.setErrorEnabled(true);
                    passwordWrapper.setError("密码不能小于6位");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       // setIconRound();

    }

    /**
     * 此方法是代码方法将非圆形图片裁剪成圆形，src和background的绘制顺序决定
     */
    private void setIconRound() {
        Bitmap bitmapOne = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);//默认为黑色
        bitmapOne.eraseColor(Color.parseColor("#00aa00"));
        Bitmap bitmapTwo = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);//默认为黑色
        bitmapTwo.eraseColor(Color.parseColor("#00aaff"));//填充颜色


        RoundedBitmapDrawable roundQQ = BitmapToRoundUtil.makeRoundBitmap(getResources(),bitmapTwo);
        RoundedBitmapDrawable roundWechat = BitmapToRoundUtil.makeRoundBitmap(getResources(),bitmapOne);
        qq.setBackground(roundQQ);
        wechat.setBackground(roundWechat);
    }

    //初始化操作
    private void init() {

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        usernameEditText = usernameWrapper.getEditText();
        passwordEditText = passwordWrapper.getEditText();

        qq = (ImageButton) findViewById(R.id.qq);
        wechat = (ImageButton) findViewById(R.id.wechat);
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.allLayout:
               hideKeyboard();
                break;
            case R.id.btn:

                //调用了传入非null参数的setError，那么setErrorEnabled(true)将自动被调用。
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(username)&&username.length()<5) {
                    usernameWrapper.setError("用户名不能小于6位!");
                    if (TextUtils.isEmpty(password)&&password.length()<5) {
                        passwordWrapper.setError("密码长度不能小于6位!");
                    }
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    //执行登陆操作
                    //doLogin();
                }
                break;
            case R.id.start:
                IntentSkipUtil.skipToNextActivity(this,RegisterActivity.class);
                break;
            case R.id.end:
                IntentSkipUtil.skipToNextActivity(this,ForgetPasswordActivity.class);

                break;
            case R.id.wechat:
                Toast.makeText(this,"第三方接入登陆wechat",Toast.LENGTH_SHORT).show();
                break;
            case R.id.qq:
                Toast.makeText(this,"第三方接入登陆qq",Toast.LENGTH_SHORT).show();
                IntentSkipUtil.skipToNextActivity(this,MainUIActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 通过反射设置错误文本的颜色
     * @param textInputLayout TextInputLayout布局控件
     * @param color 需要设置的颜色
     */
    public static void setErrorTextColor(TextInputLayout textInputLayout, int color) {
        try {
            Field fErrorView = TextInputLayout.class.getDeclaredField("mErrorView");
            fErrorView.setAccessible(true);
            TextView mErrorView = (TextView) fErrorView.get(textInputLayout);
            Field fCurTextColor = TextView.class.getDeclaredField("mCurTextColor");
            fCurTextColor.setAccessible(true);
            fCurTextColor.set(mErrorView, color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法用来隐藏键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
