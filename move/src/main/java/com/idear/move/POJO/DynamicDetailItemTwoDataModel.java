package com.idear.move.POJO;

public abstract class DynamicDetailItemTwoDataModel extends AbstractDataModel {
    private String commentCount;//评论条数

    public DynamicDetailItemTwoDataModel(String commentCount) {
        this.commentCount = commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentCount() {
        return commentCount;
    }
}
