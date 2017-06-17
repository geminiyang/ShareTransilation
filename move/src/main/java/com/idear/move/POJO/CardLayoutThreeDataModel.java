package com.idear.move.POJO;

/**
 * 作者:geminiyang on 2017/6/16.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CardLayoutThreeDataModel {
    public String activityName;
    public String picUrl;
    public String breviaryContent;//可能牵涉到提取json数据传中前34个文字数据

    public CardLayoutThreeDataModel(String activityName, String picUrl, String breviaryContent) {
        this.activityName = activityName;
        this.picUrl = picUrl;
        this.breviaryContent = breviaryContent;
    }
}
