package com.idear.move.POJO;

public abstract class DynamicDetailItemThreeDataModel extends AbstractDataModel {

    private String discussantName;//评论者的昵称
    private String commentContent;//评论的内容
    private String time;//评论的时间
    private String thumbUpCount;//点赞数
    private String commentCount;//评论数
    private String discussantIconUrl;//评论者头像的Url

    public DynamicDetailItemThreeDataModel(String discussantName, String commentContent, String time,
                                           String thumbUpCount, String commentCount, String discussantIconUrl) {
        this.discussantName = discussantName;
        this.commentContent = commentContent;
        this.time = time;
        this.thumbUpCount = thumbUpCount;
        this.commentCount = commentCount;
        this.discussantIconUrl = discussantIconUrl;
    }

    public String getDiscussantName() {
        return discussantName;
    }

    public void setDiscussantName(String discussantName) {
        this.discussantName = discussantName;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbUpCount() {
        return thumbUpCount;
    }

    public void setThumbUpCount(String thumbUpCount) {
        this.thumbUpCount = thumbUpCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getDiscussantIconUrl() {
        return discussantIconUrl;
    }

    public void setDiscussantIconUrl(String discussantIconUrl) {
        this.discussantIconUrl = discussantIconUrl;
    }
}
