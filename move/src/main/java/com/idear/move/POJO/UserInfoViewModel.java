package com.idear.move.POJO;

import com.idear.move.Activity.UserLoginActivity;

/**
 * Created by user on 2017/5/9.
 */

public class UserInfoViewModel {
    private int status;
    private int listorder;
    private String user_id;
    private String username;
    private String password;
    private String email;
    private String tel;
    private String school;
    private String sex;
    private String create_time;

    public UserInfoViewModel() {

    }

    public UserInfoViewModel(int status, String user_id, String username,
                             String password, String email, String tel, String school, String sex) {
        this.status = status;
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.school = school;
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getListorder() {
        return listorder;
    }

    public void setListorder(int listorder) {
        this.listorder = listorder;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "UserInfoViewModel{" +
                "status=" + status +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", school='" + school + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
