package com.idear.move.POJO;

import android.content.Context;

/**
 * Created by user on 2016/12/13.
 * 封装了用户的头像和昵称
 */
public class AllActivityDataModel {

    public String title;
    public String description;
    public String personNum;
    public String money;
    public String time;//用一个计时器完成
    public String username;
    public String detailImage;
    public String visitNum;
    public int type;

    public AllActivityDataModel(String title, String description, String personNum,
                                String money, String time, String username, String detailImage,
                                String visitNum, int type) {
        this.title = title;
        this.description = description;
        this.personNum = personNum;
        this.money = money;
        this.time = time;
        this.username = username;
        this.detailImage = detailImage;
        this.visitNum = visitNum;
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