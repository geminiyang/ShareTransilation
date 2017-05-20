package com.yqq.materialtest;

import android.content.Context;

/**
 * Created by user on 2016/12/13.
 * 封装了用户的头像和昵称
 */

public class User {

    public String Name;
    public String ImageName;


    public  User(String name,String imageName){
        this.Name = name;
        this.ImageName = imageName;
    }

    //获取特定资源
    public int getImageResourceId(Context context)
    {
        try
        {
            return context.getResources().getIdentifier(this.ImageName, "drawable", context.getPackageName());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
