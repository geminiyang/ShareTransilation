package com.idear.move.network;

/**
 * 作者:geminiyang on 2017/6/11.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class HttpPath {

    public static String getUserLoginPath() {
        return "http://idear.party/api/mine/login";
    }

    public static String getVerifyUserStatePath() {
        return "http://idear.party/api/mine/check";
    }

    public static String getUserLogOutPath() {return "http://idear.party/api/mine/logout";}

    public static String getUserRegisterPath() {return  "http://idear.party/api/mine/signup";}

    public static String getEmailVerifyPath() {return "http://idear.party/api/mine/send";}
}
