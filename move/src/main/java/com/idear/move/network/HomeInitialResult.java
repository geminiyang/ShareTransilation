package com.idear.move.network;

import com.google.gson.JsonArray;

/**
 * 作者:geminiyang on 2017/6/18.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class HomeInitialResult {
    private String status;
    private String message;
    private JsonArray data;

    public HomeInitialResult(String status, String message, JsonArray data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }
}
