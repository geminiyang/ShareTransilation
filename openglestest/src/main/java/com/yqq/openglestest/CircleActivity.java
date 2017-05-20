package com.yqq.openglestest;

/**
 * Created by user on 2017/4/17.
 */

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CircleActivity extends Activity {

    private final String TAG = "CircleActivity";

    private GLSurfaceView glSurfaceView;
    private float myRadius = 0.7f;

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


        private float[]  createPositions(int n){
            ArrayList<Float> data=new ArrayList<>();
            data.add(0.0f);             //设置圆心坐标
            data.add(0.0f);
            data.add(0.0f);
            float angDegSpan=360f/n;//分母决定其是几边形 （这个间接决定了每一个角的角度）
            for(float i=0;i<360+angDegSpan;i+=angDegSpan){
                //按顺序初始化顶点(x,y,z)
                data.add((float)(myRadius*Math.cos(i*Math.PI/180f)));//x
                data.add((float) (myRadius*Math.sin(i*Math.PI/180f)));//y
                data.add(0.0f);//z
            }
            float[] f=new float[data.size()];
            for (int i=0;i<f.length;i++){
                f[i]=data.get(i);
            }
            return f;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            Log.i(TAG, "onDrawFrame");
            // 清除屏幕和深度缓存(如果不调用该代码, 将不显示glClearColor设置的颜色)
            // 同样如果将该代码放到 onSurfaceCreated 中屏幕会一直闪动
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            //投影模式用来使绘制的图形的比例更加接近于标准坐标系
            /************ 启用MODELVIEW模式，并使用GLU.gluLookAt()来设置视点 ***************/
            // 设置当前矩阵为模型视图模式
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity(); // reset the matrix to its default state
            // 设置视点
            GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            /*****************************************/

            //绘制颜色
            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            //定义其是几边形
            float shapePosFirst[] = createPositions(1080);
            //生成顶点绘制信息
            FloatBuffer mFirstBuffer = BufferUtil.floatToBuffer(shapePosFirst);
            //设置顶点位置信息
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFirstBuffer);
            //根据顶点信息绘制图形
            gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0,shapePosFirst.length/3);//因为返回的数组是的点个数为n(几边形)+1
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

