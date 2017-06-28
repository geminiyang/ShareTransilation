package com.idear.move.Thread;

import android.util.Log;

import com.idear.move.POJO.MyDynamicsDataModel;
import com.idear.move.R;
import com.idear.move.network.DataGetInterface;
import com.idear.move.util.DateUtil;
import com.yqq.idear.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者:geminiyang on 2017/6/19.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class RefreshThread extends Thread {

    private DataGetInterface mListener;

    private LinkedList<MyDynamicsDataModel> dataModels;
    private String[] dynamicContents ={"aaaaaaaaa","aaaaaaaaa","aaaaaaaaa","aaaaaaaaa"};
    private int[] pics ={R.mipmap.family,R.mipmap.family,R.mipmap.family,R.mipmap.family};
    private int[] userIcons ={R.mipmap.paintbox,R.mipmap.paintbox,
            R.mipmap.paintbox,R.mipmap.paintbox};
    private long TimeStamp = 1498627673;
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 2;

    public RefreshThread(DataGetInterface mListener, LinkedList<MyDynamicsDataModel> list) {
        this.mListener = mListener;
        this.dataModels = list;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%4;
            Logger.d("RefreshMode---"+index+"---LIST SIZE : " + (dataModels.size()+1));
            dataModels.addFirst(new MyDynamicsDataModel(userIcons[index],"下拉刷新",
                    DateUtil.receiveDate(TimeStamp),
                    pics[index], dynamicContents[index]));
        }
        mListener.finishWork(new Object());
    }
}
