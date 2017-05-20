package com.yqq.horizonalscrollviewtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private Context mContext;
    private int [] mPhotosIntArray;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mGalleryLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        initData();
    }

    private void initView(){
        mContext=this;
        mGalleryLinearLayout=(LinearLayout) findViewById(R.id.galleryLinearLayout);
        mLayoutInflater=LayoutInflater.from(mContext);
    }
    private void initData(){
        mPhotosIntArray=new int[]{
                R.mipmap.rina1,R.mipmap.rina2,R.mipmap.rina3,
                R.mipmap.rina4,R.mipmap.rina5,R.mipmap.rina6,
                R.mipmap.rina7,R.mipmap.rina8,
        };
        View itemView = null;//布局主视图
        ImageView imageView = null;
        TextView textView;
        for (int i = 0; i < mPhotosIntArray.length; i++) {
            itemView = mLayoutInflater.inflate(R.layout.gallery_item, null);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView.setImageResource(mPhotosIntArray[i]);
            textView.setText("This is "+(i+1));
            //通过addView添加在视图中
            mGalleryLinearLayout.addView(itemView);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.next:
                Intent it = new Intent(SecondActivity.this,ThreeActivity.class);
                startActivity(it);
                break;
        }
    }
}
