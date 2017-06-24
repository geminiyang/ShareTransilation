package com.idear.move.POJO;

/**
 * Created by user on 2017/5/9.
 */

public class FansViewModel {
    private String pic_dir;
    private String nickname;
    private int fensi;
    private int guanzhu;

    public FansViewModel() {
    }

    public FansViewModel(String pic_dir,int fensi, int guanzhu) {
        this.pic_dir = pic_dir;
        this.fensi = fensi;
        this.guanzhu = guanzhu;
    }

    public FansViewModel(String pic_dir, String nickname, int fensi, int guanzhu) {
        this.pic_dir = pic_dir;
        this.nickname = nickname;
        this.fensi = fensi;
        this.guanzhu = guanzhu;
    }

    public String getPic_dir() {
        return pic_dir;
    }

    public void setPic_dir(String pic_dir) {
        this.pic_dir = pic_dir;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getFensi() {
        return fensi;
    }

    public void setFensi(int fensi) {
        this.fensi = fensi;
    }

    public int getGuanzhu() {
        return guanzhu;
    }

    public void setGuanzhu(int guanzhu) {
        this.guanzhu = guanzhu;
    }

    @Override
    public String toString() {
        return "FansViewModel{" +
                "pic_dir='" + pic_dir + '\'' +
                ", nickname='" + nickname + '\'' +
                ", fensi=" + fensi +
                ", guanzhu=" + guanzhu +
                '}';
    }
}
