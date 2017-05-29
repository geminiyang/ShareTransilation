package com.idear.move.POJO;

/**
 * 作者:geminiyang on 2017/5/28.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class Msg {
    public static final int RECEIVED = 0;//收到一条消息
    public static final int SENT = 1;//发出一条消息

    private String  content;//消息的内容
    private int type;//消息的类型
    private int ImgId;//图片的ID

    public  Msg(String content,int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImgId() {
        return ImgId;
    }

    public void setImgId(int imgId) {
        ImgId = imgId;
    }
}
