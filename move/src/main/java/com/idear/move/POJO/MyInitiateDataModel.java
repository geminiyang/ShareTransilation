package com.idear.move.POJO;

import android.content.Context;

/**
 * Created by user on 2016/12/13.
 * 封装了用户的头像和昵称
 */
public class MyInitiateDataModel {

    public String state;
    public String detailImage;
    public int type;

    public MyInitiateDataModel(String state, String detailImage, int type) {
        this.state = state;
        this.detailImage = detailImage;
        this.type = type;
    }

    //获取特定资源
    public int getImageResourceId(Context context) {
        try {
            return context.getResources().getIdentifier(this.detailImage, "mipmap", context.getPackageName());
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
