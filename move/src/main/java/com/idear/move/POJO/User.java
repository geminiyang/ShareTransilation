package com.idear.move.POJO;

/**
 * 作者:geminiyang on 2017/6/29.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class User {
    private String id;
    private String picUrl;

    public User() {
    }

    public User(String id, String picUrl) {
        this.id = id;
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
