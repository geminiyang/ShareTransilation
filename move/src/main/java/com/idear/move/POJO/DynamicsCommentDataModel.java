package com.idear.move.POJO;

public class DynamicsCommentDataModel {

    public String discussantIconUrl;
    public String username;
    public String time;
    public String commentStr;
    public String picUrl;
    public String textContent;

    public DynamicsCommentDataModel(String discussantIconUrl, String username, String time,
                                    String commentStr, String picUrl, String textContent) {
        this.discussantIconUrl = discussantIconUrl;
        this.username = username;
        this.time = time;
        this.commentStr = commentStr;
        this.picUrl = picUrl;
        this.textContent = textContent;
    }
}
