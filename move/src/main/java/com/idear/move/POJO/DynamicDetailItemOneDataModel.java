package com.idear.move.POJO;

public abstract class DynamicDetailItemOneDataModel extends AbstractDataModel {
    //需要填充自定义点赞组的数据
    private String userIconUrl;//动态发布的用户的头像地址
    private String dynamicPicUrl;//动态图片地址
    private String username;//动态发布的用户的昵称
    private String publishTime;//动态的发布时间
    private String dynamicContent;//动态的内容

    public DynamicDetailItemOneDataModel(String userIconUrl, String dynamicPicUrl, String username,
                                         String publishTime, String dynamicContent) {
        this.userIconUrl = userIconUrl;
        this.dynamicPicUrl = dynamicPicUrl;
        this.username = username;
        this.publishTime = publishTime;
        this.dynamicContent = dynamicContent;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getDynamicPicUrl() {
        return dynamicPicUrl;
    }

    public void setDynamicPicUrl(String dynamicPicUrl) {
        this.dynamicPicUrl = dynamicPicUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }
}
