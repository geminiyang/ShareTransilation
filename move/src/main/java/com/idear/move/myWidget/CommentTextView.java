package com.idear.move.myWidget;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idear.move.R;

/**
 * 作者:geminiyang on 2017/6/11.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CommentTextView extends LinearLayout {

    private TextView nameLabel,commentContent;
    private String nameLabelString,commentContentString;
    private LinearLayout rootLayout;

    public CommentTextView(Context context) {
        this(context, null);
    }

    public CommentTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommentTextView);
        nameLabelString = ta.getString(R.styleable.CommentTextView_nameLabel);
        commentContentString = ta.getString(R.styleable.CommentTextView_commentContent);
        ta.recycle();
        // 初始化
        init(context);

    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_comment_textview, this);//设置布局文件
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        nameLabel = (TextView) findViewById(R.id.nameLabel);
        commentContent = (TextView) findViewById(R.id.commentContent);
        nameLabel.setText(nameLabelString);
        commentContent.setText(commentContentString);
    }

    public void setNameLabelText(final String nameLabelStr) {
        nameLabel.setText(nameLabelStr);
//        this.nameLabel.post(new Runnable() {
//            @Override
//            public void run() {
//                nameLabel.setText(nameLabelStr);
//            }
//        });
    }

    public void setCommentContentText(final String commentContentStr) {
        commentContent.setText(commentContentStr);
//        this.commentContent.post(new Runnable() {
//            @Override
//            public void run() {
//                commentContent.setText(commentContentStr);
//            }
//        });
    }
}
