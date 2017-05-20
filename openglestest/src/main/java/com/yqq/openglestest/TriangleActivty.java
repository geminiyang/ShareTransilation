package com.yqq.openglestest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleActivty extends AppCompatActivity {

    private GLSurfaceView mGLView;
    private GLSurfaceView.Renderer myRender;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //设置为无标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
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

    class MyGLSurfaceView extends GLSurfaceView
    {

        public MyGLSurfaceView(Context context)
        {
            super(context);

            try
            {
                // Create an OpenGL ES 1.0/1.1 context
                setEGLContextClientVersion(1);


                // Set the Renderer for drawing on the GLSurfaceView
                myRender = new MyRenderer();
                setRenderer(myRender);

                // Render the view only when there is a change in the drawing data
                //设置绘制侧略
                setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

                //设置像素格式，默认为PixelFormat.RGB_565
                getHolder().setFormat(PixelFormat.TRANSLUCENT);

                // 注意上面语句的顺序，反了可能会出错

            }
            catch (Exception e)
            {
                e.printStackTrace();

            }

        }

        //添加事件处理
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.ACTION_DOWN) {
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getRootView(),"test",Snackbar.LENGTH_SHORT)
                                .setAction("Action",null).show();
                    }
                });
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    public class MyRenderer implements GLSurfaceView.Renderer
    {

        public void onSurfaceCreated(GL10 unused, EGLConfig config)
        {
            // 设置背景色为黑色(rgba).
            unused.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
            //启用定点数组
            unused.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        }

        public void onDrawFrame(GL10 unused)
        {
            // Redraw background color
            //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            // 清除屏幕和深度缓存(如果不调用该代码, 将不显示glClearColor设置的颜色)
            // 同样如果将该代码放到 onSurfaceCreated 中屏幕会一直闪动
            unused.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            unused.glColor4f(0.6f, 0.4f, 0.0f, 1.0f);

            //重置当前的模型视图矩阵
            unused.glLoadIdentity();
            // 平移 (矩阵相乘)
            unused.glTranslatef(-0.2f, 0.3f, 0f);

            //生成顶点数据
            FloatBuffer mTriangleBuffer = BufferUtil.floatToBuffer(squareCoords);
            //设置顶点位置数据
            unused.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
            //根据定点数据绘图
            unused.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

        }

        public void onSurfaceChanged(GL10 unused, int width, int height)
        {
            // 设置当前视口大小为新值
            unused.glViewport(0, 0, width, height);
        }


        private float[] mTriangleArray = {
                // X, Y, Z 这是一个等边三角形
                -0.5f, -0.25f, 0,
                0.5f, -0.25f, 0,
                0.0f, 0.559016994f, 0 };
        private float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right
    }

    static class BufferUtil {
        public static FloatBuffer mBuffer;

        public static FloatBuffer floatToBuffer(float[] a) {
            // 先初始化buffer，数组的长度*4，因为一个float占4个字节
            ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
            // 数组排序用nativeOrder
            mbb.order(ByteOrder.nativeOrder());
            mBuffer = mbb.asFloatBuffer();
            mBuffer.put(a);
            mBuffer.position(0);
            return mBuffer;
        }
    }

}
