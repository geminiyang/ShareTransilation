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
    private String[] states ={"[审核中]","[进行中]","[筹资中]","[已结束]"};
    private int[] pics ={R.mipmap.family,R.mipmap.family,R.mipmap.family,R.mipmap.family};
    private int[] userIcons ={R.mipmap.paintbox,R.mipmap.paintbox,
            R.mipmap.paintbox,R.mipmap.paintbox};
    private String[] commentOne = {"大丸子","一级棒"};
    private List<String[]> lists = new ArrayList<>();
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNT = 10;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 2;

    public RefreshThread(DataGetInterface mListener, LinkedList<MyDynamicsDataModel> list) {
        this.mListener = mListener;
        this.dataModels = list;
        lists.add(commentOne);
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (dataModels.size() >= TOTAL_COUNT) {
                break;
            }
            int index = dataModels.size()%4;
            Logger.d("RefreshMode------"+index+"LIST SIZE : " + (dataModels.size()+1));
            dataModels.addFirst(new MyDynamicsDataModel(userIcons[index],"下拉刷新获得",
                    DateUtil.timeStampToStr(System.currentTimeMillis()),
                    pics[index], states[index],lists.get(0)));
        }
        mListener.finishWork(new Object());
    }
}
