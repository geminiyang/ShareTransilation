package com.yqq.openglestest;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //下面这个方法调用将暂停渲染线程。如果你的OpenGL应用程序是内存敏感，你应该考虑重新分配对象将耗费大量内存
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //下面这个方法调用将恢复继续一个暂停的渲染线程。
        mGLView.onResume();
    }
}
