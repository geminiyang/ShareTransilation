package com.idear.move.POJO;

/**
 * Created by user on 2017/5/9.
 */

public class HomeInitialItem {
    private int act_id;
    private String pic_dir;
    private String act_title;

    public HomeInitialItem() {
    }

    public HomeInitialItem(int act_id, String pic_dir, String act_title) {
        this.act_id = act_id;
        this.pic_dir = pic_dir;
        this.act_title = act_title;
    }

    public int getAct_id() {
        return act_id;
    }

    public void setAct_id(int act_id) {
        this.act_id = act_id;
    }

    public String getPic_dir() {
        return pic_dir;
    }

    public void setPic_dir(String pic_dir) {
        this.pic_dir = pic_dir;
    }

    public String getAct_title() {
        return act_title;
    }

    public void setAct_title(String act_title) {
        this.act_title = act_title;
    }

    @Override
    public String toString() {
        return "HomeInitialItem{" +
                "act_id='" + act_id + '\'' +
                ", pic_dir='" + pic_dir + '\'' +
                ", act_title=" + act_title +
                '}';
    }
}
