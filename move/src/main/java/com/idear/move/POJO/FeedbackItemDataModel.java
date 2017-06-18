package com.idear.move.POJO;

/**
 * 作者:geminiyang on 2017/6/16.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class FeedbackItemDataModel {
    public String title;
    public String pic;
    public String score;
    public Long receiverTime;//可能需要牵涉到存储在本地数据库

    public FeedbackItemDataModel(String title, String pic, String score, Long receiverTime) {
        this.title = title;
        this.pic = pic;
        this.score = score;
        this.receiverTime = receiverTime;
    }
}
