package com.idear.move.POJO;

/**
 * 作者:geminiyang on 2017/6/16.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CardLayoutOneDataModel {
    public String activityName;
    public String picUrl;
    public String state;
    public Long receiverTime;//可能需要牵涉到存储在本地数据库

    public CardLayoutOneDataModel(String activityName, String picUrl, String state, Long receiverTime) {
        this.activityName = activityName;
        this.picUrl = picUrl;
        this.state = state;
        this.receiverTime = receiverTime;
    }
}
