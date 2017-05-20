package com.idear.move.POJO;

import android.content.Context;

/**
 * Created by user on 2016/12/13.
 * 封装了用户的头像和昵称
 */
public class DataModel {
    public String Name;
    public String ImageName;
    public int type;

    public DataModel(String name, String imageName,int type){
        this.Name = name;
        this.ImageName = imageName;
        this.type = type;
    }

    //获取特定资源
    public int getImageResourceId(Context context) {
        try {
            return context.getResources().getIdentifier(this.ImageName, "mipmap", context.getPackageName());
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
