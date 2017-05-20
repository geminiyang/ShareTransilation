package com.yqq.openglestest;

/**
 * Created by user on 2017/4/17.
 */

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;

import com.yqq.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ProjectionTriangleActivity extends Activity {

    private final String TAG = "ProjectionTriangle";

    private GLSurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            //投影模式下的绘制必须添加这一条
            /**
             * Android设备屏幕通常不是正方形的，而OpenGL总是默认地将正方形坐标系投影到这一设备上，这就导致图形无法按真实比例显示。
             * 要解决这一问题，我们可以使用OpenGL 的投影模式和相机视图将图形的坐标进行转换以适应不同的设备显示。
             */
            float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION); // 设置当前矩阵为投影矩阵
            gl.glLoadIdentity(); // 重置矩阵为初始值
            gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7); // 根据长宽比设置投影矩阵
        }

        private float[] mTriangleArray = {
                // X, Y, Z 这是一个等边三角形
                -0.5f, -0.25f, 0, 0.5f, -0.25f, 0, 0.0f, 0.559016994f, 0};

        @Override
        public void onDrawFrame(GL10 gl) {
            Log.i(TAG, "onDrawFrame");
            // 清除屏幕和深度缓存(如果不调用该代码, 将不显示glClearColor设置的颜色)
            // 同样如果将该代码放到 onSurfaceCreated 中屏幕会一直闪动
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            /************ 启用MODELVIEW模式，并使用GLU.gluLookAt()来设置视点 ***************/
            // 设置当前矩阵为模型视图模式
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity(); // reset the matrix to its default state
            // 设置视点
            GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            /*****************************************/

            //绘制颜色
            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            //生成顶点绘制信息
            FloatBuffer mTriangleBuffer = BufferUtil.floatToBuffer(mTriangleArray);
            //设置顶点位置信息
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
            //根据顶点信息绘制图形
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
            //重置当前的模型视图矩阵
            gl.glLoadIdentity();
            // 平移 (矩阵相乘)
            gl.glTranslatef(-0.2f, 0.3f, 0f);

        }
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
