package com.idear.move.POJO;
/**
 * Created by user on 2016/12/13.
 * 封装了用户的头像和昵称
 */
public class SpreadDataModel {

    public String title;
    public String publishTime;//用一个计时器完成
    public int smallImage;
    public String visitNum;
    public String favoriteNum;
    public int type;

    public SpreadDataModel(String title, String publishTime, int smallImage, String visitNum, String favoriteNum, int type) {
        this.title = title;
        this.publishTime = publishTime;
        this.smallImage = smallImage;
        this.visitNum = visitNum;
        this.favoriteNum = favoriteNum;
        this.type = type;
    }
}
