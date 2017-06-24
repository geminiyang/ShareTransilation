package com.idear.move.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 作者:geminiyang on 2017/6/18.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class ViewModelResult {
    private String status;
    private String message;
    private JsonObject data;

    public ViewModelResult(String status, String message, JsonObject data) {
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

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
