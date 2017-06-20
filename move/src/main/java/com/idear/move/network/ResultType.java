package com.idear.move.network;

/**
 * 作者:geminiyang on 2017/6/18.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class ResultType {
    private String status;
    private String message;

    public ResultType(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
