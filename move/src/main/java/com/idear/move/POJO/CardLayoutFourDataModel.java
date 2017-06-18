package com.idear.move.POJO;

import android.content.Context;

/**
 * Created by user on 2016/12/13.
 * 封装了用户的头像和昵称
 */
public class CardLayoutFourDataModel {

    public String activityName;
    public String personNum;
    public String moneyNum;
    public String visitNum;
    public String favoriteNum;
    public String publishTime;//用一个计时器完成
    public String activityState;
    public int activityPic;
    public int type;

    public CardLayoutFourDataModel(String activityName, String personNum, String moneyNum, String visitNum,
                                   String favoriteNum, String publishTime, String activityState,
                                   int activityPic, int type) {
        this.activityName = activityName;
        this.personNum = personNum;
        this.moneyNum = moneyNum;
        this.visitNum = visitNum;
        this.favoriteNum = favoriteNum;
        this.publishTime = publishTime;
        this.activityState = activityState;
        this.activityPic = activityPic;
        this.type = type;
    }

    //获取特定资源
    public int getImageResourceId(Context context) {
        try {
            return context.getResources().getIdentifier("a", "mipmap", context.getPackageName());
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
