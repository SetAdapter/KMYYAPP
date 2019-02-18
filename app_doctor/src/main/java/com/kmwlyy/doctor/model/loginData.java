package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/9.
 * 登录后获取的数据
 */
public class loginData {
    public String UserID;
    public String UserType;
    public String Email;
    public String Mobile;
    public String Url;
    public String Redirect;
    public String UserToken;
    public String UserCNName;
    public String UserENName;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getRedirect() {
        return Redirect;
    }

    public void setRedirect(String redirect) {
        Redirect = redirect;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public String getUserCNName() {
        return UserCNName;
    }

    public void setUserCNName(String userCNName) {
        UserCNName = userCNName;
    }

    public String getUserENName() {
        return UserENName;
    }

    public void setUserENName(String userENName) {
        UserENName = userENName;
    }
}
