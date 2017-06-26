package com.idear.move.POJO;

import java.io.File;

public class FDataModel {
    private int user_id;
    private String act_title;
    private String act_content;
    private String act_meaning;
    private String act_location;
    private int start_time;
    private int end_time;
    private String act_category;
    private int money;
    private int expire;
    private File img;

    public FDataModel(){

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAct_title() {
        return act_title;
    }

    public void setAct_title(String act_title) {
        this.act_title = act_title;
    }

    public String getAct_content() {
        return act_content;
    }

    public void setAct_content(String act_content) {
        this.act_content = act_content;
    }

    public String getAct_meaning() {
        return act_meaning;
    }

    public void setAct_meaning(String act_meaning) {
        this.act_meaning = act_meaning;
    }

    public String getAct_location() {
        return act_location;
    }

    public void setAct_location(String act_location) {
        this.act_location = act_location;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getAct_category() {
        return act_category;
    }

    public void setAct_category(String act_category) {
        this.act_category = act_category;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public File getImg() {
        return img;
    }

    public void setImg(File img) {
        this.img = img;
    }
}
