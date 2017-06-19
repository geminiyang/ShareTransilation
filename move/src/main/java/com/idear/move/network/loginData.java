package com.idear.move.network;

/**
 * 作者:geminiyang on 2017/6/18.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class loginData {
    private String email;
    private String password;

    public loginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "loginData{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
