package com.idear.move.POJO;

public class ActivityDetailContentDataModel {
    private String activityName;
    private String publishUsername;
    private String activityState;
    private String activityContent;
    private String activityMeaning;
    private String activityLocation;
    private String activityTime;
    private String activityRExpireTime;
    private String activityFExpireTime;
    private String activityPersonNum;
    private String activityMoney;
    private String imgUrl;
    private int imgId;

    public ActivityDetailContentDataModel(){

    }

    public ActivityDetailContentDataModel(String activityName, String publishUsername, String activityState,
                                          String activityContent, String activityMeaning, String activityLocation,
                                          String activityTime, String activityRExpireTime, String activityFExpireTime,
                                          String activityPersonNum, String activityMoney, int imgId) {
        this.activityName = activityName;
        this.publishUsername = publishUsername;
        this.activityState = activityState;
        this.activityContent = activityContent;
        this.activityMeaning = activityMeaning;
        this.activityLocation = activityLocation;
        this.activityTime = activityTime;
        this.activityRExpireTime = activityRExpireTime;
        this.activityFExpireTime = activityFExpireTime;
        this.activityPersonNum = activityPersonNum;
        this.activityMoney = activityMoney;
        this.imgId = imgId;
    }

    public String getActivityRExpireTime() {
        return activityRExpireTime;
    }

    public void setActivityRExpireTime(String activityRExpireTime) {
        this.activityRExpireTime = activityRExpireTime;
    }

    public String getActivityFExpireTime() {
        return activityFExpireTime;
    }

    public void setActivityFExpireTime(String activityFExpireTime) {
        this.activityFExpireTime = activityFExpireTime;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getPublishUsername() {
        return publishUsername;
    }

    public void setPublishUsername(String publishUsername) {
        this.publishUsername = publishUsername;
    }

    public String getActivityState() {
        return activityState;
    }

    public void setActivityState(String activityState) {
        this.activityState = activityState;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getActivityMeaning() {
        return activityMeaning;
    }

    public void setActivityMeaning(String activityMeaning) {
        this.activityMeaning = activityMeaning;
    }

    public String getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }


    public String getActivityPersonNum() {
        return activityPersonNum;
    }

    public void setActivityPersonNum(String activityPersonNum) {
        this.activityPersonNum = activityPersonNum;
    }

    public String getActivityMoney() {
        return activityMoney;
    }

    public void setActivityMoney(String activityMoney) {
        this.activityMoney = activityMoney;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
