package com.idear.move.POJO;

public class MyDynamicsDataModel {

    public int userIcon;
    public String username;
    public String time;
    public int pic;
    public String state;
    public String[] commentList;//用户评论信息列表,index 0 为用户名 index 1 为对应评论内容

    public MyDynamicsDataModel(int userIcon, String username, String time, int pic,
                               String state, String[] commentList) {
        this.userIcon = userIcon;
        this.username = username;
        this.time = time;
        this.pic = pic;
        this.state = state;
        this.commentList = commentList;
    }

}
