package com.yqq.openglestest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by user on 2017/4/17.
 */

public class BlackBackGroundActivity extends Activity {

    private final String TAG = "BlackBackGroundActivity";

    private GLSurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为无标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new GLSurfaceViewRender());
        this.setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onStop();
        glSurfaceView.onPause();
    }

    class GLSurfaceViewRender implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.i(TAG, "onSurfaceCreated");

            // 设置背景颜色
            gl.glClearColor(0.0f, 0f, 0f, 0.5f);
            // 启用顶点数组（否则glDrawArrays不起作用）
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.i(TAG, "onSurfaceChanged");
            // 设置输出屏幕大小
            gl.glViewport(0, 0, width, height);
        }
        @Override
        public void onDrawFrame(GL10 gl) {
            Log.i(TAG, "onDrawFrame");
            // 清除屏幕和深度缓存(如果不调用该代码, 将不显示glClearColor设置的颜色)
            // 同样如果将该代码放到 onSurfaceCreated 中屏幕会一直闪动
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glClearColor(0,0,0,0);
        }
    }
}
