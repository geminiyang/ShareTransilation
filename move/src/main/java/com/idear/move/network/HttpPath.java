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

    public static String getModifyPWDVerifyPath() {return "http://idear.party/api/mine/pwdVerify";}

    public static String getFullInInfoPath() {return "http://idear.party/api/mine/infocreate";}

    public static String getPassWordUpdatePath() {return "http://idear.party/api/mine/pwdUpdate";}

    public static String getPassWordForgetPath() { return "http://idear.party/api/mine/pwdForget";}

    public static String getUserInfoPath() {return "http://idear.party/api/mine/info";}

    public static String getUpdateUserInfoPath() {return "http://idear.party/api/mine/infoupdate";}

    public static String getFansInfoPath() {return "http://idear.party/api/mine/index";}

    public static String getRPath() {return "http://idear.party/api/apply/1";}

    public static String getFPath() {return "http://idear.party/api/apply/2";}

    public static String getRFPath() {return "http://idear.party/api/apply/both";}

    public static String getPath() {return "http://idear.party/";}
}
