package com.idear.move.POJO;

public class MyDynamicsDataModel {

    private int userIcon;
    private  String username;
    private  String time;
    private int pic;
    private String dynamicContent;

    public MyDynamicsDataModel(int userIcon, String username, String time, int pic, String dynamicContent) {
        this.userIcon = userIcon;
        this.username = username;
        this.time = time;
        this.pic = pic;
        this.dynamicContent = dynamicContent;
    }

    public int getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(int userIcon) {
        this.userIcon = userIcon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }
}
