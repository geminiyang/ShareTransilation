package com.idear.move.POJO;

/**
 * 作者:geminiyang on 2017/6/16.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CardLayoutTwoDataModel {
    public String activityName;
    public String picUrl;
    public Long receiverTime;//可能需要牵涉到存储在本地数据库

    public CardLayoutTwoDataModel(String activityName, String picUrl, Long receiverTime) {
        this.activityName = activityName;
        this.picUrl = picUrl;
        this.receiverTime = receiverTime;
    }
}
